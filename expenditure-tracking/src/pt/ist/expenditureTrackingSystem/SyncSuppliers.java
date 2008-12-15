package pt.ist.expenditureTrackingSystem;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.ist.expenditureTrackingSystem._development.PropertiesManager;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.util.Address;
import pt.ist.expenditureTrackingSystem.persistenceTier.ExternalDbOperation;
import pt.ist.expenditureTrackingSystem.persistenceTier.ExternalDbQuery;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class SyncSuppliers {

    private static class RemoteSupplierContact {

	private String codEnt;
	private String ruaEnt;
	private String locEnt;
	private String codPos;
	private String codPai;
	private String telEnt;
	private String faxEnt;
	private String email;

	public RemoteSupplierContact(final ResultSet resultSet) throws SQLException {
	    codEnt = get(resultSet, 1);
	    ruaEnt = get(resultSet, 2);
	    locEnt = get(resultSet, 3);
	    codPos = get(resultSet, 4);
	    codPai = get(resultSet, 5);
	    telEnt = get(resultSet, 6);
	    faxEnt = get(resultSet, 7);
	    email = get(resultSet, 8);
	}

	private String get(final ResultSet resultSet, final int i) throws SQLException {
	    final String string = resultSet.getString(i);
	    return string == null || string.length() == 0 ? " " : string.replace('\n', ' ').replace('\t', ' ');
	}

    }

    private static class RemoteSupplier {

	private String codEnt;
	private String numFis;
	private String nom_ent;
	private String nom_ent_abv;

	private Set<RemoteSupplierContact> remoteSupplierContacts = new HashSet<RemoteSupplierContact>();

	private RemoteSupplier(final ResultSet resultSet) throws SQLException {
	    codEnt = resultSet.getString(1);
	    numFis = resultSet.getString(2);
	    nom_ent = resultSet.getString(3);
	    nom_ent_abv = resultSet.getString(4);
	}

	private String get(final ResultSet resultSet, final int i) throws SQLException {
	    final String string = resultSet.getString(i);
	    return string == null || string.length() == 0 ? " " : string.replace('\n', ' ').replace('\t', ' ');
	}

	public RemoteSupplierContact getRemoteSupplierContact() {
	    if (remoteSupplierContacts.isEmpty()) {
		System.out.println("Supplier: " + codEnt + " has no contacts...");
		return null;
	    } else if (remoteSupplierContacts.size() == 1) {
		return remoteSupplierContacts.iterator().next();
	    } else {
		System.out.println("Supplier: " + codEnt + " has multiple contacts...");
		return remoteSupplierContacts.iterator().next();
	    }
	}

	public void registerContact(final ResultSet resultSetContact) throws SQLException {
	    remoteSupplierContacts.add(new RemoteSupplierContact(resultSetContact));
	}

    }

    private static class GiafSupplier {
	private String codEnt;
	private String numFis;
	private String nom_ent;
	private String nom_ent_abv;

	private String ruaEnt;
	private String locEnt;
	private String codPos;
	private String codPai;
	private String telEnt;
	private String faxEnt;
	private String email;

	private GiafSupplier(final String line) {
	    final String[] parts = split(line, '\t');
	    try {
	    codEnt = parts[0];
	    numFis = parts[1];
	    nom_ent = parts[2];
	    nom_ent_abv = parts[3];

	    ruaEnt = parts[4];
	    locEnt = parts[5];
	    codPos = parts[6];
	    codPai = parts[7];
	    telEnt = parts[8];
	    faxEnt = parts[9];
	    email = parts.length == 11 ? parts[10] : " ";
	    } catch (Exception ex) {
		System.out.println();
		System.out.println(parts.length);
		System.out.println(line);
		for (final String part : parts) {
		    System.out.println("   part: " + part);
		}
	    }
	}

	public GiafSupplier(final RemoteSupplier remoteSupplier) {
	    codEnt = remoteSupplier.codEnt;
	    numFis = remoteSupplier.numFis;
	    nom_ent = remoteSupplier.nom_ent;
	    nom_ent_abv = remoteSupplier.nom_ent_abv;

	    ruaEnt = remoteSupplier.getRemoteSupplierContact().ruaEnt;
	    locEnt = remoteSupplier.getRemoteSupplierContact().locEnt;
	    codPos = remoteSupplier.getRemoteSupplierContact().codPos;
	    codPai = remoteSupplier.getRemoteSupplierContact().codPai;
	    telEnt = remoteSupplier.getRemoteSupplierContact().telEnt;
	    faxEnt = remoteSupplier.getRemoteSupplierContact().faxEnt;
	    email = remoteSupplier.getRemoteSupplierContact().email;
	}

	private String[] split(final String line, final char c) {
	    final List<String> parts = new ArrayList<String>();
	    for (int offset = line.indexOf(c), prev = 0; offset >= 0; ) {
		parts.add(line.substring(prev, offset));
		prev = offset + 1;
		offset = line.indexOf(c, prev);
	    }
	    final String[] result = new String[parts.size()];
	    for (int i = 0; i < result.length; i++) {
		result[i] = parts.get(i);
	    }
	    return result;
	}
    }

    private static class SupplierMap {

	private static Map<String, GiafSupplier> giafCodEntMap = new HashMap<String, GiafSupplier>();
	private static Map<String, GiafSupplier> giafFiscalIdMap = new HashMap<String, GiafSupplier>();

	public static void index(final GiafSupplier giafSupplier) {
	    giafCodEntMap.put(giafSupplier.codEnt, giafSupplier);
	    giafFiscalIdMap.put(giafSupplier.numFis, giafSupplier);
	}

	public static GiafSupplier getGiafSupplierByFiscalId(final String fiscalIdentificationCode) {
	    return giafFiscalIdMap.get(fiscalIdentificationCode);
	}

	public static GiafSupplier getGiafSupplierByGiafKey(final String giafKey) {
	    return giafCodEntMap.get(giafKey);
	}

	public static Collection<GiafSupplier> getGiafSuppliers() {
	    return giafCodEntMap.values();
	}
    }

    private static class SupplierQuery extends ExternalDbQuery {

	final Set<RemoteSupplier> remoteSuppliers;

	private SupplierQuery(final Set<RemoteSupplier> remoteSuppliers) {
	    this.remoteSuppliers = remoteSuppliers;
	}

	@Override
	protected String getQueryString() {
	    return "SELECT GIDFORN.forn_cod_ent, GIDENTGER.num_fis, GIDENTGER.nom_ent, GIDENTGER.nom_ent_abv"
		+ " FROM GIDFORN, GIDENTGER where GIDFORN.forn_cod_ent = GIDENTGER.cod_ent";
	}

	@Override
	protected void processResultSet(final ResultSet resultSet) throws SQLException {
	    final RemoteSupplier remoteSupplier = new RemoteSupplier(resultSet);
		remoteSuppliers.add(remoteSupplier);
	}
	
    }

    private static class SupplierContactQuery extends ExternalDbQuery {

	final Set<RemoteSupplier> remoteSuppliers;

	private SupplierContactQuery(final Set<RemoteSupplier> remoteSuppliers) {
	    this.remoteSuppliers = remoteSuppliers;
	}

	@Override
	protected String getQueryString() {
	    return "SELECT cod_ent, rua_ent, loc_ent, cod_pos, cod_pai, tel_ent, fax_ent, email FROM GIDMORADA";
	}

	@Override
	protected void processResultSet(final ResultSet resultSet) throws SQLException {
	    final RemoteSupplierContact remoteSupplierContact = new RemoteSupplierContact(resultSet);
	    final RemoteSupplier remoteSupplier = findRemoteSupplier(remoteSupplierContact.codEnt);
	    if (remoteSupplier != null) {
		remoteSupplier.remoteSupplierContacts.add(remoteSupplierContact);
	    }
	}
	
	private RemoteSupplier findRemoteSupplier(final String codEnt) {
	    for (final RemoteSupplier remoteSupplier : remoteSuppliers) {
		if (remoteSupplier.codEnt.equalsIgnoreCase(codEnt)) {
		    return remoteSupplier;
		}
	    }
	    return null;
	}
    }

    private static class SupplierReader extends ExternalDbOperation {

	@Override
	protected String getDbPropertyPrefix() {
	    return "db.giaf";
	}

	@Override
	protected void doOperation() throws SQLException {
	    final Set<RemoteSupplier> remoteSuppliers = new HashSet<RemoteSupplier>();

	    final SupplierQuery supplierQuery = new SupplierQuery(remoteSuppliers);
	    executeQuery(supplierQuery);

	    final SupplierContactQuery supplierContactQuery = new SupplierContactQuery(remoteSuppliers);
	    executeQuery(supplierContactQuery);

	    for (final RemoteSupplier remoteSupplier : remoteSuppliers) {
		final GiafSupplier giafSupplier = new GiafSupplier(remoteSupplier);
		SupplierMap.index(giafSupplier);
	    }
	}	

    }

    public static void init() {
	String domainModelPath = "web/WEB-INF/classes/domain_model.dml";
	FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
	ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());
    }

    public static void main(String[] args) {
	init();
	try {
	    syncData();
	} catch (final IOException e) {
	    throw new Error(e);
	} catch (final SQLException e) {
	    throw new Error(e);
	}
	System.out.println("Done.");
    }

    @Service
    public static void syncData() throws IOException, SQLException {
	final SupplierReader supplierReader = new SupplierReader();
	supplierReader.execute();

	System.out.println("Read " + SupplierMap.giafCodEntMap.size() + " suppliers.");

	if (true) {
	    return;
	}

	final String suppliersContents = FileUtils.readFile("suppliers.csv");
	for (final String line : suppliersContents.split("\n")) {
	    final GiafSupplier giafSupplier = new GiafSupplier(line);
	    SupplierMap.index(giafSupplier);
	}

	int matched = 0;
	int created = 0;
	for (final GiafSupplier giafSupplier : SupplierMap.getGiafSuppliers()) {
	    Supplier supplier = findSupplierByGiafKey(giafSupplier.codEnt);
	    if (supplier == null) {
		final String country = getCountry(giafSupplier);
		final Address address = new Address(giafSupplier.ruaEnt, null, giafSupplier.codPos, giafSupplier.locEnt, country);
		supplier = new Supplier(giafSupplier.nom_ent, giafSupplier.nom_ent_abv, giafSupplier.numFis, address,
			giafSupplier.telEnt, giafSupplier.faxEnt, giafSupplier.email, null);
		supplier.setGiafKey(giafSupplier.codEnt);
		created++;
	    } else {
		matched++;
		updateSupplierInformation(supplier, giafSupplier);
	    }
	}

	System.out.println("Matched: " + matched + " suppliers.");
	System.out.println("Created: " + created + " suppliers.");
    }

    private static void updateSupplierInformation(final Supplier supplier, final GiafSupplier giafSupplier) {
	final String country = getCountry(giafSupplier);
	supplier.setFiscalIdentificationCode(giafSupplier.numFis);
	supplier.setName(giafSupplier.nom_ent);
	supplier.setAbbreviatedName(giafSupplier.nom_ent_abv);
	supplier.setPhone(giafSupplier.telEnt);
	supplier.setFax(giafSupplier.faxEnt);
	supplier.setEmail(giafSupplier.email);
	final Address address = new Address(giafSupplier.ruaEnt, null, giafSupplier.codPos, giafSupplier.locEnt, country);
	supplier.setAddress(address);
    }

    private static Supplier findSupplierByGiafKey(final String codEnt) {
	for (final Supplier supplier : ExpenditureTrackingSystem.getInstance().getSuppliersSet()) {
	    final String giafKey = supplier.getGiafKey();
	    if (codEnt.equals(giafKey)) {
		return supplier;
	    }
	}
	return null;
    }

    private static String getCountry(GiafSupplier giafSupplier) {
	return giafSupplier.codPai != null && giafSupplier.codPai.equals("P") ? "Portugal" : "?";
    }

}
