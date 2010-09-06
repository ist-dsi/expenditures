package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import java.util.List;

import org.apache.struts.util.RequestUtils;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.Face;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlScript;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.plugin.RenderersRequestProcessorImpl;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import org.apache.commons.collections.Predicate;

public class NewAcquisitionProcessStateRenderer extends OutputRenderer {

    public static String JAVASCRIPT_PATH = "/javaScript/stateTypeAjaxRequest.js";

    private String cancelledClasses;
    private String numberClasses;
    private String gutterClasses;
    private String selectedClasses;
    private String descriptionClasses;
    private String ajaxRequestUrl;

    public String getCancelledClasses() {
	return cancelledClasses;
    }

    public void setCancelledClasses(String cancelledClasses) {
	this.cancelledClasses = cancelledClasses;
    }

    public String getSelectedClasses() {
	return selectedClasses;
    }

    public void setSelectedClasses(String selectedClasses) {
	this.selectedClasses = selectedClasses;
    }

    public String getNumberClasses() {
	return numberClasses;
    }

    public void setNumberClasses(String numberClasses) {
	this.numberClasses = numberClasses;
    }

    public String getGutterClasses() {
	return gutterClasses;
    }

    public void setGutterClasses(String gutterClasses) {
	this.gutterClasses = gutterClasses;
    }

    public String getDescriptionClasses() {
	return descriptionClasses;
    }

    public void setDescriptionClasses(String descriptionClasses) {
	this.descriptionClasses = descriptionClasses;
    }

    public String getAjaxRequestUrl() {
	return ajaxRequestUrl;
    }

    public void setAjaxRequestUrl(String ajaxRequestUrl) {
	this.ajaxRequestUrl = ajaxRequestUrl;
    }

    @Override
    protected Layout getLayout(Object arg0, Class arg1) {
	return new Layout() {

	    private SimplifiedProcedureProcess process;

	    @Override
	    public HtmlComponent createComponent(Object arg0, Class arg1) {

		this.process = (SimplifiedProcedureProcess) arg0;

		HtmlBlockContainer container = new HtmlBlockContainer();

		HtmlTable table = new HtmlTable();
		String tableId = "showProcess-" + System.currentTimeMillis();
		table.setId(tableId);
		container.addChild(table);

		HtmlTableRow numberRow = table.createRow();
		numberRow.setClasses(getNumberClasses());
		HtmlTableRow gutterRow = table.createRow();
		gutterRow.setClasses(getGutterClasses());
		HtmlTableRow descriptionRow = table.createRow();
		descriptionRow.setClasses(getDescriptionClasses());
		HtmlTableCell descriptionCell = descriptionRow.createCell();

		final AcquisitionProcessStateType currentState = process.getAcquisitionProcessStateType();
		final List<AcquisitionProcessStateType> types = process.getAvailableStates();
		int i = 1;

		for (final AcquisitionProcessStateType stateType : types) {
		    if (stateType.showFor(currentState)) {
			HtmlBlockContainer numberContainer = new HtmlBlockContainer();
			HtmlTableCell cell = numberRow.createCell();
			cell.setType(CellType.HEADER);
			numberContainer.addChild(new HtmlText(String.valueOf(i++)));
			cell.setBody(numberContainer);
			cell.setId(stateType.toString());
			HtmlTableCell gutterCell = gutterRow.createCell();
			gutterCell.setType(CellType.HEADER);
			if (process.getAcquisitionProcessStateType().equals(stateType)) {
			    cell.addClass(getSelectedClasses());
			    gutterCell.setClasses(getSelectedClasses());
			    addStateDescripton(descriptionCell, stateType);
			}
		    }
		}

		if (!process.isActive()) {
		    addStateDescripton(descriptionCell, AcquisitionProcessStateType.CANCELED);
		}

		descriptionCell.setColspan(i - 1);
		HtmlLink link = new HtmlLink();
		link.setModuleRelative(false);
		link.setContextRelative(true);
		link.setUrl(JAVASCRIPT_PATH);
		container.addChild(new HtmlScript("text/javaScript", link.calculateUrl()));
		HtmlScript initScript = new HtmlScript();
		initScript.setContentType("text/javaScript");
		initScript.setScript("startStateTypeRenderer(\""
			+ RenderersRequestProcessorImpl.getCurrentRequest().getContextPath() + "\",\"" + process.getExternalId()
			+ "\");");
		container.addChild(initScript);
		return container;
	    }

	    private void addStateDescripton(HtmlTableCell descriptionCell, AcquisitionProcessStateType stateType) {
		HtmlText stateName = new HtmlText(stateType.getLocalizedName());
		stateName.setFace(Face.H4);
		HtmlText stateDescription = new HtmlText(stateType.getDescription());
		HtmlInlineContainer stateContainer = new HtmlInlineContainer();
		stateContainer.addChild(stateName);
		stateContainer.addChild(stateDescription);
		descriptionCell.setBody(stateContainer);
	    }

	    @Override
	    public void applyStyle(HtmlComponent component) {
		HtmlComponent actualComponent = component.getChild(new Predicate() {

		    @Override
		    public boolean evaluate(Object arg0) {
			return arg0 instanceof HtmlTable;
		    }
		});
		super.applyStyle(actualComponent);
		if (!process.isActive()) {
		    actualComponent.setClasses(getCancelledClasses());
		}
	    }
	};

    }
}
