package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.OperationLog;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class AcquisitionProcessStateRenderer extends ProcessStateRenderer<RegularAcquisitionProcess> {

    private class AcquisitionProcessStateLayout extends ProcessStateLayout<RegularAcquisitionProcess> {

	@Override
	protected HtmlComponent generateFlowChart(final HtmlBlockContainer flowChartContainer, final RegularAcquisitionProcess process) {
	    final AcquisitionProcessStateType currentState = process.getAcquisitionProcessStateType();
	    if (process.isActive()) {
		final List<AcquisitionProcessStateType> types = process.getAvailableStates();
		for (final AcquisitionProcessStateType stateType : types) {
		    if (stateType.showFor(currentState)) {
			generateStateBox(process, currentState, flowChartContainer, stateType);
			if (stateType.hasNextState()) {
			    generateArrowBox(flowChartContainer);
			}
		    }
		}
	    } else {
		final List<OperationLog> logs = process.getOperationLogs();
		int i = logs.size() - 1;

		AcquisitionProcessStateType currentType = logs.get(i--).getState();
		AcquisitionProcessStateType newStateType = null;

		flowChartContainer.addChild(generateBox(process, currentType, currentState));
		while (i >= 0) {
		    newStateType = logs.get(i).getState();
		    if (currentType != newStateType) {
			currentType = newStateType;
			generateActivityBox(flowChartContainer, logs.get(i + 1).getActivity());
			generateArrowBox(flowChartContainer);
			generateStateBox(process, currentState, flowChartContainer, newStateType);
		    }
		    i--;
		}

		// state has changed, but no activity was performed: render
		if (process.getAcquisitionProcessStateType() != newStateType) {
		    generateActivityBox(flowChartContainer, logs.get(i + 1).getActivity());
		    generateArrowBox(flowChartContainer);
		    generateStateBox(process, currentState, flowChartContainer, process.getAcquisitionProcessState().getAcquisitionProcessStateType());
		}
	    }
	    return flowChartContainer;
	}

	private void generateStateBox(final RegularAcquisitionProcess process, final AcquisitionProcessStateType currentState,
		final HtmlBlockContainer flowChartContainer, final AcquisitionProcessStateType stateType) {
	    flowChartContainer.addChild(generateBox(process, stateType, currentState));
	}

	private HtmlComponent generateBox(final RegularAcquisitionProcess process, final AcquisitionProcessStateType stateType,
		final AcquisitionProcessStateType currentStateType) {
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

	private HtmlComponent getBody(final RegularAcquisitionProcess process, final AcquisitionProcessStateType stateType) {
	    return getLink(getUrl(), RenderUtils.getEnumString(stateType), getStateParameterName(), process);
	}
    }

    @Override
    protected Layout getLayout(final Object arg0, final Class arg1) {
	return new AcquisitionProcessStateLayout();
    }

}
