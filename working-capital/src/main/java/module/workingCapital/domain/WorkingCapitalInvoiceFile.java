/*
 * @(#)WorkingCapitalInvoiceFile.java
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
package module.workingCapital.domain;

import java.util.Map;

import javax.annotation.Nonnull;

import module.workflow.domain.AbstractWFDocsGroup;
import module.workflow.domain.ProcessDocumentMetaDataResolver;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WFDocsDefaultWriteGroup;
import module.workingCapital.domain.WorkingCapitalProcess.WorkingCapitalProcessFileMetadataResolver;
import pt.ist.bennu.core.util.ClassNameBundle;

@ClassNameBundle(bundle = "resources/WorkingCapitalResources")
/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class WorkingCapitalInvoiceFile extends WorkingCapitalInvoiceFile_Base {

    public WorkingCapitalInvoiceFile(String displayName, String filename, byte[] content, WorkingCapitalTransaction transaction) {
	super();
	if (content != null) {
	    init(displayName, filename, content);
	}
	setTransaction(transaction);
    }

    @Override
    public ProcessDocumentMetaDataResolver<? extends ProcessFile> getMetaDataResolver() {
	return new WorkingCapitalInvoiceFileMetadataResolver();
    }

    public static class WorkingCapitalInvoiceFileMetadataResolver extends
 WorkingCapitalProcessFileMetadataResolver {

	private static final String SUPPLIER = "Fornecedor";
	private static final String DOCUMENT_NR = "Número do Documento";
	private static final String DESCRIPTION = "Descrição";
	private static final String CLASSIFICATION = "Classificação";
	private static final String VALUE_WITHOUT_VAT = "Valor sem IVA";
	private static final String VALUE_WITH_VAT = "Valor com IVA";

	@Override
	public @Nonnull
	Class<? extends AbstractWFDocsGroup> getWriteGroupClass() {
	    return WFDocsDefaultWriteGroup.class;
	}

	@Override
	public Map<String, String> getMetadataKeysAndValuesMap(ProcessFile processDocument) {
	    Map<String, String> metadataKeysAndValuesMap = super.getMetadataKeysAndValuesMap(processDocument);
	    WorkingCapitalAcquisitionTransaction transaction = ((WorkingCapitalAcquisitionTransaction) ((WorkingCapitalInvoiceFile) processDocument)
		    .getTransaction());
	    WorkingCapitalAcquisition workingCapitalAcquisition = transaction.getWorkingCapitalAcquisition();

	    metadataKeysAndValuesMap
.put(SUPPLIER, workingCapitalAcquisition.getSupplier().getPresentationName());
	    metadataKeysAndValuesMap
.put(DOCUMENT_NR, workingCapitalAcquisition.getDocumentNumber());
	    metadataKeysAndValuesMap.put(DESCRIPTION, workingCapitalAcquisition.getDescription());
	    metadataKeysAndValuesMap.put(CLASSIFICATION, workingCapitalAcquisition.getAcquisitionClassification()
		    .getDescription());
	    metadataKeysAndValuesMap.put(VALUE_WITHOUT_VAT, workingCapitalAcquisition.getValueWithoutVat().toFormatString());
	    metadataKeysAndValuesMap.put(VALUE_WITH_VAT, transaction.getValue().toFormatString());

	    return metadataKeysAndValuesMap;
	}

    }

    @Override
    public void delete() {
	removeTransaction();
	super.delete();
    }
}
