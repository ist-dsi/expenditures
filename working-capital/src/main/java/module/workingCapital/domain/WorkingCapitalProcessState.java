/*
 * @(#)WorkingCapitalProcessState.java
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
package module.workingCapital.domain;

import java.util.ResourceBundle;

import module.workflow.util.PresentableProcessState;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * 
 * @author Luis Cruz
 * 
 */
public enum WorkingCapitalProcessState implements PresentableProcessState {

    PENDING_ACCEPT_RESPONSIBILITY,
    PENDING_APPROVAL,
    PENDING_VERIFICATION,
    PENDING_FUND_ALLOCATION,
    PENDING_AUTHORIZATION,
    PENDING_PAYMENT,
    WORKING_CAPITAL_AVAILABLE,
    SENT_FOR_TERMINATION,
    SENT_FOR_FUND_REFUND,
    TERMINATED,
    CANCELED(false) {

	@Override
	public boolean showFor(PresentableProcessState state) {
	    return false;
	}

    }
    ;

    private final boolean isVisibleState;

    private WorkingCapitalProcessState(final boolean isVisibleState) {
	this.isVisibleState = isVisibleState;
    }

    private WorkingCapitalProcessState() {
	this(true);
    }

    private String getResource(final String suffix) {
    	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.WorkingCapitalResources", Language.getLocale());
    	return resourceBundle.getString(WorkingCapitalProcessState.class.getSimpleName() + "." + name() + suffix);	
    }

    @Override
    public String getDescription() {
	return getResource(".description");
    }

    @Override
    public String getLocalizedName() {
	return getResource("");
    }

    @Override
    public boolean showFor(final PresentableProcessState state) {
	return true;
    }

    public boolean isVisibleState() {
        return isVisibleState;
    }

}
