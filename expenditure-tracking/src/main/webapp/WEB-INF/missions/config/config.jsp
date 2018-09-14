<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% final String contextPath = request.getContextPath(); %>

<div class="page-header">
    <h2><spring:message code="missionsConfiguration.title"></spring:message></h2>
</div>

<div class="page-body">
    <div class="infobox_dotted">
        <h3><spring:message code="missionsConfiguration.text.countryForNationalMissions"/></h3>

        <p>
            <c:choose>
                <c:when test="${missionSystem.country == null}">
                    <spring:message code="missionsConfiguration.text.noCountryDefined"/>
                </c:when>
                <c:otherwise>
                    <c:out value="${missionSystem.country.name.content}"/>
                </c:otherwise>
            </c:choose>
            <br/>
            <a href="<%= request.getContextPath() %>/missions/config/selectCountry">
                <spring:message code="missionsConfiguration.link.selectCountry"/>
            </a>
        </p>
    </div>


    <div class="infobox_dotted">
        <h3><spring:message code="missionsConfiguration.text.modelForMissionAuthorizations"/></h3>

        <p>
            <c:choose>
                <c:when test="${missionSystem.organizationalModel == null}">
                    <spring:message code="missionsConfiguration.text.noModelDefined"/>
                </c:when>
                <c:otherwise>
                    <a href="<%= request.getContextPath() %>/missions/config/viewModel/${missionSystem.organizationalModel.externalId}">
                        <c:out value="${missionSystem.organizationalModel.name.content}"/>
                    </a>
                </c:otherwise>
            </c:choose>
            <br/>
            <a href="<%= request.getContextPath() %>/missions/config/selectOrganizationalModel">
                <spring:message code="missionsConfiguration.link.selectModel"/>
            </a>
        </p>

        <c:if test="${missionSystem.organizationalModel != null}">
            <h3><spring:message code="missionsConfiguration.text.accountabilityTypesForAuthorization"/></h3>

            <p>
                <a href="<%= request.getContextPath() %>/missions/config/addMissionAuthorizationAccountabilityType">
                    <spring:message code="missionsConfiguration.link.addAccountabilityType"/>
                </a>
            </p>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th><spring:message code="missionsConfiguration.label.accountabilityType"/></th>
                            <th><spring:message code="missionsConfiguration.label.toBeAuthorizedBy"/></th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${missionSystem.missionAuthorizationAccountabilityTypesSet}" var="type">
                            <tr>
                                <td><c:out value="${type.accountabilityType.name.content}"/></td>
                                <td><c:out value="${type.accountabilityTypesAsString}"/></td>
                                <td>
                                    <a href="<%= request.getContextPath() %>/missions/config/deleteMissionAuthorizationAccountabilityType/${type.externalId}">
                                        <spring:message code="link.delete"/>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>


    <div class="infobox_dotted">
        <form class="form-inline" action="<%= request.getContextPath() %>/missions/config/setAllowGrantOwnerMissionProcessNature" method="POST">
            <div class="checkbox">
                <label>
                    <spring:message code="missionsConfiguration.label.allowGrantOwnerEquivalence"/>
                    <input type="checkbox" name="allowGrantOwnerEquivalence" ${missionSystem.getInstance().allowGrantOwnerEquivalence() ? "checked='checked'" : ""}>
                </label>
            </div>
            <button type="submit" class="btn btn-default"><spring:message code="label.submit"/></button>
        </form>
    </div>

    <div class="infobox_dotted">
        <form class="form-inline" action="<%= request.getContextPath() %>/missions/config/setUseWorkingPlaceAuthorizationChain" method="POST">
            <div class="checkbox">
                <label>
                    <spring:message code="missionsConfiguration.label.useWorkingPlaceAuthorizationChain"/>
                    <input type="checkbox" name="useWorkingPlaceAuthorizationChain" ${missionSystem.getInstance().useWorkingPlaceAuthorizationChain() ? "checked='checked'" : ""}>
                </label>
            </div>
            <button type="submit" class="btn btn-default"><spring:message code="label.submit"/></button>
        </form>
    </div>


    <div class="infobox_dotted">
        <h3><spring:message code="missionsConfiguration.text.dailyPersonelExpenseTables"/></h3>

        <p>
            <a href="<%= request.getContextPath() %>/missions/config/createDailyPersonelExpenseTable">
                <spring:message code="missionsConfiguration.link.createTable"/>
            </a>
        </p>

        <c:choose>
            <c:when test="${missionSystem.dailyPersonelExpenseTablesSet == null || missionSystem.dailyPersonelExpenseTablesSet.isEmpty()}">
                <p><spring:message code="missionsConfiguration.text.noTableDefined"/></p>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th><spring:message code="missionsConfiguration.label.missionType"/></th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${missionSystem.currentDailyExpenseTables}" var="table">
                                <tr>
                                    <td><spring:message code="missionsConfiguration.label.missionType.${table.aplicableToMissionClass.simpleName}"/></td>
                                    <td>
                                        <a href="<%= request.getContextPath() %>/missions/config/viewDailyPersonelExpenseTable/${table.externalId}">
                                            <spring:message code="link.view"/>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>


    <div class="infobox_dotted">
        <h3><spring:message code="missionsConfiguration.text.verificationQueue"/></h3>

        <form class="form-horizontal" action="<%= request.getContextPath() %>/missions/config/setVerificationQueue" method="POST">
            <div class="form-group">
                <label for="verificationQueueOid" class="col-sm-2 control-label">
                    <spring:message code="missionsConfiguration.label.queue"/>
                </label>
                <div class="col-sm-10">
                    <select name="verificationQueueOid" class="form-control">
                        <c:forEach items="${workflowQueues}" var="queue">
                            <option value="${queue.externalId}" ${missionSystem.verificationQueue.externalId.equals(queue.externalId) ? "selected" : ""}>${queue.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default"><spring:message code="label.submit"/></button>
                </div>
            </div>
        </form>
    </div>


    <div class="infobox_dotted">
        <h3><spring:message code="missionsConfiguration.text.employmentAccountabilityType"/></h3>

        <form class="form-horizontal" action="<%= request.getContextPath() %>/missions/config/setEmploymentAccountabilityType" method="POST">
            <div class="form-group">
                <label for="accountabilityTypeOid" class="col-sm-2 control-label">
                    <spring:message code="missionsConfiguration.label.accountabilityType"/>
                </label>
                <div class="col-sm-10">
                    <select name="accountabilityTypeOid" class="form-control">
                        <c:forEach items="${accountabilityTypes}" var="type">
                            <option value="${type.externalId}" ${missionSystem.employmentAccountabilityType.externalId.equals(type.externalId) ? "selected" : ""}>${type.name.content}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default"><spring:message code="label.submit"/></button>
                </div>
            </div>
        </form>
    </div>


    <div class="infobox_dotted">
        <h3><spring:message code="missionsConfiguration.text.queueForAccountabilityType"/></h3>

        <p>
            <a href="<%= request.getContextPath() %>/missions/config/addQueueForAccountabilityType">
                <spring:message code="missionsConfiguration.link.addAccountabilityType"/>
            </a>
        </p>

        <c:choose>
            <c:when test="${missionSystem.accountabilityTypeQueuesSet == null || missionSystem.accountabilityTypeQueuesSet.isEmpty()}">
                <p><spring:message code="missionsConfiguration.text.noAccountabilityTypeDefined"/></p>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th><spring:message code="missionsConfiguration.label.accountabilityType"/></th>
                                <th><spring:message code="missionsConfiguration.label.queue"/></th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${missionSystem.accountabilityTypeQueuesSet}" var="typeQueue">
                                <tr>
                                    <td>${typeQueue.accountabilityType.name.content}</td>
                                    <td>${typeQueue.workflowQueue.name}</td>
                                    <td>
                                        <a href="<%= request.getContextPath() %>/missions/config/deleteAccountabilityTypeQueue/${typeQueue.externalId}">
                                            <spring:message code="link.delete"/>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>


    <div class="infobox_dotted">
        <h3><spring:message code="missionsConfiguration.text.unitsWithResumedAuthorizations"/></h3>

        <c:choose>
            <c:when test="${missionSystem.unitsWithResumedAuthorizations == null || missionSystem.unitsWithResumedAuthorizations.isEmpty()}">
                <p><spring:message code="missionsConfiguration.text.noUnitsDefined"/></p>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table">
                        <tbody>
                            <tr>
                                <c:forEach items="${missionSystem.orderedUnitsWithResumedAuthorizations}" var="unit">
                                    <td>
                                        <a href="<%= request.getContextPath() %>/missions/config/showUnit/${unit.externalId}">
                                            <c:out value="${unit.partyName.content}"/>
                                        </a>
                                    </td>
                                </c:forEach>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>


    <div class="infobox_dotted">
        <h3><spring:message code="missionsConfiguration.text.usersWhoCanCancelMissions"/></h3>

        <p>
            <a href="<%= request.getContextPath() %>/missions/config/addUserWhoCanCancelMissions">
                <spring:message code="missionsConfiguration.link.addUser"/>
            </a>
        </p>

        <c:choose>
            <c:when test="${missionSystem.usersWhoCanCancelMission == null || missionSystem.usersWhoCanCancelMission.isEmpty()}">
                <p><spring:message code="missionsConfiguration.text.noUserDefined"/></p>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th><spring:message code="missionsConfiguration.label.name"/></th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${missionSystem.usersWhoCanCancelMission}" var="user">
                                <tr>
                                    <td>${user.displayName}</td>
                                    <td>
                                        <a href="<%= request.getContextPath() %>/missions/config/removeUserWhoCanCancelMissions/${user.username}">
                                            <spring:message code="link.remove"/>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>


    <div class="infobox_dotted">
        <h3><spring:message code="missionsConfiguration.text.vehicleAuthorizers"/></h3>

        <p>
            <a href="<%= request.getContextPath() %>/missions/config/addVehicleAuthorizer">
                <spring:message code="missionsConfiguration.link.addUser"/>
            </a>
        </p>

        <c:choose>
            <c:when test="${missionSystem.vehicleAuthorizers == null || missionSystem.vehicleAuthorizers.isEmpty()}">
                <p><spring:message code="missionsConfiguration.text.noUserDefined"/></p>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th><spring:message code="missionsConfiguration.label.name"/></th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${missionSystem.vehicleAuthorizers}" var="user">
                                <tr>
                                    <td>${user.displayName}</td>
                                    <td>
                                        <a href="<%= request.getContextPath() %>/missions/config/removeVehicleAuthorizer/${user.username}">
                                            <spring:message code="link.remove"/>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>


    <div class="infobox_dotted">
        <h3><spring:message code="missionsConfiguration.text.mandatorySupplier"/></h3>

        <p>
            <a href="<%= request.getContextPath() %>/missions/config/addMandatorySupplier">
                <spring:message code="missionsConfiguration.link.addSupplier"/>
            </a>
        </p>

        <c:choose>
            <c:when test="${missionSystem.mandatorySupplierSet == null || missionSystem.mandatorySupplierSet.isEmpty()}">
                <p><spring:message code="missionsConfiguration.text.noSupplierDefined"/></p>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th><spring:message code="missionsConfiguration.label.name"/></th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${missionSystem.mandatorySupplierSet}" var="supplier">
                                <tr>
                                    <td>${supplier.name}</td>
                                    <td>
                                        <a href="<%= request.getContextPath() %>/missions/config/removeMandatorySupplier/${supplier.fiscalIdentificationCode}">
                                            <spring:message code="link.remove"/>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>

        <form class="form-horizontal" action="<%= request.getContextPath() %>/missions/config/setMandatorySupplierNotUsedErrorMessageArg" method="POST">
            <div class="form-group">
                <label for="mandatorySupplierNotUsedErrorMessageArg" class="col-sm-2 control-label">
                    <spring:message code="missionsConfiguration.label.mandatorySupplierNotUsedErrorMessageArg"/>
                </label>
                <div class="col-sm-10">
                    <input type="text" name="mandatorySupplierNotUsedErrorMessageArg" class="form-control" value="${missionSystem.mandatorySupplierNotUsedErrorMessageArg}">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default"><spring:message code="label.submit"/></button>
                </div>
            </div>
        </form>
    </div>

</div>


