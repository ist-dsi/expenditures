<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- viewRequestsForProposal.jsp -->
viewRequestsForProposal.jsp

<logic:empty name="activeRequests">
	<p><em><bean:message key="label.no.processes.that.user.can.operate" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
</logic:empty>

<fr:view name="activeRequests" schema="viewRequestsForProposal">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="link(view)" value="/requestForProposalProcess.do?method=viewRequestForProposalDetail"/>
		<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
		<fr:property name="key(view)" value="link.view"/>
		<fr:property name="param(view)" value="OID/requestForProposalProcessOid"/>
		<fr:property name="order(view)" value="1"/>
		<fr:property name="sortBy" value="requestForProposal.expireDate, asc"/>
	</fr:layout>
</fr:view>

