/*
 * @(#)AddressRenderer.java
 *
 * Copyright 2009 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import pt.ist.bennu.core.domain.util.Address;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class AddressRenderer extends OutputRenderer {

    private String styleClasses;

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Address address = (Address) object;
                if (address != null) {
                    HtmlTable htmlTable = new HtmlTable();
                    htmlTable.setStyle(getStyleClasses());
                    htmlTable.createRow().createCell().setBody(new HtmlText(address.getLine1()));

                    if (address.getLine2() != null && address.getLine2().length() != 0) {
                        htmlTable.createRow().createCell().setBody(new HtmlText(address.getLine2()));
                    }

                    htmlTable.createRow().createCell()
                            .setBody(new HtmlText(address.getPostalCode() + ", " + address.getLocation()));
                    htmlTable.createRow().createCell().setBody(new HtmlText(address.getCountry()));
                    return htmlTable;
                } else {
                    return new HtmlText("");
                }
            }

        };
    }

    public void setStyleClasses(String styleClasses) {
        this.styleClasses = styleClasses;
    }

    public String getStyleClasses() {
        return styleClasses;
    }

}
