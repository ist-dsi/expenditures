package pt.ist.expenditureTrackingSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jvstm.TransactionalCommand;
import myorg._development.PropertiesManager;
import myorg.domain.MyOrg;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class CheckSuppliersOverLimit {

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
	MyOrg.initialize(FenixWebFramework.getConfig());
    }

    public static void main(String[] args) {
	init();
	Transaction.withTransaction(false, new TransactionalCommand() {
	    @Override
	    public void doIt() {
		checkSupplierLimits();
	    }

	});

	System.out.println("Done.");
    }

    private static void checkSupplierLimits() {
	for (final Supplier supplier : MyOrg.getInstance().getExpenditureTrackingSystem().getSuppliersSet()) {
	    if (supplier.getTotalAllocated().isGreaterThan(new Money("60000"))) {
		System.out.println("Supplier: " + supplier.getPresentationName() + " exceeded limit: " + supplier.getTotalAllocated().toFormatString());
	    }
	    if (supplier.getTotalAllocated().equals(new Money("59999.99"))) {
		System.out.println("Supplier: " + supplier.getPresentationName() + " at limit: " + supplier.getTotalAllocated().toFormatString());
	    }
	}
    }

}
