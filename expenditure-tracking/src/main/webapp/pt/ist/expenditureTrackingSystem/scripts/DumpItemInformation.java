package pt.ist.expenditureTrackingSystem.script;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.joda.time.DateTime;

import module.workflow.domain.ActivityLog;
import module.workflow.domain.WorkflowLog;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.scheduler.WriteCustomTask;
import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.fenixframework.pstm.VersionNotAvailableException;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class DumpItemInformation extends WriteCustomTask {

    @Override
    protected void doService() {
	try {
	    VirtualHost.setVirtualHostForThread("dot.tecnico.ulisboa.pt");
	    doStuff();
	} finally {
	    VirtualHost.releaseVirtualHostFromThread();
	}
    }

    private void doStuff() {
	final String filename = "ItemInfo";
	final Spreadsheet spreadsheet = new Spreadsheet(filename);
	spreadsheet.setHeader("Tipo de Processo");
	spreadsheet.setHeader("Processo");
	spreadsheet.setHeader("Estado");
	spreadsheet.setHeader("Número");
	spreadsheet.setHeader("Descrição");
	spreadsheet.setHeader("CPV - Código");
	spreadsheet.setHeader("CPV - Descrição");
	spreadsheet.setHeader("Quantidade");
	spreadsheet.setHeader("IVA");
	spreadsheet.setHeader("Valor Unitário");
	spreadsheet.setHeader("Custos Adicionais");
	spreadsheet.setHeader("Classificação");
	spreadsheet.setHeader("Custo Total");
	spreadsheet.setHeader("Fornecedor - NIF");
	spreadsheet.setHeader("Fornecedor - Nome");
	spreadsheet.setHeader("Data Autorização");

	for (final PaymentProcessYear year : ExpenditureTrackingSystem.getInstance().getPaymentProcessYearsSet()) {
	    for (final PaymentProcess process : year.getPaymentProcessSet()) {
		final DateTime authorizationDate = getAuthorizationDate(process);
		if (year.getYear().intValue() == 2011
			|| year.getYear().intValue() == 2012
			|| (authorizationDate != null &&
				(authorizationDate.getYear() == 2011
					|| authorizationDate.getYear() == 2012))) {
		    if (process.isSimplifiedProcedureProcess()) {
			final SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) process;
			doStuff(spreadsheet, simplifiedProcedureProcess, authorizationDate);
		    } else if (process.isRefundProcess()) {
			final RefundProcess refundProcess = (RefundProcess) process;
		    }
		}
	    }
	}

	try {
	    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    spreadsheet.exportToXLSSheet(outputStream);
	    storeFileOutput(filename, filename + ".xls", outputStream.toByteArray(), "application/vnd.ms-excel");
	} catch (final IOException e) {
	    throw new Error(e);
	}
    }

    private DateTime getAuthorizationDate(final PaymentProcess process) {
	DateTime result = null;
	for (final WorkflowLog workflowLog : process.getExecutionLogsSet()) {
	    if (workflowLog instanceof ActivityLog) {
		final ActivityLog activityLog = (ActivityLog) workflowLog;
		final String operation = activityLog.getOperation();
		if (operation.equals("Authorize")
			|| operation.equals("AuthorizeActivity")) {
		    if (result == null || result.isBefore(activityLog.getWhenOperationWasRan())) {
			result = activityLog.getWhenOperationWasRan();
		    }
		}
	    }
	}
	return result;
    }

    private void doStuff(final Spreadsheet spreadsheet, final SimplifiedProcedureProcess process, final DateTime authorizationDate) {
	final AcquisitionProcessStateType stateType = process.getAcquisitionProcessStateType();
	final AcquisitionRequest request = process.getRequest();
	int i = 0;
	for (final AcquisitionRequestItem item : request.getOrderedRequestItemsSet()) {
	    final Row row = spreadsheet.addRow();
	    row.setCell(process.getLocalizedName());
	    row.setCell(process.getProcessNumber());
	    row.setCell(stateType.getLocalizedName());
	    row.setCell(++i);
	    row.setCell(item.getDescription());
	    row.setCell(item.getCPVReference().getCode());
	    row.setCell(item.getCPVReference().getDescription());
	    row.setCell(item.getCurrentQuantity());
	    row.setCell(item.getCurrentVatValue());
	    row.setCell(item.getCurrentUnitValue().toFormatString());
	    final Money additionalCostValue = item.getCurrentAdditionalCostValue();
	    row.setCell(additionalCostValue == null ? "0" : additionalCostValue.toFormatString());
	    row.setCell(item.getClassification().getLocalizedName());
	    row.setCell(item.getCurrentTotalItemValueWithAdditionalCostsAndVat().toFormatString());
	    final Supplier supplier = process.getRequest().getSupplier();
	    try {
		if (supplier == null) {
		    row.setCell(" ");
		    row.setCell(" ");
		} else {
		    supplier.getName();
		    supplier.getFiscalIdentificationCode();
		    row.setCell(supplier.getFiscalIdentificationCode());
		    row.setCell(supplier.getName());
		}
	    } catch (VersionNotAvailableException ex) {
		row.setCell(" ");
		row.setCell(" ");
	    }
	    if (authorizationDate == null) {
		row.setCell(" ");
	    } else {
		row.setCell(authorizationDate.toString("yyyy-MM-dd HH:mm"));
	    }
	}
    }

}
