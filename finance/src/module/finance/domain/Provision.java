package module.finance.domain;

import myorg.domain.util.Money;

public abstract class Provision extends Provision_Base {
    
    public Provision() {
        super();
        setFinanceSystem(FinanceSystem.getInstance());
    }

    public abstract Money getValueAllocatedToSupplier();

    public abstract Money getValueAllocatedToSupplierForLimit();
    
}
