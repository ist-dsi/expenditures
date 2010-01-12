package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class AcquisitionProcessStateRenderer extends ProcessStateRenderer<RegularAcquisitionProcess> {

    private class AcquisitionProcessStateLayout extends ProcessStateLayout<RegularAcquisitionProcess> {

	@Override
	protected HtmlComponent generateFlowChart(final HtmlBlockContainer flowChartContainer,
		final RegularAcquisitionProcess process) {
	    final AcquisitionProcessStateType currentState = process.getAcquisitionProcessStateType();
	    final List<AcquisitionProcessStateType> types = process.getAvailableStates();
	    for (final AcquisitionProcessStateType stateType : types) {
		if (stateType.showFor(currentState)) {
		    generateStateBox(process, currentState, flowChartContainer, stateType);
		    if (stateType.hasNextState()) {
			generateArrowBox(flowChartContainer);
		    }
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
	    return getBoxBody(getUrl(), stateType.getLocalizedName(), stateType.toString(), process);
	}
    }

    @Override
    protected Layout getLayout(final Object arg0, final Class arg1) {
	return new AcquisitionProcessStateLayout();
    }

}
