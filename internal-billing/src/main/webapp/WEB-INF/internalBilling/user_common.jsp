<jsp:include page="common.jsp"/>

<% final String contextPath = request.getContextPath(); %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="page-header">
    <h2 class="ng-scope">
        <img id="userAvatar" class="img-circle" alt="" src="#"/>
        <span id="userDisplayName"></span> <small id="username"></small>
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
    var user = ${user};
    var contextPath = '<%= contextPath %>';
    var basePagePath = contextPath + user.relativePath;
    $(document).ready(function() {
        $('#userDisplayName').html(user.name);
        $('#username').html(user.username);
        $('#userAvatar').attr('src', user.avatarUrl);
        var today = new Date();
        document.getElementById("structureTab").href=basePagePath; 
        document.getElementById("servicesTab").href=basePagePath + "/services"; 
        document.getElementById("reportsTab").href=basePagePath + "/reports/byDay?year=" + today.getFullYear() + "&month=" + (today.getMonth());
        document.getElementById("logsTab").href=basePagePath + "/logs";
    });
</script>
