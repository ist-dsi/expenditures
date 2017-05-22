<jsp:include page="user_common.jsp"/>

<% final String contextPath = request.getContextPath(); %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="submenu" style="float: left;">
  <nav class="subnav" data-pjax>
    <a id="allLink" href="#" class="navItem">
        <spring:message code="label.internalBilling.transactions.all" text="All"/>
    </a>
    <a id="byDayLink" href="#" class="navItem">
        <spring:message code="label.internalBilling.transactions.byDay" text="By Day"/>
    </a>
<!-- 
    <a id=byUserLink" href="#" class="navItem" >
        <spring:message code="label.internalBilling.transactions.byUser" text="By User"/>
    </a>
 -->
  </nav>
</div>

<script type="text/javascript">
    var user = ${user};
    var contextPath = '<%= contextPath %>';
    var basePagePath = contextPath + user.relativePath;
    $(document).ready(function() {
        document.getElementById("allLink").href=basePagePath + "/reports/all";
    	var today = new Date();
    	document.getElementById("byDayLink").href=basePagePath + "/reports/byDay?year=" + today.getFullYear() + "&month=" + (today.getMonth()+1);
    });
</script>
