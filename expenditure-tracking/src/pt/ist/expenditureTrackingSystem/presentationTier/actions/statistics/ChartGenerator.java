package pt.ist.expenditureTrackingSystem.presentationTier.actions.statistics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.Statistics;
import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcessStatistics;

public class ChartGenerator {

    protected static JFreeChart createBarChart(final CategoryDataset categoryDataset) {
	final JFreeChart chart = ChartFactory.createBarChart("Title", "categoryAxisLabel", "valueAxisLabel", categoryDataset,
		PlotOrientation.VERTICAL, true, true, true);
	return chart;
    }

    public static JFreeChart simplifiedProcessStatisticsChart(final SimplifiedProcessStatistics simplifiedProcessStatistics) {
	final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

//	for (DateTime dateTime = since; !dateTime.isAfterNow(); dateTime = dateTime.plusDays(1)) {
//	    final Statistics statistics = getStatisticsForDay(connection, dateTime);
//	    dataset.addValue(statistics.reports, "Reports", dateTime);
//	    dataset.addValue(statistics.reads, "Reads", dateTime);
//	    dataset.addValue(statistics.writes, "Writes", dateTime);
//	    dataset.addValue(statistics.aborts, "Aborts", dateTime);
//	    dataset.addValue(statistics.conflicts, "Conflicts", dateTime);
//	}
//

	final JFreeChart chart = ChartFactory.createBarChart("Title", "categoryAxisLabel", "valueAxisLabel", dataset,
		PlotOrientation.VERTICAL, true, true, true);
	return chart;
    }

}
