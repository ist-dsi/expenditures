package pt.ist.expenditureTrackingSystem.domain.acquisitions.search;

import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum SearchProcessValues implements IPresentableEnum {

    RS5000(SimplifiedProcedureProcess.class, ProcessClassification.CCP),
    CT1000(SimplifiedProcedureProcess.class, ProcessClassification.CT10000),
    CT75000(SimplifiedProcedureProcess.class, ProcessClassification.CT75000),
    ACQUISITIONS(SimplifiedProcedureProcess.class, null),
    REFUND(RefundProcess.class, null);

    private Class<? extends PaymentProcess> searchClass;
    private ProcessClassification searchClassification;

    private SearchProcessValues(Class<? extends PaymentProcess> searchClass, ProcessClassification searchClassification) {
	this.searchClass = searchClass;
	this.searchClassification = searchClassification;
    }

    @Override
    public String getLocalizedName() {
	return (searchClassification != null) ? searchClassification.getLocalizedName() :
	    BundleUtil.getStringFromResourceBundle("resources/ExpenditureResources",
		    "label.search." + searchClass.getSimpleName() + ".description");
    }

    public Class<? extends PaymentProcess> getSearchClass() {
	return searchClass;
    }

    public ProcessClassification getSearchClassification() {
	return searchClassification;
    }

}
