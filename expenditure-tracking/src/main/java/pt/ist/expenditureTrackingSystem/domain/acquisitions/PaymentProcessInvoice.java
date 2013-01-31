/*
 * @(#)PaymentProcessInvoice.java
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

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class PaymentProcessInvoice extends PaymentProcessInvoice_Base {

	public PaymentProcessInvoice() {
		super();
	}

	//    public static class PaymentProcessInvoiceMetadataResolver extends InvoiceMetadaResolver {
	////
	////	public final static String REQUEST_ITEMS = "Itens do pedido";
	////
	////	public final static String UNIT_ITEMS = "Itens de unidade";
	////	public final static String FINANCERS = "Unidades pagadoras";
	////	public final static String PROJECT_FINANCERS = "Financiadores do projecto";
	//
	//	@Override
	//	public Map<String, String> getMetadataKeysAndValuesMap(ProcessFile processFile) {
	//	    PaymentProcessInvoice processDocument = (PaymentProcessInvoice) processFile;
	//	    Map<String, String> metadataKeysAndValuesMap = super.getMetadataKeysAndValuesMap(processFile);
	//
	//	    //Actually, nothing should be done with these fields
	////	    List<RequestItem> requestItems = processDocument.getRequestItems();
	////	    
	////	    if (requestItems != null && !requestItems.isEmpty())
	////	    {
	////		metadataKeysAndValuesMap.put(key, value)
	////	    }
	////
	////	    List<UnitItem> unitItems = processDocument.getUnitItems();
	////
	////	    List<Financer> financers = processDocument.getFinancers();
	////
	////	    List<ProjectFinancer> projectFinancers = processDocument.getProjectFinancers();
	//
	//	    return metadataKeysAndValuesMap;
	//	}
	//
	//    }

	@Override
	public ProcessDocumentMetaDataResolver<ProcessFile> getMetaDataResolver() {
		return new InvoiceMetadaResolver();
	}

	@Override
	public void delete() {
		getUnitItems().clear();
		getRequestItems().clear();
		getProjectFinancers().clear();
		getFinancers().clear();
		super.delete();
	}
}
