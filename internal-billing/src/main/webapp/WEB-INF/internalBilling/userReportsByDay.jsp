<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@page import="java.util.Set"%>
<jsp:include page="user_common_reports.jsp"/>

<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="java.util.Collection" %>
<%@page import="pt.ist.internalBilling.domain.Billable" %>


<% final String contextPath = request.getContextPath(); %>
<% final String year = request.getParameter("year"); %>
<% final String month = request.getParameter("month"); %>
<% final Set<Billable> billableSet = (Set<Billable>)request.getAttribute("activeServices"); %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>

${portal.toolkit()}

<input id="startDate" name="startDate" value='${param["year"]}-${param["month"]}-1' bennu-datetime required onchange="selectNewDate();" class="form-inline">

<table class="table">
    <thead>
        <tr>
            <th rowspan="2">
                <spring:message code="label.unit" text="Unit"/>
            </th>
            <c:forEach var="service" items="${services}">
                <th colspan="2" style="text-align: center;">
                    ${service}
                </th>
            </c:forEach>
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
        <c:forEach var="entry" items="${unitReports}">
            <tr>
                <td>
                <a href='<%= contextPath %>/internalBilling/unit/${entry.key.externalId}/reports/byDay?year=${param["year"]}&month=${param["month"]}'>
                    ${entry.key.presentationName}
                </a>
                </td>
                <c:forEach var="serviceEntry" items="${entry.value.serviceMap}">
                    <td>
                        ${serviceEntry.value.count}
                    </td>
                    <td>
                        ${serviceEntry.value.value.value}
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
        <tr>
            <th style="text-align: right; padding-right: 50px;">
                <spring:message code="label.total" text="Total"/>
            </th>
            <c:forEach var="service" items="${services}">
                <c:set var="count" value="0" />
                <c:set var="value" value="0" /> 
                <c:forEach var="entry" items="${unitReports}">
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

<div id="xpto">
</div>

<div>

    <table class="table" style="width: 100%;">
        <thead>
            <tr>
                <th width="13%"><spring:message code="label.internalBilling.billing.details.txDate" text="Transaction Date"/></th>
                <th width="5%"><spring:message code="label.internalBilling.billing.details.value" text="Value"/></th>
                <th width="15%"><spring:message code="label.internalBilling.billing.details.label" text="Description"/></th>
                <th><spring:message code="label.internalBilling.billing.details.details" text="Details"/></th>
                <th width="30%"><spring:message code="label.internalBilling.billing.details.unit" text="Unit"/></th>
                <th/>
            </tr>
        </thead>
        <tbody id="txTable"/>
            <c:forEach var="tx" items="${transactions}">
                <tr>
                    <td>
                        <joda:format value="${tx.txDate}" style="SM" pattern="yyyy-MM-dd HH:mm" />
                    </td>
                    <td>
                        ${tx.value.value}
                    </td>
                    <td>
                        ${tx.description}
                    </td>
                    <td>
                        ${tx.label}
                    </td>
                    <td>
                        ${tx.billable.unit.presentationName}
                    </td>
                    
                    <%if(billableSet.size()>1){ %>
                     <td> <a href='<%= contextPath %>/internalBilling/transaction/${tx.externalId}?year=${param["year"]}&month=${param["month"]}'>
                     	 <spring:message code="label.move.transaction" text="Move Transaction"/>
                     	</a>
                      </td>
                     <%} %>
                </tr>
            </c:forEach>
    </table>
</div>

<script type="text/javascript">
    var user = ${user};
    var dayValuePairs = ${dayValuePairs};
    var contextPath = '<%= contextPath %>';

    function selectNewDate() {
        var startDate = $('#startDate').val();
        window.location = contextPath + '/internalBilling/user/' + user.username + '/reports/byDay?year=' + startDate.substring(0, 4) + '&month=' + startDate.substring(5, 7);
    };

    $(document).ready(function() {
    	document.getElementById("reportsTab").parentNode.classList.add("active");

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
