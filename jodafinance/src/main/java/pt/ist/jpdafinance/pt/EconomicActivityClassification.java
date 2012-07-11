/* 
* @(#)EconomicActivityClassification.java 
* 
* Copyright 2012 Instituto Superior Tecnico 
* Founding Authors: Luis Cruz, Susana Fernandes 
*  
*      https://fenix-ashes.ist.utl.pt/ 
*  
*   This file is part of the JodaFinance library. 
* 
*   The JodaFinance library is free software: you can 
*   redistribute it and/or modify it under the terms of the GNU Lesser General 
*   Public License as published by the Free Software Foundation, either version  
*   3 of the License, or (at your option) any later version. 
* 
*   JodaFinance is distributed in the hope that it will be useful, 
*   but WITHOUT ANY WARRANTY; without even the implied warranty of 
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
*   GNU Lesser General Public License for more details. 
* 
*   You should have received a copy of the GNU Lesser General Public License 
*   along with JodaFinance. If not, see <http://www.gnu.org/licenses/>. 
*  
*/
package pt.ist.jpdafinance.pt;

import java.io.Serializable;

/**
 * 
 * @author  Luis Cruz
 * @author  Susana Fernandes
 * 
*/
public abstract class EconomicActivityClassification implements Comparable<EconomicActivityClassification>, Serializable {

    public final String code;
    public final String description;

    public EconomicActivityClassification(final String code, final String description) {
	this.code = code;
	this.description = description;
    }

    @Override
    public int compareTo(final EconomicActivityClassification eac) {
	return code.compareTo(eac.code);
    }

    public String exportAsString() {
	return code;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getExportAsString() {
	return exportAsString();
    }
}
