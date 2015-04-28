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

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import jvstm.cps.ConsistencyPredicate;
import module.mission.domain.util.AuthorizationChain;
import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.FunctionDelegation;
import module.organization.domain.Party;
import module.organization.domain.Person;
import module.organization.domain.Unit;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

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
        final Set<AccountabilityType> accountabilityTypes =
                MissionSystem.getInstance().getAccountabilityTypesRequireingAuthorization();
        final DateTime departure = getMission().getDaparture();
        final Person person = getSubject();
        for (final Accountability accountability : person.getParentAccountabilitiesSet()) {
            final LocalDate date = departure.toLocalDate();
            if (accountability.isActive(date)) {
                final AccountabilityType accountabilityType = accountability.getAccountabilityType();
                if (accountabilityTypes.contains(accountabilityType) && matchesUnit(accountability.getParent(), date)) {
                    return accountabilityType;
                }
            }
        }
        return null;
    }

    private boolean matchesUnit(final Party party, final LocalDate date) {
        if (party.isUnit()) {
            final Unit unit = (Unit) party;
            if (unit == getUnit()) {
                return true;
            }
            final Set<AccountabilityType> typesSet =
                    MissionSystem.getInstance().getOrganizationalModel().getAccountabilityTypesSet();
            for (final Accountability accountability : unit.getParentAccountabilitiesSet()) {
                if (accountability.isActive(date) && typesSet.contains(accountability.getAccountabilityType())) {
                    if (matchesUnit(accountability.getParent(), date)) {
                        return true;
                    }
                }
            }
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
        return hasNext() && getNext().canAuthoriseParticipantActivity(person);
    }

    private boolean canAuthoriseThisParticipantActivity(final Person person) {
        final MissionSystem instance = MissionSystem.getInstance();
        final Set<AccountabilityType> accountabilityTypes = instance.getAccountabilityTypesThatAuthorize();
        return !hasAuthority() && !hasDelegatedAuthority()
                && getUnit().hasChildAccountabilityIncludingAncestry(accountabilityTypes, person);
    }

    public boolean isAvailableForAuthorization() {
        final PersonMissionAuthorization next = getNext();
        return !hasAuthority() && !hasDelegatedAuthority() && (next == null || next.isAvailableForAuthorization());
    }

    public boolean canUnAuthoriseParticipantActivity() {
        final User user = Authenticate.getUser();
        return user != null && canUnAuthoriseParticipantActivity(user.getPerson());
    }

    public boolean canUnAuthoriseParticipantActivity(final Person person) {
        if (person == getSubject()) {
            return false;
        }
        final MissionSystem instance = MissionSystem.getInstance();
        final Set<AccountabilityType> accountabilityTypes = instance.getAccountabilityTypesThatAuthorize();
        // final AccountabilityType accountabilityType =
        // IstAccountabilityType.PERSONNEL_RESPONSIBLE_MISSIONS.readAccountabilityType();
        return (hasAuthority() || hasDelegatedAuthority()) && canUnAuthorise(person, accountabilityTypes)
                && ((!hasNext()) || (!getNext().hasAuthority() && !getNext().hasDelegatedAuthority()));
    }

    private boolean canUnAuthorise(final Person person, final Collection<AccountabilityType> accountabilityTypes) {
        final Unit unitForAuthorizationCheck = hasDelegatedAuthority() && hasPrevious() ? getPrevious().getUnit() : getUnit();
        return unitForAuthorizationCheck.hasChildAccountabilityIncludingAncestry(accountabilityTypes, person)
                || (hasNext() && getNext().canUnAuthorise(person, accountabilityTypes));
    }

    public boolean canUnAuthoriseSomeParticipantActivity(final Person person) {
        return canUnAuthoriseParticipantActivity(person)
                || (hasNext() && getNext().canUnAuthoriseSomeParticipantActivity(person));
    }

    @Override
    public void setAuthority(final Person authority) {
        super.setAuthority(authority);
        final DateTime authorizationDateTime = authority == null ? null : new DateTime();
        setAuthorizationDateTime(authorizationDateTime);
        if (hasNext()) {
            getNext().setDelegatedAuthority(authority);
        }
    }

    private void setDelegatedAuthority(final Person authority) {
        if (authority == null) {
            setDelegatedAuthority((FunctionDelegation) null);
            return;
        }

        final Set<AccountabilityType> accountabilityTypes = MissionSystem.getInstance().getAccountabilityTypesThatAuthorize();
        for (final Accountability accountability : authority.getParentAccountabilitiesSet()) {
            if (!accountability.isActiveNow() || !accountabilityTypes.contains(accountability.getAccountabilityType())) {
                continue;
            }

            final FunctionDelegation functionDelegation = accountability.getFunctionDelegationDelegator();
            if (functionDelegation != null) {
                final Accountability parentAccountability = functionDelegation.getAccountabilityDelegator();
                if (getUnit() == parentAccountability.getParent()) {
                    setDelegatedAuthority(functionDelegation);
                    return;
                }
            }
        }
    }

    public boolean hasAnyAuthorization() {
        return (getAuthorizationDateTime() != null && (hasAuthority() || hasDelegatedAuthority()))
                || (hasNext() && getNext().hasAnyAuthorization());
    }

    public boolean isAuthorized() {
        return (!hasNext() && (hasAuthority() || hasDelegatedAuthority())) || (hasNext() && getNext().isAuthorized());
    }

    public boolean isPreAuthorized() {
        return !hasNext() || !getNext().hasNext() || ((hasAuthority() || hasDelegatedAuthority()) && getNext().isPreAuthorized());
    }

    public int getChainSize() {
        return hasNext() ? getNext().getChainSize() + 1 : 1;
    }

    public Mission getAssociatedMission() {
        final Mission mission = getMission();
        return mission != null || !hasPrevious() ? mission : getPrevious().getAssociatedMission();
    }

    public void clearAuthorities() {
        if (hasNext()) {
            getNext().clearAuthorities();
        }
        setAuthority(null);
        setDelegatedAuthority((FunctionDelegation) null);
    }

    public MissionProcess getMissionProcess() {
        return hasMission() ? getMission().getMissionProcess() : (hasPrevious() ? getPrevious().getMissionProcess() : null);
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
        return ((hasMission() && !hasPrevious()) || (!hasMission() && hasPrevious()));
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
