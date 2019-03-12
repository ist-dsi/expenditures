<jsp:include page="common.jsp"/>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="page-header">
    <h2 class="ng-scope">
        <span id="unitName"></span> 
    </h2>
</div>

<ul class="nav nav-tabs" style="margin-bottom: 25px;">
    <li>
        <a id="structureTab">
            <spring:message code="label.internalBilling.unit.structure" text="Structure"/>
        </a>
    </li>
    <li>
        <a id="servicesTab">
            <spring:message code="label.internalBilling.unit.services" text="Services"/>
        </a>
    </li>
    <li>
        <a id="reportsTab">
            <spring:message code="label.internalBilling.unit.reports" text="Reports"/>
        </a>
    </li>
    <li>
        <a id="logsTab">
            <spring:message code="label.internalBilling.billableService.viewLogs" text="Logs"/>
        </a>
    </li>
</ul>

<script type="text/javascript">
    var unit = ${unit};
    var contextPath = '<%= request.getContextPath() %>';
    var basePagePath = contextPath + unit.relativePath;
    $(document).ready(function() {
        $('#unitName').html(unit.name);
        var today = new Date();
        document.getElementById("structureTab").href=basePagePath; 
        document.getElementById("servicesTab").href=basePagePath + "/services"; 
        document.getElementById("reportsTab").href=basePagePath + "/reports/byDay?year=" + today.getFullYear() + "&month=" + today.getMonth();
        document.getElementById("logsTab").href=basePagePath + "/logs";
    });
</script>
