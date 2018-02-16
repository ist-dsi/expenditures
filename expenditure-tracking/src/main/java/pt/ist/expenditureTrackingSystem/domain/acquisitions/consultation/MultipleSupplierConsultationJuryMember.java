package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import org.fenixedu.bennu.core.domain.User;

public class MultipleSupplierConsultationJuryMember extends MultipleSupplierConsultationJuryMember_Base implements Comparable<MultipleSupplierConsultationJuryMember> {
    
    public MultipleSupplierConsultationJuryMember(final MultipleSupplierConsultation consultation, final User user) {
        super();
        setConsultation(consultation);
        setUser(user);
    }

    @Override
    public int compareTo(final MultipleSupplierConsultationJuryMember member) {
        final int c = getJuryMemberRole().compareTo(member.getJuryMemberRole());
        return c == 0 ? getExternalId().compareTo(member.getExternalId()) : c;
    }

}
