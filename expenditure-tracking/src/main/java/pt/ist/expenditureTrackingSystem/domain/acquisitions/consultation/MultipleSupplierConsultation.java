package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import pt.ist.expenditureTrackingSystem.domain.ContractType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;

public class MultipleSupplierConsultation extends MultipleSupplierConsultation_Base {
    
    public MultipleSupplierConsultation(final MultipleSupplierConsultationProcess process, final String description,
            final Material material, final String justification, final ContractType contractType) {
        setProcess(process);
        edit(description, material, justification, contractType);
    }

    public void edit(final String description, final Material material, final String justification, final ContractType contractType) {
        setDescription(description);
        setMaterial(material);
        setJustification(justification);
        setContractType(contractType);        
    }
    
}
