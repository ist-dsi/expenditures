package module.internalrequest.domain;

import module.finance.util.Money;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class InternalRequestItem extends InternalRequestItem_Base {
    
    public InternalRequestItem(int quantity, String description, InternalRequest request) {
        this.setQuantity(quantity);
        this.setDescription(description);
        this.setInternalRequest(request);
    }

    @Atomic(mode = TxMode.WRITE)
    public void budget(Money price, String observations) {
        this.setPrice(price);
        this.setObservations(observations);
    }

    @Atomic(mode = TxMode.WRITE)
    public void revert() {
        this.setPrice(null);
        this.setObservations(null);
    }

    @Atomic(mode = TxMode.WRITE)
    public void delete() {
        this.setInternalRequest(null);
        super.deleteDomainObject();
    }
    
}
