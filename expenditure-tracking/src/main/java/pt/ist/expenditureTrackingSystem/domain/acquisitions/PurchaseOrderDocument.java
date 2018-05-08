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

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import module.mission.domain.MissionSystem;
import module.workflow.domain.ProcessFileSignatureHandler;
import module.workflow.domain.ProcessFileSignatureHandler.Provider;
import module.workflow.domain.SigningState;
import module.workflow.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem._development.ExpenditureConfiguration;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * @author Ricardo Almeida
 * 
 */
@ClassNameBundle(bundle = "AcquisitionResources")
public class PurchaseOrderDocument extends PurchaseOrderDocument_Base {

    private static class PurchaseOrderDocumentSignHandler extends ProcessFileSignatureHandler<PurchaseOrderDocument> {

        private PurchaseOrderDocumentSignHandler(final PurchaseOrderDocument processFile) {
            super(processFile);
        }

        private SimplifiedProcedureProcess getProcess() {
            return (SimplifiedProcedureProcess) processFile.getProcess();
        }

        @Override
        public String filename() {
            return getProcess().getAcquisitionRequestDocumentID();
        }

        @Override
        public String title() {
            return "Nota de Encomenda - " + getProcess().getProcessNumber();
        }

        @Override
        public String queue() {
            return ExpenditureConfiguration.get().queueSimplifiedPurchaseOrder();
        }

        @Override
        public String signatureField() {
            return "";
        }

        @Override
        public String callbackUrl(byte[] jwtSecret) {
            return CoreConfiguration.getConfiguration().applicationUrl() + "/mission/" + getProcess().getExternalId() + "/sign?nounce=" + nounce(jwtSecret);
        }

        @Override
        public boolean canSignFile() {
            final SigningState signingState = processFile.getSigningState();
            final User user = Authenticate.getUser();
            return signingState == SigningState.CREATED && (ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
                    || MissionSystem.getInstance().isManagementCouncilMember(user));
        }
    }

    static {
        final Provider<PurchaseOrderDocument> provider = (f) -> new PurchaseOrderDocumentSignHandler(f);
        ProcessFileSignatureHandler.register(PurchaseOrderDocument.class, provider);
    }

    protected PurchaseOrderDocument(String requestId) {
        super();
        setRequestId(requestId);
        setShouldBeSigned(Boolean.TRUE);
    }

    public PurchaseOrderDocument(final AcquisitionProcess process, final byte[] contents, final String fileName,
            String requestID) {
        this(requestID);
        if (process.hasPurchaseOrderDocument()) {
            process.getPurchaseOrderDocument().delete();
        }

        init(fileName, fileName, contents);
        process.addFiles(this);
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

    @Deprecated
    public boolean hasRequestId() {
        return getRequestId() != null;
    }

}
