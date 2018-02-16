<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.SupplierCriteriaSelectionDocument"%>
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

<% final MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) request.getAttribute("process"); %>
<% final MultipleSupplierConsultation consultation = process.getConsultation(); %>

<div class="infobox">
<div style="float: right; width: 30%; text-align: center;">
    <br/>
    <span style="font-weight: bold;"><bean:message key="label.consultation.process.contractManager" bundle="EXPENDITURE_RESOURCES"/>:</span>
    <br/>
    <br/>
    <% if (consultation.getContractManager() == null) { %>
    <% } else { %>
            <img class="img-circle" width="75" height="75" src="<%= consultation.getContractManager().getProfile().getAvatarUrl() %>">
            <br/>
            <br/>
            <%= consultation.getContractManager().getDisplayName() + " (" + consultation.getContractManager().getUsername() + ")" %>
    <% } %>
</div>
<div style="width: 70%;">
    <table class="process-info mbottom0">
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
                <%= consultation.getProposalDeadline() == null ? "" : consultation.getProposalDeadline().toString("yyyy-MM-dd") %>
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
    </table>
    <span style="font-weight: bold;"><bean:message key="label.consultation.process.justification" bundle="EXPENDITURE_RESOURCES"/>:</span>
    <pre><%= consultation.getJustification() %></pre>
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
            <tr>
                <td><%= part.getNumber() %></td>
                <td><%= part.getMaterial().getFullDescription().replace(" (", "<br/>(") %></td>
                <td><%= part.getDescription() %></td>
                <td><%= part.getValue().toFormatString() %></td>
                <td>
                    <bean:message key="label.delete" bundle="EXPENDITURE_RESOURCES"/>
                </td>
            </tr>
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
    <% if (process.getFiles(SupplierCriteriaSelectionDocument.class).isEmpty()) { %>
        <bean:message key="label.consultation.process.supplierSelectionCriteria.price" bundle="EXPENDITURE_RESOURCES"/>
    <% } else { %>
        <bean:message key="label.consultation.process.supplierSelectionCriteria.priceQualityRelation" bundle="EXPENDITURE_RESOURCES"/>
    <% } %>
    <br/>
    <br/>
    <span style="font-weight: bold;"><bean:message key="label.consultation.process.tieBreakCriteria" bundle="EXPENDITURE_RESOURCES"/>:</span>
    <ul>
        <% for (final TieBreakCriteria criteria : consultation.getOrderedTieBreakCriteriaSet()) { %>
            <li>
                <%= criteria.getCriteriaOrder() %>.
                <%= criteria.getDescription() %>
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
        </tr>
        <% for (final MultipleSupplierConsultationFinancer financer : consultation.getFinancerSet()) { %>
            <tr>
                <td><%= financer.getUnit().getUnit().getPresentationName() %></td>
                <td><%= financer.getPercentage() %></td>
                <td><%= financer.getValue().toFormatString() %></td>
                <td>
                    <bean:message key="label.delete" bundle="EXPENDITURE_RESOURCES"/>
                </td>
            </tr>
        <% } %>
    </table>
</div>

<div>
    <table class="tview1" style="width: 100%;">
        <tr>
            <th>
            </th>
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
                <td><img class="img-circle" width="75" height="75" src="<%= juryMember.getUser().getProfile().getAvatarUrl() %>"></td>
                <td><%= juryMember.getUser().getDisplayName() + " (" + juryMember.getUser().getUsername() + ")" %></td>
                <td><%= juryMember.getJuryMemberRole().name() %></td>
                <td>
                    <bean:message key="label.delete" bundle="EXPENDITURE_RESOURCES"/>
                </td>
            </tr>
        <% } %>
    </table>
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
        <% for (final Supplier supplier : consultation.getSupplierSet()) { %>
            <tr>
                <td rowspan="3"><%= supplier.getFiscalIdentificationCode() %></td>
                <td rowspan="3"><%= supplier.getName() %></td>
                <td rowspan="3"><pre style="border: none; background: none;"><%= supplier.getAddress().print() %></pre></td>
                <td>
                    <bean:message key="label.consultation.process.contact.email" bundle="EXPENDITURE_RESOURCES"/>
                </td>
                <td><%= supplier.getEmail() %></td>
                <td rowspan="3">
                    <bean:message key="label.delete" bundle="EXPENDITURE_RESOURCES"/>
                </td>
            </tr>
            <tr>
                <td>
                    <bean:message key="label.consultation.process.contact.phone" bundle="EXPENDITURE_RESOURCES"/>
                </td>
                <td><%= supplier.getPhone() %></td>
            </tr>
            <tr>
                <td>
                    <bean:message key="label.consultation.process.contact.fax" bundle="EXPENDITURE_RESOURCES"/>
                </td>
                <td><%= supplier.getFax() %></td>
            </tr>
        <% } %>
    </table>
</div>
