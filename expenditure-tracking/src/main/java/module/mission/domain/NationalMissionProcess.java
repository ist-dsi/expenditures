/*
 * @(#)NationalMissionProcess.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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
package module.mission.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.mission.domain.activity.AddFinancerActivity;
import module.mission.domain.activity.AddItemActivity;
import module.mission.domain.activity.AddParticipantActivity;
import module.mission.domain.activity.AllocateFundsActivity;
import module.mission.domain.activity.AllocateProjectFundsActivity;
import module.mission.domain.activity.ApproveActivity;
import module.mission.domain.activity.ArchiveItemActivity;
import module.mission.domain.activity.AssociateMissionProcessActivity;
import module.mission.domain.activity.AuthoriseParticipantActivity;
import module.mission.domain.activity.AuthorizeActivity;
import module.mission.domain.activity.CancelProcessActivity;
import module.mission.domain.activity.ChangeAccountingUnitActivity;
import module.mission.domain.activity.CommitFundsActivity;
import module.mission.domain.activity.DefineParticipantAuthorizationChainActivity;
import module.mission.domain.activity.DistributeItemCostsActivity;
import module.mission.domain.activity.EditItemActivity;
import module.mission.domain.activity.ExceptionalChangeRequestingPerson;
import module.mission.domain.activity.ProcessCanceledPersonnelActivity;
import module.mission.domain.activity.ProcessPersonnelActivity;
import module.mission.domain.activity.RejectProcessActivity;
import module.mission.domain.activity.RemoveFinancerActivity;
import module.mission.domain.activity.RemoveItemActivity;
import module.mission.domain.activity.RemoveParticipantActivity;
import module.mission.domain.activity.RevertMissionForEditingActivity;
import module.mission.domain.activity.RevertTerminationActivity;
import module.mission.domain.activity.SendForProcessTerminationActivity;
import module.mission.domain.activity.SendForProcessTerminationWithChangesActivity;
import module.mission.domain.activity.SubmitForApprovalActivity;
import module.mission.domain.activity.SubmitForApprovalByManagerOrManagementCouncilActivity;
import module.mission.domain.activity.TogleMissionNatureActivity;
import module.mission.domain.activity.TogleParticipantSalaryActivity;
import module.mission.domain.activity.UnAllocateFundsActivity;
import module.mission.domain.activity.UnAllocateProjectFundsActivity;
import module.mission.domain.activity.UnApproveActivity;
import module.mission.domain.activity.UnAuthoriseParticipantActivity;
import module.mission.domain.activity.UnAuthorizeActivity;
import module.mission.domain.activity.UnCommitFundsActivity;
import module.mission.domain.activity.UnProcessPersonnelActivity;
import module.mission.domain.activity.UnSubmitForApprovalActivity;
import module.mission.domain.activity.UpdateMissionDetailsActivity;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.GiveProcess;
import module.workflow.activities.ReleaseProcess;
import module.workflow.activities.StealProcess;
import module.workflow.activities.TakeProcess;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.bennu.core.util.ClassNameBundle;

import org.joda.time.DateTime;

@ClassNameBundle(key = "label.module.mission.domain.NationalMissions", bundle = "resources/MissionResources")
/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class NationalMissionProcess extends NationalMissionProcess_Base {

    private static final List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> activities;
    static {
	final List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> activitiesAux = new ArrayList<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
	activitiesAux.add(new UpdateMissionDetailsActivity());
	activitiesAux.add(new AddParticipantActivity());
	activitiesAux.add(new RemoveParticipantActivity());
	activitiesAux.add(new TogleParticipantSalaryActivity());
	activitiesAux.add(new DefineParticipantAuthorizationChainActivity());
	activitiesAux.add(new AddFinancerActivity());
	activitiesAux.add(new RemoveFinancerActivity());
	activitiesAux.add(new ChangeAccountingUnitActivity());
	activitiesAux.add(new AddItemActivity());
	activitiesAux.add(new EditItemActivity());
	activitiesAux.add(new DistributeItemCostsActivity());
	activitiesAux.add(new RemoveItemActivity());
	activitiesAux.add(new SubmitForApprovalActivity());
	activitiesAux.add(new SubmitForApprovalByManagerOrManagementCouncilActivity());
	activitiesAux.add(new UnSubmitForApprovalActivity());
	activitiesAux.add(new ApproveActivity());
	activitiesAux.add(new UnApproveActivity());
	activitiesAux.add(new AssociateMissionProcessActivity());

	activitiesAux.add(new AllocateFundsActivity());
	activitiesAux.add(new AllocateProjectFundsActivity());
	activitiesAux.add(new UnAllocateFundsActivity());
	activitiesAux.add(new UnAllocateProjectFundsActivity());
	activitiesAux.add(new CommitFundsActivity());
	activitiesAux.add(new UnCommitFundsActivity());

	activitiesAux.add(new AuthorizeActivity());
	activitiesAux.add(new UnAuthorizeActivity());
	activitiesAux.add(new AuthoriseParticipantActivity());
	activitiesAux.add(new UnAuthoriseParticipantActivity());
	activitiesAux.add(new ProcessPersonnelActivity());
	activitiesAux.add(new UnProcessPersonnelActivity());
	activitiesAux.add(new ProcessCanceledPersonnelActivity());
	activitiesAux.add(new SendForProcessTerminationWithChangesActivity());
	activitiesAux.add(new SendForProcessTerminationActivity());
	activitiesAux.add(new RevertMissionForEditingActivity());
	activitiesAux.add(new ArchiveItemActivity());
	activitiesAux.add(new RevertTerminationActivity());
	activitiesAux.add(new TogleMissionNatureActivity());

	activitiesAux.add(new GiveProcess<MissionProcess>(new MissionGiveProcessUserNotifier()));
	activitiesAux.add(new TakeProcess<MissionProcess>());
	activitiesAux.add(new ReleaseProcess<MissionProcess>());
	activitiesAux.add(new StealProcess<MissionProcess>());
	activitiesAux.add(new ExceptionalChangeRequestingPerson());

	activitiesAux.add(new CancelProcessActivity());
	activitiesAux.add(new RejectProcessActivity());

	activities = Collections.unmodifiableList(activitiesAux);
    }

    public NationalMissionProcess(final String location, final DateTime daparture, final DateTime arrival,
	    final String objective, final Boolean isCurrentUserAParticipant, final Boolean grantOwnerEquivalence) {
	new NationalMission(this, location, daparture, arrival, objective, isCurrentUserAParticipant, grantOwnerEquivalence);
    }

    @Override
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities() {
	return (List) activities;
    }

    @Override
    public boolean isActive() {
	return true;
    }

    @Override
    public String getPresentationName() {
	return super.getPresentationName() + getMission().getLocation();
    }

    @Override
    protected String notificationSubjectHeader() {
	return "label.email.mission.participation.authorized.subject.nationalMission";
    }

}
