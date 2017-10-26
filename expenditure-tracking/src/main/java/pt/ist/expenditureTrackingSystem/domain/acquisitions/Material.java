package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.Iterator;

public class Material extends Material_Base {
    
    public Material() {
        super();
    }
    
    public void delete() {
        deleteDomainObject();
    }
    
    @Override
    public void setMaterialCpv(CPVReference cpv){
        
        super.setMaterialCpv(cpv);
        
        final Iterator<RequestItem> itemIterator =   getAcquisitionRequestItemSet().iterator();
        
        while (itemIterator.hasNext()) {
            itemIterator.next().setCPVReference(cpv);
        }
        
    }
    
}
