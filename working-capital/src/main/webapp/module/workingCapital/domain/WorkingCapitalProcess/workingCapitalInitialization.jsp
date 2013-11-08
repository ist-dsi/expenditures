<%@page import="org.joda.time.LocalDate"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization"%>
<%@page import="java.util.Set"%>
<%@page import="module.organization.domain.Person"%>
<%@page import="java.util.SortedMap"%>
<%@page import="module.workingCapital.domain.WorkingCapitalInitializationReenforcement"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

<%@ page import="java.math.BigDecimal" %>

<bean:define id="workingCapital" name="process" property="workingCapital" type="module.workingCapital.domain.WorkingCapital"/>

<h3>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization"/>
</h3>

<bean:define id="workingCapitalInitialization" name="workingCapital" property="workingCapitalInitialization" type="module.workingCapital.domain.WorkingCapitalInitialization"/>
<bean:define id="workingCapitalOid" name="workingCapital" property="externalId"/>
<bean:define id="workingCapitalInitializationOid" type="java.lang.String" name="workingCapitalInitialization" property="externalId"/>

<div class="infobox col2-1">
	<table class="process-info mbottom0">
		<tr>
			<td class="width215px">
				<bean:message key="label.module.workingCapital" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td>
				<bean:define id="unitOID" name="workingCapital" property="unit.externalId" type="java.lang.String"/>
				<html:link styleClass="secondaryLink" page="<%= "/expenditureTrackingOrganization.do?method=viewOrganization&unitOid=" + unitOID%>" target="_blank">
					<bean:write name="workingCapital" property="unit.presentationName"/>
				</html:link>
			</td>
		</tr>
		<tr>
			<td class="width215px">
				<bean:message key="label.module.workingCapital.unit.responsible" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td>
				<%
					final SortedMap<Person, Set<Authorization>> authorizations = workingCapital.getSortedAuthorizations();
					if (!authorizations.isEmpty()) {
				%>
						<ul style="padding: 0px 0 0px 16px;">
				<%
							for (final Entry<Person, Set<Authorization>> entry : authorizations.entrySet()) {
							    final StringBuilder builder = new StringBuilder();
							    for (final Authorization authorization : entry.getValue()) {
									if (builder.length() > 0) {
									    builder.append("; ");
									}
									final LocalDate start = authorization.getStartDate();
									final LocalDate end = authorization.getEndDate();
									if (start != null) {
									    builder.append(start.toString("yyyy-MM-dd"));
									}
									builder.append(" - ");
									if (end != null) {
									    builder.append(end.toString("yyyy-MM-dd"));
									}
							    }
				%>
								<li title="<%= builder.toString() %>">
									<html:link styleClass="secondaryLink" page="<%= "/expenditureTrackingOrganization.do?method=viewPerson&personOid=" + entry.getKey().getUser().getExpenditurePerson().getExternalId() %>" target="_blank">
										<%= entry.getKey().getName() %>
									</html:link>
								</li>
				<%
							}
				%>
						</ul>
				<%
					}
				%>
			</td>
		</tr>
		<tr>
			<td class="width215px">
				<bean:message key="label.module.workingCapital.initialization.accountingUnit" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td>
				<bean:write name="workingCapital" property="accountingUnit.name"/>
			</td>
		</tr>
		<tr>
			<td class="width215px">
				<bean:message key="label.module.workingCapital.requestingDate" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td>
				<fr:view name="workingCapitalInitialization" property="requestCreation"/>
			</td>
		</tr>
		<tr>
			<td class="width215px">
				<bean:message key="label.module.workingCapital.requester" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td>
				<bean:write name="workingCapitalInitialization" property="requestor.name"/>
			</td>
		</tr>
		<logic:present name="workingCapital" property="movementResponsible">
			<tr>
				<td class="width215px">
					<bean:message key="label.module.workingCapital.movementResponsible" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
				</td>
				<td>
					<bean:write name="workingCapital" property="movementResponsible.name"/>
				</td>
			</tr>
		</logic:present>
		<tr>
			<td class="width215px">
				<bean:message key="label.module.workingCapital.fiscalId" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td>
				<bean:write name="workingCapitalInitialization" property="fiscalId"/>
			</td>
		</tr>
		<tr>
			<td class="width215px">
				<bean:message key="label.module.workingCapital.internationalBankAccountNumber" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td>
				<bean:write name="workingCapitalInitialization" property="internationalBankAccountNumber"/>
			</td>
		</tr>
		<tr>
			<td class="width215px">
				<bean:message key="label.module.workingCapital.fundAllocationId" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td>
				<bean:write name="workingCapitalInitialization" property="fundAllocationId"/>
			</td>
		</tr>
		<tr>
			<td class="width215px">
				<bean:message key="label.module.workingCapital.requestedAnualValue.requested" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td>
				<fr:view name="workingCapitalInitialization" property="requestedAnualValue"/>
			</td>
		</tr>
		<tr>
			<td class="width215px">
				<bean:message key="label.module.workingCapital.requestedMonthlyValue.requested" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td>
				<%= workingCapitalInitialization.getRequestedAnualValue().divideAndRound(new BigDecimal(6)).toFormatString() %>
			</td>
		</tr>
		<tr>
			<td class="width215px">
				<bean:message key="label.module.workingCapital.authorizedAnualValue" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td class="bold">
				<% if (workingCapitalInitialization.getAuthorizedAnualValue() != null) { %>
					<fr:view name="workingCapitalInitialization" property="authorizedAnualValue"/>
				<% } %>
			</td>
		</tr>
		<tr>
			<td class="width215px">
				<bean:message key="label.module.workingCapital.maxAuthorizedAnualValue" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td>
				<% if (workingCapitalInitialization.getMaxAuthorizedAnualValue() != null) { %>
					<fr:view name="workingCapitalInitialization" property="maxAuthorizedAnualValue"/>
				<% } %>
			</td>
		</tr>
		<logic:iterate id="someInit" name="workingCapital" property="sortedWorkingCapitalInitializations">
			<% if (someInit instanceof WorkingCapitalInitializationReenforcement) { %>
			<tr>
				<td class="width215px">
					<bean:message key="label.module.workingCapital.initialization.requestedReenforcementValue" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
				</td>
				<td>
					<fr:view name="someInit" property="requestedReenforcementValue"/>
					<span style="color: gray;">
						<fr:view name="someInit" property="requestCreation"/>
					</span>
				</td>
			</tr>
			<tr>
				<td class="width215px">
					<bean:message key="label.module.workingCapital.initialization.authorizedReenforcementValue" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
				</td>
				<td class="bold">
					<% if (((WorkingCapitalInitializationReenforcement) someInit).getAuthorizedReenforcementValue() != null) { %>
						<fr:view name="someInit" property="authorizedReenforcementValue"/>
						<% if (((WorkingCapitalInitializationReenforcement) someInit).getAuthorizationByUnitResponsible() != null) { %>
							<span style="color: gray;">
								<fr:view name="someInit" property="authorizationByUnitResponsible"/>
							</span>
						<% } %>
					<% } %>
				</td>
			</tr>
			<% } %>
		</logic:iterate>
		<logic:present name="workingCapitalInitialization" property="lastSubmission">
			<tr>
				<td class="width215px">
					<bean:message key="label.module.workingCapital.initialization.lastSubmission" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
				</td>
				<td>
					<fr:view name="workingCapitalInitialization" property="lastSubmission"/>
				</td>
			</tr>
		</logic:present>
		<logic:present name="workingCapitalInitialization" property="refundRequested">
			<tr>
				<td class="width215px">
					<bean:message key="label.module.workingCapital.initialization.refundRequested" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
				</td>
				<td>
					<fr:view name="workingCapitalInitialization" property="refundRequested"/>
				</td>
			</tr>
		</logic:present>
	</table>
	<wf:activityLink processName="process" activityName="EditInitializationActivity" scope="request" paramName0="workingCapitalInitialization" paramValue0="<%= workingCapitalInitializationOid %>">
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.EditInitializationActivity"/>
	</wf:activityLink>
</div>

<div class="infobox2 col2-2">
	<fr:view name="process" layout="process-state-view"/>
</div>

<bean:size id="capitalInitializationsCount" name="workingCapital"  property="workingCapitalInitializations"/>
<logic:greaterThan name="capitalInitializationsCount" value="1">
	<html:link page='<%= "/workingCapital.do?method=viewAllCapitalInitializations&workingCapitalOid=" + workingCapitalOid %>'><bean:message key="label.module.workingCapital.showAllWorkingCapitalInitializationForProcess" bundle="WORKING_CAPITAL_RESOURCES"/></html:link>
</logic:greaterThan>
		
			<table class="tstyle3 mtop1 mbottom1 width100pc" >
				<tr><th><bean:message key="label.module.workingCapital.operations" bundle="WORKING_CAPITAL_RESOURCES"/></th>
				<th><bean:message key="label.module.workingCapital.date" bundle="WORKING_CAPITAL_RESOURCES"/></th>
				<th><bean:message key="label.module.workingCapital.person" bundle="WORKING_CAPITAL_RESOURCES"/></th>
				<th></th></tr>
				<tr>
					<td class="aleft">
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.aprovalByUnitResponsible"/>
					</td>
					<td>
						<logic:present name="workingCapitalInitialization" property="aprovalByUnitResponsible">
							<fr:view name="workingCapitalInitialization" property="aprovalByUnitResponsible"/>
						</logic:present>
						<logic:notPresent name="workingCapitalInitialization" property="aprovalByUnitResponsible">
							-
						</logic:notPresent>
					</td>
					<td>
					<logic:present name="workingCapitalInitialization" property="responsibleForUnitApproval">
						<fr:view name="workingCapitalInitialization" property="responsibleForUnitApproval.person.firstAndLastName"/>
					</logic:present>
					<logic:notPresent name="workingCapitalInitialization" property="responsibleForUnitApproval">
						-
					</logic:notPresent>
					</td>
					<td>
					<logic:equal name="process" property="workingCapital.workingCapitalInitialization.pendingAproval" value="true">
						<wf:activityLink processName="process" activityName="ApproveActivity" scope="request" paramName0="workingCapitalInitialization" paramValue0="<%= workingCapitalInitializationOid %>">
							<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.ApproveActivity"/>
						</wf:activityLink>
					</logic:equal>
					<wf:activityLink processName="process" activityName="UnApproveActivity" scope="request" paramName0="workingCapitalInitialization" paramValue0="<%= workingCapitalInitializationOid %>">
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.UnApproveActivity"/>
					</wf:activityLink>
					</td>
				</tr>
				
				
				<tr>
					<td class="aleft">
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.verificationByAccounting"/>
					</td>
					<td>
						<logic:present name="workingCapitalInitialization" property="verificationByAccounting">
							<fr:view name="workingCapitalInitialization" property="verificationByAccounting"/>
						</logic:present>
						<logic:notPresent name="workingCapitalInitialization" property="verificationByAccounting">
							-
						</logic:notPresent>
					</td>
					<td>
					<logic:present name="workingCapitalInitialization" property="responsibleForAccountingVerification">
						<fr:view name="workingCapitalInitialization" property="responsibleForAccountingVerification.firstAndLastName"/>
					</logic:present>
					<logic:notPresent name="workingCapitalInitialization" property="responsibleForAccountingVerification">
						-
					</logic:notPresent>
					</td>
					<td>
					<wf:activityLink processName="process" activityName="VerifyActivity" scope="request" paramName0="workingCapitalInitialization" paramValue0="<%= workingCapitalInitializationOid %>">
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.VerifyActivity"/>
					</wf:activityLink>
					<wf:activityLink processName="process" activityName="UnVerifyActivity" scope="request" paramName0="workingCapitalInitialization" paramValue0="<%= workingCapitalInitializationOid %>">
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.UnVerifyActivity"/>
					</wf:activityLink>
					</td>
				</tr>
				
				<tr>
					<td class="aleft">
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.authorizationByUnitResponsible"/>
					</td>
					<td>
						<logic:present name="workingCapitalInitialization" property="authorizationByUnitResponsible">
							<fr:view name="workingCapitalInitialization" property="authorizationByUnitResponsible"/>
						</logic:present>
						<logic:notPresent name="workingCapitalInitialization" property="authorizationByUnitResponsible">
							-
						</logic:notPresent>
					</td>
					<td>
					<logic:present name="workingCapitalInitialization" property="responsibleForUnitAuthorization">
						<fr:view name="workingCapitalInitialization" property="responsibleForUnitAuthorization.child.firstAndLastName"/>
					</logic:present>
					<logic:notPresent name="workingCapitalInitialization" property="responsibleForUnitAuthorization">
						-
					</logic:notPresent>
					</td>
					<td>
						<wf:activityLink processName="process" activityName="AuthorizeActivity" scope="request" paramName0="workingCapitalInitialization" paramValue0="<%= workingCapitalInitializationOid %>">
							<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.AuthorizeActivity"/>
						</wf:activityLink>
						<wf:activityLink processName="process" activityName="UnAuthorizeActivity" scope="request" paramName0="workingCapitalInitialization" paramValue0="<%= workingCapitalInitializationOid %>">
							<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.UnAuthorizeActivity"/>
						</wf:activityLink>
					</td>
				</tr>
			</table>
