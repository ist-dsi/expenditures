package module.workingCapital.domain;

import java.util.SortedSet;
import java.util.TreeSet;

import module.organization.domain.Party;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;

public class WorkingCapitalYear extends WorkingCapitalYear_Base {

    public WorkingCapitalYear() {
	super();
	setWorkingCapitalSystem(WorkingCapitalSystem.getInstance());
    }

    public WorkingCapitalYear(final Integer year) {
	this();
	setYear(year);
    }

    public static WorkingCapitalYear findOrCreate(final Integer year) {
	for (final WorkingCapitalYear workingCapitalYear : WorkingCapitalSystem.getInstance().getWorkingCapitalYearsSet()) {
	    if (workingCapitalYear.getYear().intValue() == year.intValue()) {
		return workingCapitalYear;
	    }
	}
	return new WorkingCapitalYear(year);
    }

    private abstract class WorkingCapitalProcessSearch {

	abstract boolean shouldAdd(final WorkingCapitalProcess workingCapitalProcess, final User user);

	SortedSet<WorkingCapitalProcess> search() {
	    final User user = UserView.getCurrentUser();
	    final SortedSet<WorkingCapitalProcess> result = new TreeSet<WorkingCapitalProcess>(
		    WorkingCapitalProcess.COMPARATOR_BY_UNIT_NAME);
	    for (final WorkingCapital workingCapital : getWorkingCapitalsSet()) {
		final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();
		if (shouldAdd(workingCapitalProcess, user)) {
		    result.add(workingCapitalProcess);
		}
	    }
	    return result;
	}

    }

    public SortedSet<WorkingCapitalProcess> getPendingAproval() {
	return new WorkingCapitalProcessSearch() {
	    @Override
	    boolean shouldAdd(final WorkingCapitalProcess workingCapitalProcess, final User user) {
		final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
		return !workingCapital.isCanceledOrRejected()
			&& (workingCapitalProcess.isPendingAproval(user)
				|| workingCapital.hasAcquisitionPendingApproval(user));
	    }
	}.search();
    }

    public SortedSet<WorkingCapitalProcess> getPendingVerification() {
	return new WorkingCapitalProcessSearch() {
	    @Override
	    boolean shouldAdd(final WorkingCapitalProcess workingCapitalProcess, final User user) {
		final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
		return !workingCapital.isCanceledOrRejected()
			&& (workingCapitalProcess.isPendingVerification(user) || workingCapital.hasAcquisitionPendingVerification(user));
	    }
	}.search();
    }

    public SortedSet<WorkingCapitalProcess> getPendingAuthorization() {
	return new WorkingCapitalProcessSearch() {
	    @Override
	    boolean shouldAdd(WorkingCapitalProcess workingCapitalProcess, User user) {
		return workingCapitalProcess.isPendingAuthorization(user);
	    }
	}.search();
    }

    public SortedSet<WorkingCapitalProcess> getPendingPayment() {
	return new WorkingCapitalProcessSearch() {
	    @Override
	    boolean shouldAdd(final WorkingCapitalProcess workingCapitalProcess, final User user) {
		final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
		final WorkingCapitalSystem workingCapitalSystem = workingCapital.getWorkingCapitalSystem();
		return !workingCapital.isCanceledOrRejected()
			&& ((workingCapital.isAccountingResponsible(user) && workingCapital.canRequestCapital())
				|| (workingCapital.isTreasuryMember(user) && workingCapital.hasWorkingCapitalRequestPendingTreasuryProcessing()));
	    }
	}.search();
    }

    public SortedSet<WorkingCapitalProcess> getMyWorkingCapital() {
	final SortedSet<WorkingCapitalProcess> result = new TreeSet<WorkingCapitalProcess>(
		WorkingCapitalProcess.COMPARATOR_BY_UNIT_NAME);
	final User user = UserView.getCurrentUser();
	final Person person = user.getPerson();
	if (person != null) {
	    for (final WorkingCapital workingCapital : person.getMovementResponsibleWorkingCapitalsSet()) {
		if (workingCapital.getWorkingCapitalYear() == this) {
		    final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();
		    result.add(workingCapitalProcess);
		}
	    }
	}
	return result;
    }

    public SortedSet<WorkingCapitalProcess> getRequestedWorkingCapital() {
	final SortedSet<WorkingCapitalProcess> result = new TreeSet<WorkingCapitalProcess>(
		WorkingCapitalProcess.COMPARATOR_BY_UNIT_NAME);
	final User user = UserView.getCurrentUser();
	final Person person = user.getPerson();
	if (person != null) {
	    for (final WorkingCapitalInitialization workingCapitalInitialization : person
		    .getRequestedWorkingCapitalInitializationsSet()) {
		final WorkingCapital workingCapital = workingCapitalInitialization.getWorkingCapital();
		if (workingCapital.getWorkingCapitalYear() == this) {
		    final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();
		    result.add(workingCapitalProcess);
		}
	    }
	}
	return result;
    }

    public SortedSet<WorkingCapitalProcess> getAprovalResponsibleWorkingCapital() {
	final SortedSet<WorkingCapitalProcess> result = new TreeSet<WorkingCapitalProcess>(
		WorkingCapitalProcess.COMPARATOR_BY_UNIT_NAME);
	final User user = UserView.getCurrentUser();
	if (user.hasExpenditurePerson()) {
	    for (final Authorization authorization : user.getExpenditurePerson().getAuthorizationsSet()) {
		final pt.ist.expenditureTrackingSystem.domain.organization.Unit unit = authorization.getUnit();
		for (final WorkingCapital workingCapital : unit.getWorkingCapitalsSet()) {
		    if (workingCapital.getWorkingCapitalYear() == this) {
			final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();
			result.add(workingCapitalProcess);
		    }
		}
	    }
	}
	return result;
    }

    public SortedSet<WorkingCapitalProcess> getForUnit(final Unit unit) {
	final User user = UserView.getCurrentUser();
	final SortedSet<WorkingCapitalProcess> result = new TreeSet<WorkingCapitalProcess>(
		WorkingCapitalProcess.COMPARATOR_BY_UNIT_NAME);
	if (unit.hasExpenditureUnit()) {
	    for (final WorkingCapital workingCapital : unit.getExpenditureUnit().getWorkingCapitalsSet()) {
		if (workingCapital.getWorkingCapitalYear() == this && workingCapital.isAvailable(user)) {
		    final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();
		    if (workingCapitalProcess.isAccessibleToCurrentUser()) {
			result.add(workingCapitalProcess);
		    }
		}
	    }
	}
	return result;
    }

    public SortedSet<WorkingCapitalProcess> getForPerson(final Person person) {
	final SortedSet<WorkingCapitalProcess> result = new TreeSet<WorkingCapitalProcess>(
		WorkingCapitalProcess.COMPARATOR_BY_UNIT_NAME);
	if (person != null) {
	    for (final WorkingCapital workingCapital : person.getMovementResponsibleWorkingCapitalsSet()) {
		if (workingCapital.getWorkingCapitalYear() == this) {
		    final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();
		    if (workingCapitalProcess.isAccessibleToCurrentUser()) {
			result.add(workingCapitalProcess);
		    }
		}
	    }
	}
	return result;
    }

    public SortedSet<WorkingCapitalProcess> getForParty(final Party party) {
	return party.isUnit() ? getForUnit((Unit) party) : getForPerson((Person) party);
    }

}
