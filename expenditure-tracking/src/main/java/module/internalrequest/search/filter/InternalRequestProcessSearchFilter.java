package module.internalrequest.search.filter;

import module.internalrequest.domain.InternalRequest;
import module.internalrequest.domain.InternalRequestItem;
import module.internalrequest.domain.InternalRequestProcess;
import module.internalrequest.util.Constants;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.search.domain.DomainIndexSystem;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.DateTime;

import module.internalrequest.domain.util.InternalRequestState;

import java.util.Comparator;
import java.util.stream.Stream;

public class InternalRequestProcessSearchFilter extends SearchFilter<InternalRequestProcess> {

    private int year = DateTime.now().getYear();
    private boolean requestedByMe = false;
    private String processNumber = "";
    private String requestingPerson = "";
    private String requestingUnit = "";
    private String requestedUnit = "";
    private String item = "";
    private InternalRequestState pendingState = null;
    private boolean includeTaken = false;
    private boolean includeCancelled = false;

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        if (year < Constants.MIN_YEAR || year > Constants.MAX_YEAR) {
            this.year = DateTime.now().getYear();
        } else {
            this.year = year;
        }
    }

    public boolean getRequestedByMe() {
        return requestedByMe;
    }
    public void setRequestedByMe(boolean requestedByMe) { this.requestedByMe = requestedByMe; }

    public String getProcessNumber() {
        return processNumber;
    }
    public void setProcessNumber(String processNumber) {
        this.processNumber = cleanInput(processNumber, "");
    }

    public String getRequestingPerson() {
        return requestingPerson;
    }
    public void setRequestingPerson(String requestingPerson) {
        this.requestingPerson = cleanInput(requestingPerson, "");
    }

    public String getRequestingUnit() {
        return requestingUnit;
    }
    public void setRequestingUnit(String requestingUnit) {
        this.requestingUnit = cleanInput(requestingUnit, "");
    }

    public String getRequestedUnit() {
        return requestedUnit;
    }
    public void setRequestedUnit(String requestedUnit) {
        this.requestedUnit = cleanInput(requestedUnit, "");
    }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = cleanInput(item, ""); }

    public InternalRequestState getPendingState() {
        return pendingState;
    }
    public void setPendingState(InternalRequestState pendingState) {
        this.pendingState = pendingState;
    }

    public boolean isIncludeTaken() { return includeTaken; }
    public void setIncludeTaken(boolean includeTaken) { this.includeTaken = includeTaken; }

    public boolean isIncludeCancelled() { return includeCancelled; }
    public void setIncludeCancelled(boolean includeCancelled) { this.includeCancelled = includeCancelled; }

    @Override
    public String getDefaultSort() { return "processNumber"; }

    @Override
    public Comparator<InternalRequestProcess> getDefaultComparator() {
        return InternalRequestProcess.COMPARATOR_BY_PROCESS_NUMBER;
    }

    @Override
    public Comparator<InternalRequestProcess> getComparator() {
        switch (this.getSort()) {
            case "requestingPerson":
                return Comparator.comparing(p -> p.getInternalRequest().getRequestingPerson().getPresentationName());
            case "requestingUnit":
                return Comparator.comparing(p -> p.getInternalRequest().getRequestingUnit().getName());
            case "requestedUnit":
                return Comparator.comparing(p -> p.getInternalRequest().getRequestedUnit().getName());
            default:
                return this.getDefaultComparator();
        }
    }

    @Override
    public Stream<InternalRequestProcess> getItems() {
        if (this.getRequestedByMe()) {
            return Authenticate.getUser().getPerson()
                    .getRequestedInternalRequestsSet().stream().map(InternalRequest::getInternalRequestProcess);
        }
        return DomainIndexSystem.getInstance().search(this.getYear(), (i) -> i.getInternalRequestProcessSet().stream());
    }

    @Override
    public boolean includeInSearch(InternalRequestProcess process) {
        if (!process.isAccessibleToCurrentUser()) { return false; }
        if (!includeTaken && process.getCurrentOwner() != null) { return false; }
        if (!includeCancelled && process.getIsCancelled()) { return false; }

        boolean passYear = process.getCreationDate().getYear() == this.getYear();

        String searchProcessNumber = StringNormalizer.normalize(this.getProcessNumber());
        String processNumber = StringNormalizer.normalize(process.getProcessNumber());
        boolean passProcessNumber = processNumber.contains(searchProcessNumber);

        String searchRequestingPerson = StringNormalizer.normalizeAndRemoveAccents(this.getRequestingPerson()).toLowerCase();
        String requestingPersonFullName =
                StringNormalizer.normalizeAndRemoveAccents(process.getInternalRequest().getRequestingPerson().getName()).toLowerCase();
        boolean passRequestingPerson = true;
        for (final String namePart : searchRequestingPerson.split(" ")) {
            if (!requestingPersonFullName.contains(namePart)) {
                passRequestingPerson = false;
                break;
            }
        }
        String requestingPersonUsername = process.getInternalRequest().getRequestingPerson().getUser().getUsername().toLowerCase();
        passRequestingPerson = passRequestingPerson || requestingPersonUsername.contains(searchRequestingPerson);

        String searchRequestingUnit = StringNormalizer.normalizeAndRemoveAccents(this.getRequestingUnit().toLowerCase());
        String requestingUnitName =
                StringNormalizer.normalizeAndRemoveAccents(process.getInternalRequest().getRequestingUnit().getName()).toLowerCase();
        boolean passRequestingUnit = true;
        for (final String namePart : searchRequestingUnit.split(" ")) {
            if (!requestingUnitName.contains(namePart)) {
                passRequestingUnit = false;
                break;
            }
        }

        String searchRequestedUnit = StringNormalizer.normalizeAndRemoveAccents(this.getRequestedUnit()).toLowerCase();
        String requestedUnitName =
                StringNormalizer.normalizeAndRemoveAccents(process.getInternalRequest().getRequestedUnit().getName()).toLowerCase();
        boolean passRequestedUnit = true;
        for (final String namePart : searchRequestedUnit.split(" ")) {
            if (!requestedUnitName.contains(namePart)) {
                passRequestedUnit = false;
                break;
            }
        }

        boolean passItem = false;
        if (this.getItem().isEmpty()) {
            passItem = true;
        } else {
            String searchItem = StringNormalizer.normalizeAndRemoveAccents(this.getItem()).toLowerCase();
            for (final InternalRequestItem item : process.getInternalRequest().getItemsSet()) {
                String itemDesc = StringNormalizer.normalizeAndRemoveAccents(item.getDescription()).toLowerCase();
                for (final String itemPart : searchItem.split(" ")) {
                    if (itemDesc.contains(itemPart)) {
                        passItem = true;
                        break;
                    }
                }
                if (passItem) { break; }
            }
        }

        boolean passPendingState = this.getPendingState() == null || this.getPendingState().isPending(process);


        return passYear && passProcessNumber && passRequestingPerson && passRequestingUnit && passRequestedUnit && passItem
                && passPendingState;
    }
}
