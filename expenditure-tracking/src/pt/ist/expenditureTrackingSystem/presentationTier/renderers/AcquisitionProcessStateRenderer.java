package pt.ist.expenditureTrackingSystem.presentationTier.renderers;



import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class AcquisitionProcessStateRenderer extends OutputRenderer {

    private String classes;
    private String boxClasses;
    private String arrowClasses;
    private String completedStateClass;
    private String failedStateClass;
    private String currentStateClass;
    private String unreachableStateclass;

    @Override
    protected Layout getLayout(Object arg0, Class arg1) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object arg0, Class arg1) {
		AcquisitionProcess process = (AcquisitionProcess) arg0;
		return generateFlowChart(process.getAcquisitionProcessState().getAcquisitionProcessStateType());
	    }

	    private HtmlComponent generateFlowChart(AcquisitionProcessStateType currentState) {
		HtmlBlockContainer flowChartContainer = new HtmlBlockContainer();
		int i = 0;
		AcquisitionProcessStateType[] types = AcquisitionProcessStateType.values();
		for (AcquisitionProcessStateType stateType : types) {
		    flowChartContainer.addChild(generateBox(stateType, currentState));
		    if (++i < types.length) {
			HtmlBlockContainer arrowContainer = new HtmlBlockContainer();
			arrowContainer.setClasses(getArrowClasses());
			flowChartContainer.addChild(arrowContainer);
		    }
		}
		return flowChartContainer;
	    }

	    private HtmlComponent generateBox(AcquisitionProcessStateType stateType, AcquisitionProcessStateType currentState) {
		HtmlBlockContainer container = new HtmlBlockContainer();
		if (stateType == currentState) {
		    container.setClasses(getBoxClasses() + " " + getCurrentStateClass());
		} else {
		    if (stateType.ordinal() < currentState.ordinal()) {
			container.setClasses(getBoxClasses() +  " " + getCompletedStateClass()) ;
		    }
		    else {
			container.setClasses(getBoxClasses());
		    }
		}
		container.addChild(new HtmlText(RenderUtils.getEnumString(stateType)));
		return container;
	    }

	};

    }

    public String getClasses() {
	return classes;
    }

    public void setClasses(String classes) {
	this.classes = classes;
    }

    public String getBoxClasses() {
	return boxClasses;
    }

    public void setBoxClasses(String boxClasses) {
	this.boxClasses = boxClasses;
    }

    public String getArrowClasses() {
	return arrowClasses;
    }

    public void setArrowClasses(String arrowClasses) {
	this.arrowClasses = arrowClasses;
    }

    public String getCompletedStateClass() {
	return completedStateClass;
    }

    public void setCompletedStateClass(String completedStateClass) {
	this.completedStateClass = completedStateClass;
    }

    public String getFailedStateClass() {
	return failedStateClass;
    }

    public void setFailedStateClass(String failedStateClass) {
	this.failedStateClass = failedStateClass;
    }

    public String getCurrentStateClass() {
	return currentStateClass;
    }

    public void setCurrentStateClass(String currentStateClass) {
	this.currentStateClass = currentStateClass;
    }

    public String getUnreachableStateclass() {
	return unreachableStateclass;
    }

    public void setUnreachableStateclass(String unreachableStateclass) {
	this.unreachableStateclass = unreachableStateclass;
    }

}
