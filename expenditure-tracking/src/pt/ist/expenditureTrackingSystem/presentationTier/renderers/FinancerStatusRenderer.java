package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.fenixWebFramework.renderers.AbstractToolTipRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class FinancerStatusRenderer extends AbstractToolTipRenderer {

    private String onClass;
    private String offClass;
    private String state1;
    private String state2;
    private String state3;
    private String explanationMessage;
    private String bundle;

    public String getExplanationMessage() {
	return explanationMessage;
    }

    public void setExplanationMessage(String stateMessage) {
	this.explanationMessage = stateMessage;
    }

    public String getBundle() {
	return bundle;
    }

    public void setBundle(String bundle) {
	this.bundle = bundle;
    }

    public String getOnClass() {
	return onClass;
    }

    public void setOnClass(String onClass) {
	this.onClass = onClass;
    }

    public String getOffClass() {
	return offClass;
    }

    public void setOffClass(String offClass) {
	this.offClass = offClass;
    }

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

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new ToolTipLayout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		Financer financer = (Financer) object;
		HtmlBlockContainer container = new HtmlBlockContainer();

		HtmlInlineContainer inlineStatus1 = new HtmlInlineContainer();
		inlineStatus1.addChild(new HtmlText(getState1()));
		inlineStatus1.setClasses(financer.isApproved() ? getOnClass() : getOffClass());
		container.addChild(inlineStatus1);

		HtmlInlineContainer inlineStatus2 = new HtmlInlineContainer();
		inlineStatus2.addChild(new HtmlText(getState2()));
		inlineStatus2.setClasses(financer.isAuthorized() ? getOnClass() : getOffClass());
		container.addChild(inlineStatus2);

		HtmlInlineContainer inlineStatus3 = new HtmlInlineContainer();
		inlineStatus3.addChild(new HtmlText(getState3()));
		inlineStatus3.setClasses(financer.isWithInvoicesConfirmed() ? getOnClass() : getOffClass());
		container.addChild(inlineStatus3);

		return wrapUpCompletion(container, new HtmlText(RenderUtils.getResourceString(getBundle(),
			getExplanationMessage()), isEscape()));
	    }
	};
    }
}
