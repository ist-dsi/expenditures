package pt.ist.expenditureTrackingSystem.presentationTier.renderers.validator;

import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.expenditureTrackingSystem.presentationTier.renderers.MoneyInputRenderer;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

public class MoneyValidator extends HtmlValidator {

    public MoneyValidator(HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);

	setBundle("EXPENDITURE_RESOURCES");
	setMessage("messages.exception.validation.invalidMoney");
    }

    @Override
    public void performValidation() {
	setValid(true);
	try {
	    new MoneyInputRenderer.MoneyInputConverter().convert(Money.class, getComponent().getValue());
	} catch (Exception e) {
	    setValid(false);
	}
    }

}
