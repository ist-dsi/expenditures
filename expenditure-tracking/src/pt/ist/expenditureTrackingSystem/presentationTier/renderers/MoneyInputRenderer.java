package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import myorg.domain.util.Money;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem.presentationTier.renderers.validator.MoneyValidator;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextInput;
import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaSlotKey;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;

public class MoneyInputRenderer extends InputRenderer {

    private String size;

    @Override
    protected Layout getLayout(Object object, Class type) {

	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		HtmlTextInput input = new HtmlTextInput();
		input.setSize(size);

		if (object != null) {
		    input.setValue(((Money) object).toFormatStringWithoutCurrency());
		}
		MetaSlotKey key = (MetaSlotKey) getInputContext().getMetaObject().getKey();
		input.setTargetSlot(key);
		input.setConverter(new MoneyInputConverter());
		HtmlChainValidator htmlChainValidator = new HtmlChainValidator(input);
		htmlChainValidator.addValidator(new MoneyValidator());
		return input;

	    }

	};
    }

    public static class MoneyInputConverter extends Converter {

	@Override
	public Object convert(Class type, Object value) {
	    String moneyValue = (String) value;
	    if (!StringUtils.isEmpty(moneyValue)) {
		try {
		    return new Money(moneyValue.replace(".", "").replace(",", "."));
		} catch (NumberFormatException e) {
		    throw new ConversionException("renderers.converter.bigdecimal", e, true, value);
		}
	    }
	    return null;
	}
    }

    public String getSize() {
	return size;
    }

    public void setSize(String size) {
	this.size = size;
    }

}
