package pt.ist.expenditureTrackingSystem.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.google.gson.JsonArray;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import module.finance.util.Money;
import module.organization.domain.AccountabilityType;
import module.organization.domain.PartyType;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValuesArray;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider.UnitAutoCompleteProvider;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
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
        for (final SearchProcessValues value : values) {
            hasValues.put(value, expenditureTrackingSystem.contains(value));
        }

        final Set<AccountabilityType> accountabilityTypes = Bennu.getInstance().getAccountabilityTypesSet();
        final Set<PartyType> partyTypes = Bennu.getInstance().getPartyTypesSet();

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
            @RequestParam(required = false,value="RAPID") String rapid,
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
            @RequestParam(required = false,
                    value = "approvalTextForRapidAcquisitions") String approvalTextForRapidAcquisitionsParam,
            @RequestParam(required = false, value = "acquisitionsUnit") String acquisitionsUnitParam,
            @RequestParam(required = false, value = "documentationUrl") String documentationUrl,
            @RequestParam(required = false, value = "documentationLabel") String documentationLabel,
            @RequestParam(required = false, value = "createSupplierUrl") String createSupplierUrl,
            @RequestParam(required = false, value = "createSupplierLabel") String createSupplierLabel,
            @RequestParam(required = false, value = "isForceRefundAssociationToMissions") String forceRefundAssociationToMissions,
            @RequestParam(required = false, value = "allowAdvancePayments") String allowAdvancePayments,
            @RequestParam(required = false, value = "advancePaymentDocumentRecipient") String advancePaymentDocumentRecipientParam) {

        final boolean processesNeedToBeReverified = isOn(processesNeedReverified);
        final boolean requireCommitmentNumber = isOn(requireCommitNumber);
        final boolean registerDiaryNumbersAndTransactionNumbers = isOn(registerDiaryNumbers);
        final boolean requireFundAllocationPriorToAcquisitionRequest = isOn(requireFundAllocation);
        final boolean invoiceAllowedToStartAcquisitionProcess = isOn(AllowedToStartAcquisitionProcess);
        final boolean isPriorConsultationAvailable = isOn(priorConsultation);
        final boolean isForceRefundAssociationToMissions = isOn(forceRefundAssociationToMissions);

        final ArrayList<SearchProcessValues> searchProcessValues = new ArrayList<SearchProcessValues>();
        final boolean allowRefund = isOn(refund);
        if (allowRefund) {
            searchProcessValues.add(SearchProcessValues.REFUND);
        }
        final boolean allowRS5000 = isOn(rs5000);
        if (allowRS5000) {
            searchProcessValues.add(SearchProcessValues.RS5000);
        }
        final boolean allowCT75000 = isOn(ct75000);
        if (allowCT75000) {
            searchProcessValues.add(SearchProcessValues.CT75000);
        }
        final boolean allowCT1000 = isOn(ct1000);
        if (allowCT1000) {
            searchProcessValues.add(SearchProcessValues.CT1000);
        }
        final boolean allowAcquisitions = isOn(acquisitions);
        if (allowAcquisitions) {
            searchProcessValues.add(SearchProcessValues.ACQUISITIONS);
        }
        final boolean allowRAPID = isOn(rapid);
        if (allowRAPID) {
            searchProcessValues.add(SearchProcessValues.RAPID);
        }
        
        Money maxValueStartedWithInvoive = null;
        if (maxValue != null && !maxValue.isEmpty()) {
            maxValueStartedWithInvoive = new Money(maxValue);
        }
        Money valueRequireingTopLevelAuthorization = null;
        if (valueTopLevelAuthorization != null && !valueTopLevelAuthorization.isEmpty()) {
            valueRequireingTopLevelAuthorization = new Money(valueTopLevelAuthorization);
        }

        final SearchProcessValuesArray array =
                new SearchProcessValuesArray(searchProcessValues.toArray(new SearchProcessValues[searchProcessValues.size()]));

        final LocalizedString approvalTextForRapidAcquisitions =approvalTextForRapidAcquisitionsParam!=null?
                LocalizedString.fromJson(new JsonParser().parse(approvalTextForRapidAcquisitionsParam)):LocalizedString.fromJson(new JsonObject());

        final LocalizedString acquisitionsUnit = LocalizedString.fromJson(new JsonParser().parse(acquisitionsUnitParam));

        final boolean allowedAdvancePayments = isOn(allowAdvancePayments);

        
        final LocalizedString advancePaymentDocumentRecipient = advancePaymentDocumentRecipientParam != null ? LocalizedString
                .fromJson(new JsonParser().parse(advancePaymentDocumentRecipientParam)) : LocalizedString
                        .fromJson(new JsonObject());
 
        ExpenditureTrackingSystem.getInstance().saveConfiguration(institutionalProcessNumberPrefix,
                institutionalRequestDocumentPrefix, acquisitionCreationWizardJsp, array, invoiceAllowedToStartAcquisitionProcess,
                requireFundAllocationPriorToAcquisitionRequest, registerDiaryNumbersAndTransactionNumbers,
                maxValueStartedWithInvoive, valueRequireingTopLevelAuthorization, documentationUrl, documentationLabel,

                requireCommitmentNumber, processesNeedToBeReverified, approvalTextForRapidAcquisitions, acquisitionsUnit,
                createSupplierUrl, createSupplierLabel, isPriorConsultationAvailable, isForceRefundAssociationToMissions,
                allowedAdvancePayments, advancePaymentDocumentRecipient);

        return "redirect:/expenditure/config";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/structuralConfiguration")
    public String structuralConfiguration(
            @RequestParam(value = "organizationalAccountabilityType") String organizationalAccountabilityTypeID,
            @RequestParam(value = "organizationalMissionAccountabilityType") String organizationalMissionAccountabilityTypeID,
            @RequestParam(value = "unitPartyType") String unitPartyTypeID,
            @RequestParam(value = "costCenterPartyType") String costCenterPartyTypeID,
            @RequestParam(value = "projectPartyType") String projectPartyTypeID,
            @RequestParam(value = "subProjectPartyType") String subProjectPartyTypeID,
            @RequestParam(value = "institutionManagementEmail") String institutionManagementEmail) {

        final AccountabilityType organizationalAccountabilityType =
                FenixFramework.getDomainObject(organizationalAccountabilityTypeID);
        final AccountabilityType organizationalMissionAccountabilityType =
                FenixFramework.getDomainObject(organizationalMissionAccountabilityTypeID);
        final PartyType unitPartyType = FenixFramework.getDomainObject(unitPartyTypeID);
        final PartyType costCenterPartyType = FenixFramework.getDomainObject(costCenterPartyTypeID);
        final PartyType projectPartyType = FenixFramework.getDomainObject(projectPartyTypeID);
        final PartyType subProjectPartyType = FenixFramework.getDomainObject(subProjectPartyTypeID);

        structuralConfiguration(organizationalAccountabilityType, organizationalMissionAccountabilityType, unitPartyType,
                costCenterPartyType, projectPartyType, subProjectPartyType, institutionManagementEmail);

        return "redirect:/expenditure/config";
    }

    @Atomic(mode = TxMode.WRITE)
    private void structuralConfiguration(final AccountabilityType organizationalAccountabilityType,
            final AccountabilityType organizationalMissionAccountabilityType, final PartyType unitPartyType,
            final PartyType costCenterPartyType, final PartyType projectPartyType, final PartyType subProjectPartyType,
            String institutionManagementEmail) {
        final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();
        expenditureTrackingSystem.setOrganizationalAccountabilityType(organizationalAccountabilityType);
        expenditureTrackingSystem.setOrganizationalMissionAccountabilityType(organizationalMissionAccountabilityType);
        expenditureTrackingSystem.setUnitPartyType(unitPartyType);
        expenditureTrackingSystem.setCostCenterPartyType(costCenterPartyType);
        expenditureTrackingSystem.setProjectPartyType(projectPartyType);
        expenditureTrackingSystem.setSubProjectPartyType(subProjectPartyType);
        expenditureTrackingSystem.setInstitutionManagementEmail(institutionManagementEmail);
    }
}
