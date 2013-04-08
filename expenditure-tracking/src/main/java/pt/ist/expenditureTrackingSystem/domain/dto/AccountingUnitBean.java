package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

public class AccountingUnitBean implements Serializable {

    private String name;

    public AccountingUnitBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
