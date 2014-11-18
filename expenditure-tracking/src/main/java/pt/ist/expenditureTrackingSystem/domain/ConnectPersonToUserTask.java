/*
 * @(#)ConnectPersonToUserTask.java
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
package pt.ist.expenditureTrackingSystem.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.scheduler.CronTask;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class ConnectPersonToUserTask extends CronTask {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/ExpenditureResources", "label.task.connectPersonToUserTask");
    }

    @Override
    public void runTask() throws Exception {
        for (Person person : Bennu.getInstance().getPeopleFromExpenditureTackingSystemSet()) {
            if (!person.hasUser()) {
                String username = person.getUsername();
                User user = User.findByUsername(username);
                if (user == null) {
                    user = new User(username);
                }
                person.setUser(user);
            }
        }
    }

}
