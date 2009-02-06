package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class RefundProcessStateRenderer extends ProcessStateRenderer<RefundProcess> {

    private class RefundProcessStateLayout extends ProcessStateLayout<RefundProcess> {

	@Override
	protected HtmlComponent generateFlowChart(final HtmlBlockContainer flowChartContainer, final RefundProcess process) {
	    final RefundProcessStateType currentState = process.getProcessState().getRefundProcessStateType();
	    if (process.isActive()) {
		final RefundProcessStateType[] types = RefundProcessStateType.values();
		for (final RefundProcessStateType stateType : types) {
		    if (stateType.showFor(currentState)) {
			generateStateBox(process, currentState, flowChartContainer, stateType);
			if (stateType.hasNextState()) {
			    generateArrowBox(flowChartContainer);
			}
		    }
		}
	    } else {
		generateStateBox(process, currentState, flowChartContainer, currentState);
	    }
	    return flowChartContainer;
	}

	private void generateStateBox(final RefundProcess process, final RefundProcessStateType currentState,
		final HtmlBlockContainer flowChartContainer, final RefundProcessStateType stateType) {
	    flowChartContainer.addChild(generateBox(process, stateType, currentState));
	}

	private HtmlComponent generateBox(final RefundProcess process, final RefundProcessStateType stateType,
		final RefundProcessStateType currentStateType) {
	    final HtmlBlockContainer container = new HtmlBlockContainer();

	    String classes = getBoxClasses();
	    if (stateType.isBlocked(currentStateType)) {
		classes += " " + getFailedStateClass();
	    } else if (stateType.isCompleted(currentStateType)) {
		classes += " " + getCompletedStateClass();
	    }
	    container.setClasses(classes);

	    container.addChild(getBody(process, stateType));
	    return container;
	}

	private HtmlComponent getBody(final RefundProcess process, final RefundProcessStateType stateType) {
	    return getBoxBody(getUrl(), stateType.getLocalizedName(), getStateParameterName(), process);
	}
    }

    @Override
    protected Layout getLayout(final Object arg0, final Class arg1) {
	return new RefundProcessStateLayout();
    }

}
