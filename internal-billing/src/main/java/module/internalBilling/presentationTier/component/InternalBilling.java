/*
 * @(#)InternalBilling.java
 *
 * Copyright 2013 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The InternalBilling Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The InternalBilling Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.internalBilling.presentationTier.component;

import java.util.Map;

import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@EmbeddedComponent(path = { "InternalBilling" }, args = { } )
/**
 * 
 * @author Luis Cruz
 * 
 */
public class InternalBilling extends CustomComponent implements EmbeddedComponentContainer {

    public InternalBilling() {
    }

    @Override
    public boolean isAllowedToOpen(final Map<String, String> arguments) {
        return true;
    }

    @Override
    public void setArguments(final Map<String, String> arguments) {
    }

    @Override
    public void attach() {
	final VerticalLayout layout = new VerticalLayout();
	setCompositionRoot(layout);
	layout.setMargin(false);
	layout.setSpacing(false);
	layout.setSizeFull();

	layout.addComponent(new Label("Hello"));

	super.attach();
    }

}
