/*
 * @(#)AcquisitionClassificationBean.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workingCapital.domain.util;

import java.io.Serializable;

import module.workingCapital.domain.AcquisitionClassification;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Luis Cruz
 * 
 */
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
