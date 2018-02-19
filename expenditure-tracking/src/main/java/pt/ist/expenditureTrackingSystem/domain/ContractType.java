package pt.ist.expenditureTrackingSystem.domain;

import java.text.Collator;
import java.util.TreeSet;

import org.fenixedu.commons.i18n.LocalizedString;

public class ContractType extends ContractType_Base implements Comparable<ContractType> {
    
    public ContractType(final LocalizedString name) {
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
        setName(name);
    }

    public static Object orderedTypes() {
        return new TreeSet<>(ExpenditureTrackingSystem.getInstance().getContractTypeSet());
    }

    @Override
    public int compareTo(final ContractType contentType) {
        final int c = Collator.getInstance().compare(getName().getContent(), contentType.getName().getContent());
        return c == 0 ? getExternalId().compareTo(contentType.getExternalId()) : c;
    }
    
}
