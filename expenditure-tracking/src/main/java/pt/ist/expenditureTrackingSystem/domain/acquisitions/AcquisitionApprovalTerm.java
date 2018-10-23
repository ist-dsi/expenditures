package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

public class AcquisitionApprovalTerm extends AcquisitionApprovalTerm_Base implements Comparable<AcquisitionApprovalTerm> {

    public AcquisitionApprovalTerm(RegularAcquisitionProcess process, Person person) {
        super();
        setProcess(process);
        setApprover(person);
        setDate(new DateTime());
        if (ExpenditureTrackingSystem.getInstance().getApprovalTextForRapidAcquisitions() == null) {
            throw new DomainException(Bundle.EXPENDITURE, "error.acquisition.approval.term.without.approval.message");
        }
        setApprovalMessage(ExpenditureTrackingSystem.getInstance().getApprovalTextForRapidAcquisitions());
    }

    @Override
    public int compareTo(AcquisitionApprovalTerm arg0) {
        int compare = getDate().compareTo(arg0.getDate());
        return compare == 0 ? getApprover().getUsername().compareTo(arg0.getApprover().getUsername()) : compare;
    }

}
