/*
 * @(#)Authorize.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Diogo Figueiredo
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class Authorize<P extends PaymentProcess> extends WorkflowActivity<P, ActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
        Person person = user.getExpenditurePerson();
        return isUserProcessOwner(process, user)
                && process.isInAllocatedToUnitState()
                && process.isResponsibleForUnit(person, process.getRequest().getTotalValueWithoutVat())
                && !process.getRequest().hasBeenAuthorizedBy(person)
                && (!process.hasMissionProcess() || (process.getMissionProcess().isExpenditureAuthorized() && process
                        .getMissionProcess().areAllParticipantsAuthorized()));
    }

    @Override
    protected void process(ActivityInformation<P> activityInformation) {
        activityInformation.getProcess().authorizeBy(Person.getLoggedPerson());
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
        return "resources/AcquisitionResources";
    }

    @Override
    public boolean isUserAwarenessNeeded(final P process, final User user) {
        final Person person = user.getExpenditurePerson();
        if (person.hasAnyValidAuthorization()) {
            final Money amount = process.getRequest().getTotalValueWithoutVat();
            for (final RequestItem requestItem : process.getRequest().getRequestItemsSet()) {
                for (final UnitItem unitItem : requestItem.getUnitItemsSet()) {
                    final Unit unit = unitItem.getUnit();
                    if (!unitItem.getItemAuthorized().booleanValue() && unit.isDirectResponsible(person, amount)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
