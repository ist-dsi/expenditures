${portal.toolkit()}
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<% final String contextPath = request.getContextPath(); %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>

<div class="page-header">
    <h2>
        <spring:message code="label.configuration.server"/>
        <c:out value="${applicationUrl}"/>
    </h2>
</div>

<div class="page-body">
    <div class="infobox_dotted">
        <h3>
            <spring:message code="label.configuration.general"/>
        </h3>

        <form action="<%= request.getContextPath() %>/expenditure/config/generalConfig" method="POST">
            ${csrf.field()}
            
            <h4>
                <spring:message code="label.configuration.process.institutionalProcessNumberPrefix"/>
            </h4>
            <input type="text" name="institutionalProcessNumberPrefix" class="form-control" value=${institutionalProcessNumberPrefix} />
            
            <h4>
                <spring:message code="label.configuration.process.institutionalRequestDocumentPrefix" />
            </h4>
            <input type="text" name="institutionalRequestDocumentPrefix" class="form-control" value=${institutionalRequestDocumentPrefix} />

            <h4>
                <spring:message code="label.configuration.process.creation.interface"/>
            </h4>
            <input type="text" name="acquisitionCreationWizardJsp" class="form-control" value=${acquisitionCreationWizardJsp} />

            <h4>
                <spring:message code="label.configuration.process.search.types" />
            </h4>
            
            <div class="table-responsive">
                <table class="table">
                    <tbody>
                        <c:forEach var="entry" items="${processValues}">
                            <tr>
                                <td>
                                    ${entry.key.getLocalizedName()}
                                </td>
                                <td>
                                    <input type="checkbox" name="${entry.key.name()}"
                                        <c:if test="${entry.value}">
                                            checked="checked"
                                        </c:if>
                                    >
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>


            <h4>
                <spring:message code="label.configuration.process.flow"/>
            </h4>
            <div class="table-responsive">
                <table class="table">
                    <tbody>
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.prior.consultation.available"/>
                            </td>
                            <td>
                                <input type="checkbox" name="isPriorConsultationAvailable"
                                    <c:if test="${expenditureTrackingSystem.isPriorConsultationAvailable()}">
                                        checked="checked"
                                    </c:if>
                                >
                            </td>
                        </tr>
                
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.flow.start.with.invoice"/>
                            </td>
                            <td>
                                <input type="checkbox" name="invoiceAllowedToStartAcquisitionProcess"
                                    <c:if test="${expenditureTrackingSystem.getInvoiceAllowedToStartAcquisitionProcess() != null
                                        && expenditureTrackingSystem.getInvoiceAllowedToStartAcquisitionProcess().booleanValue()}">
                                        checked="checked"
                                    </c:if>
                                >
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.flow.start.with.invoice.limit"/>
                            </td>
                            <td>
                                <input type="text" name="maxValueStartedWithInvoive" class="form-control"
                                    <c:if test="${expenditureTrackingSystem.getMaxValueStartedWithInvoive() != null}">
                                        value="${expenditureTrackingSystem.getMaxValueStartedWithInvoive().getValue()}"
                                    </c:if>
                                >
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.flow.require.fund.allocation.prior.to.acquisition.request" />
                            </td>
                            <td>
                                <input type="checkbox" name="requireFundAllocationPriorToAcquisitionRequest"

                                    <c:if test="${expenditureTrackingSystem.getRequireFundAllocationPriorToAcquisitionRequest() != null
                                        && expenditureTrackingSystem.getRequireFundAllocationPriorToAcquisitionRequest().booleanValue()}">
                                        checked="checked"
                                    </c:if>
                                >
                            </td>
                        </tr>

                        
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.flow.register.diary.numbers.and.transaction.numbers" />
                            </td>
                            <td>
                                <input type="checkbox" name="registerDiaryNumbersAndTransactionNumbers"
                                    <c:if test="${expenditureTrackingSystem.getRegisterDiaryNumbersAndTransactionNumbers() != null
                                        && expenditureTrackingSystem.getRegisterDiaryNumbersAndTransactionNumbers().booleanValue()}">
                                        checked="checked"
                                    </c:if>
                                >
                            </td>
                        </tr>

                        
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.value.requireing.top.level.authorization"/>
                            </td>
                            <td>
                                <input type="text" name="valueRequireingTopLevelAuthorization" class="form-control"

                                    <c:if test="${expenditureTrackingSystem.getValueRequireingTopLevelAuthorization() != null}">
                                        value="${expenditureTrackingSystem.getValueRequireingTopLevelAuthorization().getValue()}"
                                    </c:if>
                                >
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.flow.requireCommitmentNumber" />
                            </td>
                            <td>
                                <input type="checkbox" name="requireCommitmentNumber"
                                    <c:if test="${expenditureTrackingSystem.getRequireCommitmentNumber() != null
                                        && expenditureTrackingSystem.getRequireCommitmentNumber().booleanValue()}">
                                        checked="checked"
                                    </c:if>
                                >
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.flow.processesNeedToBeReverified" />
                            </td>
                            <td>
                                <input type="checkbox" name="processesNeedToBeReverified"
                                    <c:if test="${expenditureTrackingSystem.getProcessesNeedToBeReverified() != null
                                        && expenditureTrackingSystem.getProcessesNeedToBeReverified().booleanValue()}">
                                        checked="checked"
                                    </c:if>
                                >
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <h4>
                <spring:message code="label.configuration.process.documentation"/>
            </h4>
            <div class="table-responsive">
                <table class="table">
                    <tbody>
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.documentation.documentationUrl"/>
                            </td>
                            <td>
                                <input type="text" name="documentationUrl" class="form-control"
                                    <c:if test="${expenditureTrackingSystem.getDocumentationUrl() != null}">
                                        value="${expenditureTrackingSystem.getDocumentationUrl()}"
                                    </c:if>
                                >
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.documentation.documentationLabel"/>
                            </td>
                            <td>
                                <input type="text" name="documentationLabel" class="form-control"
                                    <c:if test="${expenditureTrackingSystem.getDocumentationLabel() != null}">
                                        value="${expenditureTrackingSystem.getDocumentationLabel()}"
                                    </c:if>
                                >
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.documentation.createSupplierUrl"/>
                            </td>
                            <td>
                                <input type="text" name="createSupplierUrl" class="form-control"
                                    <c:if test="${expenditureTrackingSystem.getCreateSupplierUrl() != null}">
                                        value="${expenditureTrackingSystem.getCreateSupplierUrl()}"
                                    </c:if>    
                                >
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.documentation.createSupplierLabel"/>
                            </td>
                            <td>
                                <input type="text" name="createSupplierLabel" class="form-control"
                                    <c:if test="${expenditureTrackingSystem.getCreateSupplierLabel() != null}">
                                        value="${expenditureTrackingSystem.getCreateSupplierLabel()}"
                                    </c:if>    
                                >
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <button type="submit" class="btn btn-default"><spring:message code="label.submit"/></button>
        </form>
    </div>

    <div class="infobox_dotted">
        <h3>
            <spring:message code="label.configuration.organizational.estructure"/>
        </h3>

        <form class="form-horizontal" action="<%= request.getContextPath() %>/expenditure/config/structuralConfiguration" method="POST">
            ${csrf.field()}
            <div class="form-group">
                <label for="organizationalAccountabilityType" class="col-sm-2 control-label">
                    <spring:message code="label.organizationalAccountabilityType"/>
                </label>
                <div class="col-sm-10">
                    <select name="organizationalAccountabilityType" id="AccountabilityType" class="form-control" required="true">
                        <c:forEach items="${accountabilityTypes}" var="type">
                            <option value="${type.externalId}" ${ expenditureTrackingSystem.getOrganizationalAccountabilityType().externalId.equals(type.externalId) ? "selected" : ""} >${type.name.content}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label for="organizationalMissionAccountabilityType" class="col-sm-2 control-label">
                    <spring:message code="label.organizationalMissionAccountabilityType"/>
                </label>
                <div class="col-sm-10">
                    <select name="organizationalMissionAccountabilityType" class="form-control" required="true">
                        <c:forEach items="${accountabilityTypes}" var="type">
                            <option value="${type.externalId}" ${ expenditureTrackingSystem.getOrganizationalMissionAccountabilityType().externalId.equals(type.externalId) ? "selected" : ""} >${type.name.content}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="unitPartyType" class="col-sm-2 control-label">
                    <spring:message code="label.unitPartyType"/>
                </label>
                <div class="col-sm-10">
                    <select name="unitPartyType" class="form-control" required="true">
                        <c:forEach items="${partyTypes}" var="type">
                            <option value="${type.externalId}" ${ expenditureTrackingSystem.getUnitPartyType().externalId.equals(type.externalId) ? "selected" : ""} >${type.name.content}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="costCenterPartyType" class="col-sm-2 control-label">
                    <spring:message code="label.costCenterPartyType"/>
                </label>
                <div class="col-sm-10">
                    <select name="costCenterPartyType" class="form-control" required="true">
                        <c:forEach items="${partyTypes}" var="type">
                            <option value="${type.externalId}" ${expenditureTrackingSystem.getCostCenterPartyType().externalId.equals(type.externalId) ? "selected" : ""} >${type.name.content}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="projectPartyType" class="col-sm-2 control-label">
                    <spring:message code="label.projectPartyType"/>
                </label>
                <div class="col-sm-10">
                    <select name="projectPartyType" class="form-control" required="true">
                        <c:forEach items="${partyTypes}" var="type">
                            <option value="${type.externalId}" ${expenditureTrackingSystem.getProjectPartyType().externalId.equals(type.externalId) ? "selected" : ""} >${type.name.content}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="subProjectPartyType" class="col-sm-2 control-label">
                    <spring:message code="label.subProjectPartyType"/>
                </label>
                <div class="col-sm-10">
                    <select name="subProjectPartyType" class="form-control" required="true">
                        <c:forEach items="${partyTypes}" var="type">
                            <option value="${type.externalId}" ${expenditureTrackingSystem.getSubProjectPartyType().externalId.equals(type.externalId) ? "selected" : ""} >${type.name.content}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="institutionManagementEmail" class="col-sm-2 control-label">
                    <spring:message code="label.institutionManagementEmail"/>
                </label>
                <div class="col-sm-10">
                    <input type="text" name="institutionManagementEmail" class="form-control" required="true" value="${expenditureTrackingSystem.getInstitutionManagementEmail()}">
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