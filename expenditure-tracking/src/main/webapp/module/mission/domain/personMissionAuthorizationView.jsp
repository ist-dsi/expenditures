<%@page import="module.organization.domain.Person"%>
<%@page import="module.organization.domain.Party"%>
<%@page import="module.mission.domain.PersonMissionAuthorization"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>
<%@page import="module.organization.domain.OrganizationalModel"%>
<%@page import="org.fenixedu.bennu.core.domain.Bennu"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="module.organization.domain.AccountabilityType"%>
<%@page import="java.util.Collections"%>
<%@page import="module.mission.domain.MissionSystem"%>
<%@page import="java.util.Set"%><tr>

<%
	final OrganizationalModel organizationalModel = Bennu.getInstance().getOrganizationalModelsSet().size() > 0 ?
	    Bennu.getInstance().getOrganizationalModelsSet().iterator().next() : null;
%>

	<bean:define id="personMissionAuthorization" name="personMissionAuthorization" />
	<bean:define id="personMissionAuthorizationId" name="personMissionAuthorization" property="externalId" type="java.lang.String"/>
	<td>
		<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showUnitById" paramId="unitId" paramName="personMissionAuthorization" paramProperty="unit.externalId">
			<bean:write name="personMissionAuthorization" property="unit.presentationName"/>
		</html:link>
	</td>
	<%
		final PersonMissionAuthorization personMissionAuthorizationX = (PersonMissionAuthorization) request.getAttribute("personMissionAuthorization");
		final boolean hasAuthority = personMissionAuthorizationX.hasAuthority() || personMissionAuthorizationX.hasDelegatedAuthority();
		if (hasAuthority) {
	%>
		<td>
			<%
				if (personMissionAuthorizationX.hasAuthority()) {
			%>
				<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="personMissionAuthorization" paramProperty="authority.externalId">
					<bean:write name="personMissionAuthorization" property="authority.partyName"/>
				</html:link>
			<% } else { %>
				<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="personMissionAuthorization" paramProperty="delegatedAuthority.accountabilityDelegatee.child.externalId">
					<bean:write name="personMissionAuthorization" property="delegatedAuthority.accountabilityDelegatee.child.partyName"/>
				</html:link>
				<br/>
				<bean:message key="label.delegation.by" bundle="MISSION_RESOURCES"/>
				<fr:view name="personMissionAuthorization" property="delegatedAuthority.accountabilityDelegator.child.partyName"/>
			<% } %>
		</td>
		<td>
			<%
				if (personMissionAuthorizationX.hasAuthority()) {
			%>
				<fr:view name="personMissionAuthorization" property="authorizationDateTime"/>
			<% } else { %>
				<fr:view name="personMissionAuthorization" property="previous.authorizationDateTime"/>
			<% } %>
		</td>
		<td>
			<%
				final MissionSystem instance = MissionSystem.getInstance();
				final Set<AccountabilityType> accountabilityTypes = instance.getAccountabilityTypesThatAuthorize();
				
				boolean hasChild = false;
				for (final Party party : instance.getOrganizationalModel().getPartiesSet()) {
				    if (party.isUnit()) {
						for (final Person person : party.getChildPersons(accountabilityTypes)) {
						    if (personMissionAuthorizationX.getUnit().hasChildAccountabilityIncludingAncestry(accountabilityTypes, person)) {
								hasChild = true;
						    }
						}
				    }
				}

				final boolean hasNext = personMissionAuthorizationX.hasNext();
				final boolean hasNextAthority = hasNext && (personMissionAuthorizationX.getNext().hasAuthority() || personMissionAuthorizationX.getNext().hasDelegatedAuthority());
				final boolean result = hasAuthority && hasChild && (!hasNext || !hasNextAthority);
			%>
			<%
				if (personMissionAuthorizationX.canUnAuthoriseParticipantActivity()) {
			%>
					<wf:activityLink processName="process" activityName="UnAuthoriseParticipantActivity" scope="request" paramName0="personMissionAuthorization" paramValue0="<%= personMissionAuthorizationId %>">
						<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.unauthorizatise"/>
					</wf:activityLink>
			<%
				}
			%>
		</td>
	<%
		} else {
	%>
		<td colspan="2">
		</td>
		<td>
			<bean:define id="personMissionAuthorization" name="personMissionAuthorization" type="module.mission.domain.PersonMissionAuthorization"/>
			<bean:define id="personMissionAuthorizationId" name="personMissionAuthorization" property="externalId" type="java.lang.String"/>
			<%
				if (personMissionAuthorizationX.canAuthoriseParticipantActivity()) {
			%>
					<wf:activityLink processName="process" activityName="AuthoriseParticipantActivity" scope="request" paramName0="personMissionAuthorization" paramValue0="<%= personMissionAuthorizationId%>">
						<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.authorizatise"/>
					</wf:activityLink>
			<%
				}
			%>
		</td>
	<%
		}
	%>
</tr>

<logic:present name="personMissionAuthorization" property="next">
	<bean:define id="personMissionAuthorization" name="personMissionAuthorization" property="next" toScope="request" type="module.mission.domain.PersonMissionAuthorization"/>
	<jsp:include page="personMissionAuthorizationView.jsp"/>
</logic:present>
