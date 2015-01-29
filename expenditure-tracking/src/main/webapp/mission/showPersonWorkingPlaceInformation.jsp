<%@page import="org.joda.time.LocalDate"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="module.organization.domain.Person"%>
<%@page import="module.mission.domain.MissionSystem"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="module.organization.domain.Accountability"%>
<%@page import="java.util.Set"%>
<%@page import="module.organization.domain.AccountabilityType"%>
<%@page import="java.util.Collection"%>
<%@page import="module.mission.domain.util.AuthorizationChain"%>
<%@page import="module.mission.domain.util.ParticipantAuthorizationChain"%>
<%@page import="module.organization.domain.Unit"%>
<%@page import="java.util.Iterator"%>

<%
	final MissionSystem missionSystem = MissionSystem.getInstance();
	final Person person = (Person) request.getAttribute("person");
%>

<br/>

<logic:empty name="workingPlaceAccountabilities">
	<h3>
		<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.person.working.place.information"/>
	</h3>
	<p>
		<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.person.working.place.information.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="workingPlaceAccountabilities">
	<logic:iterate id="accountability" name="workingPlaceAccountabilities" type="module.organization.domain.Accountability">
<%
		final Set<AccountabilityType> accountabilityTypes = MissionSystem.getInstance().getAccountabilityTypesForAuthorization(accountability.getAccountabilityType());
		final Collection<AuthorizationChain> participantAuthorizationChain = ParticipantAuthorizationChain.getParticipantAuthorizationChains(accountabilityTypes, accountability);
		request.setAttribute("participantAuthorizationChain", participantAuthorizationChain);
%>
		<h3>
			<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.person.working.place.information"/>:
			<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showUnitById" paramId="unitId" paramName="accountability" paramProperty="parent.externalId">
				<fr:view name="accountability" property="parent.presentationName"/>
			</html:link>
		</h3>
		<ul>
			<li>
				<fr:view name="accountability" property="accountabilityType.name"/>
			</li>
			<li>
				<logic:present name="accountability" property="endDate">
					<fr:view name="accountability" property="beginDate"/>
					-
					<fr:view name="accountability" property="endDate"/>
				</logic:present>
				<logic:notPresent name="accountability" property="endDate">
					<bean:message bundle="MISSION_RESOURCES" key="label.from"/>
					<fr:view name="accountability" property="beginDate"/>
				</logic:notPresent>
			</li>
		</ul>
		<h4>
			<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.person.authorizationChain"/>
		</h4>
		<logic:empty name="participantAuthorizationChain">
			<p>
				<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.person.authorizationChain.not.defined"/>
			</p>
		</logic:empty>
		<logic:notEmpty name="participantAuthorizationChain">
			<logic:iterate id="participantAuthorization" name="participantAuthorizationChain" type="module.mission.domain.util.AuthorizationChain">
				<table class="tstyle3 mvert1 width100pc tdmiddle punits">
					<tr>
						<th>
							<bean:message bundle="MISSION_RESOURCES" key="link.mission.dislocation.authority.order"/>
						</th>
						<th>
							<bean:message bundle="ORGANIZATION_RESOURCES" key="label.unit"/>
						</th>
						<th>
							<bean:message bundle="MISSION_RESOURCES" key="link.mission.dislocation.authority"/>
						</th>
					</tr>
<%
					int order = 0;
					for (AuthorizationChain authorizationChain = participantAuthorization;
							authorizationChain != null;
							authorizationChain = authorizationChain.getNext()) {
					    final Unit unit = authorizationChain.getUnit();
					    request.setAttribute("unit", unit);
					    final Collection<Accountability> authorities = unit.getChildrenAccountabilities(new LocalDate(), new LocalDate(), missionSystem.getAccountabilityTypesThatAuthorize().toArray(new AccountabilityType[0]));
					    final int span = Math.max(authorities.size(), 1);
					    final Iterator<Accountability> iterator = authorities.iterator();
%>
						<tr>
							<td rowspan="<%= span %>">
								<%= ++order %>
							</td>							
							<td rowspan="<%= span %>">
								<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showUnitById" paramId="unitId" paramName="unit" paramProperty="externalId">
									<%= unit.getPresentationName() %>
								</html:link>
							</td>
							<td>
<%
								if (authorities.isEmpty()) {
%>
									<bean:message bundle="MISSION_RESOURCES" key="link.mission.dislocation.authorities.none"/>
<%
								} else {
								    final Accountability authority = iterator.next();
%>
									<a href="<%= request.getContextPath() + "/missionOrganization.do?method=showPersonById&amp;personId=" + authority.getChild().getExternalId() %>" class="secondaryLink">
										<%= authority.getChild().getPresentationName() %>								
									</a>
<%
								}
%>
							</td>
						</tr>
<%
						while (iterator.hasNext()) {
						    final Accountability authority = iterator.next();
%>
							<tr>	
								<td>
									<a href="<%= request.getContextPath() + "/missionOrganization.do?method=showPersonById&amp;personId=" + authority.getChild().getExternalId() %>" class="secondaryLink">
										<%= authority.getChild().getPresentationName() %>								
									</a>
								</td>
							</tr>
<%
							}
					}
%>
				</table>
			</logic:iterate>
		</logic:notEmpty>
	</logic:iterate>
</logic:notEmpty>
