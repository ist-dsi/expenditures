package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

public class TieBreakCriteria extends TieBreakCriteria_Base implements Comparable<TieBreakCriteria> {
    
    public TieBreakCriteria(final MultipleSupplierConsultation consultation, final String description) {
        super();
        setCriteriaOrder(consultation.getTieBreakCriteriaSet().size() + 1);
        setConsultation(consultation);
        setDescription(description);
    }

    @Override
    public int compareTo(final TieBreakCriteria criteria) {
        final int c = getCriteriaOrder().compareTo(criteria.getCriteriaOrder());
        return c == 0 ? getExternalId().compareTo(criteria.getExternalId()) : c;
    }

    public void delete() {
        setConsultation(null);
        deleteDomainObject();
    }
    
}
