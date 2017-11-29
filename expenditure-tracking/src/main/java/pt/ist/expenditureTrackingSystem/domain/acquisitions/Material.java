package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.text.Collator;
import java.util.Comparator;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class Material extends Material_Base {

    public static Comparator<Material> COMPARATOR_BY_DESCRIPTION = new Comparator<Material>() {

        @Override
        public int compare(final Material o1, final Material o2) {
            return Collator.getInstance().compare(o1.getDescription(), o2.getDescription());
        }

    };

    public static Comparator<Material> COMPARATOR_BY_CODE = new Comparator<Material>() {

        @Override
        public int compare(final Material o1, final Material o2) {
            if (o1 == null && o2 == null) {
                return 0;
            }
            if (o1 == null) {
                return 1;
            }
            return o2 == null ? -1 : o1.getMaterialSapId().compareTo(o2.getMaterialSapId());
        }

    };

    public Material(String materialSapId, String description, String measurementUnit, String type, CPVReference cpv) {
        this.setMaterialSapId(materialSapId);
        this.setDescription(description);
        this.setMeasurementUnit(measurementUnit);
        this.setType(type);
        this.setMaterialCpv(cpv);

        this.setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public String getFullDescription() {
        return getMaterialSapId() + " - " + getDescription() + " (CPV: " + getMaterialCpv().getCode() + ")";
    }
}
