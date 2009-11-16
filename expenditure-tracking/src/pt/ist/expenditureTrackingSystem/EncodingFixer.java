package pt.ist.expenditureTrackingSystem;

import java.io.UnsupportedEncodingException;

public class EncodingFixer {

    private static String decode(final String value,final String resultEncoding)
    		throws UnsupportedEncodingException {
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

}
