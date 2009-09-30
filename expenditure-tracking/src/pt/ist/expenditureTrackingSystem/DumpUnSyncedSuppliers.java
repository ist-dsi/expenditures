package pt.ist.expenditureTrackingSystem;

import java.io.IOException;
import java.io.PrintWriter;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.fenixWebFramework.services.Service;

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

    @Service
    public static void dump() throws IOException {
	final PrintWriter printWriter = new PrintWriter("/tmp/fornecedores.csv");
	for (final Supplier supplier : ExpenditureTrackingSystem.getInstance().getSuppliersSet()) {
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
