<% final String contextPath = request.getContextPath(); %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<style type="text/css">
    .navItem {
        text-decoration: none;
        border-top-left-radius: 3px;
        border-bottom-left-radius: 3px;

        position: relative;
        padding: 6px 14px;
        font-weight: 600;
        line-height: 20px;
        color: #586069;
        border: 1px solid #e1e4e8;
    
        position: relative;
        padding: 6px 14px;
        font-weight: 600;
        line-height: 20px;
        border: 1px solid #e1e4e8;
    }

    .navItemSelected {
        color: #fff;
        background-color: #0366d6;
        border-color: #0366d6;
    }
</style>


<jsp:include page="unit_common.jsp"/>

<div class="submenu" style="float: left;">
  <nav class="subnav" data-pjax>
<!-- 
    <a id="allLink" href="#" class="navItem">
        <spring:message code="label.internalBilling.transactions.all" text="All"/>
    </a>
 -->
    <a id="byDayLink" href="#" class="navItem" style="display: none;">
        <spring:message code="label.internalBilling.transactions.byDay" text="By Day"/>
    </a>
<!-- 
    <a id=byUserLink" href="#" class="navItem" >
        <spring:message code="label.internalBilling.transactions.byUser" text="By User"/>
    </a>
 -->
  </nav>
</div>
