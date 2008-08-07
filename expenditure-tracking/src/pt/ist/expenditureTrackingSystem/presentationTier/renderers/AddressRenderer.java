package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

import pt.ist.expenditureTrackingSystem.domain.util.Address;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class AddressRenderer extends OutputRenderer {

    private String styleClasses;

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		Address address = (Address) object;
		if (address != null) {
		    HtmlTable htmlTable = new HtmlTable();
		    htmlTable.setStyle(getStyleClasses());
		    htmlTable.createRow().createCell().setBody(new HtmlText(address.getLine1()));

		    if (address.getLine2() != null && address.getLine2().length() != 0) {
			htmlTable.createRow().createCell().setBody(new HtmlText(address.getLine2()));
		    }

		    htmlTable.createRow().createCell().setBody(
			    new HtmlText(address.getPostalCode() + ", " + address.getLocation()));
		    htmlTable.createRow().createCell().setBody(new HtmlText(address.getCountry()));
		    return htmlTable;
		} else {
		    return new HtmlText("");
		}
	    }

	};
    }

    public void setStyleClasses(String styleClasses) {
	this.styleClasses = styleClasses;
    }

    public String getStyleClasses() {
	return styleClasses;
    }

}
