package module.internalrequest.domain;

import module.finance.util.Money;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class InternalRequestItem extends InternalRequestItem_Base implements Comparable<InternalRequestItem> {

    public InternalRequestItem(int quantity, String description, InternalRequest request) {
        setItemOrder(request.getItemsSet().stream().mapToInt(i -> i.getItemOrder()).max().orElse(0) + 1);
        setQuantity(quantity);
        setDescription(description);
        setInternalRequest(request);
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

    @Override
    public int compareTo(final InternalRequestItem item) {
        final int i = Integer.compare(getItemOrder(), item.getItemOrder());
        return i == 0 ? getExternalId().compareTo(item.getExternalId()) : i;
    }

}
