package module.workingCapital.domain;

import java.util.Collection;
import java.util.Collections;

import myorg.applicationTier.Authenticate;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.emailNotifier.domain.Email;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class EmailDigester extends EmailDigester_Base {
    
    public EmailDigester() {
        super();
    }

    @Override
    @Service
    public void executeTask() {
	Language.setLocale(Language.getDefaultLocale());
	for (Person person : ExpenditureTrackingSystem.getInstance().getPeople()) {

	    if (person.getOptions().getReceiveNotificationsByEmail()) {
	    	final User user = person.getUser();
	    	if (user.hasPerson() && user.hasExpenditurePerson()) {
	    	    final UserView userView = Authenticate.authenticate(user);
	    	    pt.ist.fenixWebFramework.security.UserView.setUser(userView);

	    	    try {
	    		final WorkingCapitalYear workingCapitalYear = WorkingCapitalYear.getCurrentYear();
	    		final int takenByUser = workingCapitalYear.getTaken().size();
	    		final int pendingApprovalCount = workingCapitalYear.getPendingAproval().size();
	    		final int pendingVerificationnCount = workingCapitalYear.getPendingVerification().size();
	    		final int pendingAuthorizationCount = workingCapitalYear.getPendingAuthorization().size();
	    		final int pendingPaymentCount = workingCapitalYear.getPendingPayment().size();
	    		final int totalPending = takenByUser + pendingApprovalCount + pendingVerificationnCount + pendingAuthorizationCount + pendingPaymentCount;

	    		if (totalPending > 0) {
	    		    final String email = user.getUsername() + "@nowhere.org";// person.getEmail();
	    		    if (email != null) {
	    			final StringBuilder body = new StringBuilder("Caro utilizador, possui processos de fundos de maneio pendentes nas aplicações centrais do IST, em http://dot.ist.utl.pt/.\n");
	    			if (takenByUser > 0) {
	    			    body.append("\n\tPendentes de Libertação\t");
	    			    body.append(takenByUser);
	    			}
	    			if (pendingApprovalCount > 0) {
	    			    body.append("\n\tPendentes de Aprovação\t");
	    			    body.append(pendingApprovalCount);
	    			}
	    			if (pendingVerificationnCount > 0) {
	    			    body.append("\n\tPendentes de Verificação\t");
	    			    body.append(pendingVerificationnCount);
	    			}
	    			if (pendingAuthorizationCount > 0) {
	    			    body.append("\n\tPendentes de Autorização\t");
	    			    body.append(pendingAuthorizationCount);
	    			}
	    			if (pendingPaymentCount > 0) {
	    			    body.append("\n\tPendentes de Pagamento\t");
	    			    body.append(pendingPaymentCount);
	    			}
	    			body.append("\n\n\tTotal de Processos de Missão Pendentes\t");
	    			body.append(totalPending);

	    			body.append("\n\n---\n");
	    			body.append("Esta mensagem foi enviada por meio das Aplicações Centrais do IST.\n");

	    			final Collection<String> toAddress = Collections.singleton(email);
	    			new Email("Aplicações Centrais do IST", "noreply@ist.utl.pt", new String[] {}, toAddress, Collections.EMPTY_LIST,
	    				Collections.EMPTY_LIST, "Processos Pendentes - Fundos de Maneio", body.toString());
	    		    }
	    		}
	    	    } finally {
	    		pt.ist.fenixWebFramework.security.UserView.setUser(null);
	    	    }
	    	}
	    }
	}
    }

    @Override
    public String getLocalizedName() {
	return getClass().getName();
    }

}
