/*
 * @(#)CommitFundsActivityInformation.java
 *
 * Copyright 2012 Instituto Superior Tecnico
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

import java.util.ArrayList;
import java.util.List;

import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.CommitmentNumberBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class CommitFundsActivityInformation extends ActivityInformation<RegularAcquisitionProcess> {

    private List<CommitmentNumberBean> commitmentNumberBeans = new ArrayList<CommitmentNumberBean>();

    public CommitFundsActivityInformation(final RegularAcquisitionProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
        process.takeProcess();

        final User user = Authenticate.getUser();
        Person person = user.getExpenditurePerson();
        final AcquisitionRequest acquisitionRequest = process.getAcquisitionRequest();
        for (final Financer financer : acquisitionRequest.getFinancersSet()) {
            if (!financer.isCommitted() /* && financer.isAccountingEmployee(person) */) {
                final CommitmentNumberBean bean = new CommitmentNumberBean(financer);
                commitmentNumberBeans.add(bean);
                if (bean.getCommitmentNumber() == null || bean.getCommitmentNumber().isEmpty()) {
                    final MissionProcess missionProcess = process.getMissionProcess();
                    if (missionProcess != null) {
                        final MissionFinancer missionFinancer = findMissionFinance(missionProcess, financer);
                        if (missionFinancer != null) {
                            bean.setCommitmentNumber(missionFinancer.getCommitmentNumber());
                        }
                    }
                }
            }
        }
    }

    private MissionFinancer findMissionFinance(final MissionProcess missionProcess, final Financer financer) {
        for (final MissionFinancer missionFinancer : missionProcess.getMission().getFinancerSet()) {
            if (missionFinancer.getUnit() == financer.getUnit()) {
                return missionFinancer;
            }
        }
        return null;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && super.hasAllneededInfo() && hasAllCommitmentNumbers();
    }

    private boolean hasAllCommitmentNumbers() {
        if (commitmentNumberBeans.isEmpty()) {
            return false;
        }
        for (final CommitmentNumberBean commitmentNumberBean : commitmentNumberBeans) {
            final String commitmentNumber = commitmentNumberBean.getCommitmentNumber();
            if (commitmentNumber == null || commitmentNumber.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public List<CommitmentNumberBean> getCommitmentNumberBeans() {
        return commitmentNumberBeans;
    }

    public void setCommitmentNumberBeans(List<CommitmentNumberBean> commitmentNumberBeans) {
        this.commitmentNumberBeans = commitmentNumberBeans;
    }

}
