<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% final String contextPath = request.getContextPath(); %>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>

<div class="page-header">
    <h2><spring:message code="missionsConfiguration.text.dailyPersonelExpenseTables"></spring:message></h2>
</div>

<div class="page-body">

    <div class="infobox_dotted">
        <form class="form-horizontal">
              <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <spring:message code="missionsConfiguration.label.missionType"/>
                    </label>
                    <div class="col-sm-10">
                        <p class="form-control-static">
                            <spring:message code="missionsConfiguration.label.missionType.${table.aplicableToMissionClass.simpleName}"/>
                        </p>
                    </div>
              </div>
              <div class="form-group">
                    <label for="inputPassword" class="col-sm-2 control-label">
                        <spring:message code="missionsConfiguration.label.applicableSince"/>
                    </label>
                    <div class="col-sm-10">
                        <p class="form-control-static">
                            <c:out value="${table.aplicableSince}"/>
                        </p>
                    </div>
              </div>
        </form>

        <p>
            <a href="<%= request.getContextPath() %>/missions/config/editDailyPersonelExpenseTable/${table.externalId}"><spring:message code="link.edit"/></a>
            &nbsp;&nbsp;
            <a href="<%= request.getContextPath() %>/missions/config/deleteDailyPersonelExpenseTable/${table.externalId}"><spring:message code="link.delete"/></a>
        </p>
        <p>
            <c:forEach items="${table.dailyPersonelExpenseTablesForSameType}" var="sameTypeTable">
                <a href="<%= request.getContextPath() %>/missions/config/viewDailyPersonelExpenseTable/${sameTypeTable.externalId}"><c:out value="${sameTypeTable.aplicableSince}"/></a>
                &nbsp;&nbsp;
            </c:forEach>
        </p>
    </div>

    <div class="infobox_dotted">
        <p>
            <a href="<%= request.getContextPath() %>/missions/config/createDailyPersonelExpenseCategory/${table.externalId}"><spring:message code="missionsConfiguration.link.addCategory"/></a>
        </p>
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th><spring:message code="missionsConfiguration.label.category"/></th>
                        <th><spring:message code="missionsConfiguration.label.value"/></th>
                        <th><spring:message code="missionsConfiguration.label.inferiorLimit"/></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${table.sortedDailyPersonelExpenseCategories}" var="category">
                        <tr>
                            <td><c:out value="${category.description}"/></td>
                            <td><c:out value="${category.value.toFormatString()}"/></td>
                            <td><c:out value="${category.minSalaryValue}"/></td>
                            <td>
                                <a href="<%= request.getContextPath() %>/missions/config/editDailyPersonelExpenseCategory/${category.externalId}"><spring:message code="link.edit"/></a>,
                                <a href="<%= request.getContextPath() %>/missions/config/deleteDailyPersonelExpenseCategory/${category.externalId}"><spring:message code="link.delete"/></a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>