/*
 * @(#)MissionYear.java
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

import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

import module.mission.domain.util.MissionAuthorizationMap;
import module.mission.domain.util.MissionPendingProcessCounter;
import module.mission.domain.util.MissionState;
import module.organization.domain.Party;
import module.organization.domain.Person;
import module.workflow.widgets.ProcessListWidget;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class MissionYear extends MissionYear_Base {

    static {
        ProcessListWidget.register(new MissionPendingProcessCounter());
    }

    public static final Comparator<MissionYear> COMPARATOR_BY_YEAR = new Comparator<MissionYear>() {
        @Override
        public int compare(MissionYear o1, MissionYear o2) {
            final Integer year1 = o1.getYear();
            final Integer year2 = o2.getYear();
            return year1.compareTo(year2);
        }
    };

    public static Integer getBiggestYearCounter() {
        int biggestCounter = 0;
        for (MissionYear year : MissionSystem.getInstance().getMissionYearSet()) {
            if (year.getCounter() > biggestCounter) {
                biggestCounter = year.getCounter();
            }
        }
        return biggestCounter;
    }

    private MissionYear(final int year) {
        super();
        if (findMissionByYearAux(year) != null) {
            throw new Error("There can only be one! (MissionYear object for each year)");
        }
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (year != currentYear && year != currentYear + 1) {
            throw new Error("There is absolutly no need to create a year other that the current and the next!");
        }
        setMissionSystem(MissionSystem.getInstance());
        setYear(new Integer(year));
        setCounter(Integer.valueOf(0));
    }

    private static MissionYear findMissionByYearAux(final int year) {
        final MissionSystem missionSystem = MissionSystem.getInstance();
        for (final MissionYear missionYear : missionSystem.getMissionYearSet()) {
            if (missionYear.getYear().intValue() == year) {
                return missionYear;
            }
        }
        return null;
    }

    public static MissionYear findMissionYear(final int year) {
        return findMissionByYearAux(year);
    }

    @Atomic
    public static MissionYear findOrCreateMissionYear(final int year) {
        final MissionYear missionYear = findMissionByYearAux(year);
        return missionYear == null ? new MissionYear(year) : missionYear;
    }

    public Integer nextNumber() {
        return getAndIncreaseNextNumber();
    }

    private Integer getAndIncreaseNextNumber() {
        setCounter(getCounter().intValue() + 1);
        return getCounter();
    }

    public static MissionYear getCurrentYear() {
        final int year = new DateTime().getYear();
        return findOrCreateMissionYear(year);
    }

    private abstract class MissionProcessSearch {

        final Supplier<Stream<MissionProcess>> supplier;

        abstract boolean shouldAdd(final MissionProcess missionProcess, final User user);

        private MissionProcessSearch() {
            supplier = () -> getMissionProcessSet().stream();
        }

        private MissionProcessSearch(final Supplier<Stream<MissionProcess>> supplier) {
            this.supplier = supplier;
        }

        Stream<MissionProcess> search() {
            final User user = Authenticate.getUser();
            return supplier.get().filter(p -> shouldAdd(p, user));
        }

    }

    private class PendingAprovalSearch extends MissionProcessSearch {

        private PendingAprovalSearch() {
        }

        private PendingAprovalSearch(final Supplier<Stream<MissionProcess>> supplier) {
            super(supplier);
        }

        @Override
        boolean shouldAdd(final MissionProcess missionProcess, final User user) {
            if (missionProcess.getCurrentOwner() != null && !missionProcess.isTakenByCurrentUser()) {
                return false;
            }
            if (missionProcess.isUnderConstruction() || missionProcess.getIsCanceled()) {
                return false;
            }
            if (missionProcess.isPendingApprovalBy(user)) {
                return true;
            }
            if (missionProcess.isCurrentUserAbleToAccessAnyQueues() && MissionState.VERIFICATION.isPending(missionProcess)) {
                return true;
            }
            return false;
        }

    }

    private class PendingVehicleAuthorizationSearch extends MissionProcessSearch {

        private PendingVehicleAuthorizationSearch() {
        }

        private PendingVehicleAuthorizationSearch(final Supplier<Stream<MissionProcess>> supplier) {
            super(supplier);
        }

        @Override
        boolean shouldAdd(final MissionProcess missionProcess, final User user) {
            if (!MissionSystem.getInstance().getVehicleAuthorizersSet().contains(user)) {
                return false;
            }
            if (missionProcess.isCanceled()) {
                return false;
            }
            if (!missionProcess.isApprovedByResponsible()) {
                return false;
            }
            if (missionProcess.getCurrentOwner() != null && !missionProcess.isTakenByCurrentUser()) {
                return false;
            }

            return missionProcess.getMission().isVehicleAuthorizationNeeded()
                    && !missionProcess.getMission().areAllVehicleItemsAuthorized();
        }
    }

    private class PendingAuthorizationSearch extends MissionProcessSearch {

        private PendingAuthorizationSearch() {
        }

        private PendingAuthorizationSearch(final Supplier<Stream<MissionProcess>> supplier) {
            super(supplier);
        }

        @Override
        boolean shouldAdd(final MissionProcess missionProcess, final User user) {
            if (missionProcess.getCurrentOwner() != null && !missionProcess.isTakenByCurrentUser()) {
                return false;
            }
            if (!missionProcess.isApproved()) {
                return false;
            }
            if (missionProcess.getIsCanceled()) {
                return false;
            }

            if (missionProcess.isPendingParticipantAuthorisationBy(user)
                    && MissionState.PARTICIPATION_AUTHORIZATION.isPending(missionProcess)) {
                return true;
            }

            return (missionProcess.areAllParticipantsAuthorized() && missionProcess.hasAllAllocatedFunds()
                    && missionProcess.isPendingDirectAuthorizationBy(user));
//            return (missionProcess.areAllParticipantsAuthorized() && missionProcess.hasAllAllocatedFunds()
//                    && isPendingDirectAuthorizationBy(missionProcess, user));
        }

        private boolean isPendingDirectAuthorizationBy(MissionProcess missionProcess, User user) {
            final boolean pendingDirectAuthorizationBy = missionProcess.isPendingDirectAuthorizationBy(user);
            return (pendingDirectAuthorizationBy && missionProcess.hasBeenCheckedByUnderlings())
                    || (!pendingDirectAuthorizationBy && !missionProcess.isAuthorized()
                            && !missionProcess.hasBeenCheckedByUnderlings() && missionProcess.isPendingCheckByUnderlings(user));
        }
    }

    private class PendingFundAllocationSearch extends MissionProcessSearch {

        private PendingFundAllocationSearch() {
        }

        private PendingFundAllocationSearch(final Supplier<Stream<MissionProcess>> supplier) {
            super(supplier);
        }

        @Override
        boolean shouldAdd(final MissionProcess missionProcess, final User user) {
            return (missionProcess.getCurrentOwner() == null || missionProcess.isTakenByCurrentUser())
                    && (isPendingFundAllocation(missionProcess, user) || isPendingFundUnAllocation(missionProcess, user));
        }

        private boolean isPendingFundAllocation(MissionProcess missionProcess, User user) {
            return missionProcess.isApproved() && !missionProcess.getIsCanceled()
                    && (((!missionProcess.hasAnyProjectFinancer() || missionProcess.hasAllAllocatedProjectFunds())
                            && !missionProcess.hasAllAllocatedFunds() && missionProcess.canAllocateFund())
                            || (!missionProcess.hasAllAllocatedProjectFunds() && missionProcess.canAllocateProjectFund())
                            || (missionProcess.getMission().hasAnyFinancer() && missionProcess.hasAllAllocatedFunds()
                                    /* && !missionProcess.hasAllCommitmentNumbers() */
                                    && missionProcess.isAccountingEmployee(user.getExpenditurePerson())));
        }

        private boolean isPendingFundUnAllocation(final MissionProcess missionProcess, final User user) {
            return missionProcess.getIsCanceled().booleanValue() && ((missionProcess.hasAnyAllocatedFunds()
                    && missionProcess.isAccountingEmployee(user.getExpenditurePerson()))
                    || (missionProcess.hasAnyAllocatedProjectFunds())
                            && missionProcess.isProjectAccountingEmployee(user.getExpenditurePerson()));
        }

    }

    private class PendingProcessingPersonelInformationSearch extends MissionProcessSearch {

        private PendingProcessingPersonelInformationSearch() {
        }

        private PendingProcessingPersonelInformationSearch(final Supplier<Stream<MissionProcess>> supplier) {
            super(supplier);
        }

        @Override
        boolean shouldAdd(final MissionProcess missionProcess, final User user) {
            if (missionProcess.getCurrentOwner() != null && !missionProcess.isTakenByCurrentUser()) {
                return false;
            }
            if (missionProcess.isCurrentUserAbleToAccessAnyQueues()
                    && MissionState.PERSONAL_INFORMATION_PROCESSING.isPending(missionProcess)) {
                return true;
            }
            if (missionProcess.isReadyForMissionTermination(user)) {
                return true;
            }
            if (missionProcess.isTerminated() && !missionProcess.isArchived() && missionProcess.canArchiveMission()) {
                return true;
            }
            return false;
        }
    }

    public Stream<MissionProcess> getPendingAproval() {
        return new PendingAprovalSearch().search();
    }

    public Stream<MissionProcess> getPendingAproval(final Supplier<Stream<MissionProcess>> result) {
        return new PendingAprovalSearch(result).search();
    }

    public Stream<MissionProcess> getPendingVehicleAuthorization() {
        return new PendingVehicleAuthorizationSearch().search();
    }

    public Stream<MissionProcess> getPendingVehicleAuthorization(final Supplier<Stream<MissionProcess>> result) {
        return new PendingVehicleAuthorizationSearch(result).search();
    }

    public Stream<MissionProcess> getPendingAuthorization() {
        return new PendingAuthorizationSearch().search();
    }

    public Stream<MissionProcess> getPendingAuthorization(final Supplier<Stream<MissionProcess>> result) {
        return new PendingAuthorizationSearch(result).search();
    }

    public Stream<MissionProcess> getPendingFundAllocation() {
        try {
            return new PendingFundAllocationSearch().search();
        } catch (Throwable t) {
            t.printStackTrace();
            throw new Error(t);
        }
    }

    public Stream<MissionProcess> getPendingFundAllocation(final Supplier<Stream<MissionProcess>> result) {
        try {
            return new PendingFundAllocationSearch(result).search();
        } catch (Throwable t) {
            t.printStackTrace();
            throw new Error(t);
        }
    }

    public Stream<MissionProcess> getDirectPendingFundAllocation() {
        try {
            return new MissionProcessSearch() {
                @Override
                boolean shouldAdd(final MissionProcess missionProcess, final User user) {
                    return (missionProcess.getCurrentOwner() == null || missionProcess.isTakenByCurrentUser())
                            && (isPendingFundAllocation(missionProcess, user) || isPendingFundUnAllocation(missionProcess, user));
                }

                private boolean isPendingFundAllocation(MissionProcess missionProcess, User user) {
                    return missionProcess.isApproved() && !missionProcess.getIsCanceled()
                            && (((!missionProcess.hasAnyProjectFinancer() || missionProcess.hasAllAllocatedProjectFunds())
                                    && !missionProcess.hasAllAllocatedFunds() && missionProcess.canAllocateFund())
                                    || (!missionProcess.hasAllAllocatedProjectFunds()
                                            && missionProcess.isDirectResponsibleForPendingProjectFundAllocation()));
                }

                private boolean isPendingFundUnAllocation(final MissionProcess missionProcess, final User user) {
                    return missionProcess.getIsCanceled().booleanValue() && ((missionProcess.hasAnyAllocatedFunds()
                            && missionProcess.isAccountingEmployee(user.getExpenditurePerson()))
                            || (missionProcess.hasAnyAllocatedProjectFunds())
                                    && missionProcess.isDirectProjectAccountingEmployee(user.getExpenditurePerson()));
                }
            }.search();
        } catch (Throwable t) {
            t.printStackTrace();
            throw new Error(t);
        }
    }

    public Stream<MissionProcess> getPendingProcessingPersonelInformation() {
        return new PendingProcessingPersonelInformationSearch().search();
    }

    public Stream<MissionProcess> getPendingProcessingPersonelInformation(final Supplier<Stream<MissionProcess>> result) {
        return new PendingProcessingPersonelInformationSearch(result).search();
    }

    public Stream<MissionProcess> getPendingDirectProcessingPersonelInformation() {
        MissionProcessSearch search = new MissionProcessSearch() {
            @Override
            boolean shouldAdd(final MissionProcess missionProcess, final User user) {
                if (missionProcess.getCurrentOwner() != null && !missionProcess.isTakenByCurrentUser()) {
                    return false;
                }
                if (missionProcess.isCurrentUserAbleToAccessAnyQueues()
                        && MissionState.PERSONAL_INFORMATION_PROCESSING.isPending(missionProcess)) {
                    return true;
                }
                if (missionProcess.isCurrentUserAbleToAccessAnyQueues() && MissionState.VERIFICATION.isPending(missionProcess)) {
                    return true;
                }

                if (missionProcess.isReadyForMissionTermination(user)) {
                    return true;
                }
                if (missionProcess.isTerminated() && !missionProcess.isArchived() && missionProcess.canArchiveMissionDirect()) {
                    return true;
                }
                return false;
            }
        };
        return search.search();
    }

    public SortedSet<MissionProcess> getRequested() {
        final User user = Authenticate.getUser();
        final SortedSet<MissionProcess> result = new TreeSet<MissionProcess>(MissionProcess.COMPARATOR_BY_PROCESS_NUMBER);
        final Person person = user.getPerson();
        if (person != null) {
            for (final Mission mission : person.getRequestedMissionsSet()) {
                final MissionProcess missionProcess = mission.getMissionProcess();
                if (missionProcess.getMissionYear() == this && !missionProcess.getIsCanceled() && !missionProcess.isArchived()) {
                    result.add(missionProcess);
                }
            }
        }
        return result;
    }

    private boolean hasValidAuthorization(final Set<Authorization> authorizations) {
        for (final Authorization authorization : authorizations) {
            if (authorization.isValid()) {
                return true;
            }
        }
        return false;
    }

    private boolean isDirectlyResponsibleFor(final User user,
            final pt.ist.expenditureTrackingSystem.domain.organization.Unit unit) {
        final Set<Authorization> authorizationsFromUnit = unit.getAuthorizationsSet();
        if (getAuthorizations(user).anyMatch(a -> a.getUnit() == unit)) {
            return true;
        }
        if (hasValidAuthorization(authorizationsFromUnit)) {
            return false;
        }
        final pt.ist.expenditureTrackingSystem.domain.organization.Unit parentUnit = unit.getParentUnit();
        return parentUnit != null && isDirectlyResponsibleFor(user, parentUnit);
    }

    private Stream<Authorization> getAuthorizations(final User user) {
        return user.getExpenditurePerson().getAuthorizationsSet().stream().filter(a -> a.isValid());
    }

    public SortedSet<MissionProcess> getAprovalResponsible() {
        final SortedSet<MissionProcess> result = new TreeSet<MissionProcess>(MissionProcess.COMPARATOR_BY_PROCESS_NUMBER);
        final User user = Authenticate.getUser();
        if (user.getExpenditurePerson() != null) {
            for (final MissionProcess missionProcess : getMissionProcessSet()) {
                if (!missionProcess.getIsCanceled() && !missionProcess.isArchived()) {
                    final Mission mission = missionProcess.getMission();
                    final Party missionResponsible = mission.getMissionResponsible();
                    if (missionResponsible != null) {
                        if (missionResponsible.isPerson()) {
                            if (missionResponsible == user.getPerson()) {
                                result.add(missionProcess);
                            }
                        } else if (missionResponsible.isUnit() && getAuthorizations(user).findAny().orElse(null) != null) {
                            final pt.ist.expenditureTrackingSystem.domain.organization.Unit unit =
                                    getExpenditureUnit(mission, (module.organization.domain.Unit) missionResponsible);
                            if (unit != null && isDirectlyResponsibleFor(user, unit)) {
                                result.add(missionProcess);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private Unit getExpenditureUnit(final Mission mission, final module.organization.domain.Unit unit) {
        return unit.getExpenditureUnit() != null ? unit.getExpenditureUnit() : getExpenditureUnit(mission,
                unit.getParentAccountabilityStream().filter(a -> a.getParent().isUnit())
                        .map(a -> (module.organization.domain.Unit) a.getParent()).findAny().orElse(null));
    }

    public SortedSet<MissionProcess> getParticipate() {
        final User user = Authenticate.getUser();
        final SortedSet<MissionProcess> result = new TreeSet<MissionProcess>(MissionProcess.COMPARATOR_BY_PROCESS_NUMBER);
        final Person person = user.getPerson();
        if (person != null) {
            for (final Mission mission : person.getMissionsSet()) {
                final MissionProcess missionProcess = mission.getMissionProcess();
                if (missionProcess.getMissionYear() == this && !missionProcess.getIsCanceled() && !missionProcess.isArchived()) {
                    result.add(missionProcess);
                }
            }
        }
        return result;
    }

    public MissionAuthorizationMap getMissionAuthorizationMap() {
        return new MissionAuthorizationMap(this);
    }

    public Collection<MissionProcess> getTaken() {
        return getTakenStream().collect(Collectors.toSet());
    }

    public Stream<MissionProcess> getTakenStream() {
        final User user = Authenticate.getUser();
        return user.getUserProcessesSet().stream().filter(wp -> wp instanceof MissionProcess).map(wp -> (MissionProcess) wp)
                .filter(mp -> mp.getMissionYear() == this && !mp.getIsCanceled());
    }

    public void delete() {
        if (hasAnyMissionProcess()) {
            throw new Error("cannot.delete.because.mission.process.exist");
        }
        setMissionSystem(null);
        deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<module.mission.domain.MissionProcess> getMissionProcess() {
        return getMissionProcessSet();
    }

    @Deprecated
    public boolean hasAnyMissionProcess() {
        return !getMissionProcessSet().isEmpty();
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasCounter() {
        return getCounter() != null;
    }

    @Deprecated
    public boolean hasMissionSystem() {
        return getMissionSystem() != null;
    }

}
