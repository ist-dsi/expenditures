package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.domain.Unit;
import module.organization.presentationTier.actions.OrganizationModelAction.OrganizationalModelChart;
import myorg.domain.MyOrg;
import myorg.domain.VirtualHost;
import myorg.domain.util.Money;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValuesArray;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/expenditureConfiguration")
public class ExpenditureConfigurationAction extends BaseAction {

    public ActionForward viewConfiguration(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	return forward(request, "/expenditureConfiguration.jsp");
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

	final String invoiceAllowedToStartAcquisitionProcessParam = request.getParameter("invoiceAllowedToStartAcquisitionProcess");
	final Boolean invoiceAllowedToStartAcquisitionProcess = Boolean.valueOf("on".equals(invoiceAllowedToStartAcquisitionProcessParam));

	final String requireFundAllocationPriorToAcquisitionRequestParam = request.getParameter("requireFundAllocationPriorToAcquisitionRequest");
	final Boolean requireFundAllocationPriorToAcquisitionRequest = Boolean.valueOf("on".equals(requireFundAllocationPriorToAcquisitionRequestParam));

	final String registerDiaryNumbersAndTransactionNumbersParam = request.getParameter("registerDiaryNumbersAndTransactionNumbers");
	final Boolean registerDiaryNumbersAndTransactionNumbers = Boolean.valueOf("on".equals(registerDiaryNumbersAndTransactionNumbersParam));

	final String maxValueStartedWithInvoiveParam = request.getParameter("maxValueStartedWithInvoive");
	final Money maxValueStartedWithInvoive = maxValueStartedWithInvoiveParam == null || maxValueStartedWithInvoiveParam.isEmpty() ? null : new Money(maxValueStartedWithInvoiveParam);

	final String valueRequireingTopLevelAuthorizationParam = request.getParameter("valueRequireingTopLevelAuthorization");
	final Money valueRequireingTopLevelAuthorization = valueRequireingTopLevelAuthorizationParam == null || valueRequireingTopLevelAuthorizationParam.isEmpty() ? null : new Money(valueRequireingTopLevelAuthorizationParam);

	
	final String requireCommitmentNumberParam = request.getParameter("requireCommitmentNumber");
	final Boolean requireCommitmentNumber = Boolean.valueOf("on".equals(requireCommitmentNumberParam));

	ExpenditureTrackingSystem.getInstance().saveConfiguration(
		institutionalProcessNumberPrefix,
		institutionalRequestDocumentPrefix,
		acquisitionCreationWizardJsp,
		array,
		invoiceAllowedToStartAcquisitionProcess,
		requireFundAllocationPriorToAcquisitionRequest,
		registerDiaryNumbersAndTransactionNumbers,
		maxValueStartedWithInvoive,
		valueRequireingTopLevelAuthorization,
		documentationUrl,
		documentationLabel,
		requireCommitmentNumber);

	return viewConfiguration(mapping, form, request, response);
    }

    public ActionForward createNewSystem(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	ExpenditureTrackingSystem.createSystem(virtualHost);

	return viewConfiguration(mapping, form, request, response);
    }

    public ActionForward prepareCreateTopLevelUnits(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final ExpenditureTrackingSystem expenditureTrackingSystem = getDomainObject(request, "systemId");
	request.setAttribute("systemId", expenditureTrackingSystem.getExternalId());

	final Set<OrganizationalModel> organizationalModels = new TreeSet<OrganizationalModel>(
		OrganizationalModel.COMPARATORY_BY_NAME);
	organizationalModels.addAll(MyOrg.getInstance().getOrganizationalModelsSet());
	request.setAttribute("organizationalModels", organizationalModels);
	final OrganizationalModelChart organizationalModelChart = new OrganizationalModelChart(organizationalModels);
	request.setAttribute("organizationalModelChart", organizationalModelChart);

	return forward(request, "/createTopLevelUnits.jsp");
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
	    if (unit.hasExpenditureUnit()) {
		throw new RuntimeException("error.configuration.organization.already.has.system");
	    }
	    pt.ist.expenditureTrackingSystem.domain.organization.Unit
		    .createTopLevelUnit(unit, expenditureTrackingSystem);
	}
	return viewConfiguration(mapping, form, request, response);
    }

    public ActionForward useSystem(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	final ExpenditureTrackingSystem expenditureTrackingSystem = getDomainObject(request, "systemId");
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	expenditureTrackingSystem.setForVirtualHost(virtualHost);

	return viewConfiguration(mapping, form, request, response);
    }

}
