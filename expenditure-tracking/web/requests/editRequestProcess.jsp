<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- requests/editRequestProcess.jsp -->
requests/editRequestProcess.jsp

<h2><bean:message key="process.requestForProposal.title.edit" bundle="EXPENDITURE_RESOURCES"/></h2>

<bean:define id="processOID" name="requestForProposalProcessOid"/>
<fr:edit id="requestForProposalProcessBean"
		name="requestForProposalProcessBean"
		schema="requestForProposalBean.edit"
		action='<%= "/requestForProposalProcess.do?method=editRequestForProposal&requestForProposalProcessOid=" + processOID %>' >
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path='<%= "/requestForProposalProcess.do?method=viewRequestForProposalProcess&requestForProposalProcessOid=" + processOID %>'/>
</fr:edit>
