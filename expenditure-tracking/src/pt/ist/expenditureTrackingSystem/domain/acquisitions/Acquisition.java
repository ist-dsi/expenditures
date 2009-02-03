package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.dto.PayingUnitTotalBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import myorg.domain.util.ByteArray;
import myorg.domain.util.Money;
import pt.ist.fenixframework.pstm.Transaction;

public abstract class Acquisition extends Acquisition_Base {

    public void receiveInvoice(final String filename, final byte[] bytes, final String invoiceNumber, final LocalDate invoiceDate) {
	final Invoice invoice = hasInvoice() ? getInvoice() : new Invoice(this);
	invoice.setFilename(filename);
	invoice.setContent(new ByteArray(bytes));
	invoice.setInvoiceNumber(invoiceNumber);
	invoice.setInvoiceDate(invoiceDate);
    }

    public String getInvoiceNumber() {
	final Invoice invoice = getInvoice();
	return invoice == null ? null : invoice.getInvoiceNumber();
    }

    public LocalDate getInvoiceDate() {
	final Invoice invoice = getInvoice();
	return invoice == null ? null : invoice.getInvoiceDate();
    }

    public boolean isInvoiceReceived() {
	final Invoice invoice = getInvoice();
	return invoice != null && invoice.isInvoiceReceived();
    }

    public void delete() {
	if (hasInvoice()) {
	    getInvoice().delete();
	}
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

    public List<PayingUnitTotalBean> getTotalAmountsForEachPayingUnit() {
	List<PayingUnitTotalBean> beans = new ArrayList<PayingUnitTotalBean>();
	for (Financer financer : getFinancers()) {
	    beans.add(new PayingUnitTotalBean(financer));
	}
	return beans;
    }

    public boolean hasPayingUnit(Unit unit) {
	Financer financer = getFinancer(unit);
	return financer != null;
    }

    public void removePayingUnit(Unit unit) {
	Financer financer = getFinancer(unit);
	if (financer != null) {
	    financer.delete();
	}
    }

    public Financer addPayingUnit(final Unit unit) {
	Financer financer = getFinancer(unit);
	return financer != null ? financer : unit.finance(this);
    }

    public Financer getFinancer(Unit unit) {
	for (Financer financer : getFinancers()) {
	    if (financer.getUnit() == unit) {
		return financer;
	    }
	}
	return null;
    }

    public Set<Unit> getPayingUnits() {
	Set<Unit> res = new HashSet<Unit>();
	for (Financer financer : getFinancers()) {
	    res.add(financer.getUnit());
	}
	return res;
    }

    public Money getAmountAllocatedToUnit(Unit unit) {
	Financer financer = getFinancer(unit);
	return financer == null ? Money.ZERO : financer.getAmountAllocated();
    }

    public Set<Financer> getFinancersWithFundsAllocated() {
	Set<Financer> res = new HashSet<Financer>();
	for (Financer financer : getFinancers()) {
	    if (financer.getAmountAllocated().isPositive()) {
		res.add(financer);
	    }
	}
	return res;
    }

    public Set<Financer> getFinancersWithFundsAllocated(Person person) {
	Set<Financer> res = new HashSet<Financer>();
	for (Financer financer : getFinancers()) {
	    if (financer.isAccountingEmployee(person) && financer.getAmountAllocated().isPositive()) {
		res.add(financer);
	    }
	}
	return res;
    }

    public Set<ProjectFinancer> getProjectFinancersWithFundsAllocated() {
	Set<ProjectFinancer> res = new HashSet<ProjectFinancer>();
	for (Financer financer : getFinancers()) {
	    if (financer instanceof ProjectFinancer && financer.getAmountAllocated().isPositive()) {
		res.add((ProjectFinancer) financer);
	    }
	}
	return res;
    }

    public Set<ProjectFinancer> getProjectFinancersWithFundsAllocated(Person person) {
	Set<ProjectFinancer> res = new HashSet<ProjectFinancer>();
	for (Financer financer : getFinancers()) {
	    if (financer instanceof ProjectFinancer && financer.isProjectAccountingEmployee(person)
		    && financer.getAmountAllocated().isPositive()) {
		res.add((ProjectFinancer) financer);
	    }
	}
	return res;
    }
}
