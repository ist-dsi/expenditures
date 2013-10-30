<%@page import="org.joda.time.LocalDate"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization"%>
<%@page import="java.util.Set"%>
<%@page import="module.organization.domain.Person"%>
<%@page import="java.util.SortedMap"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="workingCapital" name="process" property="workingCapital" type="module.workingCapital.domain.WorkingCapital"/>

<div class="infobox mtop1 mbottom1">
	<table>
		<tr>
			<td>
				<bean:message key="label.module.workingCapital" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td class="aleft">
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
			<td>
				<bean:message key="label.module.workingCapital.requestingDate" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td class="aleft">
				<fr:view name="workingCapital" property="workingCapitalInitialization.requestCreation"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.module.workingCapital.requester" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td class="aleft">
				<bean:write name="workingCapital" property="workingCapitalInitialization.requestor.name"/>
			</td>
		</tr>
		<logic:present name="workingCapital" property="movementResponsible">
			<tr>
				<td>
					<bean:message key="label.module.workingCapital.movementResponsible" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
				</td>
				<td class="aleft">
					<bean:write name="workingCapital" property="movementResponsible.name"/>
				</td>
			</tr>
		</logic:present>
	</table>
</div>
