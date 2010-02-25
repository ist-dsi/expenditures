<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<bean:define id="workingCapital" name="process" property="workingCapital"/>

<h3>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request"/>
</h3>

<logic:empty name="workingCapital" property="workingCapitalRequests">
	<p>
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.requests.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="workingCapital" property="workingCapitalRequests">
	<div class="infobox mtop1 mbottom1">
		<p>
			<% boolean hasOne = false; %>
			<logic:iterate id="workingCapitalRequest" name="workingCapital" property="workingCapitalRequests">
				<logic:notPresent name="workingCapitalRequest" property="workingCapitalPayment">
					<% hasOne = true; %>
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.creation"/>:
					<fr:view name="workingCapitalRequest" property="requestCreation"/>
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.requester"/>:
					<bean:write name="workingCapitalRequest" property="workingCapitalRequester.child.name"/>

					<br/>
					<br/>

					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.requestedValue"/>:
					<fr:view name="workingCapitalRequest" property="requestedValue"/>

					&nbsp;&nbsp;

					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.paymentMethod"/>:
					<fr:view name="workingCapitalRequest" property="paymentMethod"/>

					<br/>
					<br/>

					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.processedByTreasury"/>:
					<logic:present name="workingCapitalRequest" property="processedByTreasury">
						<fr:view name="workingCapitalRequest" property="processedByTreasury"/>
						<logic:present name="workingCapitalRequest" property="workingCapitalTreasuryProcessor">
							<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.treasuryProcessor"/>:
							<fr:view name="workingCapitalRequest" property="workingCapitalTreasuryProcessor.name"/>
						</logic:present>
					</logic:present>
					<logic:notPresent name="workingCapitalRequest" property="processedByTreasury">
						<bean:define id="workingCapitalRequestOid" type="java.lang.String" name="workingCapitalRequest" property="externalId"/>
						<wf:activityLink processName="process" activityName="PayCapitalActivity" scope="request" paramName0="workingCapitalRequest" paramValue0="<%= workingCapitalRequestOid %>">
							<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.PayCapitalActivity"/>
						</wf:activityLink>
					</logic:notPresent>
				</logic:notPresent>
			</logic:iterate>
			<% if (!hasOne) { %>
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.none.pending"/>:
			<% } %>
		</p>
	</div>
</logic:notEmpty>
