package module.mission.presentationTier.provider;

import java.util.ArrayList;
import java.util.List;

import module.mission.domain.DailyPersonelExpenseCategory;
import module.mission.domain.Mission;
import module.mission.domain.PersonelExpenseItem;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DailyPersonalExpenseCategoryProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return null;
	}

	@Override
	public Object provide(final Object source, final Object currentValue) {
		final PersonelExpenseItem personelExpenseItem = (PersonelExpenseItem) source;
		final Mission mission =
				personelExpenseItem.hasMissionVersion() ? personelExpenseItem.getMissionVersion().getMission() : personelExpenseItem
						.getMissionForCreation();
		final List<DailyPersonelExpenseCategory> result = new ArrayList<DailyPersonelExpenseCategory>();
		result.addAll(mission.getPossibleDailyPersonalExpenseCategories());
		return result;
	}

}
