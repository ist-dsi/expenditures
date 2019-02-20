<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% final String contextPath = request.getContextPath(); %>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>

<div class="page-header">
    <h2><spring:message code="missionsConfiguration.text.vehicleAuthorizers"></spring:message></h2>
</div>

<div class="page-body">

    <form class="form-horizontal" action="<%= request.getContextPath() %>/missions/config/addUserWhoCanCancelMissions" method="POST">
        ${csrf.field()}
        <div class="form-group">
            <label for="username" class="col-sm-2 control-label">
                <spring:message code="missionsConfiguration.label.username"/>
            </label>
            <div class="col-sm-10">
                <input name="username" type="text" class="form-control" required="required"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-primary"><spring:message code="link.submit"/></button>
                <a href="<%= contextPath %>/missions/config" class="btn btn-default"><spring:message code="link.cancel"/></a>
            </div>
        </div>
    </form>

</div>