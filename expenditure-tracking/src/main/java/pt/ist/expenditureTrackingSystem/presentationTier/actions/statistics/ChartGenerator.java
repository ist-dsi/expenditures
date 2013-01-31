/*
 * @(#)ChartGenerator.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.presentationTier.actions.statistics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import module.workflow.activities.WorkflowActivity;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.CategoryDataset;
import org.jfree.data.DefaultCategoryDataset;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AllocateFundsPermanently;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AllocateProjectFundsPermanently;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.Approve;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.Authorize;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.FundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.ProjectFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.ConfirmInvoices;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.RefundPerson;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.SubmitForInvoiceConfirmation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ConfirmInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CreateAcquisitionPurchaseOrderDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.PayAcquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SendPurchaseOrderToSupplier;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SetSkipSupplierFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SkipPurchaseOrderDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SubmitForApproval;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SubmitForConfirmInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SubmitForFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.UnsetSkipSupplierFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.statistics.ChartData;
import pt.ist.expenditureTrackingSystem.domain.statistics.LogEntry;
import pt.ist.expenditureTrackingSystem.domain.statistics.RefundProcessActivityLogStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.RefundProcessStatistics;
import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcessStatistics;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class ChartGenerator {

	protected static JFreeChart createBarChart(final CategoryDataset categoryDataset, final String title) {
		final JFreeChart chart =
				ChartFactory.createBarChart3D(title, "", "", categoryDataset, PlotOrientation.VERTICAL, true, true, false);
		chart.setBackgroundPaint(new Color(0xF5, 0xF5, 0xF5));

		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;
	}

	protected static byte[] createBarChartImage(final CategoryDataset categoryDataset, final String title)
			throws RuntimeException, IOException {
		final JFreeChart chart = createBarChart(categoryDataset, title);
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final BufferedImage bufferedImage = chart.createBufferedImage(625, 475);
		ImageIO.write(bufferedImage, "png", outputStream);
		bufferedImage.flush();
		outputStream.close();
		return outputStream.toByteArray();
	}

	protected static byte[] createBarChartImage(final ChartData chartData) throws RuntimeException, IOException {
		final JFreeChart chart = createBarChart(chartData.getDataset(), chartData.getTitle());
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final BufferedImage bufferedImage = chart.createBufferedImage(625, 475);
		ImageIO.write(bufferedImage, "png", outputStream);
		bufferedImage.flush();
		outputStream.close();
		return outputStream.toByteArray();
	}

	public static CategoryDataset simplifiedProcessStatisticsChart(final SimplifiedProcessStatistics simplifiedProcessStatistics) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		final Map<AcquisitionProcessStateType, Integer> map =
				simplifiedProcessStatistics.getNumberOfProcessesByAcquisitionProcessStateType();
		char c = 'A';
		for (final Entry<AcquisitionProcessStateType, Integer> entry : map.entrySet()) {
			final AcquisitionProcessStateType acquisitionProcessStateType = entry.getKey();
			final Integer numberOfProcesses = entry.getValue();

			if (numberOfProcesses.intValue() > 0) {
				dataset.addValue(numberOfProcesses, "" + c + " - " + acquisitionProcessStateType.getLocalizedName(),
						Character.valueOf(c++));
			}
		}

		return dataset;
	}

	public static CategoryDataset refundProcessStatisticsChart(final RefundProcessStatistics refundProcessStatistics) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		final Map<RefundProcessStateType, Integer> map = refundProcessStatistics.getNumberOfProcessesByRefundProcessStateType();
		char c = 'A';
		for (final Entry<RefundProcessStateType, Integer> entry : map.entrySet()) {
			final RefundProcessStateType refundProcessStateType = entry.getKey();
			final Integer numberOfProcesses = entry.getValue();

			if (numberOfProcesses.intValue() > 0) {
				dataset.addValue(numberOfProcesses, "" + c + " - " + refundProcessStateType.getLocalizedName(),
						Character.valueOf(c++));
			}
		}

		return dataset;
	}

	private static CategoryDataset refundProcessStatisticsActivityTimeChart(
			final RefundProcessActivityLogStatistics refundProcessActivityLogStatistics) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		char c = 'A';
		for (final LogEntry logEntry : refundProcessActivityLogStatistics.getLogEntries()) {
			final WorkflowActivity abstractActivity = logEntry.getAbstractActivity();
			if (isRelevanteActivity(abstractActivity)) {
				final String name = abstractActivity.getLocalizedName();
				int indexOfSpan = name.indexOf('<');
				final String label = indexOfSpan > 0 ? name.substring(0, indexOfSpan) : name;
				dataset.addValue(logEntry.getDays(), "" + c + " - " + label, Character.valueOf(c++));
			}
		}

		return dataset;
	}

	private static boolean isRelevanteActivity(final WorkflowActivity abstractActivity) {
		if (abstractActivity == null) {
			return false;
		}
		final Class clazz = abstractActivity.getClass();
		return clazz == CreateAcquisitionPurchaseOrderDocument.class || clazz == SendPurchaseOrderToSupplier.class
				|| clazz == SkipPurchaseOrderDocument.class || clazz == SubmitForApproval.class
				|| clazz == SubmitForFundAllocation.class || clazz == Authorize.class
				|| clazz == AllocateProjectFundsPermanently.class || clazz == AllocateFundsPermanently.class
				|| clazz == ProjectFundAllocation.class || clazz == FundAllocation.class || clazz == PayAcquisition.class
				|| clazz == SubmitForConfirmInvoice.class || clazz == ConfirmInvoice.class
				|| clazz == SetSkipSupplierFundAllocation.class || clazz == UnsetSkipSupplierFundAllocation.class
				|| clazz == pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.SubmitForApproval.class
				|| clazz == Approve.class || clazz == SubmitForInvoiceConfirmation.class || clazz == ConfirmInvoices.class
				|| clazz == RefundPerson.class;
	}

	public static byte[] simplifiedProcessStatisticsImage(final SimplifiedProcessStatistics simplifiedProcessStatistics)
			throws RuntimeException, IOException {
		final CategoryDataset dataset = simplifiedProcessStatisticsChart(simplifiedProcessStatistics);
		return createBarChartImage(dataset, "Número de Processos");
	}

	public static byte[] refundProcessStatisticsImage(final RefundProcessStatistics refundProcessStatistics)
			throws RuntimeException, IOException {
		final CategoryDataset dataset = refundProcessStatisticsChart(refundProcessStatistics);
		return createBarChartImage(dataset, "Número de Processos");
	}

	public static byte[] refundProcessStatisticsActivityTimeImage(
			final RefundProcessActivityLogStatistics refundProcessActivityLogStatistics) throws RuntimeException, IOException {
		final CategoryDataset dataset = refundProcessStatisticsActivityTimeChart(refundProcessActivityLogStatistics);
		return createBarChartImage(dataset, "Tempo Médio por Actividade (em Dias)");
	}

}
