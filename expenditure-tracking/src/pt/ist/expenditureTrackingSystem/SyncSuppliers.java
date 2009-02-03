package pt.ist.expenditureTrackingSystem;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import myorg.domain.util.Address;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.persistenceTier.ExternalDbOperation;
import pt.ist.expenditureTrackingSystem.persistenceTier.ExternalDbQuery;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;

public class SyncSuppliers {

    static class GiafSupplier {
	private String codEnt;
	private String numFis;
	private String nom_ent;
	private String nom_ent_abv;

	private String ruaEnt = " ";
	private String locEnt = " ";
	private String codPos = " ";
	private String codPai = " ";
	private String telEnt = " ";
	private String faxEnt = " ";
	private String email = " ";

	GiafSupplier(final ResultSet resultSet) throws SQLException {
	    codEnt = get(resultSet, 1);
	    numFis = get(resultSet, 2);
	    nom_ent = get(resultSet, 3);
	    nom_ent_abv = get(resultSet, 4);
	}

	public void setContactInformation(final ResultSet resultSet) throws SQLException {
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

	@Override
	protected String getQueryString() {
	    return "SELECT GIDFORN.forn_cod_ent, GIDENTGER.num_fis, GIDENTGER.nom_ent, GIDENTGER.nom_ent_abv"
		+ " FROM GIDFORN, GIDENTGER where GIDFORN.forn_cod_ent = GIDENTGER.cod_ent";
	}

	@Override
	protected void processResultSet(final ResultSet resultSet) throws SQLException {
	    while (resultSet.next()) {
		final GiafSupplier giafSupplier = new GiafSupplier(resultSet);
		SupplierMap.index(giafSupplier);
	    }
	}
	
    }

    private static class SupplierContactQuery extends ExternalDbQuery {

	@Override
	protected String getQueryString() {
	    return "SELECT cod_ent, rua_ent, loc_ent, cod_pos, cod_pai, tel_ent, fax_ent, email FROM GIDMORADA";
	}

	@Override
	protected void processResultSet(final ResultSet resultSet) throws SQLException {
	    while (resultSet.next()) {
		final String codEnt = resultSet.getString(1);
		final GiafSupplier giafSupplier = findRemoteSupplier(codEnt);
		if (giafSupplier != null) {
		    giafSupplier.setContactInformation(resultSet);
		}
	    }
	}
	
	private GiafSupplier findRemoteSupplier(final String codEnt) {
	    for (final GiafSupplier giafSupplier : SupplierMap.getGiafSuppliers()) {
		if (giafSupplier.codEnt.equals(codEnt)) {
		    return giafSupplier;
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
	    final SupplierQuery supplierQuery = new SupplierQuery();
	    executeQuery(supplierQuery);

	    final SupplierContactQuery supplierContactQuery = new SupplierContactQuery();
	    executeQuery(supplierContactQuery);
	}	

    }

    public static void init() {
	String domainModelPath = "web/WEB-INF/classes/domain_model.dml";
	// TODO : reimplmenent as scheduled script
	//FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
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

	System.out.println("Read " + SupplierMap.giafCodEntMap.size() + " suppliers from giaf.");

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
