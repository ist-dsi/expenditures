/*
 * @(#)UnitActiveResponsibleGroup.java
 *
 * Copyright 2012 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.CustomGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class UnitActiveResponsibleGroup extends CustomGroup {

    private static final long serialVersionUID = -2380706806227584944L;

    @Override
    public String getPresentationName() {
        return BundleUtil.getString(Bundle.ORGANIZATION, "label.persistent.group.unitActiveResponsible.name");
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        // TODO
        return null;
    }

//    protected static <T extends PersistentGroup> Stream<T> filter(Class<T> type) {
//        return (Stream<T>) Bennu.getInstance().getFenixPredicateGroupSet().stream().filter(p -> p.getClass() == type);
//    }
//
//    protected static <T extends FenixPredicateGroup> Optional<T> find(Class<T> type) {
//        return filter(type).findAny();
//    }
//
//    protected static <T extends FenixPersistentGroup> T singleton(Supplier<Optional<T>> selector, Supplier<T> creator) {
//        if (FenixFramework.getTransaction().getTxIntrospector().isWriteTransaction()) {
//            return selector.get().orElseGet(creator);
//        }
//        return selector.get().orElseGet(() -> create(selector, creator));
//    }
//
//    @Atomic(mode = TxMode.WRITE)
//    private static <T extends FenixPersistentGroup> T create(Supplier<Optional<T>> selector, Supplier<T> creator) {
//        return selector.get().orElseGet(creator);
//    }
//
    @Override
    public Stream<User> getMembers() {
        return getMembers(new DateTime());
    }

    @Override
    public Stream<User> getMembers(final DateTime when) {
        final LocalDate localDate = when.toLocalDate();
        return ExpenditureTrackingSystem.getInstance().getAuthorizationsSet().stream().filter(a -> a.isValidFor(localDate) && isExpectedUnitType(a.getUnit()))
            .map(a -> a.getPerson().getUser()).distinct();
    }

    @Override
    public boolean isMember(final User user) {
        return isMember(user, new DateTime());
    }

    @Override
    public boolean isMember(final User user, final DateTime when) {
        final Person person = user.getExpenditurePerson();
        if (person != null) {
            final LocalDate localDate = when.toLocalDate();
            for (final Authorization authorization : person.getAuthorizationsSet()) {
                if (authorization.isValidFor(localDate) && isExpectedUnitType(authorization.getUnit())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object object) {
        return object != null && getClass().equals(object.getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    protected boolean isExpectedUnitType(final Unit unit) {
        return true;
    }

}
