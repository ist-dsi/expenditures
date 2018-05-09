<%@page import="org.fenixedu.bennu.core.i18n.BundleUtil"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document.SupplierCandidacyDocument"%>
<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationPartYearExecution"%>
<%@page import="module.mission.domain.util.MissionStateProgress"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document.SupplierCriteriaSelectionDocument"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.TieBreakCriteria"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationJuryMember"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationFinancer"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationPart"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess"%>
<% final String contextPath = request.getContextPath(); %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>

<% final MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) request.getAttribute("process"); %>
<% final MultipleSupplierConsultation consultation = process.getConsultation(); %>

<jsp:include page="warnings.jsp"/>

<style>
* {
    box-sizing: border-box;
}

/* Create three unequal columns that floats next to each other */
.column {
    float: left;
    padding: 10px;
}

.left {
  width: 75%;
}

.right {
  width: 25%;
}


/* Clear floats after the columns */
.row:after {
    content: "";
    display: table;
    clear: both;
}

.state {
    text-align: center;
    border-style: solid;
    border-width: thin;
    border-color: #ccc;
    padding: 5px;
    margin-bottom: 10px;
    border-radius: 2em;
    -moz-border-radius: 2em;
}

.pending {
    background-color: #F6E3CE;
    border-color: #B45F04;
}

.complete {
    background-color: #CEF6CE;
    border-color: #04B404;
}

.stateDescription {
    border-collapse: separate;
    border-spacing: 10px;
    border-style: dotted;
    border-width: thin;
    float: right;
    padding-right: 20px;
    margin-right: 50px;
}

.stateDescriptionIdle {
    border-style: solid;
    border-width: thin;
    width: 12px;
    padding: 5px;
    border-radius: 2em;
    -moz-border-radius: 2em;
}

.stateDescriptionPending {
    background-color: #F6E3CE;
    border-color: #B45F04;
    border-style: solid;
    border-width: thin;
    width: 12px;
    adding: 5px;
    border-radius: 2em;
    -moz-border-radius: 2em;
}

.stateDescriptionComplete {
    background-color: #CEF6CE;
    border-color: #04B404;
    border-style: solid;
    border-width: thin;
    width: 12px;
    padding: 5px;
    border-radius: 2em;
    -moz-border-radius: 2em;
}
</style>

<div class="row infobox">
    <div class="column left">
        <table class="process-info mbottom0 table" style="width: 100%;">
            <tr>
                <th>
                    <bean:message key="label.consultation.process.contractType" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <td>
                    <%= consultation.getContractType().getName().getContent() %>
                </td>
            </tr>
            <tr>
                <th>
                    <bean:message key="label.consultation.process.material" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <td>
                    <%= consultation.getMaterial().getFullDescription() %>
                </td>
            </tr>
            <tr>
                <th>
                    <bean:message key="label.consultation.process.description" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <td>
                    <%= consultation.getDescription() %>
                </td>
            </tr>
            <tr>
                <th>
                    <bean:message key="label.consultation.process.contractDuration" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <td>
                    <%= consultation.getContractDuration() == null ? "N/A" : consultation.getContractDuration() %>
                </td>
            </tr>
            <tr>
                <th>
                    <bean:message key="label.consultation.process.value" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <td>
                    <%= consultation.getValue().toFormatString() %>
                </td>
            </tr>
            <tr>
                <th>
                    <bean:message key="label.consultation.process.proposalDeadline" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <td>
                    <%= consultation.getProposalDeadline() == null ? "" : consultation.getProposalDeadline() %>
                </td>
            </tr>
            <tr>
                <th>
                    <bean:message key="label.consultation.process.proposalValidity" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <td>
                    <%= consultation.getProposalValidity() == null ? "" : consultation.getProposalValidity() %>
                </td>
            </tr>
            <tr>
                <th>
                    <bean:message key="label.consultation.process.collateral" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <td>
                    <%= consultation.getCollateral() %>
                </td>
            </tr>
            <tr>
                <th>
                    <bean:message key="label.consultation.process.negotiation" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <td>
                    <% if (consultation.getNegotiation() != null && consultation.getNegotiation().booleanValue()) { %>
                        <bean:message key="label.yes" bundle="EXPENDITURE_RESOURCES"/>
                    <% } else { %>
                        <bean:message key="label.no" bundle="EXPENDITURE_RESOURCES"/>
                    <% } %>
                </td>
            </tr>
        </table>
        <span style="font-weight: bold;"><bean:message key="label.consultation.process.justification" bundle="EXPENDITURE_RESOURCES"/>:</span>
        <pre><%= consultation.getJustification() %></pre>
        <div>
            <table class="table" style="width: 100%;">
                <tr>
                    <th>
                        <bean:message key="label.consultation.process.contractManager" bundle="EXPENDITURE_RESOURCES"/>
                    </th>
                    <th>
                        <bean:message key="label.consultation.process.contractSecretary" bundle="EXPENDITURE_RESOURCES"/>
                    </th>
                    <th>
                    </th>
                </tr>
                <% if (consultation.getContractManager() == null && consultation.getContractSecretary() == null) { %>
                <% } else { %>
                        <tr>
                            <% if (consultation.getContractManager() != null) { %>
                                <td>
                                    <img class="img-circle" width="75" height="75" src="<%= consultation.getContractManager().getProfile().getAvatarUrl() %>" style="margin-right: 15px;">
                                    <a href='<%= contextPath + "/expenditureTrackingOrganization.do?method=viewPerson&personOid=" + consultation.getContractManager().getExpenditurePerson().getExternalId() %>' class="secondaryLink">
                                        <%= consultation.getContractManager().getDisplayName() + " (" + consultation.getContractManager().getUsername() + ")" %>
                                    </a>
                                </td>
                            <% } else { %>
                                <td></td>
                            <% } %>
                            <% if (consultation.getContractSecretary() != null) { %>
                                <td>
                                    <img class="img-circle" width="75" height="75" src="<%= consultation.getContractSecretary().getProfile().getAvatarUrl() %>" style="margin-right: 15px;">
                                    <a href='<%= contextPath + "/expenditureTrackingOrganization.do?method=viewPerson&personOid=" + consultation.getContractSecretary().getExpenditurePerson().getExternalId() %>' class="secondaryLink">
                                        <%= consultation.getContractSecretary().getDisplayName() + " (" + consultation.getContractSecretary().getUsername() + ")" %>
                                    </a>
                                </td>
                            <% } else { %>
                                <td></td>
                            <% } %>
                            <td>
                            </td>
                        </tr>
                <% } %>
                <tr>
                    <th>
                        <bean:message key="label.consultation.process.juryMember" bundle="EXPENDITURE_RESOURCES"/>
                    </th>
                    <th>
                        <bean:message key="label.consultation.process.juryMember.role" bundle="EXPENDITURE_RESOURCES"/>
                    </th>
                    <th>
                    </th>
                </tr>
                <% for (final MultipleSupplierConsultationJuryMember juryMember : consultation.getOrderedJuryMemberSet()) { %>
                    <tr>
                        <td>
                            <img class="img-circle" width="75" height="75" src="<%= juryMember.getUser().getProfile().getAvatarUrl() %>"  style="margin-right: 15px;">
                            <a href='<%= contextPath + "/expenditureTrackingOrganization.do?method=viewPerson&personOid=" + juryMember.getUser().getExpenditurePerson().getExternalId() %>' class="secondaryLink">
                                <%= juryMember.getUser().getDisplayName() + " (" + juryMember.getUser().getUsername() + ")" %>
                            </a>
                        </td>
                        <td style="vertical-align: middle;">
                            <%= juryMember.getJuryMemberRole().getLocalizedName() %>
                            <% if (consultation.getPresidentSubstitute() == juryMember) { %>
                                    <br/>
                                    <span style="color: gray;">(<bean:message key="label.consultation.process.juryMember.presidentialSubstitute" bundle="EXPENDITURE_RESOURCES"/>)</span>
                            <% } %>
                        </td>
                        <td>
                            <wf:activityLink id='<%= "RemoveJuryMember-" + juryMember.getExternalId() %>' processName="process" activityName="RemoveJuryMember" scope="request" paramName0="juryMember" paramValue0="<%= juryMember.getExternalId() %>">
                                <bean:message key="label.delete" bundle="EXPENDITURE_RESOURCES"/>
                            </wf:activityLink>
                        </td>
                    </tr>
                <% } %>
            </table>
        </div>
    </div>
    <div class="column right">
        <ul style="list-style: none;">
            <% MultipleSupplierConsultationProcessState currentState = process.getState(); %>
            <% if (currentState == MultipleSupplierConsultationProcessState.CANCELLED) { %>
                <li class="state">
                    <%= MultipleSupplierConsultationProcessState.CANCELLED.getLocalizedName() %>
                </li>
            <% } else { %>
                <% String previousName = ""; %>
                <% for (final MultipleSupplierConsultationProcessState state : MultipleSupplierConsultationProcessState.values()) { %>
                    <% if (state != MultipleSupplierConsultationProcessState.CANCELLED && !previousName.equals(state.getCompletedTitle())) { %>
                        <% String styleClass = currentState == state ? "pending" : currentState.compareTo(state) > 0 ? "complete" : ""; %>
                        <li class="state <%= styleClass %>">
                            <%= state.getCompletedTitle() %>
                        </li>
                        <% previousName = state.getCompletedTitle(); %>
                    <% } %>
                <% } %>
            <% } %>
        </ul>
        <br/>
        <div style="text-align: center; width: 100%">
            <table class="stateDescription">
                <tr>
                    <td align="center" colspan="2">
                        <strong>
                            <bean:message bundle="MISSION_RESOURCES" key="label.mission.state.view.label"/>
                        </strong>
                    </td>
                </tr>
                <tr>
                    <td class="stateDescriptionIdle">
                    </td>
                    <td>
                        <%=MissionStateProgress.IDLE.getLocalizedName()%>
                    </td>
                </tr>
                <tr>
                    <td class="stateDescriptionPending">
                    </td>
                    <td>
                        <%=MissionStateProgress.PENDING.getLocalizedName()%>
                    </td>
                </tr>
                <tr>
                    <td class="stateDescriptionComplete">
                    </td>
                    <td>
                        <%=MissionStateProgress.COMPLETED.getLocalizedName()%>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>

<div>
    <table class="tview1" style="width: 100%;">
        <tr>
            <th>
                <bean:message key="label.consultation.process.part.number" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <th>
                <bean:message key="label.consultation.process.material" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <th>
                <bean:message key="label.consultation.process.description" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <th>
                <bean:message key="label.consultation.process.value" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <th>
            </th>
        </tr>
        <% for (final MultipleSupplierConsultationPart part : consultation.getOrderedPartSet()) { %>
            <% int partSpan = part.getYearExecutionSet().size() + 1 + (part.getSupplier() == null ? 0 : 1); %>
            <% boolean isFirstYear = true; %>
            <tr>
                <td rowspan="<%= partSpan %>"><%= part.getNumber() %></td>
                <td><%= part.getMaterial().getFullDescription().replace(" (", "<br/>(") %></td>
                <td><%= part.getDescription() %></td>
                <td><%= part.getValue().toFormatString() %></td>
                <td>
                    <wf:activityLink id='<%= "FillPartExecutionByYear-" + part.getExternalId() %>' processName="process" activityName="FillPartExecutionByYear" scope="request" paramName0="part" paramValue0="<%= part.getExternalId() %>">
                        <bean:message key="activity.FillPartExecutionByYear" bundle="EXPENDITURE_RESOURCES"/>
                    </wf:activityLink>
                    <br/>
                    <br/>
                    <wf:activityLink id='<%= "RemoveMultipleSupplierConsultationPart-" + part.getExternalId() %>' processName="process" activityName="RemoveMultipleSupplierConsultationPart" scope="request" paramName0="part" paramValue0="<%= part.getExternalId() %>">
                        <bean:message key="label.delete" bundle="EXPENDITURE_RESOURCES"/>
                    </wf:activityLink>
                    <wf:activityLink id='<%= "SelectSupplierForConsultation-" + part.getExternalId() %>' processName="process" activityName="SelectSupplierForConsultation" scope="request" paramName0="part" paramValue0="<%= part.getExternalId() %>">
                        <bean:message key="activity.SelectSupplierForConsultation" bundle="EXPENDITURE_RESOURCES"/>
                    </wf:activityLink>
                </td>
            </tr>
            <% for (final MultipleSupplierConsultationPartYearExecution yearExecution : part.getOrderedYearExecutionSet()) { %>
                <tr>
                    <% if (isFirstYear) { %>
                        <th rowspan="<%= partSpan - 1 - (part.getSupplier() == null ? 0 : 1) %>">
                            <bean:message key="label.consultation.process.part.execution.by.year" bundle="EXPENDITURE_RESOURCES"/>
                        </th>
                        <% isFirstYear = false; %>
                    <% } %>
                    <td><%= yearExecution.getYear() %></td>
                    <td><%= yearExecution.getValue().toFormatString() %></td>
                    <td>
                        <wf:activityLink id='<%= "RemoveMultipleSupplierConsultationPartYearExecution-" + yearExecution.getExternalId() %>' processName="process" activityName="RemoveMultipleSupplierConsultationPartYearExecution" scope="request" paramName0="yearExecution" paramValue0="<%= yearExecution.getExternalId() %>">
                            <bean:message key="label.delete" bundle="EXPENDITURE_RESOURCES"/>
                        </wf:activityLink>
                    </td>
                </tr>
            <% } %>
            <% if (part.getSupplier() != null) { %>
                <tr>
                    <th rowspan="<%= partSpan - 1 %>">
                        <bean:message key="label.consultation.process.part.selected.supplier" bundle="EXPENDITURE_RESOURCES"/>
                    </th>
                    <td colspan="2">
                        <html:link styleClass="secondaryLink" page='<%= "/expenditureTrackingOrganization.do?method=manageSuppliers&supplierOid=" + part.getSupplier().getExternalId() %>' target="_blank">
                            <%= part.getSupplier().getPresentationName() %>
                        </html:link>
                    </td>
                    <td>
                    </td>
                </tr>         
            <% } %>
        <% } %>
    </table>
</div>

<div class="infobox">
    <span style="font-weight: bold;"><bean:message key="label.consultation.process.priceLimitJustification" bundle="EXPENDITURE_RESOURCES"/>:</span>
    <pre><%= consultation.getPriceLimitJustification() == null ? "" : consultation.getPriceLimitJustification() %></pre>
    <br/>
    <span style="font-weight: bold;"><bean:message key="label.consultation.process.lowPriceLimit" bundle="EXPENDITURE_RESOURCES"/>:</span>
    <% if (consultation.getLowPriceLimit() == null) { %>
        <bean:message key="label.consultation.process.lowPriceLimit.not.applicable" bundle="EXPENDITURE_RESOURCES"/>
        <br/>
    <% } else { %>
        <%= consultation.getLowPriceLimit().toFormatString() %>
        <br/>
        <br/>
        <span style="font-weight: bold;"><bean:message key="label.consultation.process.lowPriceLimitJustification" bundle="EXPENDITURE_RESOURCES"/>:</span>
        <pre><%= consultation.getLowPriceLimitJustification() == null ? "" : consultation.getLowPriceLimitJustification() %></pre>
        <br/>
        <span style="font-weight: bold;"><bean:message key="label.consultation.process.lowPriceLimitCriteria" bundle="EXPENDITURE_RESOURCES"/>:</span>
        <pre><%= consultation.getLowPriceLimitCriteria() == null ? "" : consultation.getLowPriceLimitCriteria() %></pre>
    <% } %>
    <br/>
    <span style="font-weight: bold;"><bean:message key="label.consultation.process.supplierCountJustification" bundle="EXPENDITURE_RESOURCES"/>:</span>
    <pre><%= consultation.getSupplierCountJustification() == null ? "" : consultation.getSupplierCountJustification() %></pre>
    <br/>
    <span style="font-weight: bold;"><bean:message key="label.consultation.process.supplierSelectionCriteria" bundle="EXPENDITURE_RESOURCES"/>:</span>
    <br/>
    <% if (consultation.getSpecificEvaluationMethod() == null || !consultation.getSpecificEvaluationMethod().booleanValue()) { %>
        <bean:message key="label.consultation.process.supplierSelectionCriteria.price" bundle="EXPENDITURE_RESOURCES"/>
    <% } else { %>
        <bean:message key="label.consultation.process.supplierSelectionCriteria.priceQualityRelation" bundle="EXPENDITURE_RESOURCES"/>
    <% } %>
        <br/>
        <br/>
        <span style="font-weight: bold;"><bean:message key="label.consultation.process.evaluationMethodJustification" bundle="EXPENDITURE_RESOURCES"/>:</span>
        <pre><%= consultation.getEvaluationMethodJustification() == null ? "" : consultation.getEvaluationMethodJustification() %></pre>
    

    <br/>
    <br/>
    <span style="font-weight: bold;"><bean:message key="label.consultation.process.tieBreakCriteria" bundle="EXPENDITURE_RESOURCES"/>:</span>
    <ul>
        <% for (final TieBreakCriteria criteria : consultation.getOrderedTieBreakCriteriaSet()) { %>
            <li>
                <%= criteria.getCriteriaOrder() %>.
                <%= criteria.getDescription() %>
                <span>
                    <wf:activityLink id='<%= "RemoveTieBreakCriteria-" + criteria.getExternalId() %>' processName="process" activityName="RemoveTieBreakCriteria" scope="request" paramName0="tieBreakCriteria" paramValue0="<%= criteria.getExternalId() %>">
                        <bean:message key="label.delete" bundle="EXPENDITURE_RESOURCES"/>
                    </wf:activityLink>
                </span>
            </li>
        <% } %>
    </ul>
</div>

<div>
    <table class="tview1" style="width: 100%;">
        <tr>
            <th>
                <bean:message key="label.consultation.process.financer" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <th>
                <bean:message key="label.consultation.process.financer.percentage" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <th>
                <bean:message key="label.consultation.process.value" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <th>
            </th>
            <th>
                <bean:message key="label.consultation.process.financer.fundAllocation" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <th>
            </th>
        </tr>
        <% for (final MultipleSupplierConsultationFinancer financer : consultation.getOrderedFinancerSet()) { %>
            <tr>
                <td>
                    <html:link styleClass="secondaryLink" page='<%= "/expenditureTrackingOrganization.do?method=viewOrganization&unitOid=" + financer.getUnit().getExternalId() %>'>
                        <%= financer.getUnit().getUnit().getPresentationName() %>
                    </html:link>
                </td>
                <td><%= financer.getPercentage() %></td>
                <td><%= financer.getValue().toFormatString() %></td>
                <td>
                    <% if (financer.isApproved()) { %>
                        <span style="color: green;">
                            <bean:message key="label.approved" bundle="EXPENDITURE_RESOURCES"/>
                        </span>
                    <% } %>
                </td>
                <td>
                    <%= financer.getFundAllocation() == null ? "" : financer.getFundAllocation() %>
                    <% if (financer.getFundReservation() != null) { %>
                        <br/>
                        <bean:message key="label.consultation.process.financer.fundReservation" bundle="EXPENDITURE_RESOURCES"/>:
                        <%= financer.getFundReservation() == null ? "" : financer.getFundReservation() %>
                    <% } %>
                </td>
                <td>
                    <wf:activityLink id='<%= "RemoveFinancer-" + financer.getExternalId() %>' processName="process" activityName="RemoveFinancer" scope="request" paramName0="financer" paramValue0="<%= financer.getExternalId() %>">
                        <bean:message key="label.delete" bundle="EXPENDITURE_RESOURCES"/>
                    </wf:activityLink>
                    <% if (financer.getFundReservation() == null) { %>
                        <wf:activityLink id='<%= "ReserveFunds-" + financer.getExternalId() %>' processName="process" activityName="ReserveFunds" scope="request" paramName0="financer" paramValue0="<%= financer.getExternalId() %>">
                            <bean:message key="activity.ReserveFunds" bundle="EXPENDITURE_RESOURCES"/>
                        </wf:activityLink>
                    <% } %>
                    <% if (financer.getFundReservation() != null) { %>
                        <wf:activityLink id='<%= "UnReserveFunds-" + financer.getExternalId() %>' processName="process" activityName="UnReserveFunds" scope="request" paramName0="financer" paramValue0="<%= financer.getExternalId() %>">
                            <bean:message key="activity.UnReserveFunds" bundle="EXPENDITURE_RESOURCES"/>
                        </wf:activityLink>
                    <% } %>
                    <% if (financer.getFundAllocation() == null) { %>
                        <br/>
                        <wf:activityLink id='<%= "AllocateFunds-" + financer.getExternalId() %>' processName="process" activityName="AllocateFunds" scope="request" paramName0="financer" paramValue0="<%= financer.getExternalId() %>">
                            <bean:message key="activity.AllocateFunds" bundle="EXPENDITURE_RESOURCES"/>
                        </wf:activityLink>
                    <% } %>
                    <% if (financer.getFundAllocation() != null) { %>
                        <wf:activityLink id='<%= "UnAllocateFunds-" + financer.getExternalId() %>' processName="process" activityName="UnAllocateFunds" scope="request" paramName0="financer" paramValue0="<%= financer.getExternalId() %>">
                            <bean:message key="activity.UnAllocateFunds" bundle="EXPENDITURE_RESOURCES"/>
                        </wf:activityLink>
                    <% } %>
                </td>
            </tr>
        <% } %>
    </table>
    <ul>
        <li>
            <bean:message key="label.consultation.process.expeseProcessIdentification" bundle="EXPENDITURE_RESOURCES"/>: <%= consultation.getExpenseProcessIdentification() == null ? "" : consultation.getExpenseProcessIdentification() %>
        </li>
        <li>
            <bean:message key="label.consultation.process.acquisitionRequestNumber" bundle="EXPENDITURE_RESOURCES"/>: <%= consultation.getAcquisitionRequestNumber() == null ? "" : consultation.getAcquisitionRequestNumber() %>
        </li>
        <li>
            <bean:message key="label.commitmentNumber" bundle="EXPENDITURE_RESOURCES"/>: <%= consultation.getFundCommitmentNumber() == null ? "" : consultation.getFundCommitmentNumber() %>
        </li>
        <li>
            <bean:message key="label.consultation.process.acquisitionProcessIdentification" bundle="EXPENDITURE_RESOURCES"/>: <%= consultation.getAcquisitionProcessIdentification() == null ? "" : consultation.getAcquisitionProcessIdentification() %>
        </li>
    </ul>
</div>

<div>
    <table class="tview1" style="width: 100%;">
        <tr>
            <th>
                <bean:message key="label.consultation.process.supplier.fiscalIdentificationCode" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <th>
                <bean:message key="label.consultation.process.supplier" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <th>
                <bean:message key="label.consultation.process.address" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <th colspan="2">
                <bean:message key="label.consultation.process.contacts" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <th>
            </th>
        </tr>
        <% for (final Supplier supplier : consultation.getOrderedSupplierSet()) { %>
            <tr>
                <td rowspan="4">
                    <html:link styleClass="secondaryLink" page='<%= "/expenditureTrackingOrganization.do?method=manageSuppliers&supplierOid=" + supplier.getExternalId() %>'>
                        <%= supplier.getFiscalIdentificationCode() %>
                    </html:link>
                </td>
                <td rowspan="4">
                    <html:link styleClass="secondaryLink" page='<%= "/expenditureTrackingOrganization.do?method=manageSuppliers&supplierOid=" + supplier.getExternalId() %>'>
                        <%= supplier.getName() %>
                    </html:link>
                </td>
                <td rowspan="3"><pre style="border: none; background: none;"><%= supplier.getAddress().print() %></pre></td>
                <td>
                    <bean:message key="label.consultation.process.contact.email" bundle="EXPENDITURE_RESOURCES"/>
                </td>
                <td>
                    <a href="mailto:<%= supplier.getEmail() %> %>">
                        <%= supplier.getEmail() %>
                    </a>
                </td>
                <td rowspan="3">
                    <wf:activityLink id='<%= "RemoveSupplier-" + supplier.getExternalId() %>' processName="process" activityName="RemoveSupplier" scope="request" paramName0="supplier" paramValue0="<%= supplier.getExternalId() %>">
                        <bean:message key="label.delete" bundle="EXPENDITURE_RESOURCES"/>
                    </wf:activityLink>
                </td>
            </tr>
            <tr>
                <td>
                    <bean:message key="label.consultation.process.contact.phone" bundle="EXPENDITURE_RESOURCES"/>
                </td>
                <td>
                    <a href="tel:<%= supplier.getPhone() %> %>">
                        <%= supplier.getPhone() %>
                    </a>
                </td>
            </tr>
            <tr>
                <td>
                    <bean:message key="label.consultation.process.contact.fax" bundle="EXPENDITURE_RESOURCES"/>
                </td>
                <td><%= supplier.getFax() %></td>
            </tr>
            <tr>
                <th>
                    <bean:message key="label.documents" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <td colspan="2">
                    <ul style="text-align: left;">
                        <% for (final SupplierCandidacyDocument document : supplier.getSupplierCandidacyDocumentSet()) { %>
                            <% if (document.getProcess() != null) { %>
                                <li>
                                    <a href="<%= contextPath %>/workflowProcessManagement.do?method=downloadFile&processId=<%= process.getExternalId() %>&fileId=<%= document.getExternalId() %>">
                                        <%= document.getDisplayName() %>
                                    </a>
                                    <% final String confirmSupplierDocRemove = BundleUtil.getString("resources/WorkflowResources", "label.fileRemoval.confirmation", document.getDisplayName()); %>
                                    <a href="<%= contextPath %>/workflowProcessManagement.do?method=removeFile&processId=<%= process.getExternalId() %>&fileId=<%= document.getExternalId() %>" class="secondaryLink"
                                        onclick="return confirm('<%= confirmSupplierDocRemove %>');">
                                        (<bean:message key="link.removeFile" bundle="WORKFLOW_RESOURCES"/>)
                                    </a>
                                </li>
                            <% } %>
                        <% } %>
                    </ul>
                </td>
            </tr>
        <% } %>
    </table>
</div>
