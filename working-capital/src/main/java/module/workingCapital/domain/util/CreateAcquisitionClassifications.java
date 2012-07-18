/*
 * @(#)CreateAcquisitionClassifications.java
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
package module.workingCapital.domain.util;

import module.workingCapital.domain.AcquisitionClassification;
import pt.ist.bennu.core.domain.scheduler.WriteCustomTask;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class CreateAcquisitionClassifications extends WriteCustomTask {

    @Override
    protected void doService() {
	create("Combustiveis", "622121", "020102");
	create("Laboratorio", "622151", "020120");
	create("Ferra.Utensílios", "622159", "020117");
	create("Livros", "62216", "020118");
	create("Mat.Escritorio", "622171", "020108");
	create("C.Informa", "622172", "020108");
	create("Cons.Rep", "622323", "020203");
	create("Trab. Espec.", "622369", "020220");
	create("Correio", "622221", "020209");
	create("Transportes", "622271", "020213");
	create("Alim. Aloj.", "622272", "020213");
	create("Out. Fornec.", "622981", "020121");
	create("Out.Serviços", "622982", "020225");
    }

    private void create(final String description, final String economicClassification, final String pocCode) {
	new AcquisitionClassification(description, economicClassification, pocCode);
    }

}
