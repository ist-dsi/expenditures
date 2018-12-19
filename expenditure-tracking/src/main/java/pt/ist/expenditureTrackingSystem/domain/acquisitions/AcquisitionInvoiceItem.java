package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;

import module.finance.util.Money;

public class AcquisitionInvoiceItem extends AcquisitionInvoiceItem_Base {

    public AcquisitionInvoiceItem(final AcquisitionInvoice invoice, final AcquisitionRequestItem item, final Integer quantity,
            final Money unitValue, final BigDecimal vatValue, final Money additionalCostValue) {
        setInvoice(invoice);
        setItem(item);
        setQuantity(quantity);
        setUnitValue(unitValue);
        setVatValue(vatValue);
        setAdditionalCostValue(additionalCostValue);
    }

    public Money getTotalValue() {
        return getUnitValue().multiply(getQuantity().longValue()).addPercentage(getVatValue());
    }

    public Money getVatAmount() {
        return getUnitValue().multiply(getQuantity().longValue()).percentage(getVatValue());
    }
    
    public void delete() {
        setInvoice(null);
        setItem(null);
        deleteDomainObject();
    }
}
