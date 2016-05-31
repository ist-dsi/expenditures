/*
 * @(#)ParticipantAuthorizationChain.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import module.mission.domain.MissionSystem;
import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.domain.Person;
import module.organization.domain.Unit;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ParticipantAuthorizationChain implements Serializable {

    private Person person;
    private AuthorizationChain authorizationChain;

    public ParticipantAuthorizationChain(final Person person, final AuthorizationChain authorizationChain) {
        setPerson(person);
        setAuthorizationChain(authorizationChain);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public AuthorizationChain getAuthorizationChain() {
        return authorizationChain;
    }

    public void setAuthorizationChain(final AuthorizationChain authorizationChain) {
        this.authorizationChain = removeSelfAuthorizationSteps(authorizationChain);
    }

    private AuthorizationChain removeSelfAuthorizationSteps(final AuthorizationChain authorizationChain) {
        final AuthorizationChain next = authorizationChain.getNext();
        if (next == null) {
            return authorizationChain;
        }
        if (canSelfAuthorize(authorizationChain.getUnit())) {
            return removeSelfAuthorizationSteps(next);
        }
        authorizationChain.setNext(removeSelfAuthorizationSteps(next));
        return authorizationChain;
    }

    private boolean canSelfAuthorize(final Unit unit) {
        return unit.getChildAccountabilityStream().anyMatch(
                a -> MissionSystem.AUTHORIZATION_PREDICATE.test(a) && a.getChild() == person && a.isActiveNow());
    }

    private static Collection<AccountabilityType> getAccountabilityTypes() {
        final OrganizationalModel organizationalModel = MissionSystem.getInstance().getOrganizationalModel();
        return organizationalModel == null ? null : organizationalModel.getAccountabilityTypesSet();
    }

    public static Collection<ParticipantAuthorizationChain> getParticipantAuthorizationChains(final Person person) {
        if (!isEmployeeOfInstitution(person) || !MissionSystem.getInstance().useWorkingPlaceAuthorizationChain()) {
            return Collections.emptySet();
        }
        final Collection<Accountability> parentAccountabilities =
                person.getParentAccountabilityStream().filter(a -> MissionSystem.REQUIRE_AUTHORIZATION_PREDICATE.test(a))
                        .collect(Collectors.toSet());
        final Collection<ParticipantAuthorizationChain> participantAuthorizationChains =
                new ArrayList<ParticipantAuthorizationChain>();
        for (final AuthorizationChain authorizationChain : getParticipantAuthorizationChains(parentAccountabilities)) {
            final ParticipantAuthorizationChain participantAuthorizationChain =
                    new ParticipantAuthorizationChain(person, authorizationChain);
            participantAuthorizationChains.add(participantAuthorizationChain);
        }
        return participantAuthorizationChains;
    }

    public static Collection<ParticipantAuthorizationChain> getParticipantAuthorizationChains(final Person person, final Unit unit) {
        final Collection<ParticipantAuthorizationChain> participantAuthorizationChains =
                new ArrayList<ParticipantAuthorizationChain>();

        final OrganizationalModel organizationalModel = MissionSystem.getInstance().getOrganizationalModel();
        final Set<AccountabilityType> accountabilityTypes = organizationalModel.getAccountabilityTypesSet();
        for (final Party party : organizationalModel.getPartiesSet()) {
            if (party.isUnit()) {
                final Unit topLevelUnot = (Unit) party;
                topLevelUnot.getChildAccountabilityStream().filter(a -> match(a, accountabilityTypes) && a.getChild().isUnit())
                        .map(a -> (Unit) a.getChild()).forEach(new Consumer<Unit>() {
                            @Override
                            public void accept(final Unit childUnit) {
                                final AuthorizationChain topLevelUnitChain = new AuthorizationChain(topLevelUnot);
                                final AuthorizationChain childChain = new AuthorizationChain(childUnit, topLevelUnitChain);
                                final Unit firstUnitWithResponsible = findFirstUnitWithResponsible(unit);
                                final AuthorizationChain authorizationChain =
                                        new AuthorizationChain(firstUnitWithResponsible, childChain);

                                final ParticipantAuthorizationChain participantAuthorizationChain =
                                        new ParticipantAuthorizationChain(person, authorizationChain);
                                participantAuthorizationChains.add(participantAuthorizationChain);

                                if (!unit.getChildAccountabilityStream().anyMatch(
                                        a -> MissionSystem.AUTHORIZATION_PREDICATE.test(a) && a.getChild().isPerson())) {
                                    createResponsibleForUnit(firstUnitWithResponsible);
                                }
                            }
                        });;
            }
        }

        return participantAuthorizationChains;
    }

    private static boolean match(final Accountability a, final Collection<AccountabilityType> accountabilityTypes) {
        return accountabilityTypes.isEmpty() || accountabilityTypes.contains(a.getAccountabilityType());
    }

    private static Unit findFirstUnitWithResponsible(final Unit unit) {
        final pt.ist.expenditureTrackingSystem.domain.organization.Unit expenditureUnit = unit.getExpenditureUnit();
        if (expenditureUnit != null) {
            for (final Authorization authorization : expenditureUnit.getAuthorizationsSet()) {
                if (authorization.isValid()) {
                    return unit;
                }
            }
            final pt.ist.expenditureTrackingSystem.domain.organization.Unit parentUnit = expenditureUnit.getParentUnit();
            if (parentUnit != null) {
                return findFirstUnitWithResponsible(parentUnit.getUnit());
            }
        }
        return null;
    }

    @Atomic
    private static void createResponsibleForUnit(final Unit unit) {
        for (final Authorization authorization : unit.getExpenditureUnit().getAuthorizationsSet()) {
            if (authorization.isValid()) {
                final pt.ist.expenditureTrackingSystem.domain.organization.Person authority = authorization.getPerson();
                final User user = authority.getUser();
                if (user != null && user.getPerson() != null) {
                    MissionSystem.getInstance().getMissionAuthorizationAccountabilityTypesSet().stream()
                            .flatMap(t -> t.getAccountabilityTypesSet().stream()).distinct()
                            .forEach(t -> unit.addChild(user.getPerson(), t, new LocalDate(), null, null));
                }
            }
        }
    }

    public static ParticipantAuthorizationChain getMostLikelyParticipantAuthorizationChain(final Person person) {
        final Collection<ParticipantAuthorizationChain> participantAuthorizationChains =
                ParticipantAuthorizationChain.getParticipantAuthorizationChains(person);
        final int count = participantAuthorizationChains.size();
        if (count > 0) {
            if (count == 1) {
                final ParticipantAuthorizationChain participantAuthorizationChain =
                        participantAuthorizationChains.iterator().next();
                return participantAuthorizationChain;
            } else {
                return Collections.max(participantAuthorizationChains, new Comparator<ParticipantAuthorizationChain>() {
                    @Override
                    public int compare(ParticipantAuthorizationChain o1, ParticipantAuthorizationChain o2) {
                        return o1.getChainSize() - o2.getChainSize();
                    }
                });
            }
        }
        return null;
    }

    public static boolean isEmployeeOfInstitution(final Person person) {
        final MissionSystem system = MissionSystem.getInstance();
        final OrganizationalModel model = system.getOrganizationalModel();
        final AccountabilityType employeeType = system.getEmploymentAccountabilityType();
        return person.getParentAccountabilityStream().anyMatch(
                a -> a.getAccountabilityType() == employeeType && model.getPartiesSet().contains(a.getParent()));
    }

    protected int getChainSize() {
        return getAuthorizationChain().getChainSize();
    }

    private static Collection<AuthorizationChain> getParticipantAuthorizationChains(
            final Collection<Accountability> accountabilities) {
        final Collection<AuthorizationChain> result = new ArrayList<AuthorizationChain>();
        for (final Accountability accountability : accountabilities) {
            if (accountability.isActiveNow()) {
                final AccountabilityType accountabilityType = accountability.getAccountabilityType();
                final Set<AccountabilityType> accountabilityTypes =
                        MissionSystem.getInstance().getAccountabilityTypesForAuthorization(accountabilityType);
                final Predicate<Accountability> predicate = new Predicate<Accountability>() {
                    @Override
                    public boolean test(Accountability a) {
                        return accountabilityTypes.contains(a.getAccountabilityType());
                    }
                };
                if (accountabilityTypes != null && !accountabilityTypes.isEmpty()) {
                    for (final AuthorizationChain chain : getParticipantAuthorizationChains(predicate, accountability)) {
                        if (chain.isForCurrentInstitution()) {
                            result.add(chain);
                        }
                    }
                }
            }
        }
        return result;
    }

    public static Collection<AuthorizationChain> getParticipantAuthorizationChains(final Predicate<Accountability> predicate,
            final Accountability accountability) {
        final Party party = accountability.getParent();
        if (party.isUnit()) {
            final Unit unit = (Unit) party;
            final Supplier<Stream<Accountability>> parentAccountabilities =
                    () -> unit.getParentAccountabilityStream().filter(a -> match(a, getAccountabilityTypes()));

            if (hasPersonResponsible(predicate, party)) {
                final Collection<AuthorizationChain> result = new ArrayList<AuthorizationChain>();
                if (parentAccountabilities.get().findAny().orElse(null) == null) {
                    final AuthorizationChain authorizationChain = new AuthorizationChain(unit);
                    result.add(authorizationChain);
                    return result;
                } else {
                    parentAccountabilities.get().flatMap(a -> getParticipantAuthorizationChains(predicate, a).stream())
                            .map(c -> new AuthorizationChain(unit, c)).forEach(c -> result.add(c));
                }
                return result;
            } else {
                return parentAccountabilities.get().flatMap(a -> getParticipantAuthorizationChains(predicate, a).stream())
                        .collect(Collectors.toSet());
            }
        }
        return Collections.emptyList();
    }

    private static boolean hasPersonResponsible(final Predicate<Accountability> predicate, final Party party) {
        return party.getChildAccountabilityStream().anyMatch(a -> predicate.test(a) && a.isActive(new LocalDate()));
    }

}
