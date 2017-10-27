package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class Material extends Material_Base {
    
    public Material(String materialSapId, String description, String measurementUnit, String type, CPVReference cpv) {
        this.setMaterialSapId(materialSapId);
        this.setDescription(description);
        this.setMeasurementUnit(measurementUnit);
        this.setType(type);
        this.setMaterialCpv(cpv);

        this.setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }
    
}
