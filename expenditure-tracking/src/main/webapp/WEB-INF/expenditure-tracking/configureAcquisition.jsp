${portal.toolkit()}
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<% final String contextPath = request.getContextPath(); %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>

<spring:url var="noSupplierURL" value="/expenditure/acquisitons/create/isRefund" />

<style>
.btn-xlarge {
    padding: 18px 28px;
    font-size: 20px;
    line-height: normal;
    -webkit-border-radius: 8px;
    -moz-border-radius: 8px;
    border-radius: 8px;
    border-color: #07a;
    border-width: 2px;
    border-style: dotted;
}

.btable {
    margin-top: 25px;
    margin-bottom: 25px;
    width: 100%;
    text-align: center;
}

.btable td {
    width: 25%;
}
</style>

<h3>
    <spring:message code="label.configuration.server"/>
    ${applicationUrl}
</h3>

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
    
    <table>
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
    </table>


	<h4>
        <spring:message code="label.configuration.process.flow"/>
    </h4>
    <table>
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
            
    </table>
    <h4>
		<spring:message code="label.configuration.process.documentation"/>
    </h4>
    <table>
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
	</table>

    <td><input type="submit" value="Submit"/></td>
</form>

<br/>
<br/>

<h3>
	<spring:message code="label.configuration.organizational.estructure"/>
</h3>

<form class="form-horizontal" action="<%= request.getContextPath() %>/expenditure/config/structuralConfiguration" method="POST">
    ${csrf.field()}
    <div class="form-group">
        <label for="AccountabilityType"> <spring:message code="label.organizationalAccountabilityType"/> </label>
        <select name="organizationalAccountabilityType" id="AccountabilityType" class="form-control" required="true">
            <c:forEach items="${accountabilityTypes}" var="type">
                <option value="${type.externalId}" ${ expenditureTrackingSystem.getOrganizationalAccountabilityType().name.content.equals(type.name.content) ? "selected" : ""} > ${type.name.content}</option>
            </c:forEach>
        </select>
    </div>

    <select name="organizationalMissionAccountabilityType" class="form-control" required="true">
        <c:forEach items="${accountabilityTypes}" var="type">
            <option value="${type.externalId}" ${ expenditureTrackingSystem.getOrganizationalMissionAccountabilityType().name.content.equals(type.name.content) ? "selected" : ""} >${type.name.content}</option>
        </c:forEach>
    </select>
    <select name="unitPartyType" class="form-control" required="true">
        <c:forEach items="${partyTypes}" var="type">
            <option value="${type.externalId}" ${ expenditureTrackingSystem.getUnitPartyType().name.content.equals(type.name.content) ? "selected" : ""} >${type.name.content}</option>
        </c:forEach>
    </select>
    <select name="costCenterPartyType" class="form-control" required="true">
        <c:forEach items="${partyTypes}" var="type">
            <option value="${type.externalId}" ${expenditureTrackingSystem.getCostCenterPartyType().name.content.equals(type.name.content) ? "selected" : ""} >${type.name.content}</option>
        </c:forEach>
    </select>
    <select name="subProjectPartyType" class="form-control" required="true">
        <c:forEach items="${partyTypes}" var="type">
            <option value="${type.externalId}" ${expenditureTrackingSystem.getSubProjectPartyType().name.content.equals(type.name.content) ? "selected" : ""} >${type.name.content}</option>
        </c:forEach>
    </select>
    <input type="text" name="institutionManagementEmail" class="form-control" required="true" value="${expenditureTrackingSystem.getInstitutionManagementEmail()}">
    <td><input type="submit" value="Submit"/></td>
</form>