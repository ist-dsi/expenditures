/*
 * @(#)TransactionFile.java
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

import pt.ist.bennu.core.util.ClassNameBundle;

@ClassNameBundle(bundle = "resources/WorkingCapitalResources")
public class TransactionFile extends TransactionFile_Base {

    public TransactionFile(String displayName, String filename, byte[] content, WorkingCapitalTransaction transaction) {
        super();
        if (content != null) {
            init(displayName, filename, content);
        }
        setWorkingCapitalTransaction(transaction);
    }

    @Override
    public void delete() {
        setWorkingCapitalTransaction(null);
        super.delete();
    }

}
