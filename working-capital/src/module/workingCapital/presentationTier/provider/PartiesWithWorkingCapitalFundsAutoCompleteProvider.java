package module.workingCapital.presentationTier.provider;

import module.organization.domain.Party;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.organization.presentationTier.renderers.providers.PartiesAutoCompleteProvider;

public class PartiesWithWorkingCapitalFundsAutoCompleteProvider extends PartiesAutoCompleteProvider {

    @Override
    protected boolean allowResult(final Party party) {
	return party.isUnit() ? allowResultUnit((Unit) party) : allowResultPerson((Person) party);
    }

    private boolean allowResultUnit(final Unit unit) {
	return unit.hasExpenditureUnit() && unit.getExpenditureUnit().getWorkingCapitalsCount() > 0;
    }

    private boolean allowResultPerson(final Person person) {
	return person.getMovementResponsibleWorkingCapitalsCount() > 0;
    }

}
