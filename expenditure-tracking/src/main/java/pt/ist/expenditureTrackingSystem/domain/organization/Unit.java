/*
 * @(#)Unit.java
 *
 * Copyright 2009 Instituto Superior Tecnico
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import module.organization.domain.Accountability;
import module.organization.domain.Party;
import module.organization.domain.PartyType;
import module.organization.domain.UnitBean;
import module.workflow.util.ProcessEvaluator;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.authorizations.AuthorizationLog;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateUnitBean;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.plugins.luceneIndexing.IndexableField;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author João Neves
 * @author Pedro Santos
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class Unit extends Unit_Base /* implements Indexable, Searchable */{

    public static enum UnitIndexFields implements IndexableField {

        NAME_INDEX("name"), NUMBER_INDEX("number");

        private String fieldName;

        private UnitIndexFields(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public String getFieldName() {
            return fieldName;
        }

    }

    public boolean isProject() {
        return false;
    }

    public static final Comparator<Unit> COMPARATOR_BY_PRESENTATION_NAME = new Comparator<Unit>() {

        @Override
        public int compare(final Unit unit1, Unit unit2) {
            int c = unit1.getPresentationName().compareTo(unit2.getPresentationName());
            return c == 0 ? unit1.getExternalId().compareTo(unit2.getExternalId()) : c;
        }

    };

    public Unit() {
        super();
        final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();
        setExpenditureTrackingSystem(expenditureTrackingSystem);
        setDefaultRegeimIsCCP(Boolean.TRUE);
    }

    public Unit(final Unit parentUnit, final String name) {
        this();
        final String acronym = StringUtils.abbreviate(name, 5);
        createRealUnit(this, parentUnit, ExpenditureTrackingSystem.getInstance().getUnitPartyType(), acronym, name);

        // TODO : After this object is refactored to retrieve the name and
        // parent from the real unit,
        // the following two lines may be deleted.
        setName(name);
        setParentUnit(parentUnit);
    }

    public void setName(final String name) {
        getUnit().setPartyName(new MultiLanguageString(name));
    }

    public String getName() {
        return getUnit().getPartyName().getContent();
    }

    protected static void createRealUnit(final Unit expenditureUnit, final Unit parentExpenditureUnit, final PartyType partyType,
            final String acronym, final String name) {
        final UnitBean unitBean = new UnitBean();
        unitBean.setParent(parentExpenditureUnit.getUnit());
        unitBean.setAccountabilityType(ExpenditureTrackingSystem.getInstance().getOrganizationalAccountabilityType());
        unitBean.setAcronym(acronym);
        unitBean.setBegin(new LocalDate());
        unitBean.setEnd(null);
        unitBean.setName(new MultiLanguageString(name));
        unitBean.setPartyType(partyType);
        final module.organization.domain.Unit createdUnit = unitBean.createUnit();
        expenditureUnit.setUnit(createdUnit);
    }

    public static final Unit createRealUnit(final Unit parentExpenditureUnit, final PartyType partyType, final String acronym,
            final String name) {
        final UnitBean unitBean = new UnitBean();
        unitBean.setParent(parentExpenditureUnit.getUnit());
        unitBean.setAccountabilityType(ExpenditureTrackingSystem.getInstance().getOrganizationalAccountabilityType());
        unitBean.setAcronym(acronym);
        unitBean.setBegin(new LocalDate());
        unitBean.setEnd(null);
        unitBean.setName(new MultiLanguageString(name));
        unitBean.setPartyType(partyType);
        final module.organization.domain.Unit createdUnit = unitBean.createUnit();
        return createdUnit.getExpenditureUnit();
    }

    public void setParentUnit(final Unit parentUnit) {
	final ExpenditureTrackingSystem system = ExpenditureTrackingSystem.getInstance();
	setExpenditureTrackingSystemFromTopLevelUnit(parentUnit == null ? system : null);

	final module.organization.domain.Unit realtUnit = getUnit();
	final module.organization.domain.Unit realParentUnit = parentUnit == null ? null : parentUnit.getUnit();
        final LocalDate now = new LocalDate();
        for (final Accountability accountability : realtUnit.getParentAccountabilitiesSet()) {
            if ((accountability.getEndDate() == null || accountability.getEndDate().isAfter(now))
        	    && accountability.getParent() != realParentUnit
        	    && accountability.getAccountabilityType() == system.getOrganizationalAccountabilityType()) {
                accountability.setEndDate(now);
            }
        }

        if (parentUnit != null) {
            realParentUnit.addChild(realtUnit, system.getOrganizationalAccountabilityType(), new LocalDate(), null);
        }
    }

    private void deleteUnit() {
        final module.organization.domain.Unit unit = getUnit();
        setUnit(null);
        unit.delete();
    }

    @Atomic
    public static Unit createNewUnit(final CreateUnitBean createUnitBean) {
        if (createUnitBean.getCostCenter() != null) {
            return new CostCenter(createUnitBean.getParentUnit(), createUnitBean.getName(), createUnitBean.getCostCenter());
        }
        if (createUnitBean.getProjectCode() != null) {
            final Unit unit =
                    createRealUnit(createUnitBean.getParentUnit(), ExpenditureTrackingSystem.getInstance().getProjectPartyType(),
                            createUnitBean.getProjectCode(), createUnitBean.getName());
            final Project project = (Project) unit;
            project.setName(createUnitBean.getName());
            project.setProjectCode(createUnitBean.getProjectCode());
            project.setParentUnit(createUnitBean.getParentUnit());
            return project;
        }
        return new Unit(createUnitBean.getParentUnit(), createUnitBean.getName());
    }

    @Atomic
    public static Unit createTopLevelUnit(final module.organization.domain.Unit organizationUnit,
            ExpenditureTrackingSystem expenditureTrackingSystem) {
        Unit newUnit = new Unit();
        newUnit.setUnit(organizationUnit);
        newUnit.setExpenditureTrackingSystem(expenditureTrackingSystem);
        newUnit.setExpenditureTrackingSystemFromTopLevelUnit(expenditureTrackingSystem);
        return newUnit;
    }

    @Atomic
    public void delete() {
        if (!getAuthorizationsSet().isEmpty()) {
            throw new DomainException("error.cannot.delete.units.which.have.or.had.authorizations");
        }
        if (hasAnyFinancedItems()) {
            throw new DomainException("error.cannot.delete.units.which.have.or.had.financedItems");
        }
        if (!getUnit().getParentAccountabilitiesSet().isEmpty()) {
            throw new DomainException("error.cannot.delete.units.which.have.subUnits");
        }

        setExpenditureTrackingSystemFromTopLevelUnit(null);
        deleteUnit();
        setExpenditureTrackingSystem(null);
        deleteDomainObject();
    }

    public void findAcquisitionProcessesPendingAuthorization(final Set<AcquisitionProcess> result, final boolean recurseSubUnits) {
        findAcquisitionProcessesPendingAuthorization(getUnit(), result, recurseSubUnits);
    }

    public static void findAcquisitionProcessesPendingAuthorization(final Party party, final Set<AcquisitionProcess> result,
            final boolean recurseSubUnits) {
        if (recurseSubUnits) {
            for (final Accountability accountability : party.getChildAccountabilitiesSet()) {
                final Party child = accountability.getChild();
                if (child.isUnit()) {
                    final module.organization.domain.Unit childUnit = (module.organization.domain.Unit) child;
                    if (childUnit.getExpenditureUnit() != null) {
                        final Unit expenditureUnit = childUnit.getExpenditureUnit();
                        if (expenditureUnit instanceof CostCenter || expenditureUnit instanceof Project) {
                            expenditureUnit.findAcquisitionProcessesPendingAuthorization(result, recurseSubUnits);
                        }
                    }
                    findAcquisitionProcessesPendingAuthorization(child, result, recurseSubUnits);
                }
            }
        }
    }

    public static Unit findUnitByCostCenter(final String costCenter) {
        final Party party =
                Party.findPartyByPartyTypeAndAcronymForAccountabilityTypeLink((Set) MyOrg.getInstance().getTopUnitsSet(),
                        ExpenditureTrackingSystem.getInstance().getOrganizationalAccountabilityType(), ExpenditureTrackingSystem
                                .getInstance().getCostCenterPartyType(), "CC. " + costCenter);
        return party == null || !party.isUnit() ? null : ((module.organization.domain.Unit) party).getExpenditureUnit();
    }

    public boolean isCurrentUserResponsibleForUnit() {
        return isResponsible(getUnit(), Person.getLoggedPerson());
    }

    public boolean isResponsible(Person person) {
        return isResponsible(getUnit(), person);
    }

    public static boolean isResponsible(final Party party, final Person person) {
        if (party.isUnit()) {

            if (!person.hasAnyAuthorizations()) {
                return false;
            }
            final module.organization.domain.Unit unit = (module.organization.domain.Unit) party;
            LocalDate today = new LocalDate();
            if (unit.getExpenditureUnit() != null) {
                for (Authorization authorization : unit.getExpenditureUnit().getAuthorizationsSet()) {
                    if (authorization.isValidFor(today) && authorization.getPerson() == person) {
                        return true;
                    }
                }
            }

            for (final Accountability accountability : unit.getParentAccountabilitiesSet()) {
                if (accountability.getAccountabilityType() == ExpenditureTrackingSystem.getInstance()
                        .getOrganizationalAccountabilityType()) {
                    final Party parent = accountability.getParent();
                    if (isResponsible(accountability.getParent(), person)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isResponsible(final Person person, final Money amount) {
        for (Authorization authorization : person.getAuthorizationsSet()) {
            if (authorization.isValid() && authorization.getMaxAmount().isGreaterThanOrEqual(amount)
                    && isSubUnit(authorization.getUnit())) {
                return true;
            }
        }
        return false;
    }

    public boolean isDirectResponsible(final Person person) {
        return isDirectResponsible(person, Money.ZERO);
    }

    public boolean isDirectResponsible(final Person person, final Money amount) {
        boolean hasSomeOtherValidAuthorization = false;
        for (final Authorization authorization : getAuthorizationsSet()) {
            if (authorization.isValid() && authorization.getMaxAmount().isGreaterThanOrEqual(amount)) {
                if (authorization.getPerson() == person) {
                    return true;
                }
                hasSomeOtherValidAuthorization = true;
            }
        }
        if (hasSomeOtherValidAuthorization) {
            return false;
        }
        final Unit parentUnit = getParentUnit();
        return parentUnit != null && parentUnit.isDirectResponsible(person, amount);
    }

    public Authorization findClosestAuthorization(final Person person, final Money money) {
        return findClosestAuthorization(getUnit(), person, money);
    }

    public static Authorization findClosestAuthorization(final Party party, final Person person, final Money money) {
        if (party.isUnit()) {
            final module.organization.domain.Unit unit = (module.organization.domain.Unit) party;
            if (unit.getExpenditureUnit() != null) {
                for (final Authorization authorization : unit.getExpenditureUnit().getAuthorizationsSet()) {
                    if (authorization.getPerson() == person && authorization.isValid()) {
                        if (authorization.getMaxAmount().isGreaterThanOrEqual(money)) {
                            return authorization;
                        }
                    }
                }
            }

            for (final Accountability accountability : unit.getParentAccountabilitiesSet()) {
                if (accountability.getAccountabilityType() == ExpenditureTrackingSystem.getInstance()
                        .getOrganizationalAccountabilityType()) {
                    final Party parent = accountability.getParent();
                    final Authorization authorization = findClosestAuthorization(accountability.getParent(), person, money);
                    if (authorization != null) {
                        return authorization;
                    }
                }
            }
        }
        return null;
    }

    public String getPresentationName() {
        return getName();
    }

    public String getType() {
        return ResourceBundle.getBundle("resources/ExpenditureOrganizationResources", Language.getLocale()).getString(
                "label." + getClass().getSimpleName());
    }

    public String getShortIdentifier() {
        return "";
    }

    public boolean isSubUnit(final Unit unit) {
        return isSubUnit(getUnit(), unit);
    }

    public static boolean isSubUnit(final Party party, final Unit unit) {
        if (party == null || unit == null || !party.isUnit()) {
            return false;
        }
        final module.organization.domain.Unit parentUnit = (module.organization.domain.Unit) party;
        if (unit == parentUnit.getExpenditureUnit()) {
            return true;
        }
        for (final Accountability accountability : parentUnit.getParentAccountabilitiesSet()) {
            if (accountability.getAccountabilityType() == ExpenditureTrackingSystem.getInstance()
                    .getOrganizationalAccountabilityType()) {
                final Party parent = accountability.getParent();
                if (isSubUnit(parent, unit)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAccountingEmployee(final Person person) {
        final Unit parentUnit = getParentUnit();
        return parentUnit != null && parentUnit.isAccountingEmployee(person);
    }

    public Financer finance(final RequestWithPayment acquisitionRequest) {
        throw new Error("Units with no accounting cannot finance any acquisitions: " + getExternalId());
    }

    public boolean isProjectAccountingEmployee(Person person) {
        final Unit parentUnit = getParentUnit();
        return parentUnit != null && parentUnit.isProjectAccountingEmployee(person);
    }

    public CostCenter getCostCenterUnit() {
        return getParentUnit() != null ? getParentUnit().getCostCenterUnit() : null;
    }

    public boolean hasResponsibleInSubUnits() {
        for (final Accountability accountability : getUnit().getChildAccountabilitiesSet()) {
            if (accountability.getAccountabilityType() == ExpenditureTrackingSystem.getInstance()
                    .getOrganizationalAccountabilityType()) {
                final Party child = accountability.getChild();
                if (child.isUnit()) {
                    final module.organization.domain.Unit unit = (module.organization.domain.Unit) child;
                    if (unit.getExpenditureUnit() != null && unit.getExpenditureUnit().hasResponsiblesInUnit()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasResponsiblesInUnit() {
        return getAuthorizations().size() > 0;
    }

    public boolean hasAuthorizationsFor(Person person) {
        return hasAuthorizationsFor(person, null);
    }

    public boolean hasAuthorizationsFor(final Person person, final Money money) {
        for (final Authorization authorization : getAuthorizations()) {
            if (authorization.getPerson() == person
                    && (money == null || authorization.getMaxAmount().isGreaterThanOrEqual(money))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasActiveAuthorizationsFor(final Person person, final Money money) {
        for (final Authorization authorization : getAuthorizations()) {
            if (authorization.isValid() && authorization.getPerson() == person
                    && (money == null || authorization.getMaxAmount().isGreaterThanOrEqual(money))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyAuthorizationForAmount(final Money money) {
        for (final Authorization authorization : getAuthorizations()) {
            if (authorization.getMaxAmount().isGreaterThanOrEqual(money)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyActiveAuthorizationForAmount(final Money money) {
        for (final Authorization authorization : getAuthorizations()) {
            if (authorization.isValid() && authorization.getMaxAmount().isGreaterThanOrEqual(money)) {
                return true;
            }
        }
        return false;
    }

    public boolean isMostDirectAuthorization(Person person, Money money) {
        return !hasAnyAuthorizations() ? hasParentUnit() && getParentUnit().isMostDirectAuthorization(person, money) : hasAnyActiveAuthorizationForAmount(money) ? hasActiveAuthorizationsFor(
                person, money) : hasParentUnit() && getParentUnit().isMostDirectAuthorization(person, money);
    }

    public Unit getParentUnit() {
        return getParentUnit(getUnit());
    }

    private static Unit getParentUnit(final module.organization.domain.Unit unit) {
        for (final Accountability accountability : unit.getParentAccountabilitiesSet()) {
            if (accountability.getAccountabilityType() == ExpenditureTrackingSystem.getInstance()
                    .getOrganizationalAccountabilityType()
                    && accountability.isActiveNow()) {
                final Party parent = accountability.getParent();
                if (parent.isUnit()) {
                    final module.organization.domain.Unit parentUnit = (module.organization.domain.Unit) parent;
                    if (parentUnit.getExpenditureUnit() != null) {
                        return parentUnit.getExpenditureUnit();
                    }
                    final Unit result = getParentUnit(parentUnit);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return getParentUnitSafe(unit);
    }

    private static Unit getParentUnitSafe(final module.organization.domain.Unit unit) {
        for (final Accountability accountability : unit.getParentAccountabilitiesSet()) {
            if (accountability.getAccountabilityType() == ExpenditureTrackingSystem.getInstance()
                    .getOrganizationalAccountabilityType()) {
                final Party parent = accountability.getParent();
                if (parent.isUnit()) {
                    final module.organization.domain.Unit parentUnit = (module.organization.domain.Unit) parent;
                    if (parentUnit.getExpenditureUnit() != null) {
                        return parentUnit.getExpenditureUnit();
                    }
                    final Unit result = getParentUnitSafe(parentUnit);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    public boolean hasParentUnit() {
        return hasParentUnit(getUnit());
    }

    protected static boolean isActive(final module.organization.domain.Unit unit) {
        for (final Accountability accountability : unit.getParentAccountabilitiesSet()) {
            if (accountability.getAccountabilityType() == ExpenditureTrackingSystem.getInstance()
                    .getOrganizationalAccountabilityType() && accountability.isActiveNow()) {
                final Party parent = accountability.getParent();
                if (parent.getOrganizationalModelsSet().isEmpty()) {
                    if (parent.isUnit() && isActive((module.organization.domain.Unit) parent)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isActive() {
        final module.organization.domain.Unit unit = getUnit();
        return isActive(unit);
    }

    private static boolean hasParentUnit(final module.organization.domain.Unit unit) {
        for (final Accountability accountability : unit.getParentAccountabilitiesSet()) {
            if (accountability.getAccountabilityType() == ExpenditureTrackingSystem.getInstance()
                    .getOrganizationalAccountabilityType()) {
                final Party parent = accountability.getParent();
                if (parent.isUnit()) {
                    final module.organization.domain.Unit parentUnit = (module.organization.domain.Unit) parent;
                    if (parentUnit.getExpenditureUnit() != null) {
                        return true;
                    }
                    final boolean result = hasParentUnit(parentUnit);
                    if (result) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isTreasuryMember(Person person) {
        final AccountingUnit accountingUnit = getAccountingUnit(getUnit(), person);
        return accountingUnit != null && accountingUnit.getTreasuryMembersSet().contains(person);
    }

    public static AccountingUnit getAccountingUnit(final Party party, final Person person) {
        if (party.isUnit()) {
            final module.organization.domain.Unit unit = (module.organization.domain.Unit) party;
            if (unit.getExpenditureUnit() != null) {
                final Unit expenditureUnit = unit.getExpenditureUnit();
                if (expenditureUnit.hasAccountingUnit()) {
                    return expenditureUnit.getAccountingUnit();
                }
            }
            for (final Accountability accountability : party.getParentAccountabilitiesSet()) {
                if (accountability.getAccountabilityType() == ExpenditureTrackingSystem.getInstance()
                        .getOrganizationalAccountabilityType()) {
                    final AccountingUnit accountingUnit = getAccountingUnit(accountability.getParent(), person);
                    if (accountingUnit != null) {
                        return accountingUnit;
                    }
                }
            }
        }
        return null;
    }

    public Set<PaymentProcess> getProcesses(PaymentProcessYear year) {
        Set<PaymentProcess> processes = new HashSet<PaymentProcess>();
        for (Financer financer : getFinancedItems()) {
            PaymentProcess process = financer.getFundedRequest().getProcess();
            if (year == null || process.getPaymentProcessYear() == year) {
                processes.add(process);
            }
        }
        return processes;
    }

    public List<Unit> getAllSubUnits() {
        List<Unit> result = new ArrayList<Unit>();
        addAllSubUnits(result);
        return result;
    }

    protected void addAllSubUnits(final List<Unit> result) {
        addAllSubUnits(result, getUnit());
    }

    private static void addAllSubUnits(final List<Unit> result, final Party party) {
        if (party.isUnit()) {
            final module.organization.domain.Unit unit = (module.organization.domain.Unit) party;
            if (unit.getExpenditureUnit() != null) {
                result.add(unit.getExpenditureUnit());
            }

            for (final Accountability accountability : unit.getChildAccountabilitiesSet()) {
                if (accountability.getAccountabilityType() == ExpenditureTrackingSystem.getInstance()
                        .getOrganizationalAccountabilityType()) {
                    final Party child = accountability.getChild();
                    addAllSubUnits(result, child);
                }
            }
        }
    }

    @Atomic
    public void removeUnit() {
        super.setUnit(null);
    }

    @Override
    @Atomic
    public void setUnit(final module.organization.domain.Unit unit) {
        super.setUnit(unit);
    }

    public Set<AuthorizationLog> getSortedAuthorizationLogsSet() {
        final Set<AuthorizationLog> authorizationLogs = new TreeSet<AuthorizationLog>(AuthorizationLog.COMPARATOR_BY_WHEN);
        authorizationLogs.addAll(getAuthorizationLogsSet());
        return authorizationLogs;
    }

    public SortedSet<Authorization> getSortedAuthorizationsSet() {
        final SortedSet<Authorization> authorizations = new TreeSet<Authorization>(Authorization.COMPARATOR_BY_NAME_AND_DATE);
        authorizations.addAll(getAuthorizationsSet());
        return authorizations;
    }

    public Set<Unit> getSubUnitsSet() {
        final Set<Unit> result = new HashSet<Unit>();
        getSubUnitsSet(result, getUnit());
        return result;
    }

    public static void getSubUnitsSet(final Set<Unit> result, final Party party) {
        for (final Accountability accountability : party.getChildAccountabilitiesSet()) {
            final Party child = accountability.getChild();
            if (child.isUnit()) {
                final module.organization.domain.Unit unit = (module.organization.domain.Unit) child;
                if (unit.getExpenditureUnit() != null) {
                    result.add(unit.getExpenditureUnit());
                } else {
                    getSubUnitsSet(result, unit);
                }
            }
        }
    }

    public boolean hasAnySubUnits() {
        return hasAnySubUnits(getUnit());
    }

    private static boolean hasAnySubUnits(final module.organization.domain.Unit unit) {
        for (final Accountability accountability : unit.getChildAccountabilitiesSet()) {
            final Party child = accountability.getChild();
            if (child.isUnit()) {
                final module.organization.domain.Unit childUnit = (module.organization.domain.Unit) child;
                if (childUnit.getExpenditureUnit() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Atomic
    public void addObservers(Person observer) {
        super.addObservers(observer);
    }

    @Override
    @Atomic
    public void removeObservers(Person observer) {
        super.removeObservers(observer);
    }

    @Atomic
    public void toggleDefaultRegeim() {
        final Boolean currentValue = getDefaultRegeimIsCCP();
        final boolean newValue = currentValue == null ? Boolean.TRUE : !currentValue.booleanValue();
        setDefaultRegeimIsCCP(Boolean.valueOf(newValue));
    }

    public static class UnitProcessEvaluator extends ProcessEvaluator<GenericProcess> {

        private final Set<Unit> processed = new HashSet<Unit>();

        public UnitProcessEvaluator(ProcessEvaluator<GenericProcess> genericProcessEvaluator) {
            next = genericProcessEvaluator;
        }

    }

    public void evaluateAllProcesses(final ProcessEvaluator<GenericProcess> unitProcessEvaluator, final PaymentProcessYear year) {
        // if (!unitProcessEvaluator.processed.contains(this)) {
        // unitProcessEvaluator.processed.add(this);
        for (final Financer financer : getFinancedItems()) {
            final PaymentProcess process = financer.getFundedRequest().getProcess();
            if (year == null || process.getPaymentProcessYear() == year) {
                unitProcessEvaluator.evaluate(process);
            }
        }
        evaluateAllProcesses(unitProcessEvaluator, year, getUnit());

        // for (final Unit unit : getSubUnitsSet()) {
        // unit.evaluateAllProcesses(unitProcessEvaluator, year);
        // }
        // }
    }

    private static void evaluateAllProcesses(final ProcessEvaluator<GenericProcess> unitProcessEvaluator,
            final PaymentProcessYear year, final module.organization.domain.Unit unit) {
        for (final Accountability accountability : unit.getChildAccountabilitiesSet()) {
            final Party child = accountability.getChild();
            if (child.isUnit()) {
                final module.organization.domain.Unit childUnit = (module.organization.domain.Unit) child;
                if (childUnit.getExpenditureUnit() != null) {
                    childUnit.getExpenditureUnit().evaluateAllProcesses(unitProcessEvaluator, year);
                } else {
                    evaluateAllProcesses(unitProcessEvaluator, year, childUnit);
                }
            }
        }
    }

/*
    @Override
    public IndexDocument getDocumentToIndex() {
	IndexDocument document = new IndexDocument(this);
	document.indexField(UnitIndexFields.NAME_INDEX, StringNormalizer.normalize(getName()));
	return document;
    }

    @Override
    public Set<Indexable> getObjectsToIndex() {
	Set<Indexable> set = new HashSet<Indexable>();
	set.add(this);
	return set;
    }
*/

    public boolean isAccountingResponsible(final Person person) {
        final AccountingUnit accountingUnit = getAccountingUnit();
        return accountingUnit != null && person != null && accountingUnit.getResponsiblePeopleSet().contains(person);
    }

    public boolean isAccountManager(final Person accountManager) {
        return getAccountManager() == accountManager || (hasParentUnit() && getParentUnit().isAccountManager(accountManager));
    }

    public boolean hasSomeAccountManager() {
        return hasAccountManager() || (hasParentUnit() && getParentUnit().hasSomeAccountManager());
    }

    public boolean isUnitObserver(final User user) {
        final Person person = user.getExpenditurePerson();
        return getObserversSet().contains(person);
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        return getExpenditureTrackingSystem() == ExpenditureTrackingSystem.getInstance();
    }

    public String getUnitNumber() {
        return null;
    }

    @Override
    public Person getAccountManager() {
        final Person person = super.getAccountManager();
        if (person == null) {
            final Unit parentUnit = getParentUnit();
            return parentUnit == null ? null : parentUnit.getAccountManager();
        }
        return person;
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.SavedSearch> getPayingUnitSearches() {
        return getPayingUnitSearchesSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer> getFinancedItems() {
        return getFinancedItemsSet();
    }

    @Deprecated
    public java.util.Set<module.mission.domain.Mission> getMissions() {
        return getMissionsSet();
    }

    @Deprecated
    public java.util.Set<module.mission.domain.MissionFinancer> getFinancers() {
        return getFinancersSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.Person> getObservers() {
        return getObserversSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.announcements.CCPAnnouncement> getRequestAnnouncements() {
        return getRequestAnnouncementsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.authorizations.AuthorizationLog> getAuthorizationLogs() {
        return getAuthorizationLogsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.SavedSearch> getUnitSearches() {
        return getUnitSearchesSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.announcements.CCPAnnouncement> getBuyAnnouncements() {
        return getBuyAnnouncementsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization> getAuthorizations() {
        return getAuthorizationsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment> getRequests() {
        return getRequestsSet();
    }

    @Deprecated
    public boolean hasAnyPayingUnitSearches() {
        return !getPayingUnitSearchesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyFinancedItems() {
        return !getFinancedItemsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyMissions() {
        return !getMissionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyFinancers() {
        return !getFinancersSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyObservers() {
        return !getObserversSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyRequestAnnouncements() {
        return !getRequestAnnouncementsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyAuthorizationLogs() {
        return !getAuthorizationLogsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyUnitSearches() {
        return !getUnitSearchesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyBuyAnnouncements() {
        return !getBuyAnnouncementsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyAuthorizations() {
        return !getAuthorizationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyRequests() {
        return !getRequestsSet().isEmpty();
    }

    @Deprecated
    public boolean hasDefaultRegeimIsCCP() {
        return getDefaultRegeimIsCCP() != null;
    }

    @Deprecated
    public boolean hasAccountingUnit() {
        return getAccountingUnit() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystemFromTopLevelUnit() {
        return getExpenditureTrackingSystemFromTopLevelUnit() != null;
    }

    @Deprecated
    public boolean hasAccountManager() {
        return getAccountManager() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystem() {
        return getExpenditureTrackingSystem() != null;
    }

}
