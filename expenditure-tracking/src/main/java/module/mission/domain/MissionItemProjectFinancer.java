/*
 * @(#)MissionItemProjectFinancer.java
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

/**
 * 
 * @author Luis Cruz
 * 
 */
public class MissionItemProjectFinancer extends MissionItemProjectFinancer_Base {

    public MissionItemProjectFinancer() {
        super();
    }

    public MissionItemProjectFinancer(final MissionItem missionItem, final MissionFinancer missionFinancer) {
        this();
        setMissionItem(missionItem);
        setMissionFinancer(missionFinancer);
    }

    @Override
    public String getAllFundAllocationId() {
        final String fundAllocationId = getFundAllocationId();
        final String projectFundAllocationId = getProjectFundAllocationId();
        if (fundAllocationId == null || fundAllocationId.isEmpty()) {
            return projectFundAllocationId;
        } else if (projectFundAllocationId == null || projectFundAllocationId.isEmpty()) {
            return fundAllocationId;
        } else {
            return projectFundAllocationId + ", " + fundAllocationId;
        }
    }

    @Override
    public void clearFundAllocations() {
        super.clearFundAllocations();
        final String projectFundAllocationId = getProjectFundAllocationId();
        if (projectFundAllocationId != null && !projectFundAllocationId.isEmpty()) {
            getMission().registerFundAllocation(projectFundAllocationId);
        }
        setProjectFundAllocationId(null);
    }

    @Override
    protected void setNewVersionInformation(final MissionItemFinancer missionItemFinancer) {
        super.setNewVersionInformation(missionItemFinancer);
        final MissionItemProjectFinancer missionItemProjectFinancer = (MissionItemProjectFinancer) missionItemFinancer;
        missionItemProjectFinancer.setProjectFundAllocationId(getProjectFundAllocationId());
    }

    @Override
    protected MissionItemFinancer createNewVersionInstance(final MissionItem missionItem, final MissionFinancer missionFinancer) {
        return new MissionItemProjectFinancer(missionItem, missionFinancer);
    }

    @Override
    public void autoArchive() {
        super.autoArchive();
        archiveProject();
    }

    private void archiveProject() {
        final MissionVersion missionVersion = getMissionItem().getMissionVersion();
        setMissionVersionFromProjectArchive(missionVersion);
    }

    @Override
    public void archiveForAccountingUnit() {
        super.archiveForAccountingUnit();
        if (isCurrentUserProjectAccountant()) {
            archiveProject();
        }
    }

    private boolean isCurrentUserProjectAccountant() {
        final MissionFinancer missionFinancer = getMissionFinancer();
        return missionFinancer.isCurrentUserProjectAccountant();
    }

    @Override
    public boolean isAccountantForUnArchivedMissionItemFinancer() {
        return (isProjectArchived() && super.isAccountantForUnArchivedMissionItemFinancer())
                || (!isProjectArchived() && isCurrentUserProjectAccountant());
    }

    @Override
    public boolean isDirectAccountantForUnArchivedMissionItemFinancer() {
        final MissionFinancer missionFinancer = getMissionFinancer();
        return (isProjectArchived() && super.isDirectAccountantForUnArchivedMissionItemFinancer())
                || (!isProjectArchived() && isCurrentUserProjectAccountant() && (missionFinancer
                        .isCurrentUserDirectProjectAccountant()));
    }

    private boolean isProjectArchived() {
        return getMissionVersionFromProjectArchive() != null;
    }

    @Override
    public void unArchive() {
        super.unArchive();
        setMissionVersionFromProjectArchive(null);
    }

    @Override
    public void delete() {
        setMissionVersionFromProjectArchive(null);
        super.delete();
    }

    @Deprecated
    public boolean hasProjectFundAllocationId() {
        return getProjectFundAllocationId() != null;
    }

    @Deprecated
    public boolean hasMissionVersionFromProjectArchive() {
        return getMissionVersionFromProjectArchive() != null;
    }

}
