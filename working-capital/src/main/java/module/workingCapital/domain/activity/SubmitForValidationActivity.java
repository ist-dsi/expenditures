/*
 * @(#)SubmitForValidationActivity.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workingCapital.domain.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import module.finance.util.Money;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalAcquisitionSubmission;
import module.workingCapital.domain.WorkingCapitalAcquisitionSubmissionDocument;
import module.workingCapital.domain.WorkingCapitalAcquisitionTransaction;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalTransaction;
import module.workingCapital.util.Bundle;
import net.sf.jasperreports.engine.JRException;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import pt.ist.expenditureTrackingSystem._development.ExpenditureConfiguration;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.expenditureTrackingSystem.util.ReportUtils;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class SubmitForValidationActivity extends WorkflowActivity<WorkingCapitalProcess, SubmitForValidationActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.WORKING_CAPITAL, "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
        WorkingCapital workingCapital = missionProcess.getWorkingCapital();
        return !workingCapital.isCanceledOrRejected() && workingCapital.isMovementResponsible(user)
                && workingCapital.hasApprovedAndUnSubmittedAcquisitions() && workingCapital.areAllAcquisitionsApproved();
    }

    @Override
    protected void process(final SubmitForValidationActivityInformation activityInformation) {
        final WorkingCapitalProcess workingCapitalProcess = activityInformation.getProcess();
        workingCapitalProcess.submitAcquisitionsForValidation();
        final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();

        final Money accumulatedValue = workingCapital.getLastTransaction().getAccumulatedValue();
        WorkingCapitalAcquisitionSubmission acquisitionSubmission =
                new WorkingCapitalAcquisitionSubmission(workingCapital, getLoggedPerson().getPerson(), accumulatedValue,
                        activityInformation.isPaymentRequired());
        WorkingCapitalTransaction previousTransaction = acquisitionSubmission.getPreviousTransaction();
        while (previousTransaction != null) {
            if (previousTransaction.isSubmission()) {
                break;
            }
            if ((previousTransaction.isAcquisition()) && previousTransaction.isApproved()) {
                acquisitionSubmission
                        .addWorkingCapitalAcquisitionTransactions((WorkingCapitalAcquisitionTransaction) previousTransaction);
            }
            previousTransaction = previousTransaction.getPreviousTransaction();
        }

        String txNumber = String.valueOf(acquisitionSubmission.getNumber());
        byte[] contents = createAcquisitionSubmissionDocument(acquisitionSubmission);
        WorkingCapitalAcquisitionSubmissionDocument document =
                new WorkingCapitalAcquisitionSubmissionDocument(acquisitionSubmission, contents, "SubmissionDocument" + txNumber
                        + ".pdf", activityInformation.getProcess());
        document.setFilename("Submission" + document.getOid() + document.getFilename());

        if (activityInformation.isLastSubmission()) {
            TerminateWorkingCapitalActivity terminateWorkingCapitalActivity = new TerminateWorkingCapitalActivity();
            terminateWorkingCapitalActivity.execute(activityInformation);
        }
    }

    private byte[] createAcquisitionSubmissionDocument(WorkingCapitalAcquisitionSubmission acquisitionSubmission) {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("workingCapital", acquisitionSubmission.getWorkingCapital());
        paramMap.put("responsibleName", acquisitionSubmission.getPerson().getName());
        paramMap.put("IBAN", acquisitionSubmission.getWorkingCapital().getWorkingCapitalInitialization()
                .getInternationalBankAccountNumber());
        final String url = CoreConfiguration.getConfiguration().applicationUrl();
        paramMap.put("logoFilename", "Logo_" + getLogoFileNamePart(url)  + ".png");

        paramMap.put("submissionTransactionNumber", acquisitionSubmission.getNumber());
        paramMap.put("submissionDescription", acquisitionSubmission.getDescription());
        paramMap.put("submissionValue", acquisitionSubmission.getValue());
        paramMap.put("submissionAccumulatedValue", acquisitionSubmission.getAccumulatedValue());
        paramMap.put("submissionBalance", acquisitionSubmission.getBalance());
        paramMap.put("submissionDebt", acquisitionSubmission.getDebt());
        paramMap.put("institutionSocialSecurityNumber", ExpenditureConfiguration.get().ssn());
        paramMap.put("cae", ExpenditureConfiguration.get().cae());

        paramMap.put("paymentRequired",
                BundleUtil.getString("resources/MyorgResources", acquisitionSubmission.getPaymentRequired().toString()));

        final ResourceBundle resourceBundle = ResourceBundle.getBundle(Bundle.WORKING_CAPITAL);
        try {
            byte[] byteArray =
                    ReportUtils.exportToPdfFileAsByteArray("/reports/workingCapitalAcquisitionSubmissionDocument.jasper",
                            paramMap, resourceBundle, acquisitionSubmission.getWorkingCapitalAcquisitionTransactionsSorted());
            return byteArray;
        } catch (JRException e) {
            e.printStackTrace();
            throw new DomainException(Bundle.WORKING_CAPITAL,
                    "workingCapitalAcquisitionSubmissionDocument.exception.failedCreation");
        }
    }

    private String getLogoFileNamePart(final String url) {
        final String s1 = url.substring(url.indexOf("://") + 3);
        int idd = s1.indexOf(':');
        if (idd > 0) {
            return s1.substring(0, idd);
        }
        int ifs = s1.indexOf('/');
        return ifs > 0 ? s1.substring(0, ifs) : s1;
    }

    @Override
    public String getUsedBundle() {
        return Bundle.WORKING_CAPITAL;
    }

    @Override
    protected String[] getArgumentsDescription(SubmitForValidationActivityInformation activityInformation) {
        if (activityInformation.isLastSubmission()) {
            return new String[] { "("
                    + BundleUtil.getString(Bundle.WORKING_CAPITAL, "label.module.workingCapital.initialization.lastSubmission")
                    + ")" };
        }
        return new String[] { "" };
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new SubmitForValidationActivityInformation(process, this);
    }

    @Override
    public boolean isUserAwarenessNeeded(final WorkingCapitalProcess process, final User user) {
        return false;
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
