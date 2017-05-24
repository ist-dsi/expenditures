package pt.ist.internalBilling.domain;

import java.util.Comparator;

import com.google.gson.JsonObject;

import module.finance.util.Money;

public abstract class BillableTransaction extends BillableTransaction_Base {
    
    public static final Comparator<BillableTransaction> COMPARATOR_BY_DATE =
            (bt1, bt2) -> bt1.getTxDate().compareTo(bt2.getTxDate());

    protected BillableTransaction() {
    }

    public abstract JsonObject getDetails();

    public abstract String getDescription();

    public abstract Money getTxValue();

}
