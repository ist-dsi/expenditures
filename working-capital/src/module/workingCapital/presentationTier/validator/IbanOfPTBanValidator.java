package module.workingCapital.presentationTier.validator;

import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;

public class IbanOfPTBanValidator extends IBANValidator {

    public IbanOfPTBanValidator() {
	super();
    }

    public IbanOfPTBanValidator(final HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);
    }

    @Override
    protected void performIBANValidation(final String value) {
	String iban = value == null || value.isEmpty() || !Character.isDigit(value.charAt(0))
		? value : "PT50" + value;
	super.performIBANValidation(iban);
    }

}
