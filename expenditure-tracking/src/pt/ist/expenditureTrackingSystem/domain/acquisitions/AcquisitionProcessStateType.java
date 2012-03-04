/*
 * @(#)AcquisitionProcessStateType.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ResourceBundle;

import module.workflow.util.PresentableProcessState;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * @author João Alfaiate
 * 
 */
public enum AcquisitionProcessStateType implements IPresentableEnum, PresentableProcessState {

    IN_GENESIS {

	@Override
	public boolean isCompleted(final AcquisitionProcessStateType currentStateType) {
	    return currentStateType.ordinal() > ordinal();
	}

    },

    SUBMITTED_FOR_APPROVAL,

    SUBMITTED_FOR_FUNDS_ALLOCATION,

    FUNDS_ALLOCATED_TO_SERVICE_PROVIDER,

    FUNDS_ALLOCATED,

    AUTHORIZED {
	@Override
	public String getDescriptionKey() {
	    final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();
	    return instance.getRequireCommitmentNumber() != null && instance.getRequireCommitmentNumber().booleanValue() ?
		    super.getDescriptionKey() + ".requireCommitmentNumber" : super.getDescriptionKey();
	}
    },

    INVITES_SENT,

    IN_NEGOTIATION,

    NEGOTIATION_ENDED,

    SELECTED_SERVICE_PROVIDER,

    DOCUMENTATION_INSERTED,

    ACQUISITION_PROCESSED,

    INVOICE_RECEIVED,

    SUBMITTED_FOR_CONFIRM_INVOICE,

    INVOICE_CONFIRMED,

    FUNDS_ALLOCATED_PERMANENTLY,

    ACQUISITION_PAYED {

	@Override
	public boolean hasNextState() {
	    return false;
	}
    },
    REJECTED {

	@Override
	public boolean showFor(final AcquisitionProcessStateType currentStateType) {
	    return currentStateType == this;
	}

	@Override
	public boolean hasNextState() {
	    return false;
	}

	@Override
	public boolean isBlocked(final AcquisitionProcessStateType currentStateType) {
	    return true;
	}

    },
    CANCELED {

	@Override
	public boolean showFor(final AcquisitionProcessStateType currentStateType) {
	    return false;
	}

	@Override
	public boolean hasNextState() {
	    return false;
	}

	@Override
	public boolean isBlocked(final AcquisitionProcessStateType currentStateType) {
	    return true;
	}

    };

    private AcquisitionProcessStateType() {
    }

    public String getLocalizedName() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ExpenditureEnumerationResources", Language
		.getLocale());
	return resourceBundle.getString(AcquisitionProcessStateType.class.getSimpleName() + "." + name());
    }

    public boolean showFor(final AcquisitionProcessStateType currentStateType) {
	return true;
    }

    public boolean hasNextState() {
	return true;
    }

    public boolean isCompleted(final AcquisitionProcessStateType currentStateType) {
	return currentStateType.ordinal() >= ordinal();
    }

    public boolean isBlocked(final AcquisitionProcessStateType currentStateType) {
	return false;
    }

    public boolean isActive() {
	return (this != REJECTED && this != CANCELED);
    }

    public boolean isInOrPastState(final AcquisitionProcessStateType acquisitionProcessStateType) {
	return ordinal() >= acquisitionProcessStateType.ordinal();
    }

    public String getDescriptionKey() {
	return AcquisitionProcessStateType.class.getSimpleName() + "." + name() + ".description";
    }

    public String getDescription() {
    	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ExpenditureEnumerationResources", Language.getLocale());
    	return resourceBundle.getString(getDescriptionKey());
    } 

    @Override
    public boolean showFor(PresentableProcessState state) {
	return showFor((AcquisitionProcessStateType) state);
    }

}
