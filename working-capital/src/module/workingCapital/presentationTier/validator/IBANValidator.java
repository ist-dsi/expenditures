package module.workingCapital.presentationTier.validator;

import myorg.util.BundleUtil;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;
import soastation.banking.IBAN;

public class IBANValidator extends HtmlValidator {

    public IBANValidator() {
	super();
    }

    public IBANValidator(final HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);
    }

    @Override
    public void performValidation() {
	final String iban = getComponent().getValue().replace(" ", "");
	performIBANValidation(iban);
    }

    protected void performIBANValidation(final String iban) {
	setValid(StringUtils.isEmpty(iban) || IBAN.isCheckDigitValid(iban));
    }

    @Override
    public String getErrorMessage() {
	return BundleUtil.getFormattedStringFromResourceBundle("resources.WorkingCapitalResources", "label.module.workingCapital.internationalBankAccountNumber.invalid");
    }

}
