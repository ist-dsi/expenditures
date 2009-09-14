package pt.ist.expenditureTrackingSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import myorg._development.PropertiesManager;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.ImportFile;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class CheckLimits {

    public static void init() {
	final String domainmodelPath = new File("build/WEB-INF/classes").getAbsolutePath();
	System.out.println("domainmodelPath: " + domainmodelPath);
	final File dir = new File(domainmodelPath);
	final List<String> urls = new ArrayList<String>();
	for (final File file : dir.listFiles()) {
	    if (file.isFile() && file.getName().endsWith(".dml")) {
		try {
		    urls.add(file.getCanonicalPath());
		} catch (IOException e) {
		    e.printStackTrace();
		    throw new Error(e);
		}
	    }
	}
	Collections.sort(urls);
	final String[] paths = new String[urls.size()];
	for (int i = 0; i < urls.size(); i++) {
	    paths[i] = urls.get(i);
	}

	Language.setDefaultLocale(new Locale("pt", "PT"));
	Language.setLocale(Language.getDefaultLocale());

	FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(paths));
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

	for (final AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess : importFile.getAfterTheFactAcquisitionProcessesSet()) {
	    final AcquisitionAfterTheFact acquisitionAfterTheFact = afterTheFactAcquisitionProcess.getAcquisitionAfterTheFact();
	    final Supplier supplier = acquisitionAfterTheFact.getSupplier();

	    final Money total = supplier.getTotalAllocated();
	    final Money softTotal = supplier.getSoftTotalAllocated();
	    if (softTotal.isGreaterThanOrEqual(Supplier.SOFT_SUPPLIER_LIMIT)) {
		System.out.println("Exceeded soft limit: " + softTotal.toFormatString() + " by: " + supplier.getPresentationName());
	    }
	    if (total.isGreaterThanOrEqual(Supplier.SOFT_SUPPLIER_LIMIT)) {
		System.out.println("Exceeded limit: " + total.toFormatString() + " by: " + supplier.getPresentationName());
	    }

//	    https://compras.ist.utl.pt/acquisitionAfterTheFactAcquisitionProcess.do?method=cancelImportFile&fileOID=240518175783&_CONTEXT_PATH_=335007449493,335007449497&_request_checksum_=6afd34b872ef755dc059b5f2c371672c1ea54e81
	}
    }

}
