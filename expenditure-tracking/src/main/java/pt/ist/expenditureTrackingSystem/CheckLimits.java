/*
 * @(#)CheckLimits.java
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

import java.util.Locale;

import pt.ist.bennu.core._development.PropertiesManager;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.ImportFile;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class CheckLimits {

	public static void init() {
		Language.setDefaultLocale(new Locale("pt", "PT"));
		Language.setLocale(Language.getDefaultLocale());

		FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig());
	}

	public static void main(String[] args) {
		init();
		check();
		System.out.println("Done.");
	}

	@Service
	private static void check() {
		final ImportFile importFile = AbstractDomainObject.fromExternalId("240518175783");
		System.out.println("Found: " + importFile.getDisplayName() + " - " + importFile.getFilename());
		System.out.println("   isActive ? " + importFile.getActive());
		System.out.println("   associated processes: " + importFile.getAfterTheFactAcquisitionProcessesCount());

		for (final AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess : importFile
				.getAfterTheFactAcquisitionProcessesSet()) {
			final AcquisitionAfterTheFact acquisitionAfterTheFact = afterTheFactAcquisitionProcess.getAcquisitionAfterTheFact();
			final Supplier supplier = acquisitionAfterTheFact.getSupplier();

			final Money total = supplier.getTotalAllocated();
			final Money softTotal = supplier.getSoftTotalAllocated();
			if (softTotal.isGreaterThanOrEqual(Supplier.SOFT_SUPPLIER_LIMIT)) {
				System.out.println("Exceeded soft limit: " + softTotal.toFormatString() + " by: "
						+ supplier.getPresentationName());
			}
			if (total.isGreaterThanOrEqual(Supplier.SOFT_SUPPLIER_LIMIT)) {
				System.out.println("Exceeded limit: " + total.toFormatString() + " by: " + supplier.getPresentationName());
			}

			// https://compras.ist.utl.pt/acquisitionAfterTheFactAcquisitionProcess.do?method=cancelImportFile&fileOID=240518175783&_CONTEXT_PATH_=335007449493,335007449497&_request_checksum_=6afd34b872ef755dc059b5f2c371672c1ea54e81
		}
	}

}
