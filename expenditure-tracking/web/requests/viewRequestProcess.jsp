<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<!-- requests/viewRequestProcess.jsp -->

<bean:define id="currentState" name="requestForProposalProcess" property="requestForProposalProcessStateType"/>

<%--
<fr:view name="requestForProposalProcess"> 
	<fr:layout name="process-state">
		<fr:property name="stateParameterName" value="state"/>
		<fr:property name="url" value="/viewLogs.do?method=viewOperationLog&acquisitionProcessOid=${externalId}"/>
		<fr:property name="contextRelative" value="true"/>
		<fr:property name="currentStateClass" value=""/>
	</fr:layout>
</fr:view>
--%>

<div class="wrapper">

<h2><bean:message key="process.requestForProposal.title.view" bundle="EXPENDITURE_RESOURCES"/></h2>

<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<div class="infoop1">
	<ul>
	<logic:iterate id="activity" name="requestForProposalProcess" property="activeActivitiesForRequest">
		<bean:define id="activityName" name="activity" property="class.simpleName"/> 
		<li>
			<html:link page='<%= "/requestForProposalProcess.do?method=execute" + activityName %>' paramId="requestForProposalProcessOid" paramName="requestForProposalProcess" paramProperty="externalId">
				<fr:view name="activity" property="class">
					<fr:layout name="label">
						<fr:property name="bundle" value="REQUEST_RESOURCES"/>
					</fr:layout>
				</fr:view>
			</html:link>
		</li>
	</logic:iterate>
	</ul>
	<logic:empty name="requestForProposalProcess" property="activeActivitiesForRequest">
		<em>
			<bean:message key="messages.info.noOperatesAvailabeATM" bundle="EXPENDITURE_RESOURCES"/>.
		</em>
	</logic:empty>
</div>


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
			<html:link action="/requestForProposalProcess.do?method=downloadRequestForProposalDocument" paramId="requestForProposalDocumentOid" paramName="requestForProposalProcess" paramProperty="requestForProposal.requestForProposalDocument.externalId">
				<bean:write name="requestForProposalProcess" property="requestForProposal.requestForProposalDocument.filename"/>
			</html:link>	
		</logic:present>
		<logic:notPresent name="requestForProposalProcess" property="requestForProposal.requestForProposalDocument">
			<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
		</logic:notPresent>
	</p>
</div>

</div>