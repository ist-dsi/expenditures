<%@page import="module.organization.domain.AccountabilityType"%>
<%@page import="module.mission.domain.MissionSystem"%>
<%@page import="module.organizationIst.domain.IstAccountabilityType"%>
<%@page import="module.organization.domain.Unit"%>
<%@page import="module.organization.domain.Accountability"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>
<%@page import="module.organizationIst.domain.OrganizationIstSystem"%>
<%@page import="module.mission.domain.Salary"%>
<%@page import="module.organization.domain.Person"%>

<h3 class="mtop1"><bean:message bundle="MISSION_RESOURCES" key="label.mission.participants"/></h3>

<logic:empty name="process" property="mission.participantes">
	<p>
		<em>
			<bean:message bundle="MISSION_RESOURCES" key="label.mission.participants.none"/>
		</em>
	</p>
</logic:empty>

<logic:notEmpty name="process" property="mission.participantes">
<div id="participants">
	<bean:define id="process" name="process" type="module.mission.domain.MissionProcess"/>
	<div>
		<table class="tstyle3 mvert1 width100pc tdmiddle punits">
			<logic:iterate id="entry" name="process" property="mission.participantAuthorizations">
				<bean:define id="person" name="entry" property="key" type="module.organization.domain.Person"/>
				<bean:define id="personOID" name="person" property="externalId" type="java.lang.String"/>
				<tr>
					<%
						int chainSize = process.getPersonAuthorizationChainSize(person);
					%>
					<td rowspan="<%= Integer.toString(chainSize + 5) %>">
						<bean:define id="url" type="java.lang.String">https://fenix.ist.utl.pt/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="person" property="user.username"/></bean:define>
						<img src="<%= url %>">
					</td>
					<td colspan="4">
						<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="person" paramProperty="externalId">
							<fr:view name="person" property="name"/> (<%= OrganizationIstSystem.getInstance().getUserAliasses(person) %>)
						</html:link>
						<wf:activityLink processName="process" activityName="RemoveParticipantActivity" scope="request" paramName0="person" paramValue0="<%= personOID %>">
							<bean:message bundle="MYORG_RESOURCES" key="link.remove"/>
						</wf:activityLink>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						Entidade Patronal: 
						<%
							boolean hasEmployer = false;
							final AccountabilityType type = IstAccountabilityType.EMPLOYMENT.readAccountabilityType();
							for (final Accountability accountability : ((module.organization.domain.Person) person).getParentAccountabilitiesSet()) {
							    if (accountability.isActiveNow() && accountability.isValid() && type == accountability.getAccountabilityType()) {
									final Unit unit = (Unit) accountability.getParent();
									hasEmployer = true;
									if (unit == MissionSystem.getInstance().getOrganizationalModel().getPartiesIterator().next()) {
									%>
										<%= unit.getAcronym() %>
									<%
									} else {
									%>
										<span style="color: red;">
											<%= unit.getAcronym() %>
										</span>
									<%
									}
							    }
							}
							if (!hasEmployer) {
						%>
								<span style="color: red;">
									Não Tem Entidade Patronal.
								</span>
						<%
							}
						%>

						<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.current.relation.to.institution"/>:
						<% if (process.getMission().hasAnyCurrentRelationToInstitution((module.organization.domain.Person) person)) { %>
							<%= process.getMission().getCurrentRelationToInstitution((module.organization.domain.Person) person) %>
						<% } else { %>
							<span style="color: red;">
								<%= process.getMission().getCurrentRelationToInstitution((module.organization.domain.Person) person) %>
							</span>
						<% } %>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<p class="mtop05 mbottom05">
							<logic:equal name="process" property="mission.grantOwnerEquivalence" value="true">
								<% if (process.getMission().getWithSalary((module.organization.domain.Person) person)) { %>
									<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.with.salary"/>:
								<% } else { %>
									<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.without.salary"/>:
								<% } %>
								<wf:activityLink processName="process" activityName="TogleParticipantSalaryActivity" scope="request" paramName0="person" paramValue0="<%= personOID %>">
									<bean:message bundle="MISSION_RESOURCES" key="label.change"/>
								</wf:activityLink>
								-
							</logic:equal>
							<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.number.of.days.to.discount.lunch.allowence.from.wages"/>:
							<%= process.getMission().getNunberOfLunchesToDiscount((module.organization.domain.Person) person) %>
						</p>

<!-- 
						<logic:present role="pt.ist.bennu.core.domain.RoleType.MANAGER"> 
							<p class="mtop05 mbottom05">
								<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.salary"/>:
								<logic:notPresent name="person" property="salary">
									<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.salary.not.defined"/>:
								</logic:notPresent>
								<logic:present name="person" property="salary">
									<fr:view name="person" property="salary.value"/>
								</logic:present>
								-
								<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.category"/>:
								<%= Salary.getDefaultDailyPersonelExpenseCategory(process.getMission().getDailyPersonelExpenseTable(), (module.organization.domain.Person) person).getDescription() %>
							</p>
						</logic:present>
 -->
					</td>
				</tr>
				<tr>
					<th colspan="4">
						<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.authorization.chain"/>
						<logic:present name="entry" property="value">
							<bean:define id="personMissionAuthorization" name="entry" property="value" toScope="request"/>
							<wf:activityLink processName="process" activityName="DefineParticipantAuthorizationChainActivity" scope="request" paramName0="person" paramValue0="<%= personOID %>">
								<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.authorization.chain.change"/>
							</wf:activityLink>
						</logic:present>
					</th>
				</tr>
				<logic:present name="entry" property="value">
					<tr>
						<th>
							<bean:message bundle="ORGANIZATION_RESOURCES" key="label.unit"/>
						</th>
						<th>
							<bean:message bundle="MISSION_RESOURCES" key="link.participant.authorized.by"/>
						</th>
						<th>
							<bean:message bundle="MISSION_RESOURCES" key="link.participant.authorized.at"/>
						</th>
						<th>
						</th>
					</tr>
					<bean:define id="personMissionAuthorization" name="entry" property="value" toScope="request"/>
					<jsp:include page="personMissionAuthorizationView.jsp"/>
				</logic:present>
				<logic:notPresent name="entry" property="value">
					<tr>
						<td colspan="3">
							<em>
								<b><bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.authorization.chain.not.defined"/>.</b>
							</em>
							<wf:activityLink processName="process" activityName="DefineParticipantAuthorizationChainActivity" scope="request" paramName0="person" paramValue0="<%= personOID %>">
								<bean:message bundle="MISSION_RESOURCES" key="label.mission.participant.authorization.chain.defined"/>
							</wf:activityLink>
						</td>
					</tr>
				</logic:notPresent>
			</logic:iterate>
		</table>
	</div>
</div>

</logic:notEmpty>

<wf:activityLink processName="process" activityName="AddParticipantActivity" scope="request">
	<bean:message bundle="MISSION_RESOURCES" key="activity.AddParticipantActivity"/>
</wf:activityLink>
