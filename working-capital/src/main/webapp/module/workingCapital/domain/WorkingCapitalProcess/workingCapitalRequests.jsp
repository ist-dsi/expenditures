<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>


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
		<p>
			<% boolean hasOne = false; %>
			<logic:iterate id="workingCapitalRequest" name="workingCapital" property="workingCapitalRequests">
				<logic:notPresent name="workingCapitalRequest" property="workingCapitalPayment">
			
					<% hasOne = true; %>
			
					<table class="tstyle3 width100pc">
						<tr>
							<th> <bean:message key="label.module.workingCapital.requester" bundle="WORKING_CAPITAL_RESOURCES"/> </th>
							<th> <bean:message key="label.module.workingCapital.request.creation" bundle="WORKING_CAPITAL_RESOURCES"/> </th>
							<th> <bean:message key="label.module.workingCapital.request.requestedValue" bundle="WORKING_CAPITAL_RESOURCES"/> </th>
							<th> <bean:message key="label.module.workingCapital.request.paymentMethod" bundle="WORKING_CAPITAL_RESOURCES"/> </th>
							<th>  </th>
						</tr>
						<tr>
							<td><fr:view name="workingCapitalRequest" property="workingCapitalRequester.name"/></td>
							<td><fr:view name="workingCapitalRequest" property="requestCreation"/></td>
							<td><fr:view name="workingCapitalRequest" property="requestedValue"/></td>
							<td><fr:view name="workingCapitalRequest" property="paymentMethod"/></td>
							<td>
							<logic:notPresent name="workingCapitalRequest" property="processedByTreasury">
								<bean:define id="workingCapitalRequestOid" type="java.lang.String" name="workingCapitalRequest" property="externalId"/>
								<wf:activityLink processName="process" activityName="PayCapitalActivity" scope="request" paramName0="workingCapitalRequest" paramValue0="<%= workingCapitalRequestOid %>">
									<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.PayCapitalActivity"/>
								</wf:activityLink>
								<wf:activityLink processName="process" activityName="UnRequestCapitalActivity" scope="request" paramName0="workingCapitalRequest" paramValue0="<%= workingCapitalRequestOid %>">
									<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.UnRequestCapitalActivity"/>
								</wf:activityLink>
							</logic:notPresent>	
							</td>
						</tr>
					</table>
				</logic:notPresent>
			</logic:iterate>
			<% if (!hasOne) { %>
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.request.none.pending"/>
			<% } %>
		</p>
</logic:notEmpty>
