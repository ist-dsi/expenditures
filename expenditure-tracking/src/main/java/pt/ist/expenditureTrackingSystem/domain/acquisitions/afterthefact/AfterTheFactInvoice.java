/*
 * @(#)AfterTheFactInvoice.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import module.workflow.domain.ProcessDocumentMetaDataResolver;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.bennu.core.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Invoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.fileBeans.AfterTheFactInvoiceBean;
import pt.ist.fenixWebFramework.services.Service;

@ClassNameBundle(bundle = "resources/AcquisitionResources")
/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class AfterTheFactInvoice extends AfterTheFactInvoice_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(AfterTheFactInvoice.class, AfterTheFactInvoiceBean.class);
    }

    public AfterTheFactInvoice(String displayName, String filename, byte[] content) {
        super();
        init(displayName, filename, content);
    }

    public AfterTheFactInvoice(final AfterTheFactAcquisitionProcess process) {
        super();
        process.addFiles(this);
    }

    public AfterTheFactInvoice() {
        super();
    }

    @Override
    public ProcessDocumentMetaDataResolver<ProcessFile> getMetaDataResolver() {
        //TODO confirm that we have no extra fields to use
        return new Invoice.InvoiceMetadaResolver();
    }

    @Override
    public void fillInNonDefaultFields(WorkflowFileUploadBean bean) {
        AfterTheFactInvoiceBean invoiceBean = (AfterTheFactInvoiceBean) bean;
        setInvoiceDate(invoiceBean.getInvoiceDate());
        setInvoiceNumber(invoiceBean.getInvoiceNumber());
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) {
        AfterTheFactAcquisitionProcess process = (AfterTheFactAcquisitionProcess) workflowProcess;
        if (process.getInvoice() != null) {
            throw new ProcessFileValidationException("resources/AcquisitionResources",
                    "error.message.cannotHaveMoreThanOneInvoice");
        }
    }

    @Override
    @Service
    public void delete() {
        removeProcess();
        super.delete();
    }
}
