<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<bean:define id="workingCapital" name="process" property="workingCapital"/>

<h3>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transactions"/>
</h3>

<logic:empty name="workingCapital" property="workingCapitalTransactions">
	<p>
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transactions.none"/>
	</p>
</logic:empty>

<logic:notEmpty name="workingCapital" property="workingCapitalTransactions">
	<br/>
	<table class="tstyle3 width100pc">
		<tr>
			<jsp:include page="workingCapitalTransactionLineHeader.jsp"/>
			<th>
			</th>
		</tr>
		<logic:iterate id="workingCapitalTransaction" name="workingCapital" property="sortedWorkingCapitalTransactions">
			<tr>
				<bean:define id="workingCapitalTransaction" name="workingCapitalTransaction" toScope="request"/>
				<jsp:include page="workingCapitalTransactionLine.jsp"/>
				<td>
					<html:link action="/workingCapital.do?method=viewWorkingCapitalTransaction" paramId="workingCapitalTransactionOid" paramName="workingCapitalTransaction" paramProperty="externalId">
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="link.view"/>
					</html:link>
					<bean:define id="workingCapitalTransactionOid" type="java.lang.String" name="workingCapitalTransaction" property="externalId"/>
					<logic:equal name="workingCapitalTransaction" property="acquisition" value="true">
						<logic:equal name="workingCapitalTransaction" property="pendingApproval" value="true">
						 | 	<wf:activityLink processName="process" activityName="EditWorkingCapitalActivity" scope="request" paramName0="workingCapitalAcquisitionTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
								<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.EditWorkingCapitalActivity"/>
							</wf:activityLink>
						</logic:equal>
					</logic:equal>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
