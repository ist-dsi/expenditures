/*
 * @(#)PartiesWithWorkingCapitalFundsAutoCompleteProvider.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package module.workingCapital.presentationTier.provider;

import module.organization.domain.Party;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.organization.presentationTier.renderers.providers.PartiesAutoCompleteProvider;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class PartiesWithWorkingCapitalFundsAutoCompleteProvider extends PartiesAutoCompleteProvider {

    @Override
    protected boolean allowResult(final Party party) {
        return party.isUnit() ? allowResultUnit((Unit) party) : allowResultPerson((Person) party);
    }

    private boolean allowResultUnit(final Unit unit) {
        return unit.getExpenditureUnit() != null && unit.getExpenditureUnit().getWorkingCapitalsSet().size() > 0;
    }

    private boolean allowResultPerson(final Person person) {
        return person.getMovementResponsibleWorkingCapitalsSet().size() > 0;
    }

}
