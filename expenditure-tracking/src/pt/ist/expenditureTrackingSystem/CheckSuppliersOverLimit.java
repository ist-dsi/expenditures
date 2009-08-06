package pt.ist.expenditureTrackingSystem;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jvstm.TransactionalCommand;
import myorg._development.PropertiesManager;
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
    }

    public static void main(String[] args) {
	init();
	Transaction.withTransaction(false, new TransactionalCommand() {
	    @Override
	    public void doIt() {
		try {
		    checkSupplierLimits();
		} catch (final UnsupportedEncodingException e) {
		    throw new Error(e);
		}
	    }

	});

	System.out.println("Done.");
    }

    private static void checkSupplierLimits() throws UnsupportedEncodingException {
	// for (final ProcessComment processComment :
	// MyOrg.getInstance().getExpenditureTrackingSystem().getCommentsSet())
	// {
	// final String comment = processComment.getComment();
	// if (comment != null && processComment.get**Id**Internal().intValue()
	// == 37804) {
	// final byte[] bytes = comment.getBytes("ISO8859-1");
	// final String piglet = new String(bytes);
	// System.out.println("Comment: " + comment);
	// System.out.println("Piglet : " + piglet);
	// if (piglet.length() < comment.length()) {
	// System.out.println("Process comment: " +
	// processComment.getExternalId() +
	// " is busted... and it is ficable...");
	// }
	// }
	// if (comment != null && processComment.get**Id**Internal().intValue()
	// == 38003) {
	// final byte[] bytes = comment.getBytes("ISO8859-1");
	// final String piglet = new String(bytes);
	// System.out.println("Comment: " + comment);
	// System.out.println("Piglet : " + piglet);
	// if (piglet.length() < comment.length()) {
	// System.out.println("Process comment: " +
	// processComment.getExternalId() +
	// " is not busted... and it will be f*cked up...");
	// } else {
	// System.out.println("All is ok");
	// }
	// }
	// }
    }

}
