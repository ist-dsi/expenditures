package pt.ist.expenditureTrackingSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import jvstm.TransactionalCommand;
import myorg._development.PropertiesManager;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.OperationLog;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericLog;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class RevertProcessState {

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
		    revert();
		} catch (final Throwable e) {
		    throw new Error(e);
		}
	    }

	});

	System.out.println("Done.");
    }

    @Service
    private static void revert() {
	final String processToFix = "2009/1908";
	final int year = Integer.parseInt(processToFix.substring(0, 4));

	final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();
	for (final PaymentProcessYear paymentProcessYear : expenditureTrackingSystem.getPaymentProcessYearsSet()) {
	    if (paymentProcessYear.getYear().intValue() == year) {
		for (final PaymentProcess paymentProcess : paymentProcessYear.getPaymentProcessSet()) {
		    if (paymentProcess.getAcquisitionProcessId().equals(processToFix)) {
			System.out.println("Found it: " + paymentProcess.getExternalId() + " - "
				+ paymentProcess.getAcquisitionProcessId());
			revert(paymentProcess);
		    }
		}
	    }
	}
    }

    private static void revert(final PaymentProcess paymentProcess) {
	final DateTime expected = new DateTime(2009, 6, 9, 20, 18, 8, 0);

	final Set<GenericLog> genericLogs = paymentProcess.getExecutionLogsSet();
	final GenericLog genericLog = Collections.min(genericLogs, GenericLog.COMPARATOR_BY_WHEN_REVERSED);
	final OperationLog operationLog = (OperationLog) genericLog;
	final String operation = genericLog.getOperation();
	final DateTime when = genericLog.getWhenOperationWasRan();
	System.out.println("Operation: " + operation + " " + when.toString("yyyy-MM-dd HH:mm:ss"));
	if (operation.equals("FundAllocationExpirationDate") && when.equals(expected)) {
	    System.out.println("   Found log to delete.");
	    operationLog.delete();
	}

	final Set<ProcessState> processStates = paymentProcess.getProcessStatesSet();
	final ProcessState processState = Collections.max(processStates, ProcessState.COMPARATOR_BY_WHEN);
	final AcquisitionProcessState acquisitionProcessState = (AcquisitionProcessState) processState;
	if (acquisitionProcessState.getAcquisitionProcessStateType() == AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER
		&& acquisitionProcessState.getWhenDateTime().equals(expected)) {
	    System.out.println("Found process state to delete: "
		    + acquisitionProcessState.getAcquisitionProcessStateType().name());
	    acquisitionProcessState.removeProcess();
	    acquisitionProcessState.removeWho();
	    acquisitionProcessState.removeExpenditureTrackingSystem();
	    Transaction.deleteObject(acquisitionProcessState);
	}

	final RegularAcquisitionProcess regularAcquisitionProcess = (RegularAcquisitionProcess) paymentProcess;
	regularAcquisitionProcess.setFundAllocationExpirationDate(expected.plusDays(90).toLocalDate());
    }

}
