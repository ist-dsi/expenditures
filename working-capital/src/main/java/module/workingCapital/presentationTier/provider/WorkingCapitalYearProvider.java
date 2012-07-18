/*
 * @(#)WorkingCapitalYearProvider.java
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
package module.workingCapital.presentationTier.provider;

import module.workingCapital.domain.WorkingCapitalSystem;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * 
 */
public class WorkingCapitalYearProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
	return WorkingCapitalSystem.getInstanceForCurrentHost().getWorkingCapitalYears();
    }

}
