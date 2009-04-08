package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class FundAllocationStatus extends OutputRenderer {

    private String onClass;
    private String offClass;
    private String state1;
    private String state2;
    private String state3;
    private String state4;

    public String getState1() {
	return state1;
    }

    public void setState1(String state1) {
	this.state1 = state1;
    }

    public String getState2() {
	return state2;
    }

    public void setState2(String state2) {
	this.state2 = state2;
    }

    public String getState3() {
	return state3;
    }

    public void setState3(String state3) {
	this.state3 = state3;
    }

    public String getState4() {
	return state4;
    }

    public void setState4(String start4) {
	this.state4 = start4;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		AcquisitionRequest request = (AcquisitionRequest) object;
		HtmlBlockContainer container = new HtmlBlockContainer();

		boolean hasProjectFinancers = request.hasAnyProjectFinancers();

		if (hasProjectFinancers) {
		    HtmlInlineContainer inlineStatus1 = new HtmlInlineContainer();
		    inlineStatus1.addChild(new HtmlText(getState1()));
		    inlineStatus1.setClasses(request.hasAllocatedFundsForAllProjectFinancers() ? getOnClass() : getOffClass());
		    container.addChild(inlineStatus1);
		}
		HtmlInlineContainer inlineStatus2 = new HtmlInlineContainer();
		inlineStatus2.addChild(new HtmlText(getState2()));
		inlineStatus2.setClasses(request.hasAllFundAllocationId() ? getOnClass() : getOffClass());
		container.addChild(inlineStatus2);

		if (hasProjectFinancers) {
		    HtmlInlineContainer inlineStatus3 = new HtmlInlineContainer();
		    inlineStatus3.addChild(new HtmlText(getState3()));
		    inlineStatus3.setClasses(request.hasAllocatedFundsPermanentlyForAllProjectFinancers() ? getOnClass()
			    : getOffClass());
		    container.addChild(inlineStatus3);
		}
		HtmlInlineContainer inlineStatus4 = new HtmlInlineContainer();
		inlineStatus4.addChild(new HtmlText(getState4()));
		inlineStatus4.setClasses(request.hasAllEffectiveFundAllocationId() ? getOnClass() : getOffClass());
		container.addChild(inlineStatus4);
		return container;
	    }

	};
    }

    public void setOnClass(String onClass) {
	this.onClass = onClass;
    }

    public String getOnClass() {
	return onClass;
    }

    public void setOffClass(String offClass) {
	this.offClass = offClass;
    }

    public String getOffClass() {
	return offClass;
    }

}
