<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<!-- requests/viewRequestProcess.jsp -->
public/viewRequestProcess.jsp

<div class="wrapper">

<h2><bean:message key="process.requestForProposal.title.details" bundle="EXPENDITURE_RESOURCES"/></h2>

<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<div class="infoop2">
<fr:view name="requestForProposalProcess" property="requestForProposal" schema="viewRequestForProposal">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
</fr:view>
</div>

<div class="documents">
	<p>
		<bean:message key="label.proposalDocument" bundle="REQUEST_RESOURCES"/>:
		<logic:present name="requestForProposalProcess" property="requestForProposal.requestForProposalDocument">
			<html:link action="/requestForProposalProcess.do?method=downloadRequestForProposalDocument" paramId="requestForProposalDocumentOid" paramName="requestForProposalProcess" paramProperty="requestForProposal.requestForProposalDocument.OID">
				<bean:write name="requestForProposalProcess" property="requestForProposal.requestForProposalDocument.filename"/>
			</html:link>	
		</logic:present>
		<logic:notPresent name="requestForProposalProcess" property="requestForProposal.requestForProposalDocument">
			<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
		</logic:notPresent>
	</p>
</div>

</div>