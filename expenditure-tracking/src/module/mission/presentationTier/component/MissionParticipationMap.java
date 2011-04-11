package module.mission.presentationTier.component;

import java.util.Set;

import module.mission.domain.util.SearchUnitMemberPresence;
import module.organization.domain.Person;
import module.organization.domain.Unit;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import vaadin.annotation.EmbeddedComponent;

import com.vaadin.data.Item;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@EmbeddedComponent(path = { "MissionParticipationMap-(.*)" })
public class MissionParticipationMap extends CustomComponent implements EmbeddedComponentContainer {

    private class MissionTable extends Table {

	private MissionTable() {
	    setSizeFull();
	    setPageLength(0);

	    setSelectable(false);
	    setMultiSelect(false);
	    setImmediate(true);
	    setSortDisabled(false);
	    setColumnReorderingAllowed(false);
	    setColumnCollapsingAllowed(false);

	    addContainerProperty("person", String.class, null, null, null, null);
	    for (int i = 1; i <= 31; i++) {
		addContainerProperty(i);
	    }

	    setCellStyleGenerator(new CellStyleGenerator() {
	        @Override
	        public String getStyle(final Object itemId, final Object propertyId) {
	    		return null;
	        }
	    });
	}

	public void addContainerProperty(final int day)
		throws UnsupportedOperationException {
	    final String propertyId = Integer.toString(day);
	    addContainerProperty(propertyId, String.class, null, propertyId, null, null);
	    setColumnWidth(propertyId, 1);
	}

	@Override
	public void attach() {
	    super.attach();

	    final SearchUnitMemberPresence searchUnitMemberPresence = new SearchUnitMemberPresence(unit);
	    searchUnitMemberPresence.setIncludeSubUnits(true);
	    searchUnitMemberPresence.setOnMission(true);
	    for (int day = 1; day <= 31; day++) {
		final String propertyId = Integer.toString(day);
		searchUnitMemberPresence.setDay(new LocalDate(2011, 3, day));
		final Set<Person> people = searchUnitMemberPresence.search();
		for (final Person person : people) {
		    add(propertyId, person);
		}
	    }

	    sort(new Object[] { "person" }, new boolean[] { true });
	}

	private void add(final String propertyId, final Person person) {
	    final String oid = person.getExternalId();
	    Item item = getItem(oid);
	    if (item == null) {
		item = addItem(oid);
		item.getItemProperty("person").setValue(person.getFirstAndLastName());
	    }
	    item.getItemProperty(propertyId).setValue("X");
	}

    }

    private Unit unit;

    public MissionParticipationMap() {
    }

    @Override
    public void setArguments(final String... arg0) {
	if (arg0.length > 0) {
	    int index = arg0[0].indexOf('-');
	    final String unitOID = arg0[0].substring(index + 1);
	    if (unitOID != null && !unitOID.isEmpty()) {
		unit = AbstractDomainObject.fromExternalId(unitOID);
	    }
	}
    }

    @Override
    public void attach() {
        super.attach();

        final VerticalLayout layout = new VerticalLayout();
        setCompositionRoot(layout);
        layout.setSpacing(false);
        layout.setMargin(false);

        final Label title = new Label("<h2>" + unit.getPresentationName() + "</h2>", Label.CONTENT_XHTML);
        layout.addComponent(title);

        final TimeTable timeTable = new TimeTable(2011, 3, "Bla bla bla");
        layout.addComponent(timeTable);

/*
        final MissionTable missionTable = new MissionTable();
        layout.addComponent(missionTable);
*/
    }

}
