package pt.ist.expenditureTrackingSystem;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jvstm.TransactionalCommand;
import module.fileSupport.domain.GenericFile;
import myorg._development.PropertiesManager;
import myorg.domain.MyOrg;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class EncodingFixer {

    private static void init() {
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
		    fix();
		} catch (final Throwable e) {
		    throw new Error(e);
		}
	    }

	});

	System.out.println("Done.");
    }

    private static String decode(final String value, final String resultEncoding) throws UnsupportedEncodingException {
	final byte[] bytes = value.getBytes(resultEncoding);
	return new String(bytes);
    }

    public static String fix(final String value) throws UnsupportedEncodingException {
	final int nb = value.getBytes().length;

	final String decode1 = decode(value, "UTF-8");
	final String decode2 = decode(value, "ISO8859-1");
	final String decode3 = decode(value, "windows-1252");

	final int l = value.length();
	final int l1 = decode1.length();
	final int l2 = decode2.length();
	final int l3 = decode3.length();

	if (l < l1 && l < l2) {
	    return value;
	} else if (decode1.getBytes().length <= nb && l1 <= l2 && l1 <= l3 && isValid(decode1)) {
	    return decode1;
	} else if (decode2.getBytes().length <= nb && l2 <= l1 && l2 <= l3 && isValid(decode2)) {
	    return decode2;
	} else if (decode3.getBytes().length <= nb && l3 <= l1 && l3 <= l2 && isValid(decode3)) {
	    return decode3;
	}
	return null;
    }

    private static boolean isValid(final String string) {
	for (final char c : string.toCharArray()) {
	    if (((int) c) == 65533) {
		return false;
	    }
	}
	return true;
    }

    public static void fix() throws UnsupportedEncodingException, SecurityException, IllegalArgumentException,
	    NoSuchMethodException, IllegalAccessException, InvocationTargetException, CharacterCodingException {

	for (final GenericFile genericFile : MyOrg.getInstance().getGenericFilesSet()) {
	    final String displayName = genericFile.getDisplayName();
	    if (displayName != null) {
		final String fix = fix(displayName);
		if (fix != null && !displayName.equals(fix)) {
		    System.out.println("Original    : " + displayName);
		    System.out.println("Possible fix: " + fix);
		    System.out.println();
		    genericFile.setDisplayName(fix);
		}
	    }
	}
    }

}
