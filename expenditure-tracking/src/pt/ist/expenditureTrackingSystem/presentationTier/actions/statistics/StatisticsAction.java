package pt.ist.expenditureTrackingSystem.presentationTier.actions.statistics;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.JFreeChart;

import pt.ist.expenditureTrackingSystem.domain.statistics.SimplifiedProcessStatistics;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import sun.awt.image.codec.JPEGImageEncoderImpl;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@Mapping(path = "/statistics")
@Forwards( {
    @Forward(name = "show.statistics", path = "/statistics/showStatistics.jsp"),
    @Forward(name = "show.statistics.simplified.process", path = "/statistics/showStatisticsSimplifiedProcess.jsp")
})
public class StatisticsAction extends BaseAction {

    private static final Context CONTEXT = new Context("statistics");

    @Override
    protected Context getContextModule(final HttpServletRequest request) {
	return CONTEXT;
    }

    public ActionForward showStatistics(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	return mapping.findForward("show.statistics");
    }

    public ActionForward showSimplifiedProcessStatistics(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	YearBean yearBean = getRenderedObject();
	if (yearBean == null) {
	    yearBean = new YearBean();
	}
	request.setAttribute("yearBean", yearBean);

	final SimplifiedProcessStatistics simplifiedProcessStatistics = SimplifiedProcessStatistics.create(yearBean.getYear());
	request.setAttribute("simplifiedProcessStatistics", simplifiedProcessStatistics);

	return mapping.findForward("show.statistics.simplified.process");
    }

    public ActionForward simplifiedProcessStatisticsChart(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final String year = request.getParameter("year");

	final SimplifiedProcessStatistics simplifiedProcessStatistics = SimplifiedProcessStatistics.create(new Integer(year));
	final JFreeChart chart = ChartGenerator.simplifiedProcessStatisticsChart(simplifiedProcessStatistics);

	final BufferedImage bufferedImage = chart.createBufferedImage(1000, 1000);

	OutputStream outputStream = null;
	try {
	    outputStream = response.getOutputStream();
	    final JPEGImageEncoder imageEncoder = new JPEGImageEncoderImpl(outputStream);
	    imageEncoder.encode(bufferedImage);
	} catch (final FileNotFoundException e) {
	    throw new Error(e);
	} catch (final ImageFormatException e) {
	    throw new Error(e);
	} catch (final IOException e) {
	    throw new Error(e);
	} finally {
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (final IOException e) {
		    throw new Error(e);
		}
	    }
	}

	return null;
    }

}
