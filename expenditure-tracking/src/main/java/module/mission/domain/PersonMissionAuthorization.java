/*
 * @(#)PersonMissionAuthorization.java
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

import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import jvstm.cps.ConsistencyPredicate;
import module.mission.domain.util.AuthorizationChain;
import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.FunctionDelegation;
import module.organization.domain.Party;
import module.organization.domain.Person;
import module.organization.domain.Unit;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class PersonMissionAuthorization extends PersonMissionAuthorization_Base {

    public static final Comparator<PersonMissionAuthorization> COMPARATOR_BY_PROCESS_NUMBER =
            new Comparator<PersonMissionAuthorization>() {
                @Override
                public int compare(final PersonMissionAuthorization pma1, final PersonMissionAuthorization pma2) {
                    final MissionProcess missionProcess1 = pma1.getMissionProcess();
                    final MissionProcess missionProcess2 = pma2.getMissionProcess();

                    final int c = MissionProcess.COMPARATOR_BY_PROCESS_NUMBER.compare(missionProcess1, missionProcess2);
                    return c == 0 ? pma1.getExternalId().compareTo(pma2.getExternalId()) : c;
                }
            };

    public static final Comparator<PersonMissionAuthorization> COMPARATOR_BY_DEPARTURE_DATE =
            new Comparator<PersonMissionAuthorization>() {
                @Override
                public int compare(final PersonMissionAuthorization pma1, final PersonMissionAuthorization pma2) {
                    final MissionProcess missionProcess1 = pma1.getMissionProcess();
                    final MissionProcess missionProcess2 = pma2.getMissionProcess();

                    final Mission mission1 = missionProcess1.getMission();
                    final Mission mission2 = missionProcess2.getMission();

                    final DateTime departure1 = mission1.getDaparture();
                    final DateTime departure2 = mission2.getDaparture();

                    final int c = departure1.compareTo(departure2);

                    return c == 0 ? pma1.getExternalId().compareTo(pma2.getExternalId()) : c;
                }
            };

    public PersonMissionAuthorization() {
        super();
        setMissionSystem(MissionSystem.getInstance());
    }

    public PersonMissionAuthorization(final Person subject, AuthorizationChain authorizationChain) {
        this();
        setSubject(subject);
        final Unit unit = authorizationChain.getUnit();
        setUnit(unit);
        if (authorizationChain.getNext() != null) {
            setNext(new PersonMissionAuthorization(subject, authorizationChain.getNext()));
        }
    }

    public PersonMissionAuthorization(final Mission mission, final Person subject, final AuthorizationChain authorizationChain) {
        this(subject, authorizationChain);
        setMission(mission);
    }

    public void delete() {
        if (hasNext()) {
            getNext().delete();
        }
        setPrevious(null);
        setSubject(null);
        setUnit(null);
        setMission(null);
        setAuthority(null);
        setMissionSystem(null);
        deleteDomainObject();
    }

    public AccountabilityType getWorkingAccountabilityType() {
        final DateTime departure = getMission().getDaparture();
        final Person person = getSubject();

        return person.getParentAccountabilityStream().filter(new Predicate<Accountability>() {
            @Override
            public boolean test(Accountability a) {
                final LocalDate date = departure.toLocalDate();
                if (a.isActive(date)) {
                    if (MissionSystem.REQUIRE_AUTHORIZATION_PREDICATE.test(a) && matchesUnit(a.getParent(), date)) {
                        return true;
                    }
                }
                return false;
            }
        }).map(a -> a.getAccountabilityType()).findAny().orElse(null);
    }

    private boolean matchesUnit(final Party party, final LocalDate date) {
        if (party.isUnit()) {
            final Unit unit = (Unit) party;
            if (unit == getUnit()) {
                return true;
            }
            final Set<AccountabilityType> typesSet =
                    MissionSystem.getInstance().getOrganizationalModel().getAccountabilityTypesSet();
            return unit.getParentAccountabilityStream().anyMatch(
                    a -> a.isActive(date) && typesSet.contains(a.getAccountabilityType()) && matchesUnit(a.getParent(), date));
        }
        return false;
    }

    public boolean canAuthoriseParticipantActivity() {
        final User user = Authenticate.getUser();
        return user != null && canAuthoriseParticipantActivity(user.getPerson());
    }

    public boolean canAuthoriseParticipantActivity(final Person person) {
        return canAuthoriseThisParticipantActivity(person) || canAuthoriseNextParticipantActivity(person);
    }

    private boolean canAuthoriseNextParticipantActivity(Person person) {
        final PersonMissionAuthorization next = getNext();
        return next != null && next.canAuthoriseParticipantActivity(person);
    }

    private boolean canAuthoriseThisParticipantActivity(final Person person) {
        return getAuthority() == null && getDelegatedAuthority() == null
                && getUnit().hasChildAccountabilityIncludingAncestry(MissionSystem.AUTHORIZATION_PREDICATE, person);
    }

    public boolean isAvailableForAuthorization() {
        return getAuthority() == null && getDelegatedAuthority() == null
                && (getNext() == null || getNext().isAvailableForAuthorization());
    }

    public boolean canUnAuthoriseParticipantActivity() {
        final User user = Authenticate.getUser();
        return user != null && canUnAuthoriseParticipantActivity(user.getPerson());
    }

    public boolean canUnAuthoriseParticipantActivity(final Person person) {
        if (person == getSubject()) {
            return false;
        }
        return (getAuthority() != null || getDelegatedAuthority() != null)
                && canUnAuthorise(person, MissionSystem.AUTHORIZATION_PREDICATE)
                && ((getNext() == null) || (getNext().getAuthority() == null && getNext().getDelegatedAuthority() == null));
    }

    private boolean canUnAuthorise(final Person person, final Predicate<Accountability> predicate) {
        final Unit unitForAuthorizationCheck =
                getDelegatedAuthority() != null && getPrevious() != null ? getPrevious().getUnit() : getUnit();
        return unitForAuthorizationCheck.hasChildAccountabilityIncludingAncestry(predicate, person)
                || (getNext() != null && getNext().canUnAuthorise(person, predicate));
    }

    public boolean canUnAuthoriseSomeParticipantActivity(final Person person) {
        return canUnAuthoriseParticipantActivity(person)
                || (getNext() != null && getNext().canUnAuthoriseSomeParticipantActivity(person));
    }

    @Override
    public void setAuthority(final Person authority) {
        super.setAuthority(authority);
        final DateTime authorizationDateTime = authority == null ? null : new DateTime();
        setAuthorizationDateTime(authorizationDateTime);
        if (getNext() != null) {
            getNext().setDelegatedAuthority(authority);
        }
    }

    private void setDelegatedAuthority(final Person authority) {
        if (authority == null) {
            setDelegatedAuthority((FunctionDelegation) null);
            return;
        }

        authority.getParentAccountabilityStream().filter(a -> a.isActiveNow() && MissionSystem.AUTHORIZATION_PREDICATE.test(a))
                .map(a -> a.getFunctionDelegationDelegator()).filter(fd -> fd != null)
                .filter(fd -> fd.getAccountabilityDelegator().getParent() == getUnit()).findAny()
                .ifPresent(fd -> setDelegatedAuthority(fd));
    }

    public boolean hasAnyAuthorization() {
        return (getAuthorizationDateTime() != null && (getAuthority() != null || getDelegatedAuthority() != null))
                || (getNext() != null && getNext().hasAnyAuthorization());
    }

    public boolean isAuthorized() {
        return (getNext() == null && (getAuthority() != null || getDelegatedAuthority() != null)) || (getNext() != null && getNext().isAuthorized());
    }

    public boolean isPreAuthorized() {
        return getNext() == null || getNext().getNext() == null || ((getAuthority() != null || getDelegatedAuthority() != null) && getNext().isPreAuthorized());
    }

    public int getChainSize() {
        return getNext() != null ? getNext().getChainSize() + 1 : 1;
    }

    public Mission getAssociatedMission() {
        final Mission mission = getMission();
        return mission != null || getPrevious() == null ? mission : getPrevious().getAssociatedMission();
    }

    public void clearAuthorities() {
        if (hasNext()) {
            getNext().clearAuthorities();
        }
        setAuthority(null);
        setDelegatedAuthority((FunctionDelegation) null);
    }

    public MissionProcess getMissionProcess() {
        return getMission() != null ? getMission().getMissionProcess() : (getPrevious() != null ? getPrevious().getMissionProcess() : null);
    }

    public boolean isProcessTakenByOtherUser() {
        final MissionProcess missionProcess = getMissionProcess();
        return missionProcess.getCurrentOwner() != null && !missionProcess.isTakenByCurrentUser();
    }

    public boolean isUnitObserver(final User user) {
        final Unit unit = getUnit();
        final pt.ist.expenditureTrackingSystem.domain.organization.Unit expenditureUnit = unit.getExpenditureUnit();
        if (expenditureUnit != null && expenditureUnit.isUnitObserver(user)) {
            return true;
        }
        return hasNext() && getNext().isUnitObserver(user);
    }

    @ConsistencyPredicate
    public boolean checkIsConnectedToList() {
        return ((getMission() != null && getPrevious() == null) || (getMission() == null && getPrevious() != null));
    }

    @Deprecated
    public boolean hasAuthorizationDateTime() {
        return getAuthorizationDateTime() != null;
    }

    @Deprecated
    public boolean hasMissionSystem() {
        return getMissionSystem() != null;
    }

    @Deprecated
    public boolean hasSubject() {
        return getSubject() != null;
    }

    @Deprecated
    public boolean hasMission() {
        return getMission() != null;
    }

    @Deprecated
    public boolean hasAuthority() {
        return getAuthority() != null;
    }

    @Deprecated
    public boolean hasDelegatedAuthority() {
        return getDelegatedAuthority() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

    @Deprecated
    public boolean hasPrevious() {
        return getPrevious() != null;
    }

    @Deprecated
    public boolean hasNext() {
        return getNext() != null;
    }

}
