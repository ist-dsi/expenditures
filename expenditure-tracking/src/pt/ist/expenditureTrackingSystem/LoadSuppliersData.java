package pt.ist.expenditureTrackingSystem;

import java.io.IOException;
import java.util.HashMap;

import pt.ist.expenditureTrackingSystem._development.PropertiesManager;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.util.Address;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class LoadSuppliersData {

    public static void init() {
	String domainModelPath = "web/WEB-INF/classes/domain_model.dml";
	FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
	ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());
    }

    public static void main(String[] args) {
	init();
	try {
	    loadData();
	} catch (final IOException e) {
	    throw new Error(e);
	}
	System.out.println("Done.");
    }

    public static void loadData() throws IOException {
	final String suppliersContents = FileUtils.readFile("fornecedores.csv");
	final String contactsContents = FileUtils.readFile("contactos.csv");
	final FenixSuppliersMap fenixSupplierMap = new FenixSuppliersMap(suppliersContents);
	final FenixContactsMap fenixContactsMap = new FenixContactsMap(contactsContents);
	createSuppliers(fenixSupplierMap, fenixContactsMap);

    }

    @Service
    private static void createSuppliers(final FenixSuppliersMap fenixSupplierMap, final FenixContactsMap fenixContactsMap) {
	for (final FenixSupplier fenixSupplier : fenixSupplierMap.values()) {
	    FenixSupplierContacts fenixSupplierContacts = fenixContactsMap.get(fenixSupplier.getSupplierID());
	    if (fenixSupplierContacts == null) {
		new Supplier(fenixSupplier.getName(), fenixSupplier.getAbbreviatedName(), fenixSupplier
			.getFiscalIdentificationCode(), null, null, null, null);
	    } else {
		new Supplier(fenixSupplier.getName(), fenixSupplier.getAbbreviatedName(), fenixSupplier
			.getFiscalIdentificationCode(), getAddress(fenixSupplierContacts), fenixSupplierContacts.getTelefone(),
			fenixSupplierContacts.getFax(), fenixSupplierContacts.getEmail());
	    }
	}

    }

    private static Address getAddress(FenixSupplierContacts fenixSupplierContacts) {
	return new Address(fenixSupplierContacts.getMorada().isEmpty() ? " " : fenixSupplierContacts.getMorada(), null,
		fenixSupplierContacts.getCodigoPostal().isEmpty() ? " " : fenixSupplierContacts.getCodigoPostal(),
		fenixSupplierContacts.getLocalidade().isEmpty() ? " " : fenixSupplierContacts.getLocalidade(), "Portugal");
    }

    private static class FenixSupplier {
	private String supplierID;
	private String name;
	private String abbreviatedName;
	private String fiscalIdentificationCode;

	public FenixSupplier(final String contents) {
	    String[] split = contents.split("\t");
	    if (split[3] != null && !split[3].trim().isEmpty() && split[4].trim().equals("P")) {
		supplierID = split[0].trim();
		name = split[1].trim();
		abbreviatedName = split[2].trim();
		fiscalIdentificationCode = split[3].trim();
	    } else {
		throw new RuntimeException();
	    }
	}

	public String getSupplierID() {
	    return supplierID;
	}

	public String getName() {
	    return name;
	}

	public String getAbbreviatedName() {
	    return abbreviatedName;
	}

	public String getFiscalIdentificationCode() {
	    return fiscalIdentificationCode;
	}

    }

    private static class FenixSuppliersMap extends HashMap<String, FenixSupplier> {

	public FenixSuppliersMap(final String contents) {
	    for (final String line : contents.split("\n")) {
		add(line);
	    }
	}

	private void add(final String line) {
	    try {
		final FenixSupplier fenixSupplier = new FenixSupplier(line);
		put(fenixSupplier.getSupplierID(), fenixSupplier);
	    } catch (Exception e) {
	    }
	}
    }

    private static class FenixSupplierContacts {
	private String supplierID;
	private String morada;
	private String localidade;
	private String codigoPostal;
	private String telefone;
	private String fax;
	private String email;

	public FenixSupplierContacts(final String line) {
	    String[] split = line.split("\t");
	    supplierID = split[0].trim();
	    morada = split[1].trim();
	    localidade = split[2].trim();
	    codigoPostal = split[3].trim();
	    telefone = split[4].trim();
	    fax = split[5].trim();
	    email = split[6].trim();
	}

	public String getSupplierID() {
	    return supplierID;
	}

	public String getTelefone() {
	    return telefone.isEmpty() ? null : telefone;
	}

	public String getFax() {
	    return fax.isEmpty() ? null : fax;
	}

	public String getEmail() {
	    return email.isEmpty() ? null : email;
	}

	public String getMorada() {
	    return morada;
	}

	public String getLocalidade() {
	    return localidade;
	}

	public String getCodigoPostal() {
	    return codigoPostal;
	}

    }

    private static class FenixContactsMap extends HashMap<String, FenixSupplierContacts> {
	public FenixContactsMap(final String contents) {
	    for (String line : contents.split("\n")) {
		add(line);
	    }
	}

	private void add(final String line) {
	    final FenixSupplierContacts fenixSupplierContacts = new FenixSupplierContacts(line);
	    put(fenixSupplierContacts.getSupplierID(), fenixSupplierContacts);
	}
    }

}
