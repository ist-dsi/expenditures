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

import module.workflow.util.ClassNameBundle;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
@ClassNameBundle(bundle = "AcquisitionResources")
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
