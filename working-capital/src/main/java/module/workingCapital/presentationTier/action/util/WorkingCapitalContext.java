/*
 * @(#)WorkingCapitalContext.java
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
package module.workingCapital.presentationTier.action.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.SortedSet;
import java.util.TreeSet;

import module.organization.domain.Party;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalSystem;
import module.workingCapital.domain.WorkingCapitalYear;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalContext implements Serializable {

    private Integer year;
    private WorkingCapitalYear workingCapitalYear;
    private Party party;

    public WorkingCapitalContext() {
        super();
        setYear(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(final Integer year) {
        this.year = year;
        if (year != null) {
            for (final WorkingCapitalYear workingCapitalYear : WorkingCapitalSystem.getInstanceForCurrentHost()
                    .getWorkingCapitalYearsSet()) {
                if (workingCapitalYear.getYear().intValue() == year.intValue()) {
                    this.workingCapitalYear = workingCapitalYear;
                }
            }
        }
    }

    public WorkingCapitalYear getWorkingCapitalYear() {
        return workingCapitalYear;
    }

    public void setWorkingCapitalYear(final WorkingCapitalYear year) {
        this.workingCapitalYear = year;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(final Party party) {
        this.party = party;
    }

    public SortedSet<WorkingCapitalProcess> getWorkingCapitalSearchByUnit() {
        final WorkingCapitalYear workingCapitalYear = getWorkingCapitalYear();
        final Party party = getParty();
        return workingCapitalYear == null || party == null ? new TreeSet<WorkingCapitalProcess>() : workingCapitalYear
                .getForParty(party);
    }

}
