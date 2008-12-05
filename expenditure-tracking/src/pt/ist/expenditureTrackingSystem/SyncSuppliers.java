package pt.ist.expenditureTrackingSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.expenditureTrackingSystem._development.PropertiesManager;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.util.Address;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class SyncSuppliers {

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

	public static GiafSupplier getGiafSupplierByDicalId(final String fiscalIdentificationCode) {
	    return giafFiscalIdMap.get(fiscalIdentificationCode);
	}

	public static GiafSupplier getGiafSupplierByGiafKey(final String giafKey) {
	    return giafCodEntMap.get(giafKey);
	}

	public static Collection<GiafSupplier> getGiafSuppliers() {
	    return giafCodEntMap.values();
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
	}
	System.out.println("Done.");
    }

    @Service
    public static void syncData() throws IOException {
	final String suppliersContents = FileUtils.readFile("suppliers.csv");
	for (final String line : suppliersContents.split("\n")) {
	    final GiafSupplier giafSupplier = new GiafSupplier(line);
	    SupplierMap.index(giafSupplier);
	}

	int matched = 0;
	int unmatched = 0;
	for (final Supplier supplier : ExpenditureTrackingSystem.getInstance().getSuppliersSet()) {
	    final String giafKey = supplier.getGiafKey();
	    if (giafKey == null) {
		final GiafSupplier giafSupplier = SupplierMap.getGiafSupplierByDicalId(supplier.getFiscalIdentificationCode());
		if (giafSupplier == null) {
		    unmatched++;
		    System.out.println("No giaf supplier found for: " + supplier.getFiscalIdentificationCode() + " - " + supplier.getName());
		} else {
		    matched++;
		    supplier.setGiafKey(giafSupplier.codEnt);
		}
	    } else {
		final GiafSupplier giafSupplier = SupplierMap.getGiafSupplierByGiafKey(giafKey);
		if (giafSupplier == null) {
		    unmatched++;
		    System.out.println("Holly shit, a supplier was deleted from giaf!");
		} else {
		    matched++;
		    // TODO : update information
		}
	    }
	}

	int created = 0;
	for (final GiafSupplier giafSupplier : SupplierMap.getGiafSuppliers()) {
	    Supplier supplier = findSupplierByGiafKey(giafSupplier.codEnt);
	    if (supplier == null) {
		final Address address = new Address(giafSupplier.ruaEnt, null, giafSupplier.codPos, giafSupplier.locEnt, "Portugal");
		supplier = new Supplier(giafSupplier.nom_ent, giafSupplier.nom_ent_abv, giafSupplier.numFis, address,
			giafSupplier.telEnt, giafSupplier.faxEnt, giafSupplier.email, null);
		supplier.setGiafKey(giafSupplier.codEnt);
		created++;
	    }
	}

	System.out.println("Matched: " + matched + " suppliers.");
	System.out.println("Did not match: " + unmatched + " suppliers.");
	System.out.println("Created: " + created + " suppliers.");
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

}
