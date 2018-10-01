/*
 * @(#)WorkingCapitalPendingProcessCounter.java
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
package module.workingCapital.domain.util;

import module.workflow.domain.ProcessCounter;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalSystem;
import module.workingCapital.domain.WorkingCapitalYear;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalPendingProcessCounter extends ProcessCounter {

    public WorkingCapitalPendingProcessCounter() {
        super(WorkingCapitalProcess.class);
    }

    @Override
    public int getCount() {
        final User user = Authenticate.getUser();
        final WorkingCapitalYear year = WorkingCapitalSystem.getInstance().getWorkingCapitalYears().stream()
            .max(WorkingCapitalYear.COMPARATOR).orElse(null);
            
        return year == null ? 0 : (int) year.getWorkingCapitalsSet().stream()
            .map(wc -> wc.getWorkingCapitalProcess())
            .filter(p -> shouldCountProcess(p, user))
            .count();
    }

}
