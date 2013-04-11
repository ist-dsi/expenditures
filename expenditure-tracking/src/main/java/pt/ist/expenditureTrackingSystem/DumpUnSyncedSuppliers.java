/*
 * @(#)DumpUnSyncedSuppliers.java
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
package pt.ist.expenditureTrackingSystem;

import java.io.IOException;
import java.io.PrintWriter;

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class DumpUnSyncedSuppliers {

    public static void init() {
        String domainModelPath = "web/WEB-INF/classes/domain_model.dml";
        // TODO : reimplmenent as scheduled script
        //FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
        //ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());
    }

    public static void main(String[] args) {
        init();
        try {
            dump();
        } catch (final IOException e) {
            throw new Error(e);
        }
        System.out.println("Done.");
    }

    @Atomic
    public static void dump() throws IOException {
        final PrintWriter printWriter = new PrintWriter("/tmp/fornecedores.csv");
        for (final Supplier supplier : MyOrg.getInstance().getSuppliersSet()) {
            if (supplier.getGiafKey() == null || supplier.getGiafKey().isEmpty()) {
                printWriter.append(supplier.getFiscalIdentificationCode());
                printWriter.append("\t");
                printWriter.append(supplier.getName());
                printWriter.append("\n");
            }
        }
        printWriter.close();
    }

}
