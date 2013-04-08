/*
 * @(#)MissionItemFinancer.java
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

import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.util.Money;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class MissionItemFinancer extends MissionItemFinancer_Base {

    public MissionItemFinancer() {
        super();
        setMissionSystem(MissionSystem.getInstance());
        setAmount(Money.ZERO);
    }

    public MissionItemFinancer(final MissionItem missionItem, final MissionFinancer missionFinancer) {
        this();
        setMissionItem(missionItem);
        setMissionFinancer(missionFinancer);
    }

    public void delete() {
        removeMissionVersionFromArchive();
        removeMissionItem();
        removeMissionFinancer();
        removeMissionSystem();
        deleteDomainObject();
    }

    public String getDescription() {
        final StringBuilder stringBuilder = new StringBuilder();
        final MissionItem missionItem = getMissionItem();
        stringBuilder.append(missionItem.getLocalizedName());
        stringBuilder.append(" - ");
        stringBuilder.append(missionItem.getItemDescription());
        return stringBuilder.toString();
    }

    public String getAllFundAllocationId() {
        return getFundAllocationId();
    }

    public void clearFundAllocations() {
        final String fundAllocationId = getFundAllocationId();
        if (fundAllocationId != null && !fundAllocationId.isEmpty()) {
            getMission().registerFundAllocation(fundAllocationId);
        }
        setFundAllocationId(null);
    }

    public Mission getMission() {
        return getMissionFinancer().getMissionVersion().getMission();
    }

    MissionItemFinancer createNewVersion(final MissionFinancer missionFinancer, final MissionItem missionItem) {
        final MissionItemFinancer missionItemFinancer = createNewVersionInstance(missionItem, missionFinancer);
        setNewVersionInformation(missionItemFinancer);
        return missionItemFinancer;
    }

    protected MissionItemFinancer createNewVersionInstance(final MissionItem missionItem, final MissionFinancer missionFinancer) {
        return new MissionItemFinancer(missionItem, missionFinancer);
    }

    protected void setNewVersionInformation(final MissionItemFinancer missionItemFinancer) {
        missionItemFinancer.setAmount(getAmount());
        missionItemFinancer.setFundAllocationId(getFundAllocationId());
    }

    public void autoArchive() {
        archive();
    }

    private void archive() {
        final MissionVersion missionVersion = getMissionItem().getMissionVersion();
        setMissionVersionFromArchive(missionVersion);
    }

    public boolean isArchived() {
        return getMissionVersionFromArchive() != null;
    }

    public void archiveForAccountingUnit() {
        if (isCurrentUserAccountant()) {
            archive();
        }
    }

    private boolean isCurrentUserAccountant() {
        final MissionFinancer missionFinancer = getMissionFinancer();
        return missionFinancer.isCurrentUserAccountant();
    }

    public boolean isAccountantForUnArchivedMissionItemFinancer() {
        return !isArchived() && isCurrentUserAccountant();
    }

    public boolean isDirectAccountantForUnArchivedMissionItemFinancer() {
        return !isArchived() && isCurrentUserAccountant();
    }

    public void unArchive() {
        setMissionVersionFromArchive(null);
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        return getMissionSystem() == VirtualHost.getVirtualHostForThread().getMissionSystem();
    }

}
