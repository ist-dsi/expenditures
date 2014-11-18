package module.mission.domain;

import java.util.ArrayList;
import java.util.List;

import jvstm.cps.ConsistencyPredicate;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.fenixframework.dml.runtime.Relation;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class MissionProcessAssociation extends MissionProcessAssociation_Base {

    static {
        MissionProcess.getRelationMissionProcessAssociationMissionProcess().addListener(
                new RelationAdapter<MissionProcess, MissionProcessAssociation>() {
                    @Override
                    public void afterAdd(Relation<MissionProcess, MissionProcessAssociation> relation, MissionProcess process,
                            MissionProcessAssociation association) {
                        if (!association.checkHasNoRepeatedMissionSystems()) {
                            throw new DomainException(Bundle.MISSION, "error.cannot.associate.MissionProcesses.from.same.system");
                        }
                    }
                });

        MissionProcess.getRelationMissionProcessAssociationMissionProcess().addListener(
                new RelationAdapter<MissionProcess, MissionProcessAssociation>() {
                    @Override
                    public void afterAdd(Relation<MissionProcess, MissionProcessAssociation> relation, MissionProcess process,
                            MissionProcessAssociation association) {
                        if (!association.checkAllMissionProcessesOfSameType()) {
                            throw new DomainException(Bundle.MISSION, "error.cannot.associate.MissionProcesses.of.diferent.types");
                        }
                    }
                });
    }

    public MissionProcessAssociation() {
        super();
    }

    public MissionProcessAssociation(MissionProcess... missionProcesses) {
        this();
        setBennu(Bennu.getInstance());
        for (MissionProcess process : missionProcesses) {
            addMissionProcesses(process);
        }
    }

    @ConsistencyPredicate
    public boolean checkHasAtLeastTwoMissionProcesses() {
        return getMissionProcessesSet().size() >= 2;
    }

    @ConsistencyPredicate
    public boolean checkHasMyOrg() {
        return hasMyOrg();
    }

    //@ConsistencyPredicate
    public boolean checkHasNoRepeatedMissionSystems() {
        List<MissionSystem> systemsFound = new ArrayList<MissionSystem>();
        for (MissionProcess process : getMissionProcesses()) {
            if (systemsFound.contains(process.getMissionSystem())) {
                return false;
            }
            systemsFound.add(process.getMissionSystem());
        }

        return true;
    }

    //@ConsistencyPredicate
    public boolean checkAllMissionProcessesOfSameType() {
        Boolean isGrantOwnerType = getMissionProcesses().iterator().next().getMission().getGrantOwnerEquivalence();
        for (MissionProcess process : getMissionProcesses()) {
            if (!process.getMission().getGrantOwnerEquivalence().equals(isGrantOwnerType)) {
                return false;
            }
        }

        return true;
    }

    public void delete() {
        for (MissionProcess process : getMissionProcesses()) {
            removeMissionProcesses(process);
        }
        setBennu(null);
        deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<module.mission.domain.MissionProcess> getMissionProcesses() {
        return getMissionProcessesSet();
    }

    @Deprecated
    public boolean hasAnyMissionProcesses() {
        return !getMissionProcessesSet().isEmpty();
    }

    @Deprecated
    public boolean hasMyOrg() {
        return getBennu() != null;
    }

}
