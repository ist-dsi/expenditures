/*
 * @(#)OperationLog.java
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
package pt.ist.expenditureTrackingSystem.domain.announcements;

import module.workflow.domain.WorkflowProcess;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

/**
 * WorkflowLog is a generalization of this class - first version of the a
 * logging class that came with the expenditureTrackingSystem
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class OperationLog extends OperationLog_Base {

    public OperationLog(AnnouncementProcess process, User user, String operation, AnnouncementProcessStateType state) {
        super();
        init(process, user);
        super.setOperation(operation);
        super.setState(state);
    }

    // @Override
    // public AbstractActivity<GenericProcess> getActivity() {
    // AnnouncementProcess process = (AnnouncementProcess) getProcess();
    // return process.getActivityByName(getOperation());
    // }

    @Override
    public void setOperation(String operation) {
        throw new DomainException(Bundle.EXPENDITURE, "error.unable.to.change.operation");
    }

    @Override
    public void setProcess(WorkflowProcess process) {
        throw new DomainException(Bundle.EXPENDITURE, "error.unable.to.change.process");
    }

    @Override
    public void setActivityExecutor(User executor) {
        throw new DomainException(Bundle.EXPENDITURE, "error.unable.to.change.executor");
    }

    @Override
    public void setWhenOperationWasRan(DateTime when) {
        throw new DomainException(Bundle.EXPENDITURE, "error.unable.to.change.when.operation.was.executed");
    }

    @Override
    public void setState(AnnouncementProcessStateType state) {
        throw new DomainException(Bundle.EXPENDITURE, "error.unable.to.change.when.state");
    }

    @Override
    public String getDescription() {
        return BundleUtil.getString("resources/AnnouncementsResources", "label." + getOperation());
    }

    @Deprecated
    public boolean hasState() {
        return getState() != null;
    }

}
