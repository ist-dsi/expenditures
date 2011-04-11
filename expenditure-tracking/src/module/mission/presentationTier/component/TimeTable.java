package module.mission.presentationTier.component;

import java.util.Locale;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class TimeTable extends VerticalLayout {

    private static final String[] MARKED_STYLE = new String[] { "style", "color: graytext; background-color: #A3D1D9; " };
    private static final String[] EMPTY_STYLE  = new String[] { "style", "color: graytext;" };

    private final int year;
    private final int month;
    private final String firstColumnTitle;
    private StringBuilder stringBuilder = null;
    private SortedMap<String, boolean[]> rowInfo = new TreeMap<String, boolean[]>();

    public TimeTable(final int year, final int month, final String firstColumnTitle) {
	this.year = year;
	this.month = month;
	this.firstColumnTitle = firstColumnTitle;
	setSpacing(false);
	setMargin(true, false, false, false);
	setSizeFull();
    }

    public void fillSlot(final String rowHeader, final int day) {
	if (!rowInfo.containsKey(rowHeader)) {
	    final int daysInMonth = daysInMonth(year, month);
	    rowInfo.put(rowHeader, new boolean[daysInMonth]);
	}
	final boolean[] row = rowInfo.get(rowHeader);
	row[day - 1] = true;
    }

    @Override
    public void attach() {
	super.attach();

	stringBuilder = new StringBuilder();

	final int daysInMonth = daysInMonth(year, month);

	openTable("width", "100%", "border", "1", "style", "text-align: center;"); {
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
		    openHeader("style", "background-color: #EEEEEE; border: thin; width: 15px"); {
			stringBuilder.append(i);
		    } closeHeader();
		}
	    } closeRow();

	    for (final Entry<String, boolean[]> entry : rowInfo.entrySet()) {
		final String rowHeader = entry.getKey();
		final boolean[] value = entry.getValue();

		openRow(); {
		    openCell("align", "left"); {
			stringBuilder.append(rowHeader);
		    } closeCell();
		    for (int i = 1; i <= daysInMonth; i++) {
			final String[] args = value[i - 1] ? MARKED_STYLE : EMPTY_STYLE;
			openCell(args);
				stringBuilder.append(i);
			closeCell();
		    }
		} closeRow();
	    }
	} closeTable();

	final Label label = new Label(stringBuilder.toString(), Label.CONTENT_XHTML);
	addComponent(label);
    }

    static int daysInMonth(final int year, final int month) {
	final LocalDate thisMonth = new LocalDate(year, month, 1);
	final LocalDate nextMonth = thisMonth.plusMonths(1);
	return Days.daysBetween(thisMonth, nextMonth).getDays();
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

    public Spreadsheet export() {
	final Spreadsheet spreadsheet = new Spreadsheet(getTitle());
	spreadsheet.setHeader(" ");
	final int daysInMonth = TimeTable.daysInMonth(year, month);
	for (int i = 1; i < daysInMonth; i++) {
	    spreadsheet.setHeader(Integer.toString(i));
	}
	for (final Entry<String, boolean[]> entry : rowInfo.entrySet()) {
	    final String rowHeader = entry.getKey();
	    final boolean[] value = entry.getValue();

	    final Row row = spreadsheet.addRow();
	    row.setCell(rowHeader);
	    for (int i = 1; i <= daysInMonth; i++) {
		final String cellValue = value[i - 1] ? "M" : " ";
		row.setCell(cellValue);
	    }
	}
	return spreadsheet;
    }

}
