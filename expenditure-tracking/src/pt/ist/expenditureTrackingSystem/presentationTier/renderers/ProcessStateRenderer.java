package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class ProcessStateRenderer<T extends GenericProcess> extends OutputRenderer {

    protected abstract class ProcessStateLayout<T extends GenericProcess> extends Layout {

	@Override
	public HtmlComponent createComponent(Object arg0, Class arg1) {
	    final T process = (T) arg0;
	    final HtmlBlockContainer flowChartContainer = new HtmlBlockContainer();
	    return generateFlowChart(flowChartContainer, process);
	}

	protected abstract HtmlComponent generateFlowChart(final HtmlBlockContainer flowChartContainer, final T process);

	protected void generateActivityBox(final HtmlBlockContainer flowChartContainer, final String name) {
	    flowChartContainer.addChild(generateBox(name));
	}

	protected void generateArrowBox(final HtmlBlockContainer flowChartContainer) {
	    final HtmlBlockContainer arrowContainer = new HtmlBlockContainer();
	    arrowContainer.setClasses(getArrowClasses());
	    flowChartContainer.addChild(arrowContainer);
	}

	protected HtmlComponent generateBox(final String activity) {
	    final HtmlBlockContainer container = new HtmlBlockContainer();
	    container.setClasses(getBoxClasses());
	    container.addChild(new HtmlText(activity, false));
	    return container;
	}

	protected HtmlComponent getBoxBody(final String url, final String text, final String parameterValue, final T process) {
	    final HtmlComponent htmlComponent = new HtmlText(text, false);
	    if (url == null || !isLinkable()) {
		return htmlComponent;
	    } else {
		final HtmlLink link = new HtmlLink();
		link.setBody(htmlComponent);
		link.setModuleRelative(isModuleRelative());
		link.setContextRelative(isContextRelative());
		final StringBuilder stringBuilder = new StringBuilder(RenderUtils.getFormattedProperties(url, process));
		stringBuilder.append("&");
		stringBuilder.append(getStateParameterName());
		stringBuilder.append("=");
		stringBuilder.append(parameterValue);
		link.setUrl(stringBuilder.toString());
		return link;
	    }
	}

    }

    private boolean isLinkable;
    private String classes;
    private String boxClasses;
    private String arrowClasses;
    private String completedStateClass;
    private String failedStateClass;
    private String unreachableStateclass;

    private String url;

    private boolean moduleRelative;
    private boolean contextRelative;

    private String stateParameterName;

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

    public String getUnreachableStateclass() {
	return unreachableStateclass;
    }

    public void setUnreachableStateclass(String unreachableStateclass) {
	this.unreachableStateclass = unreachableStateclass;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String logUrl) {
	this.url = logUrl;
    }

    public boolean isModuleRelative() {
	return moduleRelative;
    }

    public void setModuleRelative(boolean moduleRelative) {
	this.moduleRelative = moduleRelative;
    }

    public boolean isContextRelative() {
	return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
	this.contextRelative = contextRelative;
    }

    public String getStateParameterName() {
	return stateParameterName;
    }

    public void setStateParameterName(String stateParameterName) {
	this.stateParameterName = stateParameterName;
    }

    public boolean isLinkable() {
	return isLinkable;
    }

    public void setLinkable(boolean isLinkable) {
	this.isLinkable = isLinkable;
    }

}
