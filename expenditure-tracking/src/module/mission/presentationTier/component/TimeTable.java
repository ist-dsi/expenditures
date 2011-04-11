package module.mission.presentationTier.component;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class TimeTable extends VerticalLayout {

    private final int year;
    private final int month;
    private final String firstColumnTitle;
    private StringBuilder stringBuilder = null;

    public TimeTable(final int year, final int month, final String firstColumnTitle) {
	this.year = year;
	this.month = month;
	this.firstColumnTitle = firstColumnTitle;
	setSpacing(false);
	setMargin(false);
	setSizeFull();
    }

    @Override
    public void attach() {
	super.attach();
	stringBuilder = new StringBuilder();

	final int daysInMonth = daysInMonth(year, month);

	openTable("width", "100%", "style", "border: thin;"); {
	    openRow(); {
		openHeader("rowspan", "2", "style", "background-color: #EEEEEE; border: thin;"); {
		    stringBuilder.append(firstColumnTitle);
		} closeHeader();
		openHeader("colspan", Integer.toString(daysInMonth), "style", "background-color: #EEEEEE; border: thin;"); {
		    stringBuilder.append(getTitle());
		} closeHeader();
	    } closeRow();
	    openRow(); {
		for (int i = 1; i <= daysInMonth; i++) {
		    openHeader("style", "background-color: #EEEEEE; border: thin; width: 10px"); {
			stringBuilder.append(i);
		    } closeHeader();
		}
	    } closeRow();
	} closeTable();

	

	final Label label = new Label(stringBuilder.toString(), Label.CONTENT_XHTML);
	addComponent(label);
    }

    private static int daysInMonth(final int year, final int month) {
	final LocalDate thisMonth = new LocalDate(year, month, 1);
	final LocalDate nextMonth = thisMonth.plusMonths(1);
	return Days.daysBetween(thisMonth, nextMonth).getDays();
    }

    public static void main(String[] args) {
	for (int i = 1; i <= 12; i++) {
	}
    }

    private String getTitle() {
	final DateTime dateTime = new DateTime(year, month, 1, 0, 0, 0, 0);
	final Locale locale = Language.getLocale();
	final String monthName = dateTime.monthOfYear().getAsText(locale);
	return monthName + " " + year;
    }

    private void openTag(final String tagName, final String... args) {
	stringBuilder.append('<');
	stringBuilder.append(tagName);
	for (int i = 0; i < args.length; ) {
	    stringBuilder.append(' ');
	    stringBuilder.append(args[i++]);
	    stringBuilder.append('=');
	    stringBuilder.append('"');
	    stringBuilder.append(args[i++]);
	    stringBuilder.append('"');
	}
	stringBuilder.append('>');
    }

    private void closeTag(final String tagName) {
	stringBuilder.append('<');
	stringBuilder.append('/');
	stringBuilder.append(tagName);
	stringBuilder.append('>');
    }

    private void openTable(final String... args) {
	openTag("table", args);
    }

    private void closeTable() {
	closeTag("table");
    }

    private void openRow(final String... args) {
	openTag("tr", args);
    }

    private void closeRow() {
	closeTag("tr");
    }

    private void openHeader(final String... args) {
	openTag("th", args);
    }

    private void closeHeader() {
	closeTag("th");
    }

    private void openCell(final String... args) {
	openTag("td", args);
    }

    private void closeCell() {
	closeTag("td");
    }

}
