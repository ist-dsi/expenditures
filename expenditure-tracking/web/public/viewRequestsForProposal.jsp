<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- public/viewRequestsForProposal.jsp -->
public/viewRequestsForProposal.jsp

<div class="wrapper">

<h2><bean:message key="process.requestForProposal.title.openRequests" bundle="EXPENDITURE_RESOURCES"/></h2>


<logic:empty name="activeRequests">
	<p><em><bean:message key="process.messages.info.noProcessesToOperate" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
</logic:empty>

<table>
	<logic:iterate id="requestProcess" name="activeRequests" indexId="Id">
		<tr>
			<th><bean:message key="label.expireDate" bundle="REQUEST_RESOURCES"/></th>
			<td><fr:view name="requestProcess" property="requestForProposal.expireDate"/></td>
		</tr>
		<tr>
			<th><bean:message key="label.title" bundle="REQUEST_RESOURCES"/></th>
			<td><fr:view name="requestProcess" property="requestForProposal.title"/></td>
		</tr>
		<tr>
			<th></th>
			<td>
				<html:link page="<%= "/home.do?method=viewRequestForProposalProcess" %>" paramId="requestForProposalProcessOid" paramName="requestProcess" paramProperty="OID">
				<bean:message key="link.view" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>

<%--
<fr:view name="activeRequests" schema="viewRequestsForProposal.public">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="link(view)" value="/requestForProposalProcess.do?method=viewRequestForProposalProcess"/>
		<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
		<fr:property name="key(view)" value="link.view"/>
		<fr:property name="param(view)" value="OID/requestForProposalProcessOid"/>
		<fr:property name="order(view)" value="1"/>
		<fr:property name="sortBy" value="requestForProposal.expireDate, asc"/>
	</fr:layout>
</fr:view>
--%>
