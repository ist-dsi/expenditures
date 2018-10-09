/*
 * @(#)OrganizationAction.java
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
package pt.ist.expenditureTrackingSystem.presentationTier.actions.organization;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.authorizations.AuthorizationLog;
import pt.ist.expenditureTrackingSystem.domain.authorizations.DelegatedAuthorization;
import pt.ist.expenditureTrackingSystem.domain.dto.AccountingUnitBean;
import pt.ist.expenditureTrackingSystem.domain.dto.AuthorizationBean;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateSupplierBean;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateUnitBean;
import pt.ist.expenditureTrackingSystem.domain.dto.SupplierBean;
import pt.ist.expenditureTrackingSystem.domain.dto.UnitBean;
import pt.ist.expenditureTrackingSystem.domain.dto.VariantBean;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.ist.expenditureTrackingSystem.domain.organization.SearchUsers;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.organization.UserAcquisitionProcessStatistics;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions.SearchPaymentProcessesAction;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.organization.util.AcquisitionForSupplierAndCPVBean;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.organization.util.RefundForSupplierAndCPVBean;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.excel.ExcelStyle;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = SearchPaymentProcessesAction.class, path = "expenditure-organization",
        titleKey = "link.viewOrganization")
@Mapping(path = "/expenditureTrackingOrganization")
/**
 * 
 * @author Pedro Santos
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class OrganizationAction extends BaseAction {

    @EntryPoint
    public final ActionForward viewOrganization(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        UnitBean unitBean = getRenderedObject("unitBean");
        if (unitBean == null) {
            unitBean = new UnitBean();
        }
        final Unit unit = unitBean.getUnit() == null ? (Unit) getDomainObject(request, "unitOid") : unitBean.getUnit();
        request.setAttribute("unitBean", unitBean);
        return viewOrganization(mapping, request, unit);
    }

    public final ActionForward viewOrganization(final ActionMapping mapping, final HttpServletRequest request, final Unit unit) {
        final Set<Unit> units;
        if (unit == null) {
            final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();
            units = expenditureTrackingSystem.getTopLevelUnitsSet();
            request.setAttribute("accountingUnits", expenditureTrackingSystem.getAccountingUnitsSet());
        } else {
            request.setAttribute("unit", unit);
            if (unit.getAccountingUnit() != null) {
                request.setAttribute("accountingUnits", Collections.singleton(unit.getAccountingUnit()));
            }
            units = unit.getSubUnitsSet();
        }
        request.setAttribute("units", units);
        return forward("/expenditureTrackingOrganization/viewOrganization.jsp");
    }

    public final ActionForward prepareCreateUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Unit unit = getDomainObject(request, "unitOid");
        request.setAttribute("bean", new CreateUnitBean(unit));
        return forward("/expenditureTrackingOrganization/createUnit.jsp");
    }

    public final ActionForward createNewUnit(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        CreateUnitBean createUnitBean = getRenderedObject();
        final Unit newUnit = Unit.createNewUnit(createUnitBean);
        final UnitBean unitBean = new UnitBean();
        unitBean.setUnit(newUnit);
        request.setAttribute("unitBean", unitBean);
        return viewOrganization(mapping, request, newUnit);
    }

    public final ActionForward prepareCreateAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        request.setAttribute("accountingUnitBean", new AccountingUnitBean());
        return forward("/expenditureTrackingOrganization/createAccountingUnit.jsp");
    }

    public final ActionForward createNewAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AccountingUnitBean accountingUnitBean = getRenderedObject();
        AccountingUnit.createNewAccountingUnit(accountingUnitBean);
        request.setAttribute("unitBean", new UnitBean());
        return viewOrganization(mapping, request, null);
    }

    private ActionForward editUnit(ActionMapping mapping, HttpServletRequest request, Unit unit) {
        request.setAttribute("unit", unit);
        return forward("/expenditureTrackingOrganization/editUnit.jsp");
    }

    public final ActionForward editUnit(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Unit unit = getDomainObject(request, "unitOid");
        return editUnit(mapping, request, unit);
    }

    public final ActionForward deleteUnit(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Unit unit = getDomainObject(request, "unitOid");
        final Unit parentUnit = unit.getParentUnit();
        unit.delete();
        return viewOrganization(mapping, request, parentUnit);
    }

    public final ActionForward searchUsers(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        ExpenditureTrackingSystem.getInstance();
        SearchUsers searchUsers = getRenderedObject("searchUsers");
        if (searchUsers == null) {
            searchUsers = new SearchUsers();
        }
        request.setAttribute("searchUsers", searchUsers);
        return forward("/expenditureTrackingOrganization/searchUsers.jsp");
    }

    public final ActionForward addRoleAcquisitionCentralGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return addRole(ExpenditureTrackingSystem.getInstance().getAcquisitionCentralGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setAcquisitionCentralGroup(g), mapping, request);
    }

    public final ActionForward removeRoleAcquisitionCentralGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return removeRole(ExpenditureTrackingSystem.getInstance().getAcquisitionCentralGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setAcquisitionCentralGroup(g), mapping, request);
    }

    public final ActionForward addRoleFundCommitmentManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return addRole(ExpenditureTrackingSystem.getInstance().getFundCommitmentManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setFundCommitmentManagerGroup(g), mapping, request);
    }

    public final ActionForward removeFundCommitmentManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return removeRole(ExpenditureTrackingSystem.getInstance().getFundCommitmentManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setFundCommitmentManagerGroup(g), mapping, request);
    }

    public final ActionForward addRoleAcquisitionCentralManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return addRole(ExpenditureTrackingSystem.getInstance().getAcquisitionCentralManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setAcquisitionCentralManagerGroup(g), mapping, request);
    }

    public final ActionForward removeRoleAcquisitionCentralManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return removeRole(ExpenditureTrackingSystem.getInstance().getAcquisitionCentralManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setAcquisitionCentralManagerGroup(g), mapping, request);
    }

    public final ActionForward addRoleAccountingManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return addRole(ExpenditureTrackingSystem.getInstance().getAccountingManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setAccountingManagerGroup(g), mapping, request);
    }

    public final ActionForward removeRoleAccountingManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return removeRole(ExpenditureTrackingSystem.getInstance().getAccountingManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setAccountingManagerGroup(g), mapping, request);
    }

    public final ActionForward addRoleProjectAccountingManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return addRole(ExpenditureTrackingSystem.getInstance().getProjectAccountingManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setProjectAccountingManagerGroup(g), mapping, request);
    }

    public final ActionForward removeRoleProjectAccountingManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return removeRole(ExpenditureTrackingSystem.getInstance().getProjectAccountingManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setProjectAccountingManagerGroup(g), mapping, request);
    }

    public final ActionForward addRoleTreasuryMemberGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return addRole(ExpenditureTrackingSystem.getInstance().getTreasuryMemberGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setTreasuryMemberGroup(g), mapping, request);
    }

    public final ActionForward removeRoleTreasuryMemberGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return removeRole(ExpenditureTrackingSystem.getInstance().getTreasuryMemberGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setTreasuryMemberGroup(g), mapping, request);
    }

    public final ActionForward addRoleSupplierManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return addRole(ExpenditureTrackingSystem.getInstance().getSupplierManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setSupplierManagerGroup(g), mapping, request);
    }

    public final ActionForward removeRoleSupplierManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return removeRole(ExpenditureTrackingSystem.getInstance().getSupplierManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setSupplierManagerGroup(g), mapping, request);
    }

    public final ActionForward addRoleSupplierFundAllocationManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return addRole(ExpenditureTrackingSystem.getInstance().getSupplierFundAllocationManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setSupplierFundAllocationManagerGroup(g), mapping, request);
    }

    public final ActionForward removeRoleSupplierFundAllocationManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return removeRole(ExpenditureTrackingSystem.getInstance().getSupplierFundAllocationManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setSupplierFundAllocationManagerGroup(g), mapping, request);
    }

    public final ActionForward addRoleStatisticsViewerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return addRole(ExpenditureTrackingSystem.getInstance().getStatisticsViewerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setStatisticsViewerGroup(g), mapping, request);
    }

    public final ActionForward removeRoleStatisticsViewerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return removeRole(ExpenditureTrackingSystem.getInstance().getStatisticsViewerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setStatisticsViewerGroup(g), mapping, request);
    }

    public final ActionForward addRoleAcquisitionsUnitManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return addRole(ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setAcquisitionsUnitManagerGroup(g), mapping, request);
    }

    public final ActionForward removeRoleAcquisitionsUnitManagerGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return removeRole(ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setAcquisitionsUnitManagerGroup(g), mapping, request);
    }

    public final ActionForward addRoleAcquisitionsProcessAuditorGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return addRole(ExpenditureTrackingSystem.getInstance().getAcquisitionsProcessAuditorGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setAcquisitionsProcessAuditorGroup(g), mapping, request);
    }

    public final ActionForward removeRoleAcquisitionsProcessAuditorGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return removeRole(ExpenditureTrackingSystem.getInstance().getAcquisitionsProcessAuditorGroup(),
                (g) -> ExpenditureTrackingSystem.getInstance().setAcquisitionsProcessAuditorGroup(g), mapping, request);
    }

    @Atomic
    public final ActionForward addRole(final PersistentGroup persistentGroup, final Consumer<PersistentGroup> consumer,
            final ActionMapping mapping, final HttpServletRequest request) {
        final Person person = getDomainObject(request, "personOid");
        final User user = person == null ? null : person.getUser();
        final Group group = persistentGroup.toGroup();
        final PersistentGroup result = group.grant(user).toPersistentGroup();
        consumer.accept(result);
        return viewPerson(mapping, request, person);
    }

    @Atomic
    public final ActionForward removeRole(final PersistentGroup persistentGroup, final Consumer<PersistentGroup> consumer,
            final ActionMapping mapping, final HttpServletRequest request) {
        final Person person = getDomainObject(request, "personOid");
        final User user = person == null ? null : person.getUser();
        final Group group = persistentGroup.toGroup();
        PersistentGroup result = group.revoke(user).toPersistentGroup();
        consumer.accept(result);
        return viewPerson(mapping, request, person);
    }

    public final ActionForward viewPerson(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personOid");
        return viewPerson(mapping, request, person);
    }

    public final ActionForward viewPerson(final ActionMapping mapping, final HttpServletRequest request, final Person person) {
        request.setAttribute("person", person);

        request.setAttribute("availableRoles", RoleType.values());
        return forward("/expenditureTrackingOrganization/viewPerson.jsp");
    }

    public final ActionForward viewLoggedPerson(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final User user = Authenticate.getUser();
        final Person person = user.getExpenditurePerson();
        return viewPerson(mapping, request, person);
    }

    public final ActionForward editAuthorization(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Authorization authorization = getDomainObject(request, "authorizationOid");
        request.setAttribute("authorization", authorization);
        return forward("/expenditureTrackingOrganization/editAuthorization.jsp");
    }

    public final ActionForward viewAuthorization(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Authorization authorization = getDomainObject(request, "authorizationOid");
        request.setAttribute("authorization", authorization);
        return forward("/expenditureTrackingOrganization/viewAuthorization.jsp");
    }

    public final ActionForward attributeAuthorization(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personOid");
        request.setAttribute("person", person);
        RenderUtils.invalidateViewState();
        final UnitBean unitBean = new UnitBean();
        request.setAttribute("unitBean", unitBean);
        return expandAuthorizationUnit(mapping, request, person, null);
    }

    public final ActionForward prepareAddResponsibleAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personOid");
        request.setAttribute("person", person);
        final Set<AccountingUnit> accountingUnits = ExpenditureTrackingSystem.getInstance().getAccountingUnitsSet();
        request.setAttribute("accountingUnits", accountingUnits);
        return forward("/expenditureTrackingOrganization/selectResponsibleAccountingUnitToAddMember.jsp");
    }

    public final ActionForward addResponsibleAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personOid");
        final AccountingUnit accountingUnit = getDomainObject(request, "accountingUnitOid");
        accountingUnit.addResponsiblePeople(person);
        return viewPerson(mapping, request, person);
    }

    public final ActionForward prepareAddToAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personOid");
        request.setAttribute("person", person);
        final Set<AccountingUnit> accountingUnits = ExpenditureTrackingSystem.getInstance().getAccountingUnitsSet();
        request.setAttribute("accountingUnits", accountingUnits);
        return forward("/expenditureTrackingOrganization/selectAccountingUnitToAddMember.jsp");
    }

    public final ActionForward addToAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personOid");
        final AccountingUnit accountingUnit = getDomainObject(request, "accountingUnitOid");
        accountingUnit.addPeople(person);
        return viewPerson(mapping, request, person);
    }

    public final ActionForward prepareAddResponsibleProjectAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personOid");
        request.setAttribute("person", person);
        final Set<AccountingUnit> accountingUnits = ExpenditureTrackingSystem.getInstance().getAccountingUnitsSet();
        request.setAttribute("accountingUnits", accountingUnits);
        return forward("/expenditureTrackingOrganization/selectResponsibleProjectAccountingUnitToAddMember.jsp");
    }

    public final ActionForward addResponsibleProjectAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personOid");
        final AccountingUnit accountingUnit = getDomainObject(request, "accountingUnitOid");
        accountingUnit.addResponsibleProjectAccountants(person);
        return viewPerson(mapping, request, person);
    }

    public final ActionForward prepareAddToProjectAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personOid");
        request.setAttribute("person", person);
        final Set<AccountingUnit> accountingUnits = ExpenditureTrackingSystem.getInstance().getAccountingUnitsSet();
        request.setAttribute("accountingUnits", accountingUnits);
        return forward("/expenditureTrackingOrganization/selectProjectAccountingUnitToAddMember.jsp");
    }

    public final ActionForward addToProjectAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personOid");
        final AccountingUnit accountingUnit = getDomainObject(request, "accountingUnitOid");
        accountingUnit.addProjectAccountants(person);
        return viewPerson(mapping, request, person);
    }

    public final ActionForward prepareAddToTreasuryAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personOid");
        request.setAttribute("person", person);
        final Set<AccountingUnit> accountingUnits = ExpenditureTrackingSystem.getInstance().getAccountingUnitsSet();
        request.setAttribute("accountingUnits", accountingUnits);
        return forward("/expenditureTrackingOrganization/selectTreasuryAccountingUnitToAddMember.jsp");
    }

    public final ActionForward addToTreasuryAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personOid");
        final AccountingUnit accountingUnit = getDomainObject(request, "accountingUnitOid");
        accountingUnit.addTreasuryMembers(person);
        return viewPerson(mapping, request, person);
    }

    public final ActionForward expandAuthorizationUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final UnitBean unitBean = getRenderedObject();
        final Person person = getDomainObject(request, "personOid");
        if (unitBean == null || unitBean.getUnit() == null) {
            final Unit unit = getDomainObject(request, "unitOid");
            return expandAuthorizationUnit(mapping, request, person, unit);
        } else {
            return expandAuthorizationUnit(mapping, request, person, unitBean.getUnit());
        }
    }

    public final ActionForward expandAuthorizationUnit(final ActionMapping mapping, final HttpServletRequest request,
            final Person person, final Unit unit) {
        request.setAttribute("person", person);
        request.setAttribute("unit", unit);
        final Set<Unit> units =
                unit == null ? ExpenditureTrackingSystem.getInstance().getTopLevelUnitsSet() : unit.getSubUnitsSet();
        request.setAttribute("units", units);
        final UnitBean unitBean = new UnitBean();
        request.setAttribute("unitBean", unitBean);
        return forward("/expenditureTrackingOrganization/changeAuthorizationUnit.jsp");
    }

/*    public final ActionForward changeAuthorizationUnit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final Person person = getDomainObject(request, "personOid");
	final Unit unit = getDomainObject(request, "unitOid");
	person.createAuthorization(unit);
	return viewPerson(mapping, request, person);
}
*/
    public final ActionForward prepareCreateAuthorizationUnitWithoutPerson(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Unit unit = getDomainObject(request, "unitOid");
        return prepareCreateAuthorizationUnit(request, null, unit, true);
    }

    public final ActionForward prepareCreateAuthorizationUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personOid");
        final Unit unit = getDomainObject(request, "unitOid");
        return prepareCreateAuthorizationUnit(request, person, unit, false);
    }

    public final ActionForward prepareCreateAuthorizationUnit(final HttpServletRequest request, final Person person,
            final Unit unit, final boolean returnToUnitInterface) {
        final AuthorizationBean authorizationBean = new AuthorizationBean(person, unit, returnToUnitInterface);
        request.setAttribute("authorizationBean", authorizationBean);
        request.setAttribute("person", person);
        request.setAttribute("unit", unit);
        return forward("/expenditureTrackingOrganization/createAuthorizationUnit.jsp");
    }

    public final ActionForward createAuthorizationUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AuthorizationBean authorizationBean = getRenderedObject();
        authorizationBean.getPerson().createAuthorization(authorizationBean, authorizationBean.getJustification());
        if (authorizationBean.isReturnToUnitInterface()) {
            final UnitBean unitBean = new UnitBean(authorizationBean.getUnit());;
            request.setAttribute("unitBean", unitBean);
            return viewOrganization(mapping, request, authorizationBean.getUnit());
        }
        return viewPerson(mapping, request, authorizationBean.getPerson());
    }

    public final ActionForward deleteAuthorization(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Authorization authorization = getDomainObject(request, "authorizationOid");
        final Person person = authorization.getPerson();
        authorization.delete();
        return viewPerson(mapping, request, person);
    }

    public final ActionForward manageSuppliers(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        SupplierBean supplierBean = getRenderedObject("supplierBean");
        if (supplierBean == null) {
            supplierBean = new SupplierBean();
            Supplier supplier = getDomainObject(request, "supplierOid");
            supplierBean.setSupplier(supplier);
        }

        request.setAttribute("supplierBean", supplierBean);
        return forward("/expenditureTrackingOrganization/manageSuppliers.jsp");
    }

    public final ActionForward manageSuppliers(final ActionMapping mapping, final HttpServletRequest request,
            final SupplierBean supplierBean) {
        request.setAttribute("supplierBean", supplierBean);
        return forward("/expenditureTrackingOrganization/manageSuppliers.jsp");
    }

    public final ActionForward viewSupplier(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Supplier supplier = getDomainObject(request, "supplierOid");

        return viewSupplier(mapping, form, request, response, supplier);
    }

    private final ActionForward viewSupplier(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response, final Supplier supplier) {
        request.setAttribute("supplier", supplier);
        return forward("/expenditureTrackingOrganization/viewSupplier.jsp");
    }

    public final ActionForward prepareEditSupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Supplier supplier = getDomainObject(request, "supplierOid");

        request.setAttribute("supplier", supplier);
        return forward("/expenditureTrackingOrganization/editSupplier.jsp");
    }

    public final ActionForward editSupplier(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Supplier supplier = getRenderedObject();
        final SupplierBean supplierBean = new SupplierBean(supplier);
        return manageSuppliers(mapping, request, supplierBean);
    }

    public final ActionForward deleteSupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Supplier supplier = getDomainObject(request, "supplierOid");
        supplier.delete();
        return manageSuppliers(mapping, form, request, response);
    }

    public ActionForward revokeAuthorization(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final Authorization authorization = getDomainObject(request, "authorizationOid");

        try {
            authorization.revoke();
        } catch (DomainException e) {
            addMessage(request, e.getMessage());
        }

        return viewAuthorization(mapping, form, request, response);
    }

    public ActionForward delegateAuthorization(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        Authorization authorization = getDomainObject(request, "authorizationOid");
        Unit unit = getDomainObject(request, "unitOid");
        request.setAttribute("authorization", authorization);
        Person person = getLoggedPerson();

        if (authorization.getPerson() == person && authorization.getCanDelegate()) {
            AuthorizationBean bean = new AuthorizationBean(authorization);
            bean.setUnit(unit);
            request.setAttribute("bean", bean);
        } else {
            addMessage(request, "label.unable.to.delegate.that.action");
        }

        return forward("/expenditureTrackingOrganization/delegateAuthorization.jsp");
    }

    public ActionForward chooseDelegationUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Authorization authorization = getDomainObject(request, "authorizationOid");
        request.setAttribute("authorization", authorization);
        Unit unit = getDomainObject(request, "unitOid");
        if (unit == null) {
            unit = authorization.getUnit();
        }
        request.setAttribute("unit", unit);
        request.setAttribute("units", unit.getSubUnitsSet());
        return forward("/expenditureTrackingOrganization/delegateChooseUnit.jsp");
    }

    public ActionForward createDelegation(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final AuthorizationBean bean = getRenderedObject("bean");
        try {
            DelegatedAuthorization.delegate(bean.getAuthorization(), bean.getPerson(), bean.getUnit(), bean.getCanDelegate(),
                    bean.getEndDate(), bean.getMaxAmount());
        } catch (DomainException e) {
            addMessage(request, e.getMessage());
            request.setAttribute("bean", bean);
            return forward("/expenditureTrackingOrganization/delegateAuthorization.jsp");
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("authorization", bean.getAuthorization());
        return forward("/expenditureTrackingOrganization/viewAuthorization.jsp");
    }

    public ActionForward removeResponsibleFromAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AccountingUnit accountingUnit = getDomainObject(request, "accountingUnitOid");
        final Person person = getDomainObject(request, "personOid");
        accountingUnit.removeResponsiblePeople(person);
        return viewPerson(mapping, request, person);
    }

    public ActionForward removePersonFromAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AccountingUnit accountingUnit = getDomainObject(request, "accountingUnitOid");
        final Person person = getDomainObject(request, "personOid");
        accountingUnit.removePeople(person);
        return viewPerson(mapping, request, person);
    }

    public ActionForward removePersonResponsibleForProjectAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AccountingUnit accountingUnit = getDomainObject(request, "accountingUnitOid");
        final Person person = getDomainObject(request, "personOid");
        accountingUnit.removeResponsibleProjectAccountants(person);
        return viewPerson(mapping, request, person);
    }

    public ActionForward removePersonFromProjectAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AccountingUnit accountingUnit = getDomainObject(request, "accountingUnitOid");
        final Person person = getDomainObject(request, "personOid");
        accountingUnit.removeProjectAccountants(person);
        return viewPerson(mapping, request, person);
    }

    public ActionForward removePersonFromTreasuryAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AccountingUnit accountingUnit = getDomainObject(request, "accountingUnitOid");
        final Person person = getDomainObject(request, "personOid");
        accountingUnit.removeTreasuryMembers(person);
        return viewPerson(mapping, request, person);
    }

    public ActionForward viewAccountingUnit(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final AccountingUnit accountingUnit = getDomainObject(request, "accountingUnitOid");
        request.setAttribute("accountingUnit", accountingUnit);
        return forward("/expenditureTrackingOrganization/viewAccountingUnit.jsp");
    }

    public ActionForward prepareAddUnitToAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AccountingUnit accountingUnit = getDomainObject(request, "accountingUnitOid");
        request.setAttribute("accountingUnit", accountingUnit);

        UnitBean unitBean = getRenderedObject();
        if (unitBean == null || unitBean.getUnit() == null) {
            final Unit unit = getDomainObject(request, "unitOid");
            if (unit != null) {
                unitBean = new UnitBean(unit);
            }
        }
        if (unitBean == null) {
            unitBean = new UnitBean();
        }
        request.setAttribute("unitBean", unitBean);

        final Set<Unit> units = unitBean.getUnit() == null ? ExpenditureTrackingSystem.getInstance()
                .getTopLevelUnitsSet() : unitBean.getUnit().getSubUnitsSet();
        request.setAttribute("units", units);

        RenderUtils.invalidateViewState();

        return forward("/expenditureTrackingOrganization/addUnitToAccountingUnit.jsp");
    }

    public ActionForward addUnitToAccountingUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AccountingUnit accountingUnit = getDomainObject(request, "accountingUnitOid");
        request.setAttribute("accountingUnit", accountingUnit);

        final Unit unit;
        final UnitBean unitBean = getRenderedObject();
        if (unitBean == null) {
            unit = getDomainObject(request, "unitOid");
        } else {
            RenderUtils.invalidateViewState();
            unit = unitBean.getUnit();
        }

        accountingUnit.addUnits(unit);

        return forward("/expenditureTrackingOrganization/viewAccountingUnit.jsp");
    }

    public ActionForward listSuppliers(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {

        Spreadsheet suppliersSheet = getSuppliersSheet();
        response.setContentType("application/xls ");
        response.setHeader("Content-disposition", "attachment; filename=fornecedores.xls");

        ServletOutputStream outputStream = response.getOutputStream();

        suppliersSheet.exportToXLSSheet(outputStream);
        outputStream.flush();
        outputStream.close();

        return null;
    }

    private Spreadsheet getSuppliersSheet() {
        Spreadsheet spreadsheet = new Spreadsheet("Fornecedores");
        spreadsheet.setHeader("Fornecedor");
        spreadsheet.setHeader("NIF");
        spreadsheet.setHeader("Total alocado");
        spreadsheet.setHeader("Ajuste Directo");
        spreadsheet.setHeader("Fundos de Maneio");
        spreadsheet.setHeader("Reembolsos");
        spreadsheet.setHeader("Por outras Vias");

        for (Supplier supplier : Bennu.getInstance().getSuppliersSet()) {
            Row row = spreadsheet.addRow();
            row.setCell(supplier.getName());
            row.setCell(supplier.getFiscalIdentificationCode());
            row.setCell(supplier.getTotalAllocated().getValue());
            row.setCell(supplier.getTotalAllocatedByAcquisitionProcesses().getValue());
            row.setCell(supplier.getTotalAllocatedByWorkingCapitals().getValue());
            row.setCell(supplier.getTotalAllocatedByRefunds().getValue());
            row.setCell(supplier.getTotalAllocatedByPurchases().getValue());
        }

        return spreadsheet;
    }

    public final ActionForward editSupplierLimit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Supplier supplier = getDomainObject(request, "supplierOid");
        request.setAttribute("supplier", supplier);
        return forward("/expenditureTrackingOrganization/editSupplierLimit.jsp");
    }

    public final ActionForward prepareMergeSupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Supplier supplier = getDomainObject(request, "supplierToTransferOID");
        request.setAttribute("supplierToTransfer", supplier);

        SupplierBean supplierBean = getRenderedObject("supplierBean");
        if (supplierBean == null) {
            supplierBean = new SupplierBean();
        }
        request.setAttribute("supplierBean", supplierBean);

        return forward("/expenditureTrackingOrganization/mergeSupplier.jsp");
    }

    public final ActionForward mergeSupplier(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Supplier supplierToTransfer = getDomainObject(request, "supplierToTransferOID");
        final Supplier supplierDestination = getDomainObject(request, "supplierDestinationOID");

        supplierDestination.merge(supplierToTransfer);

        final SupplierBean supplierBean = new SupplierBean(supplierDestination);

        return manageSuppliers(mapping, request, supplierBean);
    }

    private String determineProjectRegime(final String projectCode) {
        final Project project = Project.findProjectByCode(projectCode);
        return project == null ? "" : (project.getDefaultRegeimIsCCP() != null
                && project.getDefaultRegeimIsCCP().booleanValue() ? "CCP" : "C&T");
    }

    public final ActionForward downloadSupplierAcquisitionInformation(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        final Supplier supplier = getDomainObject(request, "supplierOid");
        final Spreadsheet suppliersSheet = getSupplierAcquisitionInformationSheet(supplier);
        response.setContentType("application/xls ");
        response.setHeader("Content-disposition", "attachment; filename=fornecedor.xls");

        final ServletOutputStream outputStream = response.getOutputStream();

        suppliersSheet.exportToXLSSheet(outputStream);
        outputStream.flush();
        outputStream.close();

        return null;
    }

    private Spreadsheet getSupplierAcquisitionInformationSheet(final Supplier supplier) {
        Spreadsheet spreadsheet = new Spreadsheet(supplier.getPresentationName());
        spreadsheet.setHeader("Identificação Processo");
        spreadsheet.setHeader("Descrição");
        spreadsheet.setHeader("Valor alocado ao fornecedor");
        spreadsheet.setHeader("IVA");
        spreadsheet.setHeader("Total");

        Money totalForSupplierLimit = Money.ZERO;
        Money totalForSupplier = Money.ZERO;

        for (final AcquisitionAfterTheFact acquisitionAfterTheFact : supplier.getAcquisitionsAfterTheFactSet()) {
            if (!acquisitionAfterTheFact.getDeletedState().booleanValue() && acquisitionAfterTheFact.isInAllocationPeriod()) {
                final Money value = acquisitionAfterTheFact.getValue();
                totalForSupplierLimit = totalForSupplierLimit.add(value);
                totalForSupplier = totalForSupplier.add(value);

                final Row row = spreadsheet.addRow();
                row.setCell(acquisitionAfterTheFact.getAcquisitionProcessId());
                row.setCell(acquisitionAfterTheFact.getDescription());
                row.setCell(value.toFormatString());
                row.setCell(acquisitionAfterTheFact.getVatValue());
                row.setCell(value.toFormatString());
            }
        }

        for (final AcquisitionRequest acquisitionRequest : supplier.getAcquisitionRequestsSet()) {
            if (acquisitionRequest.isInAllocationPeriod()) {
                final AcquisitionProcess acquisitionProcess = acquisitionRequest.getAcquisitionProcess();
                if (acquisitionProcess.isActive() && acquisitionProcess.isAllocatedToSupplier()) {
                    Money forSupplierLimit = Money.ZERO;
                    Money currentValue = Money.ZERO;
                    for (final RequestItem requestItem : acquisitionRequest.getRequestItemsSet()) {
                        AcquisitionRequestItem acqRequestItem = (AcquisitionRequestItem) requestItem;
                        if (acquisitionProcess.getAcquisitionProcessState().hasBeenAllocatedPermanently()) {
                            forSupplierLimit = forSupplierLimit.add(acqRequestItem.getTotalRealValue());
                            currentValue = currentValue.add(acqRequestItem.getTotalRealValueWithAdditionalCostsAndVat());
                        } else {
                            forSupplierLimit = forSupplierLimit.add(acqRequestItem.getTotalItemValue());
                            currentValue = currentValue.add(acqRequestItem.getTotalItemValueWithAdditionalCostsAndVat());
                        }
                    }

                    totalForSupplierLimit = totalForSupplierLimit.add(forSupplierLimit);
                    totalForSupplier = totalForSupplier.add(currentValue);

                    final Row row = spreadsheet.addRow();
                    row.setCell(acquisitionRequest.getAcquisitionProcessId());
                    row.setCell(acquisitionRequest.getClass().getSimpleName());
                    row.setCell(forSupplierLimit.toFormatString());
                    row.setCell(acquisitionRequest.getCurrentTotalVatValue().toFormatString());
                    row.setCell(currentValue.toFormatString());
                }
            }
        }

        final Map<RefundProcess, Money> refundValueMap = new HashMap<RefundProcess, Money>();
        final Map<RefundProcess, Money> vatMap = new HashMap<RefundProcess, Money>();
        final Map<RefundProcess, Money> totalValueMap = new HashMap<RefundProcess, Money>();

        for (final RefundableInvoiceFile invoiceFile : supplier.getActiveRefundInvoicesSet()) {
            if (invoiceFile.isInAllocationPeriod()) {
                final RefundProcess refundProcess = invoiceFile.getRefundItem().getRequest().getProcess();
                if (refundProcess.isActive() && !refundProcess.getShouldSkipSupplierFundAllocation()) {
                    final RefundRequest refundRequest = refundProcess.getRequest();

                    if (!refundValueMap.containsKey(refundProcess)) {
                        refundValueMap.put(refundProcess, Money.ZERO);
                        vatMap.put(refundProcess, Money.ZERO);
                        totalValueMap.put(refundProcess, Money.ZERO);
                    }

                    refundValueMap.put(refundProcess, refundValueMap.get(refundProcess).add(invoiceFile.getRefundableValue()));
                    vatMap.put(refundProcess,
                            vatMap.get(refundProcess).add(invoiceFile.getValueWithVat().subtract(invoiceFile.getValue())));
                    totalValueMap.put(refundProcess, totalValueMap.get(refundProcess).add(invoiceFile.getValueWithVat()));
                }
            }
        }

        for (final Entry<RefundProcess, Money> entry : refundValueMap.entrySet()) {
            final RefundProcess refundProcess = entry.getKey();

            final Money refundableValue = entry.getValue();
            final Money vatValue = vatMap.get(refundProcess);
            final Money totalValue = totalValueMap.get(refundProcess);

            totalForSupplierLimit = totalForSupplierLimit.add(refundableValue);
            totalForSupplier = totalForSupplier.add(totalValue);

            final Row row = spreadsheet.addRow();
            row.setCell(refundProcess.getProcessNumber());
            row.setCell(refundProcess.getClass().getSimpleName());
            row.setCell(refundableValue.toFormatString());
            row.setCell(vatValue.toFormatString());
            row.setCell(totalValue.toFormatString());
        }

        spreadsheet.addRow();
        final Row row = spreadsheet.addRow();
        row.setCell("Total");
        row.setCell("");
        row.setCell(totalForSupplierLimit.toFormatString());
        row.setCell("");
        row.setCell(totalForSupplier.toFormatString());

        return spreadsheet;
    }

    public final ActionForward viewSupplierProcessesByCPV(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        final Supplier supplier = getDomainObject(request, "supplierOID");
        final CPVReference cpvReference = getDomainObject(request, "cpvReferenceOID");

        request.setAttribute("supplier", supplier);
        request.setAttribute("cpvReference", cpvReference);

        // supplier.getAllocationsByCPVReference();
        // supplier.getUnconfirmedAllocationsByCPVReference();

        final SortedSet<AcquisitionForSupplierAndCPVBean> acquisitionBeans = new TreeSet<AcquisitionForSupplierAndCPVBean>();
        for (final AcquisitionRequest acquisitionRequest : supplier.getAcquisitionRequestsSet()) {
            if (acquisitionRequest.isInAllocationPeriod()) {
                final AcquisitionProcess acquisitionProcess = acquisitionRequest.getAcquisitionProcess();
                if (acquisitionProcess.isActive() && acquisitionRequest.hasRequestItemForCPV(cpvReference)) {
                    acquisitionBeans.add(new AcquisitionForSupplierAndCPVBean(supplier, cpvReference, acquisitionProcess));
                }
            }
        }
        request.setAttribute("acquisitionBeans", acquisitionBeans);

        final SortedSet<RefundForSupplierAndCPVBean> refundBeans = new TreeSet<RefundForSupplierAndCPVBean>();
        // download
        for (final RefundableInvoiceFile invoiceFile : supplier.getActiveRefundInvoicesSet()) {
            if (invoiceFile.isInAllocationPeriod() && invoiceFile.getRefundItem().getCPVReference() == cpvReference) {
                final RefundProcess refundProcess = invoiceFile.getRefundItem().getRequest().getProcess();
                if (refundProcess.isActive()) {
                    refundBeans.add(new RefundForSupplierAndCPVBean(supplier, cpvReference, refundProcess));
                }
            }
        }
        for (final RefundItem refundItem : supplier.getRefundItemSet()) {
            if (refundItem.isInAllocationPeriod() && refundItem.getInvoicesFilesSet().isEmpty()) {
                final RefundProcess refundProcess = refundItem.getRequest().getProcess();
                if (refundProcess.isActive() && !refundProcess.getShouldSkipSupplierFundAllocation()) {
                    refundBeans.add(new RefundForSupplierAndCPVBean(supplier, cpvReference, refundProcess));
                }                
            }
        }
        request.setAttribute("refundBeans", refundBeans);

        final SortedSet<AcquisitionAfterTheFact> afterTheFactProcesses =
                new TreeSet<AcquisitionAfterTheFact>(new Comparator<AcquisitionAfterTheFact>() {
                    @Override
                    public int compare(AcquisitionAfterTheFact o1, AcquisitionAfterTheFact o2) {
                        return PaymentProcess.COMPARATOR_BY_YEAR_AND_ACQUISITION_PROCESS_NUMBER
                                .compare(o1.getAfterTheFactAcquisitionProcess(), o2.getAfterTheFactAcquisitionProcess());
                    }
                });
        for (final AcquisitionAfterTheFact acquisitionAfterTheFact : supplier.getAcquisitionsAfterTheFactSet()) {
            if (acquisitionAfterTheFact.isInAllocationPeriod() && !acquisitionAfterTheFact.getDeletedState().booleanValue()
                    && acquisitionAfterTheFact.getCpvReference() == cpvReference) {
                afterTheFactProcesses.add(acquisitionAfterTheFact);
            }
        }
        request.setAttribute("afterTheFactProcesses", afterTheFactProcesses);

        return forward("/expenditureTrackingOrganization/viewSupplierProcessesByCPV.jsp");
    }

    public final ActionForward managePriorityCPVs(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        List<CPVReference> priorityCPVReferences =
                new ArrayList<CPVReference>(ExpenditureTrackingSystem.getInstance().getPriorityCPVReferences());
        Collections.sort(priorityCPVReferences, new BeanComparator("code"));
        request.setAttribute("cpvs", priorityCPVReferences);
        request.setAttribute("bean", new VariantBean());

        return forward("/expenditureTrackingOrganization/priorityCVPs.jsp");
    }

    public final ActionForward addPriorityCPV(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        CPVReference reference = getRenderedObject("cpvToAdd");
        reference.markAsPriority();
        RenderUtils.invalidateViewState();

        return managePriorityCPVs(mapping, form, request, response);
    }

    public final ActionForward removePriorityCPV(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        CPVReference reference = getDomainObject(request, "cpvId");
        reference.unmarkAsPriority();
        RenderUtils.invalidateViewState();

        return managePriorityCPVs(mapping, form, request, response);
    }

    public final ActionForward viewAuthorizationLogs(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        final Person person = getDomainObject(request, "personOid");
        request.setAttribute("person", person);

        final Unit unit = getDomainObject(request, "unitOid");
        request.setAttribute("unit", unit);

        final Set<AuthorizationLog> authorizationLogs;
        if (person == null && unit == null) {
            authorizationLogs = null;
        } else if (person == null) {
            authorizationLogs = unit.getSortedAuthorizationLogsSet();
        } else if (unit == null) {
            authorizationLogs = person.getSortedAuthorizationLogsSet();
        } else {
            authorizationLogs = new TreeSet<AuthorizationLog>(AuthorizationLog.COMPARATOR_BY_WHEN);
            for (final AuthorizationLog authorizationLog : unit.getAuthorizationLogsSet()) {
                if (authorizationLog.getPerson() == person) {
                    authorizationLogs.add(authorizationLog);
                }
            }
        }
        request.setAttribute("authorizationLogs", authorizationLogs);

        return forward("/expenditureTrackingOrganization/viewAuthorizationLogs.jsp");
    }

    public final ActionForward manageObservers(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        final Unit unit = getDomainObject(request, "unitOid");
        request.setAttribute("unit", unit);

        final Person loggedPerson = Person.getLoggedPerson();
        if (!unit.isResponsible(loggedPerson) && !RoleType.MANAGER.group().isMember(loggedPerson.getUser())
                && !ExpenditureTrackingSystem.isAcquisitionsUnitManagerGroupMember()) {
            return viewLoggedPerson(mapping, form, request, response);
        }

        request.setAttribute("bean", new SearchUsers());
        return forward("/expenditureTrackingOrganization/manageUnitObservers.jsp");
    }

    public final ActionForward addObserver(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final Unit unit = getDomainObject(request, "unitOid");
        SearchUsers bean = getRenderedObject("bean");

        unit.addObservers(bean.getPerson());
        RenderUtils.invalidateViewState("bean");

        return manageObservers(mapping, form, request, response);

    }

    public final ActionForward removeObserver(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        final Unit unit = getDomainObject(request, "unitOid");
        final Person observer = getDomainObject(request, "observerOid");
        unit.removeObservers(observer);
        return manageObservers(mapping, form, request, response);
    }

    public final ActionForward changeDefaultRegeimIsCCP(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Unit unit = getDomainObject(request, "unitOid");
        unit.toggleDefaultRegeim();
        final UnitBean unitBean = new UnitBean(unit);
        request.setAttribute("unitBean", unitBean);
        return viewOrganization(mapping, request, unit);
    }

    public final ActionForward viewAcquisitionProcessStatistics(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        UserAcquisitionProcessStatistics userAcquisitionProcessStatistics = getRenderedObject();
        if (userAcquisitionProcessStatistics == null) {
            final User user = getDomainObject(request, "userOid");
            final PaymentProcessYear paymentProcessYear =
                    PaymentProcessYear.getPaymentProcessYearByYear(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            userAcquisitionProcessStatistics = new UserAcquisitionProcessStatistics(user, paymentProcessYear);
        } else {
            RenderUtils.invalidateViewState();
        }
        request.setAttribute("userAcquisitionProcessStatistics", userAcquisitionProcessStatistics);
        return forward("/expenditureTrackingOrganization/viewAcquisitionProcessStatistics.jsp");
    }

    public ActionForward listCPVReferences(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Set<CPVReference> cvpReferences = Bennu.getInstance().getCPVReferencesSet();
        final SortedSet<CPVReference> sortedCPVReferences = new TreeSet<CPVReference>(CPVReference.COMPARATOR_BY_DESCRIPTION);
        sortedCPVReferences.addAll(cvpReferences);
        request.setAttribute("cvpReferences", sortedCPVReferences);
        return forward("/expenditureTrackingOrganization/listCPVReferences.jsp");
    }

    public ActionForward listMaterials(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Set<Material> materials = Bennu.getInstance().getExpenditureTrackingSystem().getMaterialsSet();
        final SortedSet<Material> sortedMaterials = new TreeSet<Material>(Material.COMPARATOR_BY_DESCRIPTION);
        sortedMaterials.addAll(materials);
        request.setAttribute("materials", sortedMaterials);
        return forward("/expenditureTrackingOrganization/listMaterials.jsp");
    }

    public final ActionForward downloadUnitResponsibles(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException, SQLException {

        response.setContentType("application/xls ");
        response.setHeader("Content-disposition", "attachment; filename=ResponsaveisUnidades.xls");

        final ServletOutputStream outputStream = response.getOutputStream();
        final HSSFWorkbook workbook = new HSSFWorkbook();
        final ExcelStyle excelStyle = new ExcelStyle(workbook);

        final HSSFSheet sheet = workbook.createSheet("Responsaveis");
        sheet.setDefaultColumnWidth(20);

        final HSSFRow row = sheet.createRow(sheet.getLastRowNum());
        createHeaderCell(excelStyle, row, 0, "Centro de Custo");
        createHeaderCell(excelStyle, row, 1, "Unidade");
        createHeaderCell(excelStyle, row, 2, "Responsável Aprovação");
        createHeaderCell(excelStyle, row, 4, "Responsável Despesa");

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));

        for (final Unit unit : ExpenditureTrackingSystem.getInstance().getTopLevelUnitsSet()) {
            writeUnitResponsibleInfo(excelStyle, sheet, unit);
        }

        workbook.write(outputStream);

        outputStream.flush();
        outputStream.close();

        return null;
    }

    private void writeUnitResponsibleInfo(final ExcelStyle excelStyle, final HSSFSheet sheet, final Unit unit) {
        writeUnitResponsibleInfo(excelStyle, sheet, unit, new HashSet<Unit>());
    }

    private void writeUnitResponsibleInfo(final ExcelStyle excelStyle, final HSSFSheet sheet, final Unit unit,
            final Set<Unit> processed) {
        final int rowIndex = sheet.getLastRowNum() + 1;
        HSSFRow row = sheet.createRow(rowIndex);
        if (unit instanceof CostCenter) {
            final CostCenter costCenter = (CostCenter) unit;
            createCell(excelStyle, row, 0, costCenter.getCostCenter());
        }
        createCell(excelStyle, row, 1, unit.getName());

        final List<Person>[] approvalsAndAuthorizations = getApprovalsAndAuthorizations(unit);
        final List<Person> approvals = approvalsAndAuthorizations[0];
        final List<Person> authorizations = approvalsAndAuthorizations[1];

        Collections.sort(approvals, Person.COMPARATOR_BY_NAME);
        Collections.sort(authorizations, Person.COMPARATOR_BY_NAME);

        for (int i = 0; i < approvals.size() || i < authorizations.size(); i++) {
            final Person approval = i < approvals.size() ? approvals.get(i) : null;
            final Person authorization = i < authorizations.size() ? authorizations.get(i) : null;

            if (i > 0) {
                row = sheet.createRow(sheet.getLastRowNum() + 1);
            }

            if (approval != null) {
                createCell(excelStyle, row, 2, approval.getUsername());
                createCell(excelStyle, row, 3, approval.getUser().getDisplayName());
            }
            if (authorization != null) {
                createCell(excelStyle, row, 4, authorization.getUsername());
                createCell(excelStyle, row, 5, authorization.getUser().getDisplayName());
            }
        }

        final int totalRows = Math.max(approvals.size(), authorizations.size());
        if (totalRows > 0) {
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + totalRows - 1, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + totalRows - 1, 1, 1));
        }

        for (final Unit subUnit : sortUnitsByCostCenter(unit.getSubUnitsSet())) {
            if (!processed.contains(subUnit)) {
                processed.add(subUnit);
                if (!(subUnit instanceof Project)) {
                    writeUnitResponsibleInfo(excelStyle, sheet, subUnit, processed);
                }
            }
        }
    }

    private List<Person>[] getApprovalsAndAuthorizations(final Unit unit) {
        final List<Person> approvals = new ArrayList<Person>();
        final List<Person> authorizations = new ArrayList<Person>();
        if (unit != null) {
            for (final Authorization authorization : unit.getAuthorizationsSet()) {

                if (authorization.isValid()) {
                    final Person person = authorization.getPerson();
                    if (authorization.getMaxAmount().isZero()) {
                        approvals.add(person);
                    } else {
                        authorizations.add(person);
                    }
                }
            }

            if (approvals.isEmpty() && authorizations.isEmpty()) {
                return getApprovalsAndAuthorizations(unit.getParentUnit());
            }

            if (approvals.isEmpty()) {
                approvals.addAll(authorizations);
            } else if (authorizations.isEmpty()) {
                final List<Person>[] approvalsAndAuthorizations = getApprovalsAndAuthorizations(unit.getParentUnit());
                return new List[] { approvals, approvalsAndAuthorizations[1] };
            }
        }

        return new List[] { approvals, authorizations };
    }

    private SortedSet<Unit> sortUnitsByCostCenter(final Set<Unit> units) {
        final SortedSet<Unit> result = new TreeSet<Unit>(new Comparator<Unit>() {
            @Override
            public int compare(final Unit u1, final Unit u2) {
                final int cc1 = getMinCostCenter(u1);
                final int cc2 = getMinCostCenter(u2);
                final int i = cc1 - cc2;
                return i == 0 ? u1.hashCode() - u2.hashCode() : i;
            }

            private int getMinCostCenter(final Unit unit) {
                if (unit instanceof CostCenter) {
                    final CostCenter costCenter = (CostCenter) unit;
                    return Integer.parseInt(getParsableStr(costCenter.getCostCenter()));
                }
                int min = Integer.MAX_VALUE;
                for (final Unit subUnit : unit.getSubUnitsSet()) {
                    final int cc = getMinCostCenter(subUnit);
                    min = Math.min(min, cc);
                }
                return min;
            }
        });
        result.addAll(units);
        return result;
    }

    private static String getParsableStr(String input) {
        final StringBuilder b = new StringBuilder("0");
        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);
            if (Character.isDigit(c)) {
                b.append(c);
            }
        }
        return b.toString();
    }

    private void createHeaderCell(final ExcelStyle excelStyle, final HSSFRow row, final int index, final String value) {
        final HSSFCell hcell = createHeaderCell(excelStyle, row, index);
        hcell.setCellValue(value);
    }

    private HSSFCell createHeaderCell(final ExcelStyle excelStyle, final HSSFRow row, final int index) {
        return createCell(excelStyle.getHeaderStyle(), row, index);
    }

    private void createCell(final ExcelStyle excelStyle, final HSSFRow row, final int index, final String value) {
        final HSSFCell hcell = createCell(excelStyle, row, index);
        hcell.setCellValue(value);
    }

    private HSSFCell createCell(final ExcelStyle excelStyle, final HSSFRow row, final int index) {
        return createCell(excelStyle.getStringStyle(), row, index);
    }

    private HSSFCell createCell(final HSSFCellStyle style, final HSSFRow row, final int index) {
        final HSSFCell cell = row.createCell(index);
        cell.setCellStyle(style);
        return cell;
    }

    public ActionForward prepareCreateSupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final CreateSupplierBean bean = new CreateSupplierBean();
        request.setAttribute("bean", bean);
        return forward("/expenditureTrackingOrganization/createSupplier.jsp");
    }

    public final ActionForward createSupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final CreateSupplierBean bean = getRenderedObject("createBean");
        final Supplier supplier = bean.create();
        final SupplierBean supplierBean = new SupplierBean(supplier);
        request.setAttribute("supplierBean", supplierBean);
        return forward("/expenditureTrackingOrganization/manageSuppliers.jsp");
    }

}
