<%@page import="module.workingCapital.domain.WorkingCapitalTransaction"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

<%@ page import="module.workingCapital.domain.WorkingCapitalAcquisitionSubmission" %>
<%@ page import="module.workingCapital.domain.ExceptionalWorkingCapitalAcquisitionTransaction" %>

<bean:define id="workingCapital" name="process" property="workingCapital"/>

<h3>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transactions"/>
</h3>

<logic:empty name="workingCapital" property="workingCapitalTransactions">
	<p>
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transactions.none"/>
	</p>
</logic:empty>

<style media="screen" type="text/css">
tr.exceptionalAcquisition {
	background-color : #FAFFBA
}
tr.exceptionalAcquisition td {
	background-color : #FAFFBA
}

</style>
<logic:notEmpty name="workingCapital" property="workingCapitalTransactions">
	<br/>
	<table class="tstyle3 width100pc">
		<tr>
			<jsp:include page="workingCapitalTransactionLineHeader.jsp"/>
			<th>
			</th>
		</tr>
		<logic:iterate id="workingCapitalTransaction" name="workingCapital" property="sortedWorkingCapitalTransactions">
			<% if (((WorkingCapitalTransaction) workingCapitalTransaction).isExceptionalAcquisition()) { %>
			<tr class="exceptionalAcquisition">
			<% } else { %>
			<tr>
			<% } %>
				<bean:define id="workingCapitalTransaction" name="workingCapitalTransaction" toScope="request"/>
				<jsp:include page="workingCapitalTransactionLine.jsp"/>  
				<td>
					<html:link action="/workingCapital.do?method=viewWorkingCapitalTransaction" paramId="workingCapitalTransactionOid" paramName="workingCapitalTransaction" paramProperty="externalId">
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="link.view"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
