/*
 * @(#)CreditNoteDocument.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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

import module.workflow.domain.AbstractWFDocsGroup;
import module.workflow.domain.ProcessDocumentMetaDataResolver;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WFDocsDefaultWriteGroup;
import module.workflow.domain.WorkflowProcess;
import pt.ist.bennu.core.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess.AcquisitionProcessBasedMetadataResolver;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

@ClassNameBundle(bundle = "resources/AcquisitionResources")
/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class CreditNoteDocument extends CreditNoteDocument_Base {

    public CreditNoteDocument(String displayName, String filename, byte[] content) {
        super();
        init(displayName, filename, content);
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) {
        if (!ExpenditureTrackingSystem.isAcquisitionCentralGroupMember()) {
            throw new ProcessFileValidationException("resources/AcquisitionResources", "error.creditNoteDocument.upload.invalid");
        }
    }

    public static class CreditNoteMetaDataResolver extends AcquisitionProcessBasedMetadataResolver<CreditNoteDocument> {

        @Override
        public Map<String, String> getMetadataKeysAndValuesMap(ProcessFile processFile) {
            CreditNoteDocument processDocument = (CreditNoteDocument) processFile;
            Map<String, String> metadataMap = super.getMetadataKeysAndValuesMap(processDocument);
            SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) processDocument.getProcess();

            //not needed, right?
            //	    String suppliersDescription = simplifiedProcedureProcess.getSuppliersDescription();
            //	    if (StringUtils.isNotBlank(suppliersDescription)) {
            //		metadataMap.put(AcquisitionProcess.SUPPLIERS_METADATA_KEY, suppliersDescription);
            //	    }
            //	    
            return metadataMap;
        }

        @Override
        public @Nonnull
        Class<? extends AbstractWFDocsGroup> getWriteGroupClass() {
            // TODO confirm that the default is what we want
            return WFDocsDefaultWriteGroup.class;
        }

    }

    @Override
    public ProcessDocumentMetaDataResolver<? extends ProcessFile> getMetaDataResolver() {
        // TODO Auto-generated method stub
        return super.getMetaDataResolver();
    }

    @Override
    public boolean isPossibleToArchieve() {
        return ExpenditureTrackingSystem.isAcquisitionCentralGroupMember();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        final GenericProcess genericProcess = (GenericProcess) getProcess();
        return genericProcess != null && genericProcess.isConnectedToCurrentHost();
    }

}
