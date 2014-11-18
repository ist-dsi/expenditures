<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact"%>
<%@page import="pt.ist.expenditureTrackingSystem.presentationTier.actions.organization.util.RefundForSupplierAndCPVBean"%>
<%@page import="java.util.SortedSet"%>
<%@page import="pt.ist.expenditureTrackingSystem.presentationTier.actions.organization.util.AcquisitionForSupplierAndCPVBean"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.Map"%>
<%@page import="module.finance.util.Money"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference"%>
<%@page import="java.util.Map.Entry"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<%
	final Supplier supplier = (Supplier) request.getAttribute("supplier");
	final CPVReference cpvReference = (CPVReference) request.getAttribute("cpvReference");
%>

	<div class="infobox">
		<h3>
			<bean:message key="label.supplier" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
		</h3>
		<p>
			<bean:message key="supplier.label.fiscalCode" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
			<span style="font-weight: bold;">
				<bean:write name="supplier" property="fiscalIdentificationCode"/>
			</span>
			&nbsp;&nbsp;&nbsp;
			<bean:message key="supplier.label.name" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
			<span style="font-weight: bold;">
				<bean:write name="supplier" property="name"/>
			</span>
			&nbsp;&nbsp;&nbsp;
			<bean:message key="supplier.label.abbreviatedName" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
			<bean:write name="supplier" property="abbreviatedName"/>
		</p>
		<p>
			<bean:message key="supplier.soft.limit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
			<span style="font-weight: bold;">
				<%= supplier.getSupplierLimit().toFormatString() %>
			</span>
			&nbsp;&nbsp;&nbsp;
			<bean:message key="supplier.label.nib" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
			<bean:write name="supplier" property="nib"/>
		</p>
	</div>

	<h3>
		<bean:message key="label.list.processes.for.supplier.by.cpv" bundle="MISSION_RESOURCES"/>
		<% if (cpvReference == null) { %>
			--
		<% } else { %>
			<%= cpvReference.getFullDescription() %>
		<% } %>
	</h3>

	<%
		final Map<CPVReference, Money> confirmedValues = supplier.getAllocationsByCPVReference();
		final Map<CPVReference, Money> unConfirmedValues = supplier.getUnconfirmedAllocationsByCPVReference();
	%>
	<ul>
		<li>
			<bean:message key="label.value.confirmed" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
			<%= unConfirmedValues.get(cpvReference).toFormatString() %>
		</li>
		<li>
			<bean:message key="label.value" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>*:
			<%= confirmedValues.get(cpvReference).toFormatString() %>
		</li>
	</ul>
	* <bean:message key="label.value.unconfirmed.details" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>


	<%
		final SortedSet<AcquisitionForSupplierAndCPVBean> acquisitionBeans = (SortedSet<AcquisitionForSupplierAndCPVBean>) request.getAttribute("acquisitionBeans");
		final SortedSet<RefundForSupplierAndCPVBean> refundBeans = (SortedSet<RefundForSupplierAndCPVBean>) request.getAttribute("refundBeans");
		final SortedSet<AcquisitionAfterTheFact> afterTheFactProcesses = (SortedSet<AcquisitionAfterTheFact>) request.getAttribute("afterTheFactProcesses");
	%>
		<table class="tstyle2">
			<tr>
				<th>
					<bean:message key="label.process.type" bundle="EXPENDITURE_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.process.number" bundle="EXPENDITURE_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.value.confirmed" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.value" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>*
				</th>
				<th>
					<bean:message key="label.total" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</th>
				<th>
				</th>
			</tr>
			<% for (final AcquisitionForSupplierAndCPVBean acquisitionBean : acquisitionBeans) { %>
				<tr>
					<td>
						<bean:message key="link.create.process.Normal" bundle="EXPENDITURE_RESOURCES"/>
					</td>
					<td>
						<html:link action='<%= "/workflowProcessManagement.do?method=viewProcess&amp;processId=" + acquisitionBean.getAcquisitionProcess().getExternalId() %>'>
							<%= acquisitionBean.getAcquisitionProcess().getProcessNumber() %>
						</html:link>
					</td>
					<td>
						<%= acquisitionBean.getConfirmedValue().toFormatString() %>
					</td>
					<td>
						<%= acquisitionBean.getUnconfirmedValue().toFormatString() %>
					</td>
					<td>
						<%= acquisitionBean.getAcquisitionProcess().getTotalValue().toFormatString() %>
					</td>
					<td>
					</td>
				</tr>
			<% } %>
			<% for (final RefundForSupplierAndCPVBean refundBean : refundBeans) { %>
				<tr>
					<td>
						<bean:message key="link.create.refundProcess" bundle="EXPENDITURE_RESOURCES"/>
					</td>
					<td>
						<html:link action='<%= "/workflowProcessManagement.do?method=viewProcess&amp;processId=" + refundBean.getRefundProcess().getExternalId() %>'>
							<%= refundBean.getRefundProcess().getProcessNumber() %>
						</html:link>
					</td>
					<td>
						<%= refundBean.getConfirmedValue().toFormatString() %>
					</td>
					<td>
						<%= refundBean.getUnconfirmedValue().toFormatString() %>
					</td>
					<td>
						<%= refundBean.getRefundProcess().getTotalValue().toFormatString() %>
					</td>
					<td>
					</td>
				</tr>
			<% } %>
			<% for (final AcquisitionAfterTheFact acquisitionAfterTheFact : afterTheFactProcesses) { %>
				<tr>
					<td>
						<bean:message key="label.process.afterTheFactAcquisition" bundle="EXPENDITURE_RESOURCES"/>
					</td>
					<td>
						<html:link action='<%= "/workflowProcessManagement.do?method=viewProcess&amp;processId=" + acquisitionAfterTheFact.getAfterTheFactAcquisitionProcess().getExternalId() %>'>
							<%= acquisitionAfterTheFact.getAfterTheFactAcquisitionProcess().getProcessNumber() %>
						</html:link>
					</td>
					<td>
						<%= acquisitionAfterTheFact.getValue().toFormatString() %>
					</td>
					<td>
						<%= acquisitionAfterTheFact.getValue().toFormatString() %>
					</td>
					<td>
						<%= acquisitionAfterTheFact.getValue().toFormatString() %>
					</td>
					<td>
					</td>
				</tr>
			<% } %>
		</table>




