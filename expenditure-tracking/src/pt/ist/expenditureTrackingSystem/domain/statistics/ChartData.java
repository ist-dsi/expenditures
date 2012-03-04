/*
 * @(#)ChartData.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.statistics;

import org.jfree.data.CategoryDataset;
import org.jfree.data.DefaultCategoryDataset;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ChartData {

    private final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private char c = 'A';

    private String title;

    protected void registerData(final String key, final Number number) {
	dataset.addValue(number, "" + c + " - " + key, Character.valueOf(nextChar()));
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    public CategoryDataset getDataset() {
	return dataset;
    }

    public String getTitle() {
        return title;
    }

    protected char nextChar() {
	return c == 'Z' ? c = 'a' : c++;
    }

}
