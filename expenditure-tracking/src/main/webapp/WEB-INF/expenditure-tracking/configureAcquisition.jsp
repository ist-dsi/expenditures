${portal.toolkit()}
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<% final String contextPath = request.getContextPath(); %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>

<style>
.ui-autocomplete-loading{background: url(<%= request.getContextPath() %>/images/autocomplete/spinner.gif) no-repeat right center}
</style>

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

        <form class="form-horizontal" action="<%= request.getContextPath() %>/expenditure/config/generalConfig" method="POST">
            ${csrf.field()}
            
            
            <div class="form-group">
                <label for="institutionalProcessNumberPrefix" class="col-sm-2 control-label">
                    <spring:message code="label.configuration.process.institutionalProcessNumberPrefix"/>
                </label>
                <div class="col-sm-10">
                    <input type="text" name="institutionalProcessNumberPrefix" class="form-control" value=${institutionalProcessNumberPrefix}>
                </div>
            </div>
            
            
            <div class="form-group">
                <label for="institutionalRequestDocumentPrefix" class="col-sm-2 control-label">
                    <spring:message code="label.configuration.process.institutionalRequestDocumentPrefix" />
                </label>
                <div class="col-sm-10">
                    <input type="text" name="institutionalRequestDocumentPrefix" class="form-control" value=${institutionalRequestDocumentPrefix}>
                </div>
            </div>

            
            <div class="form-group">
                <label for="acquisitionCreationWizardJsp" class="col-sm-2 control-label">
                    <spring:message code="label.configuration.process.creation.interface"/>
                </label>
                <div class="col-sm-10">
                    <input type="text" name="acquisitionCreationWizardJsp" class="form-control" value=${acquisitionCreationWizardJsp}>
                </div>
            </div>

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
                                    <input type="checkbox" name="${entry.key.name()}" ${entry.value ? "checked='checked'" : ""}>
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
                                <input type="checkbox" name="isPriorConsultationAvailable" ${expenditureTrackingSystem.isPriorConsultationAvailable() ? "checked='checked'" : ""}>
                            </td>
                        </tr>
                
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.flow.start.with.invoice"/>
                            </td>
                            <td>
                                <input type="checkbox" name="invoiceAllowedToStartAcquisitionProcess" ${(expenditureTrackingSystem.getInvoiceAllowedToStartAcquisitionProcess() != null && expenditureTrackingSystem.getInvoiceAllowedToStartAcquisitionProcess().booleanValue()) ? "checked='checked'" : ""}>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.flow.start.with.invoice.limit"/>
                            </td>
                            <td>
                                <input type="text" name="maxValueStartedWithInvoive" class="form-control" value="${expenditureTrackingSystem.getMaxValueStartedWithInvoive() != null ? expenditureTrackingSystem.getMaxValueStartedWithInvoive().getValue() : ""}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.flow.require.fund.allocation.prior.to.acquisition.request" />
                            </td>
                            <td>
                                <input type="checkbox" name="requireFundAllocationPriorToAcquisitionRequest" ${(expenditureTrackingSystem.getRequireFundAllocationPriorToAcquisitionRequest() != null && expenditureTrackingSystem.getRequireFundAllocationPriorToAcquisitionRequest().booleanValue()) ? "checked='checked'" : ""}>
                            </td>
                        </tr>

                        
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.flow.register.diary.numbers.and.transaction.numbers" />
                            </td>
                            <td>
                                <input type="checkbox" name="registerDiaryNumbersAndTransactionNumbers" ${(expenditureTrackingSystem.getRegisterDiaryNumbersAndTransactionNumbers() != null && expenditureTrackingSystem.getRegisterDiaryNumbersAndTransactionNumbers().booleanValue()) ? "checked='checked'" : ""}>
                            </td>
                        </tr>

                        
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.value.requireing.top.level.authorization"/>
                            </td>
                            <td>
                                <input type="text" name="valueRequireingTopLevelAuthorization" class="form-control" value="${expenditureTrackingSystem.getValueRequireingTopLevelAuthorization() != null ? expenditureTrackingSystem.getValueRequireingTopLevelAuthorization().getValue() : ""}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.flow.requireCommitmentNumber" />
                            </td>
                            <td>
                                <input type="checkbox" name="requireCommitmentNumber" ${(expenditureTrackingSystem.getRequireCommitmentNumber() != null && expenditureTrackingSystem.getRequireCommitmentNumber().booleanValue()) ? "checked='checked'" : ""}>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <spring:message code="label.configuration.process.flow.processesNeedToBeReverified" />
                            </td>
                            <td>
                                <input type="checkbox" name="processesNeedToBeReverified" ${(expenditureTrackingSystem.getProcessesNeedToBeReverified() != null && expenditureTrackingSystem.getProcessesNeedToBeReverified().booleanValue()) ? "checked='checked'" : ""}>
                            </td>
                        </tr>
                        
                        <tr>

							<td> <spring:message code="label.configuration.process.flow.approvalTextForRapidAcquisitions" /></td>
							<td>
								<textarea  cols="75" rows="10" bennu-localized-string id="approvalTextForRapidAcquisitions" name="approvalTextForRapidAcquisitions"  >${(expenditureTrackingSystem.getApprovalTextForRapidAcquisitions() != null) ? expenditureTrackingSystem.getApprovalTextForRapidAcquisitions().json(): ""}</textarea>
							</td>
						</tr>

                            <td>
                                <spring:message code="label.configuration.process.force.refund.association.missions"/>
                            </td>
                            <td>
                                <input type="checkbox" name="isForceRefundAssociationToMissions" ${(expenditureTrackingSystem.getForceRefundAssociationToMissions() != null && expenditureTrackingSystem.getForceRefundAssociationToMissions().booleanValue()) ? "checked='checked'" : ""}>
                            </td>
                         </tr>
                         <tr>   
                            <td>
                                <spring:message code="label.configuration.process.flow.allowAdvancePayments"/>
                            </td>
                            <td>
                                <input type="checkbox" name="allowAdvancePayments" ${(expenditureTrackingSystem.getAllowedAdvancePayments() != null && expenditureTrackingSystem.getAllowedAdvancePayments().booleanValue()) ? "checked='checked'" : ""}>
                            </td>
                        </tr>
         

                    </tbody>
                </table>
            </div>

            <h4>
                <spring:message code="label.configuration.process.documentation"/>
            </h4>


            <div class="form-group">
                <label for="documentationUrl" class="col-sm-2 control-label">
                    <spring:message code="label.configuration.process.documentation.documentationUrl"/>
                </label>
                <div class="col-sm-10">
                    <input type="text" name="documentationUrl" class="form-control" value="${expenditureTrackingSystem.documentationUrl}">
                </div>
            </div>
            
            <div class="form-group">
                <label for="documentationLabel" class="col-sm-2 control-label">
                    <spring:message code="label.configuration.process.documentation.documentationLabel"/>
                </label>
                <div class="col-sm-10">
                    <input type="text" name="documentationLabel" class="form-control" value="${expenditureTrackingSystem.documentationLabel}">
                </div>
            </div>
            
            <div class="form-group">
                <label for="createSupplierUrl" class="col-sm-2 control-label">
                    <spring:message code="label.configuration.process.documentation.createSupplierUrl"/>
                </label>
                <div class="col-sm-10">
                    <input type="text" name="createSupplierUrl" class="form-control" value="${expenditureTrackingSystem.createSupplierUrl}">
                </div>
            </div>
            
            <div class="form-group">
                <label for="createSupplierLabel" class="col-sm-2 control-label">
                    <spring:message code="label.configuration.process.documentation.createSupplierLabel"/>
                </label>
                <div class="col-sm-10">
                    <input type="text" name="createSupplierLabel" class="form-control" value="${expenditureTrackingSystem.createSupplierLabel}">
                </div>
            </div>

			<div class="form-group">
                <label for="acquisitionsUnit" class="col-sm-2 control-label">
                    <spring:message code="label.configuration.process.documentation.acquisitionUnit"/>
                </label>
                <div class="col-sm-10">
                	<textarea  cols="75" rows="1" bennu-localized-string id="acquisitionsUnit" name="acquisitionsUnit"  >${(expenditureTrackingSystem.getAcquisitionsUnit() != null) ? expenditureTrackingSystem.getAcquisitionsUnit().json(): ""}</textarea>
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