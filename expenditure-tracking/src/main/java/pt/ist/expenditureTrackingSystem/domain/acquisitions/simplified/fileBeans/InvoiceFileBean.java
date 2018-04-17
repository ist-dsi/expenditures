/*
 * @(#)InvoiceFileBean.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

import module.finance.util.Money;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class InvoiceFileBean extends WorkflowFileUploadBean {

    private String invoiceNumber;
    private LocalDate invoiceDate;
    private List<RequestItemHolder> items;
    private AcquisitionRequest request;
    private Boolean hasMoreInvoices = Boolean.TRUE;

    public class RequestItemHolder implements Serializable {
        private boolean accountable;
        private AcquisitionRequestItem item;
        private String description;
        private int amount;
        private Money unitValue;
        private BigDecimal vatValue;
        private Money additionalCostValue;

        RequestItemHolder(AcquisitionRequestItem item) {
            this.accountable = true;
            this.description = item.getDescription();
            this.item = item;
            this.amount = item.getCurrentQuantity();
            this.unitValue = item.getCurrentUnitValue();
            this.vatValue = item.getVatValue();
            this.additionalCostValue = item.getAdditionalCostValue();
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isAccountable() {
            return accountable;
        }

        public void setAccountable(boolean accountable) {
            this.accountable = accountable;
        }

        public AcquisitionRequestItem getItem() {
            return item;
        }

        public void setItem(AcquisitionRequestItem item) {
            this.item = item;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public Money getUnitValue() {
            return unitValue;
        }

        public void setUnitValue(Money unitValue) {
            this.unitValue = unitValue;
        }

        public BigDecimal getVatValue() {
            return vatValue;
        }

        public void setVatValue(BigDecimal vatValue) {
            this.vatValue = vatValue;
        }

        public Money getAdditionalCostValue() {
            return additionalCostValue;
        }

        public void setAdditionalCostValue(Money additionalCostValue) {
            this.additionalCostValue = additionalCostValue;
        }

    }

    public Boolean getHasMoreInvoices() {
        return hasMoreInvoices;
    }

    public void setHasMoreInvoices(Boolean hasMoreInvoices) {
        this.hasMoreInvoices = hasMoreInvoices;
        if (hasMoreInvoices) {
            setExtraArguments(BundleUtil.getString("resources/AcquisitionResources", "acquisitionProcess.label.invoice.partial"));
        } else {
            setExtraArguments("");
        }
    }

    public InvoiceFileBean(WorkflowProcess process) {
        super(process);
        AcquisitionRequest request = ((PaymentProcess) process).getRequest();
        setRequest(request);
        this.items = new ArrayList<RequestItemHolder>();
        request.getAcquisitionRequestItemStream().forEach(i -> this.items.add(new RequestItemHolder(i)));
    }

    public InvoiceFileBean(AcquisitionRequest request) {
        super(request.getProcess());
        setRequest(request);
        this.items = new ArrayList<RequestItemHolder>();
        request.getAcquisitionRequestItemStream().forEach(i -> this.items.add(new RequestItemHolder(i)));
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public List<RequestItemHolder> getItems() {
        return items;
    }

    public void setItems(List<RequestItemHolder> items) {
        this.items = items;
    }

    public void setRequest(AcquisitionRequest request) {
        this.request = request;
    }

    public AcquisitionRequest getRequest() {
        return this.request;
    }

    @Override
    public boolean isDefaultUploadInterfaceUsed() {
        return false;
    }
}
