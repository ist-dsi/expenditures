/*
 * @(#)WorkingCapitalAcquisitionSubmissionDocument.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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

import module.workflow.domain.ProcessDocumentMetaDataResolver;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;
import module.workingCapital.domain.WorkingCapitalProcess.WorkingCapitalProcessFileMetadataResolver;

/**
 * 
 * @author João Neves
 * 
 */
public class WorkingCapitalAcquisitionSubmissionDocument extends WorkingCapitalAcquisitionSubmissionDocument_Base {

	public WorkingCapitalAcquisitionSubmissionDocument(final WorkingCapitalAcquisitionSubmission submissionTransaction,
			final byte[] contents, final String fileName, WorkflowProcess process) {
		super();
		setTransaction(submissionTransaction);
		setContent(contents);
		setFilename(fileName);
		init(fileName, fileName, contents);
		process.addFiles(this);
	}

	@Override
	public ProcessDocumentMetaDataResolver<? extends ProcessFile> getMetaDataResolver() {
		return new WorkingCapitalAcquisitionSubmissionDocumentMetadataResolver();
	}

	public static class WorkingCapitalAcquisitionSubmissionDocumentMetadataResolver extends
			WorkingCapitalProcessFileMetadataResolver {

		private static final String TX_NUMBER = "Número de Tx";
		private static final String VALUE = "Valor";

		@Override
		public Map<String, String> getMetadataKeysAndValuesMap(ProcessFile processDocument) {
			Map<String, String> metadataKeysAndValuesMap = super.getMetadataKeysAndValuesMap(processDocument);
			WorkingCapitalAcquisitionSubmission transaction =
					((WorkingCapitalAcquisitionSubmissionDocument) processDocument).getTransaction();
			metadataKeysAndValuesMap.put(TX_NUMBER, String.valueOf(transaction.getNumber()));
			metadataKeysAndValuesMap.put(VALUE, transaction.getValue().toFormatString());

			return metadataKeysAndValuesMap;
		}

	}

}
