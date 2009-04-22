package pt.ist.expenditureTrackingSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.DateTime;

import myorg._development.PropertiesManager;
import myorg.domain.MyOrg;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.ProcessComment;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class MarkCommentsAsRead {
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
	mark();
	System.out.println("Done.");
    }

    private static void mark() {
	DateTime now = new DateTime();
	DateTime timeStamp = now.minusDays(7);
	for (Person person : ExpenditureTrackingSystem.getInstance().getPeople()) {
	    Set<SimplifiedProcedureProcess> processesWhereUserWasInvolved = person
		    .getProcessesWhereUserWasInvolved(SimplifiedProcedureProcess.class);
	    for (SimplifiedProcedureProcess process : processesWhereUserWasInvolved) {
		List<ProcessComment> unreadCommentsForPerson = process.getUnreadCommentsForPerson(person);
		if (!unreadCommentsForPerson.isEmpty()) {
		    Collections.sort(unreadCommentsForPerson, new ReverseComparator(new BeanComparator("date")));
		    if (unreadCommentsForPerson.get(0).getDate().isBefore(timeStamp)) {
			process.markCommentsAsReadForPerson(person);
		    }
		}
	    }
	}

    }
}
