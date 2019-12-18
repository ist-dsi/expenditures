package pt.ist.internalBilling.domain;

import java.util.Comparator;

import com.google.gson.JsonObject;

import module.finance.util.Money;
import pt.ist.fenixframework.Atomic;

public abstract class BillableTransaction extends BillableTransaction_Base {
    
    public static final Comparator<BillableTransaction> COMPARATOR_BY_DATE =
            (bt1, bt2) -> {
                final int i = bt1.getTxDate().compareTo(bt2.getTxDate());
                return i == 0 ? bt1.getExternalId().compareTo(bt2.getExternalId()) : i;
            };

    protected BillableTransaction() {
    }

    public abstract JsonObject getDetails();

    public abstract String getDescription();

    public abstract Money getTxValue();

    public abstract String getServiceCode(); 

    public abstract int getCount(); 
    
    @Atomic
    public void changeBillable(Billable billable) {
        this.setBillable(billable);        
    }
}
