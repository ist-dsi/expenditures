/*
 * @(#)MissionAuthorizationMap.java
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
package module.mission.domain.util;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.LocalDate;

import module.mission.domain.Mission;
import module.mission.domain.MissionAuthorizationAccountabilityType;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import module.mission.domain.MissionYear;
import module.mission.domain.PersonMissionAuthorization;
import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.domain.Unit;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class MissionAuthorizationMap implements Serializable {

    private final int NUMBER_OF_LEVELS = 3;

    private final Unit[] levels = new Unit[NUMBER_OF_LEVELS];
    private final Unit[] levelsForUser = new Unit[NUMBER_OF_LEVELS];
    private final Set<PersonMissionAuthorization>[] personMissionAuthorizations = new Set[NUMBER_OF_LEVELS];

    private final User user = Authenticate.getUser();

    public MissionAuthorizationMap(final MissionYear missionYear) {
        final MissionSystem missionSystem = MissionSystem.getInstance();
        final OrganizationalModel organizationalModel = missionSystem.getOrganizationalModel();
        if (organizationalModel == null) {
            return;
        }
        findLevel(0, organizationalModel.getPartiesSet().stream());
        if (levels[0] != null) {
            final Set<AccountabilityType> types = organizationalModel.getAccountabilityTypesSet();
            findLevel(1, levels[0].getChildAccountabilityStream().filter(a -> match(a, types)).map(a -> a.getChild()));
            if (levels[1] != null) {
                findLevel(2, levels[1].getChildAccountabilityStream().filter(a -> match(a, types)).map(a -> a.getChild()));
            }
        }
        findPersonMissionAuthorizations();
    }

    private boolean match(final Accountability a, final Set<AccountabilityType> types) {
        return types.isEmpty() || types.contains(a.getAccountabilityType());
    }

    private void findPersonMissionAuthorizations() {
        for (int i = 0; i < levelsForUser.length; i++) {
            Unit unit = levelsForUser[i];
            if (unit != null) {
                personMissionAuthorizations[i] =
                        new TreeSet<PersonMissionAuthorization>(PersonMissionAuthorization.COMPARATOR_BY_PROCESS_NUMBER);
                for (PersonMissionAuthorization personMissionAuthorization : unit.getPersonMissionAuthorizationSet()) {
                    Mission mission = personMissionAuthorization.getAssociatedMission();
                    MissionProcess missionProcess = mission.getMissionProcess();
                    if (!personMissionAuthorization.hasAuthority() && !personMissionAuthorization.hasDelegatedAuthority()

                            && (!personMissionAuthorization.hasPrevious() || (personMissionAuthorization.hasPrevious()
                                    && (personMissionAuthorization.getPrevious().hasAuthority()
                                            || personMissionAuthorization.getPrevious().hasDelegatedAuthority())))

                            && missionProcess.canAuthoriseParticipantActivity()

                            && MissionState.PARTICIPATION_AUTHORIZATION.isPending(missionProcess)
                            && !personMissionAuthorization.isProcessTakenByOtherUser()) {
                        personMissionAuthorizations[i].add(personMissionAuthorization);
                    }
                }
            }
        }
    }

    private void findLevel(final int index, final Stream<Party> parties) {
        for (final Unit unit : parties.filter(p -> p.isUnit()).map(p -> (Unit) p)
                .filter(u -> u.getMissionSystemFromUnitWithResumedAuthorizations() != null).collect(Collectors.toSet())) {
            final boolean[] hasSomeResponsible = new boolean[] { false };
            unit.getChildAccountabilityStream().filter(a -> a.isActive(new LocalDate())).forEach(new Consumer<Accountability>() {
                @Override
                public void accept(final Accountability a) {
                    final AccountabilityType accountabilityType = a.getAccountabilityType();
                    if (isResponsibleAccountabilityType(accountabilityType)) {
                        hasSomeResponsible[0] = true;
                        if (a.getChild() == user.getPerson()) {
                            levelsForUser[index] = unit;
                        }
                    }
                }
            });
            if (hasSomeResponsible[0]) {
                levels[index] = unit;
                return;
            }
        }
    }

    private boolean isResponsibleAccountabilityType(AccountabilityType accountabilityType) {
        final MissionSystem missionSystem = MissionSystem.getInstance();
        for (final MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType : missionSystem
                .getMissionAuthorizationAccountabilityTypesSet()) {
            if (missionAuthorizationAccountabilityType.getAccountabilityTypesSet().contains(accountabilityType)) {
                return true;
            }
        }
        return false;
    }

    public Unit[] getLevelsForUser() {
        return levelsForUser;
    }

    public Set<PersonMissionAuthorization>[] getPersonMissionAuthorizations() {
        return personMissionAuthorizations;
    }

    public boolean hasSomeUnit() {
        for (Unit element : levelsForUser) {
            if (element != null) {
                return true;
            }
        }
        return false;
    }

}
