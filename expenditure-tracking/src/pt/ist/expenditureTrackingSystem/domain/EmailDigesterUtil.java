package pt.ist.expenditureTrackingSystem.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import myorg.applicationTier.Authenticate;
import myorg.domain.VirtualHost;
import myorg.domain.groups.PersistentGroup;
import myorg.domain.groups.SingleUserGroup;
import myorg.util.MultiCounter;

import org.apache.commons.lang.StringUtils;
import org.jfree.data.time.Month;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.util.ProcessMapGenerator;
import pt.ist.fenixframework.plugins.remote.domain.exception.RemoteException;
import pt.ist.messaging.domain.Message;
import pt.ist.messaging.domain.Sender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class EmailDigesterUtil {

    public static void executeTask() {
	final String ts = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();

	List<String> toAddress = new ArrayList<String>();
	Language.setLocale(Language.getDefaultLocale());
	for (Person person : getPeopleToProcess()) {
	    Authenticate.authenticate(person.getUsername(), StringUtils.EMPTY, false);
	    Map<AcquisitionProcessStateType, MultiCounter<AcquisitionProcessStateType>> generateAcquisitionMap =
		ProcessMapGenerator.generateAcquisitionMap(person, true);
	    Map<RefundProcessStateType, MultiCounter<RefundProcessStateType>> generateRefundMap =
		ProcessMapGenerator.generateRefundMap(person, true);

	    if (!generateAcquisitionMap.isEmpty() || !generateRefundMap.isEmpty()) {
		toAddress.clear();
		try {
		    final String email = person.getEmail();
		    if (email != null) {
			final Sender sender = virtualHost.getSystemSender();
			final PersistentGroup group = SingleUserGroup.getOrCreateGroup(person.getUser());
			new Message(sender, Collections.EMPTY_SET, Collections.singleton(group), Collections.EMPTY_SET, Collections.EMPTY_SET,
				"Processos Pendentes - Aquisições", getBody(generateAcquisitionMap, generateRefundMap), Collections.EMPTY_SET);

/*
			toAddress.add(email);
			System.out.println("Sending aquisition email digest for: " + person.getUser().getUsername() + " to: " + email + " - " + ts);
			new Email(virtualHost.getApplicationSubTitle().getContent(),
				virtualHost.getSystemEmailAddress(),
				new String[] {},
				toAddress,
				Collections.EMPTY_LIST,
				Collections.EMPTY_LIST,
				"Processos Pendentes",
				getBody(generateAcquisitionMap, generateRefundMap));
*/
		    }
		} catch (final RemoteException ex) {
		    System.out.println("Unable to lookup email address for: " + person.getUsername());
		    // skip this person... keep going to next.
		}
	    }
	}
    }

    private static Collection<Person> getPeopleToProcess() {
	final Set<Person> people = new HashSet<Person>();
	final LocalDate today = new LocalDate();
	final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();
	for (final Authorization authorization : instance.getAuthorizationsSet()) {
	    if (authorization.isValidFor(today)) {
		final Person person = authorization.getPerson();
		if (person.getOptions().getReceiveNotificationsByEmail()) {
		    people.add(person);
		}
	    }
	}
	for (final RoleType roleType : RoleType.values()) {
	    addPeopleWithRole(people, roleType);
	}
	for (final AccountingUnit accountingUnit : instance.getAccountingUnitsSet()) {
	    addPeople(people, accountingUnit.getPeopleSet());
	    addPeople(people, accountingUnit.getProjectAccountantsSet());
	    addPeople(people, accountingUnit.getResponsiblePeopleSet());
	    addPeople(people, accountingUnit.getResponsibleProjectAccountantsSet());
	    addPeople(people, accountingUnit.getTreasuryMembersSet());
	}
	final PaymentProcessYear paymentProcessYear = PaymentProcessYear.getPaymentProcessYearByYear(Calendar.getInstance().get(Calendar.YEAR));
	addRequestors(people, paymentProcessYear);
	if (today.getMonthOfYear() == Month.JANUARY) {
	    final PaymentProcessYear previousYear = PaymentProcessYear.getPaymentProcessYearByYear(Calendar.getInstance().get(Calendar.YEAR) - 1);
	    addRequestors(people, previousYear);
	}
	return people;
    }

    private static void addRequestors(final Set<Person> people, final PaymentProcessYear paymentProcessYear) {
	for (final PaymentProcess paymentProcess : paymentProcessYear.getPaymentProcessSet()) {
	    if (paymentProcess.getRequest() != null) {
		final Person person = paymentProcess.getRequestor();
	    	if (person != null && person.getOptions().getReceiveNotificationsByEmail()) {
	    	    people.add(person);
	    	}
	    }
	}
    }

    private static void addPeopleWithRole(final Set<Person> people, final RoleType roleType) {
	final Role role = Role.getRole(roleType);
	addPeople(people, role.getPersonSet());
    }

    private static void addPeople(final Set<Person> people, Collection<Person> unverified) {
	for (final Person person : unverified) {
	    if (person.getOptions().getReceiveNotificationsByEmail()) {
		people.add(person);
	    }
	}
    }

    private static String getBody(Map<AcquisitionProcessStateType, MultiCounter<AcquisitionProcessStateType>> acquisitionMap,
	    Map<RefundProcessStateType, MultiCounter<RefundProcessStateType>> refundMap) {

	StringBuilder builder = new StringBuilder("Caro utilizador, possui processos pendentes na central de compras.\n\n");
	if (!acquisitionMap.isEmpty()) {
	    builder.append("Regime simplificado\n");
	    for (final MultiCounter<AcquisitionProcessStateType> multiCounter : acquisitionMap.values()) {
		builder.append("\t");
		builder.append(multiCounter.getCountableObject().getLocalizedName());
		builder.append("\t");
		builder.append(ProcessMapGenerator.getDefaultCounter(multiCounter).getValue());
		builder.append("\n");
		final Set<SimplifiedProcedureProcess> processes = (Set) ProcessMapGenerator.getDefaultCounter(multiCounter).getObjects();
		if (processes.size() < 25) {
		    for (final SimplifiedProcedureProcess process : processes) {
			builder.append("\t\t");
			builder.append(process.getProcessNumber());
			builder.append("\n");			
		    }
		}
	    }
	}
	if (!refundMap.isEmpty()) {
	    builder.append("Processos de reembolso\n");
	    for (final MultiCounter<RefundProcessStateType> multiCounter : refundMap.values()) {
		builder.append("\t");
		builder.append(multiCounter.getCountableObject().getLocalizedName());
		builder.append("\t");
		builder.append(ProcessMapGenerator.getDefaultCounter(multiCounter).getValue());
		builder.append("\n");
		final Set<RefundProcess> processes = (Set) ProcessMapGenerator.getDefaultCounter(multiCounter).getObjects();
		if (processes.size() < 25) {
		    for (final RefundProcess process : processes) {
			builder.append("\t\t");
			builder.append(process.getProcessNumber());
			builder.append("\n");			
		    }
		}
	    }
	}
	return builder.toString();
    }

}
