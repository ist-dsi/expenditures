/*
 * @(#)Invoice.java
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

import module.workflow.domain.AbstractWFDocsGroup;
import module.workflow.domain.ProcessDocumentMetaDataResolver;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WFDocsDefaultWriteGroup;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess.AcquisitionProcessBasedMetadataResolver;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

/**
 * 
 * @author Shezad Anavarali
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class Invoice extends Invoice_Base {

    public Invoice() {
        super();
    }

    public boolean isInvoiceReceived() {
        return getInvoiceNumber() != null && getInvoiceNumber().length() > 0 && getInvoiceDate() != null;
    }

    @Override
    public void delete() {
        super.delete();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        final GenericProcess genericProcess = (GenericProcess) getProcess();
        return genericProcess != null && genericProcess.isConnectedToCurrentHost();
    }

    public static class InvoiceMetadaResolver extends AcquisitionProcessBasedMetadataResolver<Invoice> {
        public static final String INVOICE_NUMBER = "Número da factura";
        public static final String INVOICE_DATE = "Data da factura";

        @Override
        public Map<String, String> getMetadataKeysAndValuesMap(ProcessFile processFile) {
            Invoice processDocument = (Invoice) processFile;
            Map<String, String> metadataKeysAndValuesMap = super.getMetadataKeysAndValuesMap(processDocument);

            metadataKeysAndValuesMap.put(INVOICE_NUMBER, processDocument.getInvoiceNumber());
            metadataKeysAndValuesMap.put(INVOICE_DATE, processDocument.getInvoiceDate().toString());

            return metadataKeysAndValuesMap;
        }

        @Override
        public @Nonnull
        Class<? extends AbstractWFDocsGroup> getWriteGroupClass() {
            //TODO probably review it case by case, but for now let it be like this
            return WFDocsDefaultWriteGroup.class;
        }

    }

    @Override
    public ProcessDocumentMetaDataResolver<ProcessFile> getMetaDataResolver() {
        return new InvoiceMetadaResolver();
    }

    @Deprecated
    public boolean hasInvoiceNumber() {
        return getInvoiceNumber() != null;
    }

    @Deprecated
    public boolean hasInvoiceDate() {
        return getInvoiceDate() != null;
    }

}
