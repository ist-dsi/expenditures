/*
 * @(#)AcquisitionProposalDocument.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;

import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans.AcquisitionProposalDocumentFileBean;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
@ClassNameBundle(bundle = "AcquisitionResources")
public class AcquisitionProposalDocument extends AcquisitionProposalDocument_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(AcquisitionProposalDocument.class,
                AcquisitionProposalDocumentFileBean.class);
    }

    public AcquisitionProposalDocument(String displayName, String filename, byte[] content) {
        super();
        init(displayName, filename, content);
    }

    public AcquisitionProposalDocument() {
        super();
    }

//    public AcquisitionProposalDocument(FileNode fileNode) {
//        super();
//        init(fileNode);
//    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fillInNonDefaultFields(WorkflowFileUploadBean bean) {
        AcquisitionProposalDocumentFileBean fileBean = (AcquisitionProposalDocumentFileBean) bean;
        setProposalId(fileBean.getProposalID());
        setDisplayName(fileBean.getProposalID());
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) {
        SimplifiedProcedureProcess process = (SimplifiedProcedureProcess) workflowProcess;

        if (!process.getFiles(AcquisitionProposalDocument.class).isEmpty()) {
            throw new ProcessFileValidationException("resources/AcquisitionResources",
                    "error.acquisitionProposalDocument.allowedOnlyOneProposal");
        }
        if (process.getProcessClassification() != ProcessClassification.CT75000
                && (!process.isInGenesis() || process.getProcessCreator() != Authenticate.getUser())) {
            throw new ProcessFileValidationException("resources/AcquisitionResources",
                    "error.acquisitionProposalDocument.upload.invalid");
        }
        if (process.getProcessClassification() == ProcessClassification.CT75000
                && !((process.isAuthorized() || process.isAcquisitionProcessed()) && ExpenditureTrackingSystem
                        .isAcquisitionCentralGroupMember())) {
            throw new ProcessFileValidationException("resources/AcquisitionResources",
                    "error.acquisitionProposalDocument.ct75000.upload.invalid");
        }

        super.validateUpload(workflowProcess);
    }

    @Override
    public boolean isPossibleToArchieve() {
        SimplifiedProcedureProcess process = (SimplifiedProcedureProcess) getProcess();
        return process.getProcessClassification() != ProcessClassification.CT75000 ? process.isInGenesis()
                && process.getProcessCreator() == Authenticate.getUser() : (process.isAuthorized() || process
                .isAcquisitionProcessed()) && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember();
    }

    @Deprecated
    public boolean hasProposalId() {
        return getProposalId() != null;
    }

}
