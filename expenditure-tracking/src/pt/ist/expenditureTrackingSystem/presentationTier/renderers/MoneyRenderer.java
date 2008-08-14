package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import java.text.DecimalFormat;
import java.util.Currency;

import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class MoneyRenderer extends OutputRenderer {

    private String format;

    private String currencyBundle;
    
    private Boolean escapeCurrencyLabel;
    
    public String getCurrencyBundle() {
        return currencyBundle;
    }

    public void setCurrencyBundle(String currencyBundle) {
        this.currencyBundle = currencyBundle;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout()  {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		Money money = (Money) object;
		Currency currency = money.getCurrency();
		HtmlText currencyText = new HtmlText(RenderUtils.getResourceString(getCurrencyBundle(), "label." + currency.getCurrencyCode()),getEscapeCurrencyLabel());
		HtmlText valueText = new HtmlText(new DecimalFormat(getFormat()).format(money.getValue()));
		
		HtmlInlineContainer container = new HtmlInlineContainer();
		container.addChild(valueText);
		container.addChild(currencyText);
		
		return container;
	    }
	    
	};
	
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Boolean getEscapeCurrencyLabel() {
        return escapeCurrencyLabel;
    }

    public void setEscapeCurrencyLabel(Boolean escapeCurrencyLabel) {
        this.escapeCurrencyLabel = escapeCurrencyLabel;
    }

}
