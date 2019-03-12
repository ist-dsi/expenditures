<jsp:include page="unit_common_reports.jsp"/>

<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% final String contextPath = request.getContextPath(); %>
<% final String year = request.getParameter("year"); %>
<% final String month = request.getParameter("month"); %>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>

${portal.toolkit()}

<input id="startDate" name="startDate" value="${param["year"]}-${param["month"]}-1" bennu-datetime required onchange="selectNewDate();" class="form-inline">

<table class="table">
    <thead>
        <tr>
            <th rowspan="2">
                <spring:message code="label.user" text="User"/>
            </th>
            <c:forEach var="service" items="${services}">
                <th colspan="2" style="text-align: center;">
                    ${service}
                </th>
            </c:forEach>
            <th rowspan="2">
            </th>
        </tr>
        <tr>
            <c:forEach var="service" items="${services}">
                <th>
                    <spring:message code="label.count" text="Count"/>
                </th>
                <th>
                    <spring:message code="label.value" text="Value"/>
                </th>
            </c:forEach>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="entry" items="${userReports}">
            <tr>
                <td>
                    <img class="img-circle avatarIcon" alt="" src="${entry.key.profile.avatarUrl}"/>
                    ${entry.key.profile.displayName}
                    <span style="color: gray;">
                        ( ${entry.key.username} )
                    </span>
                </td>
                <c:forEach var="serviceEntry" items="${entry.value.serviceMap}">
                    <td>
                        ${serviceEntry.value.count}
                    </td>
                    <td>
                        ${serviceEntry.value.value.value}
                    </td>
                </c:forEach>
                <td>
                    <a href='<%= contextPath %>/internalBilling/user/${entry.key.externalId}/reports/byDay?year=${param["year"]}&month=${param["month"]}'
                            class="btn btn-default">
                        <spring:message code="label.view" text="View"/>
                    </a>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <th style="text-align: right; padding-right: 50px;">
                <spring:message code="label.total" text="Total"/>
            </th>
            <c:forEach var="service" items="${services}">
                <c:set var="count" value="0" />
                <c:set var="value" value="0" /> 
                <c:forEach var="entry" items="${userReports}">
                    <c:forEach var="serviceEntry" items="${entry.value.serviceMap}">
                        <c:if test="${serviceEntry.key == service}">
                            <c:set var="count" value="${count + serviceEntry.value.count}" />
                            <c:set var="value" value="${value + serviceEntry.value.value.value}" />  
                        </c:if>
                    </c:forEach>
                </c:forEach>
                <th>
                    ${count}
                </th>
                <th>
                    ${value}
                </th>
            </c:forEach>
            <th>
            </th>
        </tr>
    </tbody>
</table>

<br/>
<br/>

<div id="xpto">
</div>

<script type="text/javascript">
    var unit = ${unit};
    var dayValuePairs = ${dayValuePairs};
    var contextPath = '<%= contextPath %>';

    function selectNewDate() {
    	var startDate = $('#startDate').val();
    	window.location = contextPath + '/internalBilling/unit/' + unit.id + '/reports/byDay?year=' + startDate.substring(0, 4) + '&month=' + startDate.substring(5, 7);
    };

    $(document).ready(function() {
        document.getElementById("reportsTab").parentNode.classList.add("active");
    	document.getElementById("byDayLink").classList.add("navItemSelected");

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
    	        crosshair: true,
                title: {
                    text: '<spring:message code="label.dayOfMonth" text="Day of Month"/>'
                }
    	    },
    	    yAxis: {
    	        min: 0,
    	        title: {
    	            text: '<spring:message code="label.numberOfPages" text="Number of Pages"/>'
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
