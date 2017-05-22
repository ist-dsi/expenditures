<jsp:include page="unit_common_reports.jsp"/>

<%@page import="org.fenixedu.commons.i18n.I18N"%>
<% final String contextPath = request.getContextPath(); %>
<% final String year = request.getParameter("year"); %>
<% final String month = request.getParameter("month"); %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src='<%= contextPath + "/js/internal-billing/datepicker-pt.js" %>'></script>

<input name="startDate" id="startDate" class="date-picker form-control" style="float: right; width: 25%;"/>

<br/>
<br/>

<div id="xpto">
</div>

<script type="text/javascript">
    var unit = ${unit};
    var dayValuePairs = ${dayValuePairs};
    var contextPath = '<%= contextPath %>';

    $(document).ready(function() {
        document.getElementById("reportsTab").parentNode.classList.add("active");
    	document.getElementById("byDayLink").classList.add("navItemSelected");

        $('.date-picker').datepicker( {
            changeMonth: true,
            changeYear: true,
            showButtonPanel: true,
            dateFormat: 'MM yy',
            regional: ['pt'],
            onClose: function(dateText, inst) { 
                $(this).datepicker('setDate', new Date(inst.selectedYear, inst.selectedMonth, 1));
                window.location = contextPath + '/internalBilling/unit/' + unit.id + '/reports/byDay?year=' + inst.selectedYear + '&month=' + (inst.selectedMonth + 1);
            }
        });
        $.datepicker.setDefaults(
        	    $.extend(
        	    	    {'dateFormat':'MM yy'},
        	    	    $.datepicker.regional['<%= I18N.getLocale().getLanguage() %>']
        	    )
        );
        $('.date-picker').datepicker('setDate', new Date(<%= year %>, <%= month %> - 1, 1, 0, 0, 0, 0));
        
    	var s = $('<span/>').appendTo($('#xpto'));
    	s.text(dayValuePairs);

    	var categories = [];
    	$.each(dayValuePairs, function (i, v) { categories.push(v.dayOfMonth); });
        var values = [];
        $.each(dayValuePairs, function (i, v) { values.push(v.value); });

    	Highcharts.chart('xpto', {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'column'
            },
            title: {
                text: ''
            },
    	    xAxis: {
    	        categories: categories,
    	        crosshair: true
    	    },
    	    yAxis: {
    	        min: 0,
    	        title: {
    	            text: '<spring:message code="label.internalBilling.transactions" text="Transactions"/>'
    	        }
    	    },
    	    tooltip: {
    	    	headerFormat: '<span style="font-size:10px"><%= year %>-<%= month %>-{point.key}</span><br/>',
    	    	pointFormat: '<b>{point.y}</b> ({point.percentage:.1f}%)',
    	        shared: true,
    	        useHTML: true
    	    },
    	    plotOptions: {
    	        column: {
                    dataLabels: {
                        enabled: false
                    },
    	            pointPadding: 0.2,
    	            borderWidth: 0
    	        }
    	    },
    	    series: [{
    	    	showInLegend: false,
    	    	name: '',
    	        data: values
    	    }]
    	});
    });
</script>
