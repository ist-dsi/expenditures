<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>
<%@page import="module.organization.domain.OrganizationalModel"%>
<%@page import="myorg.domain.MyOrg"%>
<%@page import="myorg.domain.User"%>
<%@page import="module.organization.domain.AccountabilityType"%>
<%@page import="module.organizationIst.domain.IstAccountabilityType"%>
<%@page import="java.util.Collections"%>

<%
	final OrganizationalModel organizationalModel = MyOrg.getInstance().hasAnyOrganizationalModels() ?
	    MyOrg.getInstance().getOrganizationalModelsIterator().next() : null;
%>


<%@page import="module.mission.domain.MissionSystem"%>
<%@page import="java.util.Set"%><tr>
	<td>
		<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showUnitById" paramId="unitId" paramName="personMissionAuthorization" paramProperty="unit.externalId">
			<bean:write name="personMissionAuthorization" property="unit.presentationName"/>
		</html:link>
	</td>
	<logic:present name="personMissionAuthorization" property="authority">
		<td>
			<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="personMissionAuthorization" paramProperty="authority.externalId">
				<bean:write name="personMissionAuthorization" property="authority.partyName"/>
			</html:link>
		</td>
		<td>
			<fr:view name="personMissionAuthorization" property="authorizationDateTime"/>
		</td>
		<td>
			<bean:define id="personMissionAuthorization" name="personMissionAuthorization" type="module.mission.domain.PersonMissionAuthorization"/>
			<bean:define id="personMissionAuthorizationId" name="personMissionAuthorization" property="externalId" type="java.lang.String"/>
			<%
				final User user = User.findByUsername("ist11791");
				final MissionSystem instance = MissionSystem.getInstance();
				final Set<AccountabilityType> accountabilityTypes = instance.getAccountabilityTypesThatAuthorize();
				//final AccountabilityType accountabilityType = IstAccountabilityType.PERSONNEL_RESPONSIBLE_MISSIONS.readAccountabilityType();
				final boolean hasAuthority = personMissionAuthorization.hasAuthority();
				final boolean hasChild = personMissionAuthorization.getUnit().hasChildAccountabilityIncludingAncestry(accountabilityTypes, user.getPerson());
				final boolean hasNext = personMissionAuthorization.hasNext();
				final boolean hasNextAthority = hasNext && personMissionAuthorization.getNext().hasAuthority();
				final boolean result = hasAuthority && hasChild && (!hasNext || !hasNextAthority);
			%>
			<%
				if (personMissionAuthorization.canUnAuthoriseParticipantActivity()) {
			%>
					<wf:activityLink processName="process" activityName="UnAuthoriseParticipantActivity" scope="request" paramName0="personMissionAuthorization" paramValue0="<%= personMissionAuthorizationId %>">
						<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.unauthorizatise"/>
					</wf:activityLink>
			<%
				}
			%>
		</td>
	</logic:present>
	<logic:notPresent name="personMissionAuthorization" property="authority">
		<td colspan="2">
		</td>
		<td>
			<bean:define id="personMissionAuthorization" name="personMissionAuthorization" type="module.mission.domain.PersonMissionAuthorization"/>
			<bean:define id="personMissionAuthorizationId" name="personMissionAuthorization" property="externalId" type="java.lang.String"/>
			<%
				if (personMissionAuthorization.canAuthoriseParticipantActivity()) {
			%>
					<wf:activityLink processName="process" activityName="AuthoriseParticipantActivity" scope="request" paramName0="personMissionAuthorization" paramValue0="<%= personMissionAuthorizationId%>">
						<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.authorizatise"/>
					</wf:activityLink>
			<%
				}
			%>
		</td>
	</logic:notPresent>
</tr>

<logic:present name="personMissionAuthorization" property="next">
	<bean:define id="personMissionAuthorization" name="personMissionAuthorization" property="next" toScope="request"/>
	<jsp:include page="personMissionAuthorizationView.jsp"/>
</logic:present>
