/*
 * @(#)RefundProcessStateType.java
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
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public enum RefundProcessStateType implements IPresentableEnum, PresentableProcessState {

    IN_GENESIS {

        @Override
        public boolean isCompleted(final RefundProcessStateType currentStateType) {
            return currentStateType.ordinal() > ordinal();
        }

    },

    SUBMITTED_FOR_APPROVAL,

    APPROVED,

    FUNDS_ALLOCATED,

    AUTHORIZED,

    SUBMITTED_FOR_INVOICE_CONFIRMATION,

    INVOICES_CONFIRMED,

    FUNDS_ALLOCATED_PERMANENTLY,

    REFUNDED {
        @Override
        public boolean hasNextState() {
            return false;
        }
    },

    CANCELED {

        @Override
        public boolean showFor(final RefundProcessStateType currentStateType) {
            return false;
        }

        @Override
        public boolean hasNextState() {
            return false;
        }

        @Override
        public boolean isBlocked(final RefundProcessStateType currentStateType) {
            return true;
        }

    };

    private RefundProcessStateType() {
    }

    @Override
    public String getLocalizedName() {
        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle("resources.ExpenditureEnumerationResources", Language.getLocale());
        return resourceBundle.getString(RefundProcessStateType.class.getSimpleName() + "." + name());
    }

    public boolean showFor(final RefundProcessStateType currentStateType) {
        return true;
    }

    public boolean hasNextState() {
        return true;
    }

    public boolean isCompleted(final RefundProcessStateType currentStateType) {
        return currentStateType.ordinal() >= ordinal();
    }

    public boolean isBlocked(final RefundProcessStateType currentStateType) {
        return false;
    }

    public boolean isActive() {
        return (this != CANCELED);
    }

    @Override
    public String getDescription() {
        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle("resources.ExpenditureEnumerationResources", Language.getLocale());
        return resourceBundle.getString(RefundProcessStateType.class.getSimpleName() + "." + name() + ".description");
    }

    @Override
    public boolean showFor(PresentableProcessState state) {
        return showFor((RefundProcessStateType) state);
    }

}
