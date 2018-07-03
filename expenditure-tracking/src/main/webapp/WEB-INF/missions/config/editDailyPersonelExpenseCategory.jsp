<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% final String contextPath = request.getContextPath(); %>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>

<div class="page-header">
    <h2><spring:message code="missionsConfiguration.text.dailyPersonelExpenseCategories"></spring:message></h2>
</div>

<div class="page-body">

    <form class="form-horizontal" action="<%= request.getContextPath() %>/missions/config/editDailyPersonelExpenseCategory/${category.externalId}" method="POST">
        <div class="form-group">
            <label for="description" class="col-sm-2 control-label">
                <spring:message code="missionsConfiguration.label.category"/>
            </label>
            <div class="col-sm-10">
                <input type="text" name="description" class="form-control" required="required" value="${category.description}">
            </div>
        </div>
        <div class="form-group">
            <label for="value" class="col-sm-2 control-label">
                <spring:message code="missionsConfiguration.label.value"/>
            </label>
            <div class="col-sm-10">
                <div class="input-group">
                    <input type="number" step="0.01" min="0" name="value" class="form-control" required="required" value="${category.value.value}">
                    <span class="input-group-addon">&euro;</span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label for="minSalaryValue" class="col-sm-2 control-label">
                <spring:message code="missionsConfiguration.label.inferiorLimit"/>
            </label>
            <div class="col-sm-10">
                <input type="number" step="0.01" min="0" name="minSalaryValue" class="form-control" required="required" value="${category.minSalaryValue}">
            </div>
        </div>


        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-primary"><spring:message code="link.submit"/></button>
                <a href="<%= contextPath %>/missions/config/viewDailyPersonelExpenseTable/${category.dailyPersonelExpenseTable.externalId}" class="btn btn-default"><spring:message code="link.cancel"/></a>
            </div>
        </div>
    </form>

</div>