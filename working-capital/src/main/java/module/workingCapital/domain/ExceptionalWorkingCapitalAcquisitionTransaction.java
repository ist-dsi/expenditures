package module.workingCapital.domain;

import module.organization.domain.Accountability;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.bennu.core.util.BundleUtil;

public class ExceptionalWorkingCapitalAcquisitionTransaction extends ExceptionalWorkingCapitalAcquisitionTransaction_Base {

    private static final String WORKING_CAPITAL_RESOURCES = "resources/WorkingCapitalResources";

    public ExceptionalWorkingCapitalAcquisitionTransaction() {
        super();
    }

    public ExceptionalWorkingCapitalAcquisitionTransaction(final WorkingCapitalAcquisition workingCapitalAcquisition,
            final Money value) {
        setWorkingCapital(workingCapitalAcquisition.getWorkingCapital());
        setWorkingCapitalAcquisition(workingCapitalAcquisition);
        addValue(value);
    }

    @Override
    public boolean isApproved() {
        final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
        return (workingCapitalAcquisition.getApproved() != null) && isManagementApproved();
    }

    public boolean isPendingManagementApproval() {
        return (getApprovalByManagement() == null) && !isCanceledOrRejected();
    }

    public boolean isPendingManagementApprovalByUser(User user) {
        final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstanceForCurrentHost();
        final Accountability accountability = workingCapitalSystem.getManagementAccountability(user);
        return isPendingManagementApproval() && accountability != null;
    }

    public boolean isManagementApproved() {
        return getApprovalByManagement() != null;
    }

    public void approveByManagement(final User user) {
        final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstanceForCurrentHost();
        final Accountability accountability = workingCapitalSystem.getManagementAccountability(user);
        if (accountability == null) {
            throw new DomainException("error.person.cannot.authorize.expense", user.getPerson().getName());
        }
        setApprovalByManagement(new DateTime());
        setManagementApprover(accountability);
    }

    public void rejectByManagement(final User user) {
        final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstanceForCurrentHost();
        final Accountability accountability = workingCapitalSystem.getManagementAccountability(user);
        if (accountability == null) {
            throw new DomainException("error.person.cannot.authorize.expense", user.getPerson().getName());
        }
        final WorkingCapitalAcquisition workingCapitalAcquisition = getWorkingCapitalAcquisition();
        reject(user);
        setRejectByManagement(new DateTime());
        setManagementApprover(accountability);
    }

    @Override
    public void approve(final User user) {
        if (!isManagementApproved()) {
            throw new DomainException("error.missing.management.aproval", user.getPerson().getName());
        }
        super.approve(user);

    }

    @Override
    public boolean isPendingApproval() {
        return super.isPendingApproval() && !isPendingManagementApproval();
    }

    @Override
    public void reject(final User user) {
        super.reject(user);
    }

    @Override
    public void addValue(final Money value) {
        super.addUncheckedValue(value);
    }

    @Override
    public DateTime getRejectByManagement() {
        return super.getRejectByManagement();
    }

    @Override
    public boolean isExceptionalAcquisition() {
        return true;
    }

    @Override
    public boolean isCanceledOrRejected() {
        return super.isCanceledOrRejected() || getRejectByManagement() != null;
    }

    @Override
    public void resetValue(final Money value) {
        Money limit = getWorkingCapitalSystem().getAcquisitionValueLimit();
        if ((limit != null) && (value.compareTo(limit) < 1)) {
            throw new DomainException(BundleUtil.getStringFromResourceBundle(WORKING_CAPITAL_RESOURCES,
                    "error.acquisition.exceptional.lower.limit.exceeded"));
        }
        super.resetUncheckedValue(value);
    }
}
