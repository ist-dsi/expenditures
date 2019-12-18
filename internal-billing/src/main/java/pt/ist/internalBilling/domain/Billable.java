package pt.ist.internalBilling.domain;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import pt.ist.expenditureTrackingSystem.domain.organization.SubProject;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.Atomic;
import pt.ist.internalBilling.BillingInformationHook;

public class Billable extends Billable_Base {

    Billable(final BillableService billableService, final Unit financer, final Beneficiary beneficiary) {
        setBillableService(billableService);
        if (!(financer instanceof SubProject)) {
            throw new Error("only.cost.centers.are.allowed.to.be.financers");
        }
        if (!financer.isActive()) {
            throw new Error("selected.unit.is.not.active");
        }
        setUnit(financer);
        setBeneficiary(beneficiary);
        setBillableStatus(BillableStatus.PENDING_AUTHORIZATION);
        setServiceStatus(ServiceStatus.PENDING_ACTIVATION);
    }

    void log(final String description) {
        new BillableLog(this, description);
    }

    @Atomic
    public void authorize() {
        if (getBillableStatus() == BillableStatus.PENDING_AUTHORIZATION) {
            setBillableStatus(BillableStatus.AUTHORIZED);
            log("Authorized subscription of service " + getBillableService().getTitle() + " for unit "
                    + getUnit().getPresentationName() + " to user " + getBeneficiary().getPresentationName()
                    + " with configuration " + getConfiguration());
            BillingInformationHook.HOOKS.forEach(h -> h.authorize(this));
        }
    }

    @Atomic
    public void revoke() {
        if (getBillableStatus() != BillableStatus.REVOKED) {
            setBillableStatus(BillableStatus.REVOKED);
            log("Revoked subscription of service " + getBillableService().getTitle() + " for unit "
                    + getUnit().getPresentationName() + " to user " + getBeneficiary().getPresentationName()
                    + " with configuration " + getConfiguration());
            BillingInformationHook.HOOKS.forEach(h -> h.revoke(this));
        }
    }

    protected JsonObject getConfigurationAsJson() {
        final String config = getConfiguration();
        return config == null ? new JsonObject() : new JsonParser().parse(config).getAsJsonObject();
    }

    public boolean isAllowedToView(final User user) {
        final Unit unit = getUnit();
        return InternalBillingService.canViewUnitServices(user, unit) || isBenificiary(user);
    }

    public boolean isBenificiary(final User user) {
        final Beneficiary beneficiary = getBeneficiary();
        return beneficiary instanceof UserBeneficiary && ((UserBeneficiary) beneficiary).getUser() == user;
    }

    @Atomic
    public void setUserFromCurrentBillable(final User user) {
        new CurrentBillableHistory(this, user);
        log("Set current billing unit to " + getUnit().getPresentationName() + " for user " + getBeneficiary().getPresentationName());
    }

    public static Billable forUserOnDate(final User user, final DateTime dt) {
        final CurrentBillableHistory current = user.getCurrentBillableHistory();
        return forUserOnDate(user, dt, current);
    }

    public static Billable forUserOnDate(final User user, final DateTime dt, final CurrentBillableHistory current) {
        if (current != null) {
            if (current.getWhenInstant().isBefore(dt)) {
                final Billable billable = current.getBillable();
                if (billable.getBillableStatus() == BillableStatus.AUTHORIZED) {
                    return current.getBillable();
                }
            }
            return forUserOnDate(user, dt, current.getPreviouseHistory());
        }
        return null;
    }
    
    
    @Atomic
    public void addTransactionToUnit(BillableTransaction tx) {
        this.addBillableTransaction(tx);        
    }
    
    
    @Atomic
    public void removeTransactionFromUnit(BillableTransaction tx, Billable billableTo) {   
        this.removeBillableTransaction(tx);
        tx.setBillable(billableTo);        
    }

}
