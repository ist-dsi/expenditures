/*
 * @(#)ExpenditureConfigurationAction.java
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
package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.finance.util.Money;
import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.domain.Unit;
import module.organization.presentationTier.actions.OrganizationModelAction.OrganizationalModelChart;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValuesArray;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions.SearchPaymentProcessesAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = SearchPaymentProcessesAction.class, path = "expenditureConfiguration",
        titleKey = "link.topBar.configuration", accessGroup = "#managers")
@Mapping(path = "/expenditureConfiguration")
/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class ExpenditureConfigurationAction extends BaseAction {

    @EntryPoint
    public ActionForward viewConfiguration(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        return forward("/expenditureConfiguration.jsp");
    }

    public ActionForward saveConfiguration(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final Set<SearchProcessValues> valuesToSet = new HashSet<SearchProcessValues>();
        for (final SearchProcessValues values : SearchProcessValues.values()) {
            final String parameter = request.getParameter(values.name());
            if ("on".equals(parameter)) {
                valuesToSet.add(values);
            }
        }
        final SearchProcessValuesArray array = SearchProcessValuesArray.importFromString(valuesToSet);

        final String acquisitionCreationWizardJsp = request.getParameter("acquisitionCreationWizardJsp");
        final String institutionalProcessNumberPrefix = request.getParameter("institutionalProcessNumberPrefix");
        final String institutionalRequestDocumentPrefix = request.getParameter("institutionalRequestDocumentPrefix");

        final String documentationUrl = request.getParameter("documentationUrl");
        final String documentationLabel = request.getParameter("documentationLabel");

        final String createSupplierUrl = request.getParameter("createSupplierUrl");
        final String createSupplierLabel = request.getParameter("createSupplierLabel");

        final String invoiceAllowedToStartAcquisitionProcessParam =
                request.getParameter("invoiceAllowedToStartAcquisitionProcess");
        final Boolean invoiceAllowedToStartAcquisitionProcess =
                Boolean.valueOf("on".equals(invoiceAllowedToStartAcquisitionProcessParam));

        final String requireFundAllocationPriorToAcquisitionRequestParam =
                request.getParameter("requireFundAllocationPriorToAcquisitionRequest");
        final Boolean requireFundAllocationPriorToAcquisitionRequest =
                Boolean.valueOf("on".equals(requireFundAllocationPriorToAcquisitionRequestParam));

        final String registerDiaryNumbersAndTransactionNumbersParam =
                request.getParameter("registerDiaryNumbersAndTransactionNumbers");
        final Boolean registerDiaryNumbersAndTransactionNumbers =
                Boolean.valueOf("on".equals(registerDiaryNumbersAndTransactionNumbersParam));

        final String maxValueStartedWithInvoiveParam = request.getParameter("maxValueStartedWithInvoive");
        final Money maxValueStartedWithInvoive =
                maxValueStartedWithInvoiveParam == null || maxValueStartedWithInvoiveParam.isEmpty() ? null : new Money(
                        maxValueStartedWithInvoiveParam);

        final String valueRequireingTopLevelAuthorizationParam = request.getParameter("valueRequireingTopLevelAuthorization");
        final Money valueRequireingTopLevelAuthorization =
                valueRequireingTopLevelAuthorizationParam == null || valueRequireingTopLevelAuthorizationParam.isEmpty() ? null : new Money(
                        valueRequireingTopLevelAuthorizationParam);

        final String requireCommitmentNumberParam = request.getParameter("requireCommitmentNumber");
        final Boolean requireCommitmentNumber = Boolean.valueOf("on".equals(requireCommitmentNumberParam));

        final String processesNeedToBeReverifiedParam = request.getParameter("processesNeedToBeReverified");
        final Boolean processesNeedToBeReverified = Boolean.valueOf("on".equals(processesNeedToBeReverifiedParam));

        ExpenditureTrackingSystem.getInstance().saveConfiguration(institutionalProcessNumberPrefix,
                institutionalRequestDocumentPrefix, acquisitionCreationWizardJsp, array, invoiceAllowedToStartAcquisitionProcess,
                requireFundAllocationPriorToAcquisitionRequest, registerDiaryNumbersAndTransactionNumbers,
                maxValueStartedWithInvoive, valueRequireingTopLevelAuthorization, documentationUrl, documentationLabel,
                requireCommitmentNumber, processesNeedToBeReverified, createSupplierUrl, createSupplierLabel);

        return viewConfiguration(mapping, form, request, response);
    }

    public ActionForward createNewSystem(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        ExpenditureTrackingSystem.createSystem();

        return viewConfiguration(mapping, form, request, response);
    }

    public ActionForward prepareCreateTopLevelUnits(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        final ExpenditureTrackingSystem expenditureTrackingSystem = getDomainObject(request, "systemId");
        request.setAttribute("systemId", expenditureTrackingSystem.getExternalId());

        final Set<OrganizationalModel> organizationalModels =
                new TreeSet<OrganizationalModel>(OrganizationalModel.COMPARATORY_BY_NAME);
        organizationalModels.addAll(Bennu.getInstance().getOrganizationalModelsSet());
        request.setAttribute("organizationalModels", organizationalModels);
        final OrganizationalModelChart organizationalModelChart = new OrganizationalModelChart(organizationalModels);
        request.setAttribute("organizationalModelChart", organizationalModelChart);

        return forward("/createTopLevelUnits.jsp");
    }

    public ActionForward createTopLevelUnits(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        final OrganizationalModel organizationModel = getDomainObject(request, "organizationalModelOid");
        final ExpenditureTrackingSystem expenditureTrackingSystem = getDomainObject(request, "systemId");
        for (Party party : organizationModel.getParties()) {
            if (!party.isUnit()) {
                continue;
            }
            Unit unit = (Unit) party;
            if (unit.getExpenditureUnit() != null) {
                throw new RuntimeException("error.configuration.organization.already.has.system");
            }
            pt.ist.expenditureTrackingSystem.domain.organization.Unit.createTopLevelUnit(unit, expenditureTrackingSystem);
        }
        return viewConfiguration(mapping, form, request, response);
    }

}
