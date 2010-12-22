package pt.ist.expenditureTrackingSystem.domain;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.metadata.SimplifiedProcedureProcessMetaData;
import pt.ist.fenixWebFramework.services.Service;

public class SimplifiedProcedureProcessSignature extends SimplifiedProcedureProcessSignature_Base {

    @Service
    public static SimplifiedProcedureProcessSignature factory(SimplifiedProcedureProcess process) {
	SimplifiedProcedureProcessSignature signatureProcess = new SimplifiedProcedureProcessSignature();
	signatureProcess.init(process);

	return signatureProcess;
    }

    protected SimplifiedProcedureProcessSignature() {
	super();
    }

    protected void init(SimplifiedProcedureProcess process) {
	super.init(process);
    }

    @Override
    public SimplifiedProcedureProcess getProcess() {
	return fromExternalId(getSignObjectId());
    }

    @Override
    public SimplifiedProcedureProcessMetaData getMetaData() {
	return new SimplifiedProcedureProcessMetaData(getProcess());
    }

    @Override
    public String getDescription() {
	return getType() + " (" + getProcess().getProcessNumber() + ")";
    }

    @Override
    public String getType() {
	return "Processos de Aquisição";
    }
}
