package pt.ist.expenditureTrackingSystem.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import module.finance.util.Money;
import module.organization.domain.AccountabilityType;
import module.organization.domain.PartyType;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValuesArray;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@SpringApplication(group = "logged", path = "expenditure-tracking", title = "configureAcquisition.title.configureAcquisition",
        hint = "expenditure-tracking")
@SpringFunctionality(app = ConfigureAcquisitonController.class, title = "configureAcquisition.title.configureAcquisition")
@RequestMapping("/expenditure/config")
public class ConfigureAcquisitonController {

    @RequestMapping(method = RequestMethod.GET)
    public String home(final Model model) {

        final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();

        final String institutionalProcessNumberPrefix =
                expenditureTrackingSystem.getInstitutionalProcessNumberPrefix() == null ? "" : expenditureTrackingSystem
                        .getInstitutionalProcessNumberPrefix();
        final String institutionalRequestDocumentPrefix =
                expenditureTrackingSystem.getInstitutionalRequestDocumentPrefix() == null ? "" : expenditureTrackingSystem
                        .getInstitutionalRequestDocumentPrefix();
        final String acquisitionCreationWizardJsp =
                expenditureTrackingSystem.getAcquisitionCreationWizardJsp() == null ? "" : expenditureTrackingSystem
                        .getAcquisitionCreationWizardJsp();
        final SearchProcessValues[] values = SearchProcessValues.values();
        final HashMap<SearchProcessValues, Boolean> hasValues = new HashMap<SearchProcessValues, Boolean>();
        for (SearchProcessValues value : values) {
            hasValues.put(value, expenditureTrackingSystem.contains(value));
        }

        Set<AccountabilityType> accountabilityTypes = Bennu.getInstance().getAccountabilityTypesSet();
        Set<PartyType> partyTypes = Bennu.getInstance().getPartyTypesSet();

        model.addAttribute("applicationUrl", CoreConfiguration.getConfiguration().applicationUrl());
        model.addAttribute("institutionalProcessNumberPrefix", institutionalProcessNumberPrefix);
        model.addAttribute("institutionalRequestDocumentPrefix", institutionalRequestDocumentPrefix);
        model.addAttribute("acquisitionCreationWizardJsp", acquisitionCreationWizardJsp);
        model.addAttribute("processValues", hasValues);
        model.addAttribute("expenditureTrackingSystem", expenditureTrackingSystem);
        model.addAttribute("accountabilityTypes", accountabilityTypes);
        model.addAttribute("partyTypes", partyTypes);

        return "expenditure-tracking/configureAcquisition";
    }

    private boolean isOn(String s) {
        return s != null && s.equals("on");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/generalConfig")
    public String generalConfiguration(
            @RequestParam(required = false, value = "institutionalProcessNumberPrefix") String institutionalProcessNumberPrefix,
            @RequestParam(required = false,
                    value = "institutionalRequestDocumentPrefix") String institutionalRequestDocumentPrefix,
            @RequestParam(required = false, value = "acquisitionCreationWizardJsp") String acquisitionCreationWizardJsp,
            @RequestParam(required = false, value = "ACQUISITIONS") String acquisitions,
            @RequestParam(required = false, value = "CT1000") String ct1000,
            @RequestParam(required = false, value = "CT75000") String ct75000,
            @RequestParam(required = false, value = "REFUND") String refund,
            @RequestParam(required = false, value = "RS5000") String rs5000,
            @RequestParam(required = false, value = "isPriorConsultationAvailable") String priorConsultation,
            @RequestParam(required = false,
                    value = "invoiceAllowedToStartAcquisitionProcess") String AllowedToStartAcquisitionProcess,
            @RequestParam(required = false, value = "maxValueStartedWithInvoive") String maxValue,
            @RequestParam(required = false,
                    value = "requireFundAllocationPriorToAcquisitionRequest") String requireFundAllocation,
            @RequestParam(required = false, value = "registerDiaryNumbersAndTransactionNumbers") String registerDiaryNumbers,
            @RequestParam(required = false, value = "valueRequireingTopLevelAuthorization") String valueTopLevelAuthorization,
            @RequestParam(required = false, value = "requireCommitmentNumber") String requireCommitNumber,
            @RequestParam(required = false, value = "processesNeedToBeReverified") String processesNeedReverified,
            @RequestParam(required = false, value = "documentationUrl") String documentationUrl,
            @RequestParam(required = false, value = "documentationLabel") String documentationLabel,
            @RequestParam(required = false, value = "createSupplierUrl") String createSupplierUrl,
            @RequestParam(required = false, value = "createSupplierLabel") String createSupplierLabel) {

        boolean processesNeedToBeReverified = isOn(processesNeedReverified);
        boolean requireCommitmentNumber = isOn(requireCommitNumber);
        boolean registerDiaryNumbersAndTransactionNumbers = isOn(registerDiaryNumbers);
        boolean requireFundAllocationPriorToAcquisitionRequest = isOn(requireFundAllocation);
        boolean invoiceAllowedToStartAcquisitionProcess = isOn(AllowedToStartAcquisitionProcess);
        boolean isPriorConsultationAvailable = isOn(priorConsultation);

        ArrayList<SearchProcessValues> searchProcessValues = new ArrayList<SearchProcessValues>();
        boolean allowRefund = isOn(refund);
        if (allowRefund) {
            searchProcessValues.add(SearchProcessValues.REFUND);
        }
        boolean allowRS5000 = isOn(rs5000);
        if (allowRS5000) {
            searchProcessValues.add(SearchProcessValues.RS5000);
        }
        boolean allowCT75000 = isOn(ct75000);
        if (allowCT75000) {
            searchProcessValues.add(SearchProcessValues.CT75000);
        }
        boolean allowCT1000 = isOn(ct1000);
        if (allowCT1000) {
            searchProcessValues.add(SearchProcessValues.CT1000);
        }
        boolean allowAcquisitions = isOn(acquisitions);
        if (allowAcquisitions) {
            searchProcessValues.add(SearchProcessValues.ACQUISITIONS);
        }
        Money maxValueStartedWithInvoive = null;
        if (maxValue != null && !maxValue.isEmpty()) {
            maxValueStartedWithInvoive = Money.importFromString(maxValue);
        }
        Money valueRequireingTopLevelAuthorization = null;
        if (valueTopLevelAuthorization != null && !valueTopLevelAuthorization.isEmpty()) {
            valueRequireingTopLevelAuthorization = Money.importFromString(valueTopLevelAuthorization);
        }

        SearchProcessValuesArray array =
                new SearchProcessValuesArray(searchProcessValues.toArray(new SearchProcessValues[searchProcessValues.size()]));

        ExpenditureTrackingSystem.getInstance().saveConfiguration(institutionalProcessNumberPrefix,
                institutionalRequestDocumentPrefix, acquisitionCreationWizardJsp, array, invoiceAllowedToStartAcquisitionProcess,
                requireFundAllocationPriorToAcquisitionRequest, registerDiaryNumbersAndTransactionNumbers,
                maxValueStartedWithInvoive, valueRequireingTopLevelAuthorization, documentationUrl, documentationLabel,
                requireCommitmentNumber, processesNeedToBeReverified, createSupplierUrl, createSupplierLabel,
                isPriorConsultationAvailable);

        return "redirect:/expenditure/config";
    }

    @Atomic//TODO change to write
    @RequestMapping(method = RequestMethod.POST, value = "/structuralConfiguration")
    public String structuralConfiguration(
            @RequestParam(value = "organizationalAccountabilityType") String organizationalAccountabilityTypeID,
            @RequestParam(value = "organizationalMissionAccountabilityType") String organizationalMissionAccountabilityTypeID,
            @RequestParam(value = "unitPartyType") String unitPartyTypeID,
            @RequestParam(value = "costCenterPartyType") String costCenterPartyTypeID,
            @RequestParam(value = "subProjectPartyType") String subProjectPartyTypeID,
            @RequestParam(value = "institutionManagementEmail") String institutionManagementEmail) {

        final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();

        AccountabilityType organizationalAccountabilityType = FenixFramework.getDomainObject(organizationalAccountabilityTypeID);
        AccountabilityType organizationalMissionAccountabilityType =
                FenixFramework.getDomainObject(organizationalMissionAccountabilityTypeID);
        PartyType unitPartyType = FenixFramework.getDomainObject(unitPartyTypeID);
        PartyType costCenterPartyType = FenixFramework.getDomainObject(costCenterPartyTypeID);
        PartyType subProjectPartyType = FenixFramework.getDomainObject(subProjectPartyTypeID);

        expenditureTrackingSystem.setOrganizationalAccountabilityType(organizationalAccountabilityType);
        expenditureTrackingSystem.setOrganizationalMissionAccountabilityType(organizationalMissionAccountabilityType);
        expenditureTrackingSystem.setUnitPartyType(unitPartyType);
        expenditureTrackingSystem.setCostCenterPartyType(costCenterPartyType);
        expenditureTrackingSystem.setSubProjectPartyType(subProjectPartyType);
        expenditureTrackingSystem.setInstitutionManagementEmail(institutionManagementEmail);

        return "redirect:/expenditure/config";
    }

}
