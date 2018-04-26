/*
 * @(#)ForeignMissionProcess.java
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
import java.util.List;
import java.util.function.BiPredicate;

import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;

import module.geography.domain.Country;
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
import module.mission.domain.activity.AuthorizeVehicleItemActivity;
import module.mission.domain.activity.CancelProcessActivity;
import module.mission.domain.activity.ChangeAccountingUnitActivity;
import module.mission.domain.activity.DefineParticipantAuthorizationChainActivity;
import module.mission.domain.activity.DisassociateMissionProcessActivity;
import module.mission.domain.activity.DistributeItemCostsActivity;
import module.mission.domain.activity.EditItemActivity;
import module.mission.domain.activity.ExceptionalChangeRequestingPerson;
import module.mission.domain.activity.PreAuthorizeActivity;
import module.mission.domain.activity.ProcessPersonnelInformationForCanceledProcessActivity;
import module.mission.domain.activity.ProcessPersonnelInformationForNotCanceledProcessActivity;
import module.mission.domain.activity.RejectProcessActivity;
import module.mission.domain.activity.RemoveFinancerActivity;
import module.mission.domain.activity.RemoveItemActivity;
import module.mission.domain.activity.RemoveParticipantActivity;
import module.mission.domain.activity.RevertMissionForEditingActivity;
import module.mission.domain.activity.RevertTerminationActivity;
import module.mission.domain.activity.RevertVerifyActivity;
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
import module.mission.domain.activity.UnAuthorizeVehicleItemActivity;
import module.mission.domain.activity.UnPreAuthorizeActivity;
import module.mission.domain.activity.UnProcessPersonnelActivity;
import module.mission.domain.activity.UnSubmitForApprovalActivity;
import module.mission.domain.activity.UpdateForeignMissionDetailsActivity;
import module.mission.domain.activity.VerifyActivity;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.AddObserver;
import module.workflow.activities.GiveProcess;
import module.workflow.activities.ReleaseProcess;
import module.workflow.activities.StealProcess;
import module.workflow.activities.TakeProcess;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;

/**
 *
 * @author João Neves
 * @author Luis Cruz
 *
 */
@ClassNameBundle(bundle = "MissionResources")
public class ForeignMissionProcess extends ForeignMissionProcess_Base {

    private static final List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> activities =
            new ArrayList<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
    static {
        activities.add(new UpdateForeignMissionDetailsActivity());
        activities.add(new AddParticipantActivity());
        activities.add(new RemoveParticipantActivity());
        activities.add(new TogleParticipantSalaryActivity());
        activities.add(new DefineParticipantAuthorizationChainActivity());
        activities.add(new AddFinancerActivity());
        activities.add(new RemoveFinancerActivity());
        activities.add(new ChangeAccountingUnitActivity());
        activities.add(new AddItemActivity());
        activities.add(new EditItemActivity());
        activities.add(new DistributeItemCostsActivity());
        activities.add(new RemoveItemActivity());
        activities.add(new SubmitForApprovalActivity());
        activities.add(new SubmitForApprovalByManagerOrManagementCouncilActivity());
        activities.add(new UnSubmitForApprovalActivity());
        activities.add(new ApproveActivity());
        activities.add(new UnApproveActivity());
        activities.add(new VerifyActivity());
        activities.add(new RevertVerifyActivity());
        activities.add(new AssociateMissionProcessActivity());
        activities.add(new DisassociateMissionProcessActivity());

        activities.add(new AllocateFundsActivity());
        activities.add(new AllocateProjectFundsActivity());
        activities.add(new UnAllocateFundsActivity());
        activities.add(new UnAllocateProjectFundsActivity());
//        activities.add(new CommitFundsActivity());
//        activities.add(new UnCommitFundsActivity());

        activities.add(new PreAuthorizeActivity());
        activities.add(new UnPreAuthorizeActivity());
        activities.add(new AuthorizeActivity());
        activities.add(new UnAuthorizeActivity());
        activities.add(new AuthoriseParticipantActivity());
        activities.add(new UnAuthoriseParticipantActivity());
        activities.add(new AuthorizeVehicleItemActivity());
        activities.add(new UnAuthorizeVehicleItemActivity());
        activities.add(new ProcessPersonnelInformationForNotCanceledProcessActivity());
        activities.add(new UnProcessPersonnelActivity());
        activities.add(new ProcessPersonnelInformationForCanceledProcessActivity());
        activities.add(new SendForProcessTerminationWithChangesActivity());
        activities.add(new SendForProcessTerminationActivity());
        activities.add(new RevertMissionForEditingActivity());
        activities.add(new ArchiveItemActivity());
        activities.add(new RevertTerminationActivity());
        activities.add(new TogleMissionNatureActivity());

        activities.add(new GiveProcess<MissionProcess>(new MissionGiveProcessUserNotifier()));
        activities.add(new TakeProcess<MissionProcess>());
        activities.add(new ReleaseProcess<MissionProcess>());
        activities.add(new StealProcess<MissionProcess>());
        activities.add(new AddObserver<MissionProcess>());
        activities.add(new ExceptionalChangeRequestingPerson());

        activities.add(new CancelProcessActivity());
        activities.add(new RejectProcessActivity());
    }

    public static void registerActivity(
            WorkflowActivity<? extends MissionProcess, ? extends ActivityInformation<? extends MissionProcess>> activity) {
        activities.add(activity);
    }

    public static void registerActivityPredicate(final Class clazz, final BiPredicate predicate) {
        for (final WorkflowActivity activity : activities) {
            if (activity.getClass().equals(clazz)) {
                activity.registerIsActivePredicate(predicate);
            }
        }
    }

    public ForeignMissionProcess(final Country country, final String location, final DateTime daparture, final DateTime arrival,
            final String objective, final Boolean isCurrentUserAParticipant, final Boolean grantOwnerEquivalence) {
        new ForeignMission(this, country, location, daparture, arrival, objective, isCurrentUserAParticipant,
                grantOwnerEquivalence);
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
        final Mission mission = getMission();
        final Country country = mission.getCountry();
        final LocalizedString name = country == null ? null : country.getName();
        final String countryName = name == null ? "" : name.getContent();
        return super.getPresentationName() + countryName + ", " + mission.getLocation();
    }

}
