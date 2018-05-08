package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.workflow.domain.ProcessFile;
import module.workflow.domain.ProcessFileSignatureHandler;
import pt.ist.expenditureTrackingSystem._development.Bundle;

public abstract class DocumentSignHandler<T extends ProcessFile> extends ProcessFileSignatureHandler<T> {

    DocumentSignHandler(final T processFile) {
        super(processFile);
    }

    @Override
    public String filename() {
        return processFile.getFilename();
    }

    @Override
    public String title() {
        return BundleUtil.getString(Bundle.EXPENDITURE, "label." + processFile.getClass().getName()) 
                + " - " + processFile.getProcess().getProcessNumber();
    }

}
