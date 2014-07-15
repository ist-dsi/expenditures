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

import java.util.Map;

import javax.annotation.Nonnull;

import module.fileManagement.domain.FileNode;
import module.fileManagement.tools.StringUtils;
import module.workflow.domain.AbstractWFDocsGroup;
import module.workflow.domain.ProcessDocumentMetaDataResolver;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WFDocsDefaultWriteGroup;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess.AcquisitionProcessBasedMetadataResolver;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans.AcquisitionProposalDocumentFileBean;

@ClassNameBundle(bundle = "resources/AcquisitionResources")
/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
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

    public AcquisitionProposalDocument(FileNode fileNode) {
        super();
        init(fileNode);
    }

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
                && (!process.isInGenesis() || process.getProcessCreator() != UserView.getCurrentUser())) {
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

    public static class AcquisitionProposalDocumentMetaDataResolver extends
            AcquisitionProcessBasedMetadataResolver<AcquisitionProposalDocument> {

        @Override
        public Map<String, String> getMetadataKeysAndValuesMap(ProcessFile processFile) {
            AcquisitionProposalDocument processDocument = (AcquisitionProposalDocument) processFile;
            Map<String, String> metadata = super.getMetadataKeysAndValuesMap(processDocument);
            metadata.put("Id. da proposta", processDocument.getProposalId());
            AcquisitionProcess process = (AcquisitionProcess) processDocument.getProcess();

            if (StringUtils.isNotBlank(process.getSuppliersDescription())) {
                //joantune: TODO not sure if this string equals the one on the upload
                //as the one in the upload uses the contractSimpleDescription - but I do not know where that is set yet
                metadata.put("Fornecedores", process.getSuppliersDescription());

            }

            metadata.put("Unidade Requisitante", process.getRequestingUnit().getPresentationName());

            return metadata;
        }

        @Override
        public @Nonnull
        Class<? extends AbstractWFDocsGroup> getWriteGroupClass() {
            return WFDocsDefaultWriteGroup.class;
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public ProcessDocumentMetaDataResolver<AcquisitionProposalDocument> getMetaDataResolver() {
        return (ProcessDocumentMetaDataResolver) new AcquisitionProposalDocumentMetaDataResolver();
    }

    @Override
    public boolean isPossibleToArchieve() {
        SimplifiedProcedureProcess process = (SimplifiedProcedureProcess) getProcess();
        return process.getProcessClassification() != ProcessClassification.CT75000 ? process.isInGenesis()
                && process.getProcessCreator() == UserView.getCurrentUser() : (process.isAuthorized() || process
                .isAcquisitionProcessed()) && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        final WorkflowProcess genericProcess = getProcess();
        return (genericProcess != null && genericProcess.isConnectedToCurrentHost())
        			|| isDeletedProcessConnectedToCurrentHost();
    }

    private boolean isDeletedProcessConnectedToCurrentHost() {
    	final WorkflowProcess process = getProcessWithDeleteFile();
    	return process != null && process.isConnectedToCurrentHost();
	}

	@Deprecated
    public boolean hasProposalId() {
        return getProposalId() != null;
    }

}
