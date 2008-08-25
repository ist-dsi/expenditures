package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem.domain.util.Address;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlMultipleHiddenField;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextInput;
import pt.ist.fenixWebFramework.renderers.components.controllers.HtmlController;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaSlotKey;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

public class AddressInputRenderer extends InputRenderer {

    private String bundle;
    private String line1Key;
    private String line2Key;
    private String postalCodeKey;
    private String locationKey;
    private String countryKey;
    private String classes;

    public String getClasses() {
	return classes;
    }

    public void setClasses(String classes) {
	this.classes = classes;
    }

    public String getBundle() {
	return bundle;
    }

    public void setBundle(String bundle) {
	this.bundle = bundle;
    }

    public String getLine1Key() {
	return line1Key;
    }

    public void setLine1Key(String line1Key) {
	this.line1Key = line1Key;
    }

    public String getLine2Key() {
	return line2Key;
    }

    public void setLine2Key(String line2Key) {
	this.line2Key = line2Key;
    }

    public String getPostalCodeKey() {
	return postalCodeKey;
    }

    public void setPostalCodeKey(String postalCodeKey) {
	this.postalCodeKey = postalCodeKey;
    }

    public String getLocationKey() {
	return locationKey;
    }

    public void setLocationKey(String locationKey) {
	this.locationKey = locationKey;
    }

    public String getCountryKey() {
	return countryKey;
    }

    public void setCountryKey(String countryKey) {
	this.countryKey = countryKey;
    }

    @Override
    protected Layout getLayout(Object arg0, Class arg1) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class arg1) {
		Address address = (Address) object;

		HtmlBlockContainer container = new HtmlBlockContainer();

		final HtmlTableCell[] errorCells = new HtmlTableCell[5];
		
		HtmlMultipleHiddenField htmlMultipleHiddenField = new HtmlMultipleHiddenField();
		MetaSlotKey key = (MetaSlotKey) getInputContext().getMetaObject().getKey();
		htmlMultipleHiddenField.setTargetSlot(key);
		htmlMultipleHiddenField.setConverter(new AddressConverter());
		container.addChild(htmlMultipleHiddenField);

		HtmlTable table = new HtmlTable();
		table.setClasses(getClasses());
		HtmlTableRow row = table.createRow();
		row.createCell().setBody(new HtmlText(RenderUtils.getResourceString(bundle, line1Key) + ":"));
		HtmlTextInput line1 = new HtmlTextInput();
		line1.setName(key.toString() + "_line1");
		line1.setValue(address != null ? address.getLine1() : null);
		row.createCell().setBody(line1);
		errorCells[0] = row.createCell();
		
		HtmlTextInput line2 = new HtmlTextInput();
		HtmlTableRow row2 = table.createRow();
		row2.createCell().setBody(new HtmlText(RenderUtils.getResourceString(bundle, line2Key) + ":"));
		line2.setName(key.toString() + "_line2");
		line2.setValue(address != null ? address.getLine2() : null);
		row2.createCell().setBody(line2);
		errorCells[1] = row2.createCell(); 
		
		HtmlTextInput location = new HtmlTextInput();
		HtmlTableRow row3 = table.createRow();
		row3.createCell().setBody(new HtmlText(RenderUtils.getResourceString(bundle, locationKey) + ":"));
		location.setName(key.toString() + "_location");
		location.setValue(address != null ? address.getLocation() : null);
		row3.createCell().setBody(location);
		errorCells[2] = row3.createCell();
 		
		HtmlTextInput postalCode = new HtmlTextInput();
		HtmlTableRow row4 = table.createRow();
		row4.createCell().setBody(new HtmlText(RenderUtils.getResourceString(bundle, postalCodeKey) + ":"));
		postalCode.setName(key.toString() + "_postalCode");
		postalCode.setValue(address != null ? address.getPostalCode() : null);
		row4.createCell().setBody(postalCode);
		errorCells[3] = row4.createCell();
		
		HtmlTextInput country = new HtmlTextInput();
		HtmlTableRow row5 = table.createRow();
		row5.createCell().setBody(new HtmlText(RenderUtils.getResourceString(bundle, countryKey) + ":"));
		country.setName(key.toString() + "_country");
		country.setValue(address != null ? address.getCountry() : null);
		row5.createCell().setBody(country);
		errorCells[4] = row5.createCell();
		
		htmlMultipleHiddenField.setConverter(new AddressConverter());
		country
			.setController(new AddressController(line1, line2, location, postalCode, country, htmlMultipleHiddenField));

		HtmlChainValidator htmlChainValidator = new HtmlChainValidator(htmlMultipleHiddenField);
		htmlMultipleHiddenField.setChainValidator(htmlChainValidator);
		new HtmlValidator(htmlChainValidator) {

		    List<String> errorMessages = new ArrayList<String>();

		    @Override
		    public void performValidation() {
			String[] values = getComponent().getValues();
			if (values[0] == null || values[0].isEmpty()) {
			    errorCells[0].setBody(new HtmlText("error.line1.cannot.be.empty"));
			    errorMessages.add("error.line1.cannot.be.empty");
			}
			if (values[2] == null || values[2].isEmpty()) {
			    errorCells[2].setBody(new HtmlText("error.location.cannot.be.empty"));
			    errorMessages.add("error.location.cannot.be.empty");
			}
			if (values[3] == null || values[3].isEmpty()) {
			    errorCells[3].setBody(new HtmlText("error.postalCode.cannot.be.empty"));
			    errorMessages.add("error.postalCode.cannot.be.empty");
			}
			if (values[4] == null || values[4].isEmpty()) {
			    errorCells[4].setBody(new HtmlText("error.country.cannot.be.empty"));
			    errorMessages.add("error.country.cannot.be.empty");
			}

			if (!errorMessages.isEmpty()) {
			    setValid(false);
			}

		    }

		};

		container.addChild(table);

		return container;
	    }

	};
    }

    private static class AddressConverter extends Converter {

	@Override
	public Object convert(Class type, Object value) {
	    String[] values = (String[]) value;
	    if (hasAnyValue(values)) {
		return new Address(values[0], values[1], values[3], values[2], values[4]);
	    }
	    return null;
	}

	private boolean hasAnyValue(String[] values) {
	    for (String string : values) {
		if (!StringUtils.isEmpty(string)) {
		    return true;
		}
	    }
	    return false;
	}
    }

    private static class AddressController extends HtmlController {
	private HtmlMultipleHiddenField hiddenFields;
	private HtmlTextInput line1;
	private HtmlTextInput line2;
	private HtmlTextInput location;
	private HtmlTextInput postalCode;
	private HtmlTextInput country;

	public AddressController(HtmlTextInput line1, HtmlTextInput line2, HtmlTextInput location, HtmlTextInput postalCode,
		HtmlTextInput country, HtmlMultipleHiddenField htmlMultipleHiddenField) {
	    super();
	    this.country = country;
	    this.line1 = line1;
	    this.line2 = line2;
	    this.location = location;
	    this.postalCode = postalCode;
	    this.hiddenFields = htmlMultipleHiddenField;
	}

	@Override
	public void execute(IViewState viewState) {
	    hiddenFields.setValues(new String[] {});
	    hiddenFields.addValue(line1.getValue());
	    hiddenFields.addValue(line2.getValue());
	    hiddenFields.addValue(location.getValue());
	    hiddenFields.addValue(postalCode.getValue());
	    hiddenFields.addValue(country.getValue());
	}
    }
}
