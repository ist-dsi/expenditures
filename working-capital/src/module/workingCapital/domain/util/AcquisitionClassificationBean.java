package module.workingCapital.domain.util;

import java.io.Serializable;

import module.workingCapital.domain.AcquisitionClassification;
import pt.ist.fenixWebFramework.services.Service;

public class AcquisitionClassificationBean implements Serializable {

    private String description;
    private String economicClassification;
    private String pocCode;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEconomicClassification() {
        return economicClassification;
    }

    public void setEconomicClassification(String economicClassification) {
        this.economicClassification = economicClassification;
    }

    public String getPocCode() {
        return pocCode;
    }

    public void setPocCode(String pocCode) {
        this.pocCode = pocCode;
    }

    @Service
    public AcquisitionClassification create() {
	return new AcquisitionClassification(description, economicClassification, pocCode);
    }

}
