/*
 * @(#)PurchaseOrderDocument.java
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

import module.workflow.domain.ProcessDocumentMetaDataResolver;
import module.workflow.domain.ProcessFile;
import pt.ist.bennu.core.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

@ClassNameBundle(bundle = "resources/AcquisitionResources")
/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class PurchaseOrderDocument extends PurchaseOrderDocument_Base {

    protected PurchaseOrderDocument(String requestId) {
        super();
        setRequestId(requestId);
    }

    public PurchaseOrderDocument(final AcquisitionProcess process, final byte[] contents, final String fileName, String requestID) {
        this(requestID);
        if (process.hasPurchaseOrderDocument()) {
            process.getPurchaseOrderDocument().delete();
        }

        setContent(contents);
        setFilename(fileName);
        process.addFiles(this);
    }

    @Override
    public ProcessDocumentMetaDataResolver<? extends ProcessFile> getMetaDataResolver() {
        return new AcquisitionProcess.AcquisitionProcessBasedMetadataResolver<PurchaseOrderDocument>();
    }

    @Override
    public void delete() {

        super.delete();
    }

    @Override
    public boolean isPossibleToArchieve() {
        return false;
    }

    @Override
    public String getDisplayName() {
        return getFilename();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        final GenericProcess genericProcess = (GenericProcess) getProcess();
        return genericProcess != null && genericProcess.isConnectedToCurrentHost();
    }

}
