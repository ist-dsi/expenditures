package pt.ist.expenditureTrackingSystem;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import jvstm.TransactionalCommand;
import myorg._development.PropertiesManager;
import myorg.domain.MyOrg;
import myorg.domain.util.Address;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.organization.DeliveryInfo;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import dml.DomainClass;
import dml.DomainModel;
import dml.Role;
import dml.Slot;
import dml.ValueType;

public class EncodingFixer {

    private static class ResolutionCounter {

	private final Map<String, Integer> fixedSlots = new TreeMap<String, Integer>();

	public void count(final DomainClass domainClass, final String slotName) {
	    final String key = domainClass.getFullName() + "." + slotName;
	    if (!fixedSlots.containsKey(key)) {
		fixedSlots.put(key, Integer.valueOf(0));
	    }
	    final Integer count = fixedSlots.get(key);
	    fixedSlots.put(key, Integer.valueOf(count.intValue() + 1));
	}

	public void printResults() {
	    for (final Entry<String, Integer> entry : fixedSlots.entrySet()) {
		System.out.println(entry.getKey() + ": " + entry.getValue());
	    }
	}

    }

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
		    fix();
		} catch (final Throwable e) {
		    throw new Error(e);
		}
	    }

	});

	System.out.println("Done.");
    }

    private static void fix() throws UnsupportedEncodingException, SecurityException, IllegalArgumentException,
	    NoSuchMethodException, IllegalAccessException, InvocationTargetException, CharacterCodingException {
	final ResolutionCounter resolutionCounter = new ResolutionCounter();
	final MyOrg myOrg = MyOrg.getInstance();
	fixConnectedObjects(myOrg, resolutionCounter);
	final ExpenditureTrackingSystem expenditureTrackingSystem = myOrg.getExpenditureTrackingSystem();
	fixConnectedObjects(expenditureTrackingSystem, resolutionCounter);

	// final DomainModel domainModel = FenixWebFramework.getDomainModel();
	// for (final DomainClass domainClass : domainModel.getDomainClasses())
	// {
	// for (final Slot slot : domainClass.getSlotsList()) {
	// final ValueType valueType = slot.getSlotType();
	// if (valueType.getFullname().equals(String.class.getName())) {
	// System.out.println("Class: " + domainClass.getFullName() +
	// " has string slot: " + slot.getName());
	// }
	// }
	// }

	// for (final ProcessComment processComment :
	// MyOrg.getInstance().getExpenditureTrackingSystem().getCommentsSet())
	// {
	// final String comment = processComment.getComment();
	// if (comment != null && processComment.get**Id**Internal().intValue()
	// ==
	// 37804) {
	// final byte[] bytes = comment.getBytes("ISO8859-1");
	// final String piglet = new String(bytes);
	// System.out.println("Comment: " + comment);
	// System.out.println("Piglet : " + piglet);
	// if (piglet.length() < comment.length()) {
	// System.out.println("Process comment: " +
	// processComment.get**Id**Internal() +
	// " is busted... and it is ficable...");
	// }
	// }
	// if (comment != null && processComment.get**Id**Internal().intValue()
	// ==
	// 38003) {
	// final byte[] bytes = comment.getBytes("ISO8859-1");
	// final String piglet = new String(bytes);
	// System.out.println("Comment: " + comment);
	// System.out.println("Piglet : " + piglet);
	// if (piglet.length() < comment.length()) {
	// System.out.println("Process comment: " +
	// processComment.get**Id**Internal() +
	// " is not busted... and it will be f*cked up...");
	// } else {
	// System.out.println("All is ok");
	// }
	// }
	// }

	resolutionCounter.printResults();
    }

    @SuppressWarnings(value = { "unchecked" })
    private static void fixConnectedObjects(final AbstractDomainObject abstractDomainObject,
	    final ResolutionCounter resolutionCounter) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
	    IllegalAccessException, InvocationTargetException, UnsupportedEncodingException, CharacterCodingException {
	final DomainClass domainClass = findDomainClass(abstractDomainObject);
	for (final Role role : domainClass.getRoleSlotsList()) {
	    final String roleName = role.getName();
	    if (!roleName.equals("fileContents")) {
		final Object object = callGetter(abstractDomainObject, roleName);
		if (object instanceof Set) {
		    final Set<AbstractDomainObject> domainObjects = (Set<AbstractDomainObject>) object;
		    for (final AbstractDomainObject domainObject : domainObjects) {
			fix(domainObject, resolutionCounter);
		    }
		} else {
		    final AbstractDomainObject domainObject = (AbstractDomainObject) object;
		    fix(domainObject, resolutionCounter);
		}
	    }
	}
    }

    @SuppressWarnings(value = { "unchecked" })
    private static Object callGetter(final AbstractDomainObject abstractDomainObject, final String fieldName)
	    throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
	    InvocationTargetException {
	final Class clazz = abstractDomainObject.getClass();
	final Method method = clazz.getMethod("get" + StringUtils.capitalize(fieldName));
	return method.invoke(abstractDomainObject);
    }

    @SuppressWarnings(value = { "unchecked" })
    private static Object callSetter(final AbstractDomainObject abstractDomainObject, final String fieldName, final Object value)
	    throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
	    InvocationTargetException {
	final Class clazz = abstractDomainObject.getClass();
	final Method method = clazz.getMethod("set" + StringUtils.capitalize(fieldName), value.getClass());
	return method.invoke(abstractDomainObject, value);
    }

    private static DomainClass findDomainClass(final AbstractDomainObject abstractDomainObject) {
	final DomainModel domainModel = FenixWebFramework.getDomainModel();
	return domainModel.findClass(abstractDomainObject.getClass().getName());
    }

    private static void fix(final AbstractDomainObject domainObject, final ResolutionCounter resolutionCounter)
	    throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException,
	    InvocationTargetException, UnsupportedEncodingException, CharacterCodingException {
	final DomainClass domainClass = findDomainClass(domainObject);
	// if (domainObject.getExternalId() != 236223215292l) {
	// return;
	// }

	// [java]
	// pt.ist.expenditureTrackingSystem.domain.SavedSearch.searchName: 6
	// [java]
	// pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProposalDocument.proposalId:
	// 37
	// [java]
	// pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem.proposalReference:
	// 137
	// [java]
	// pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem.recipient:
	// 636
	// [java]
	// pt.ist.expenditureTrackingSystem.domain.organization.Person.name: 4

	fix(domainObject, resolutionCounter, domainClass);
    }

    private static boolean handleClass(final AbstractDomainObject domainObject) {
	// if (domainObject instanceof AcquisitionRequestItem) {
	// return true;
	// }
	// if (domainObject instanceof
	// pt.ist.expenditureTrackingSystem.domain.File) {
	// return true;
	// }
	// if (domainObject instanceof GenericFile) {
	// return true;
	// }
	// if (domainObject instanceof
	// pt.ist.expenditureTrackingSystem.domain.processes.GenericFile) {
	// return true;
	// }
	if (domainObject instanceof AcquisitionRequestItem) {
	    return true;
	}
	if (domainObject instanceof RequestItem) {
	    return true;
	}
	if (domainObject instanceof DeliveryInfo) {
	    return true;
	}
	if (domainObject instanceof Supplier) {
	    return true;
	}
	if (domainObject instanceof pt.ist.expenditureTrackingSystem.domain.File) {
	    return true;
	}
	return false;
	// return true;
    }

    private static void fix(final AbstractDomainObject domainObject, final ResolutionCounter resolutionCounter,
	    final DomainClass domainClass) throws SecurityException, IllegalArgumentException, NoSuchMethodException,
	    IllegalAccessException, InvocationTargetException, UnsupportedEncodingException, CharacterCodingException {
	if (handleClass(domainObject)) {
	    for (final Slot slot : domainClass.getSlotsList()) {
		fix(domainObject, resolutionCounter, domainClass, slot);
	    }
	}

	final DomainClass superClass = (DomainClass) domainClass.getSuperclass();
	if (superClass != null) {
	    fix(domainObject, resolutionCounter, superClass);
	}
    }

    private static void fix(final AbstractDomainObject domainObject, final ResolutionCounter resolutionCounter,
	    final DomainClass domainClass, final Slot slot) throws SecurityException, IllegalArgumentException,
	    NoSuchMethodException, IllegalAccessException, InvocationTargetException, UnsupportedEncodingException,
	    CharacterCodingException {
	final ValueType valueType = slot.getSlotType();
	final String slotName = slot.getName();
	if (valueType.getFullname().equals(String.class.getName())) {
	    // if (!slotName.equals("displayName")) {
	    // return;
	    // }
	    final String value = (String) callGetter(domainObject, slotName);
	    if (value != null) {
		// final byte[] bytes = value.getBytes("UTF-8");
		// final String piglet = new String(bytes, "ISO8859-1");
		final byte[] bytes = value.getBytes("ISO8859-1");
		// final String piglet = new String(bytes, "UTF-8");
		// final String piglet = deporkify(value, bytes);
		final String piglet = porkify(value, bytes);

		if (piglet.length() < value.length() && matchSizes(value, piglet)) {
		    System.out.println(domainObject.getClass().getSimpleName() + " . " + slotName);
		    System.out.println("Value : " + value);
		    System.out.println("Piglet: " + piglet);
		    System.out.println();

		    // System.out.println("Norm  : " +
		    // StringNormalizer.normalizePreservingCapitalizedLetters(value).replaceAll("AA",
		    // ""));
		    // System.out.println("Norm P: " +
		    // StringNormalizer.normalizePreservingCapitalizedLetters(piglet).replace("?",
		    // ""));

		    // x(value);
		    // System.exit(0);

		    // System.out.println("N Value: " +
		    // StringNormalizer.normalize(value));
		    // System.out.println("N Pig: " +
		    // StringNormalizer.normalize(piglet));
		    // System.out.println();
		    callSetter(domainObject, slotName, piglet);
		    resolutionCounter.count(domainClass, slotName);
		}
	    }
	} else if (valueType.getFullname().equals(Address.class.getName())) {
	    final Address address = (Address) callGetter(domainObject, slotName);
	    final Address newAddress = fixAddress(address, domainObject, slotName);
	    if (address != newAddress) {
		callSetter(domainObject, slotName, newAddress);
		resolutionCounter.count(domainClass, slotName);
	    }
	}
    }

    private static Address fixAddress(Address address, AbstractDomainObject domainObject, String slotName)
	    throws UnsupportedEncodingException, CharacterCodingException {
	if (address == null) {
	    return address;
	}
	final String line1 = address.getLine1();
	final String line2 = address.getLine2();
	final String location = address.getLocation();
	final String postalCode = address.getPostalCode();
	final String country = address.getCountry();

	final String fline1 = fixString(line1, domainObject, slotName);
	final String fline2 = fixString(line2, domainObject, slotName);
	final String flocation = fixString(location, domainObject, slotName);
	final String fpostalCode = fixString(postalCode, domainObject, slotName);
	final String fcountry = fixString(country, domainObject, slotName);

	return line1 != fline1 || line2 != fline2 || location != flocation || postalCode != fpostalCode || country != fcountry ? new Address(
		fline1, fline2, fpostalCode, flocation, fcountry)
		: address;
    }

    private static String fixString(final String value, final AbstractDomainObject domainObject, final String slotName)
	    throws UnsupportedEncodingException, CharacterCodingException {
	final byte[] bytes = value.getBytes("ISO8859-1");
	final String piglet = porkify(value, bytes);
	if (piglet.length() < value.length() && matchSizes(value, piglet)) {
	    System.out.println(domainObject.getClass().getSimpleName() + " . " + slotName);
	    System.out.println("Value : " + value);
	    System.out.println("Piglet: " + piglet);
	    System.out.println();
	    return piglet;
	}
	return value;
    }

    private static String porkifyInc(final String string, final String charSet1, final String charset2)
	    throws UnsupportedEncodingException, CharacterCodingException {
	final String aux = new String(string.getBytes(charSet1), charset2);
	if (isALittlePiggy(aux, string)) {
	    final String nextAux = new String(aux.getBytes(charSet1), charset2);
	    if (isALittlePiggy(nextAux, string, aux)) {
		return porkifyInc(aux, charSet1, charset2);
	    }
	    return aux;
	}
	return string;
    }

    private static String porkify(final String string, byte[] bytes) throws UnsupportedEncodingException,
	    CharacterCodingException {
	final String aux0 = new String(bytes, "UTF-8");
	// final String aux0 = porkifyInc(string, "ISO8859-1", "UTF-8");
	if (isALittlePiggy(string, aux0))
	    return aux0;

	final String aux1 = porkifyInc(string, "windows-1252", "UTF-8");
	return isALittlePiggy(aux1, string) ? aux1 : string;
    }

    private static boolean isALittlePiggy(final String value, final String... priors) throws CharacterCodingException {
	final Charset charset = Charset.forName("UTF-8");
	final ByteBuffer byteBuffer = ByteBuffer.wrap(value.getBytes());

	final CharBuffer charBuffer = charset.decode(byteBuffer);
	if (true) throw new Error("Check this...");
//	final CharsetDecoder charsetDecoder = ThreadLocalCoders.decoderFor(charset).onMalformedInput(CodingErrorAction.REPORT)
//		.onUnmappableCharacter(CodingErrorAction.REPORT);
//	final CharBuffer charBuffer = charsetDecoder.decode(byteBuffer);
//	if (charBuffer.toString().indexOf(charsetDecoder.replacement()) >= 0) {
//	    return false;
//	}

	final int ib = value.getBytes().length;
	final int il = value.length();
	for (final String prior : priors) {
	    if (ib >= prior.getBytes().length || il >= prior.getBytes().length) {
		return false;
	    }
	}
	return true;
    }

    private static boolean matchSizes(final String string1, final String string2) throws CharacterCodingException {
	final Charset charset = Charset.forName("UTF-8");
	final ByteBuffer byteBuffer = ByteBuffer.wrap(string2.getBytes());

	final CharBuffer charBuffer = charset.decode(byteBuffer);
//	final CharsetDecoder charsetDecoder = ThreadLocalCoders.decoderFor(charset).onMalformedInput(CodingErrorAction.REPORT)
//		.onUnmappableCharacter(CodingErrorAction.REPORT);
//	final CharBuffer charBuffer = charsetDecoder.decode(byteBuffer);
	// final CodingErrorAction codingErrorAction =
	// charsetDecoder.malformedInputAction();
	throw new Error("Check what should be done here...");
//	return charBuffer.toString().indexOf(charsetDecoder.replacement()) < 0;
    }

}
