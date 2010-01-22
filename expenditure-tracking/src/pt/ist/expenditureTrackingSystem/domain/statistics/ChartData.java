package pt.ist.expenditureTrackingSystem.domain.statistics;

import org.jfree.data.CategoryDataset;
import org.jfree.data.DefaultCategoryDataset;

public class ChartData {

    private final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private char c = 'A';

    private String title;

    protected void registerData(final String key, final Number number) {
	dataset.addValue(number, "" + c + " - " + key, Character.valueOf(c++));
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    public CategoryDataset getDataset() {
	return dataset;
    }

    public String getTitle() {
        return title;
    }

}
