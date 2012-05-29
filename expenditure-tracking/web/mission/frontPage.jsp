<%@page import="java.util.TreeSet"%>
<%@page import="java.util.SortedSet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<%@page import="java.util.Set"%>
<%@page import="module.mission.domain.Mission"%>
<%@page import="module.mission.domain.MissionProcess"%>
<%@page import="module.organization.domain.Person"%>
<%@page import="module.mission.domain.NationalMission"%>

<h2>
	<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page"/>
</h2>

<fr:form action="/missionProcess.do?method=frontPage">
	<fr:edit id="missionContext" name="missionContext">
		<fr:schema type="module.mission.presentationTier.action.util.MissionContext" bundle="MISSION_RESOURCES">
			<fr:slot name="missionYear" key="label.module.mission.seeYear" required="true" layout="menu-select-postback">
				<fr:property name="providerClass" value="module.mission.presentationTier.provider.MissionYearProvider"/>
				<fr:property name="saveOptions" value="true"/>
				<fr:property name="format" value="${year}"/>
				<fr:property name="nullOptionHidden" value="true"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form" />
			<fr:property name="columnClasses" value=",,tderror" />
		</fr:layout>
	</fr:edit>
</fr:form>


	<bean:define id="missionAuthorizationMap" toScope="request" name="missionContext" property="missionYear.missionAuthorizationMap" type="module.mission.domain.util.MissionAuthorizationMap"/>
	<%
		if (missionAuthorizationMap.hasSomeUnit()) {
	%>
		<div style="border: 1px dotted #aaa; background: #E6E6E6; padding: 10px;  margin-top: 10px;">
			<logic:iterate id="unit" indexId="i" name="missionAuthorizationMap" property="levelsForUser">
				<logic:present name="unit">
					<h3 class="mtop0">
						<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.dislocation.aproval"/>
						<bean:write name="unit" property="presentationName"/>
					</h3>
				<%
					final Set<module.mission.domain.PersonMissionAuthorization> personMissionAuthorizations = missionAuthorizationMap.getPersonMissionAuthorizations()[i];
					if (personMissionAuthorizations.size() == 0) {
				%>
						<p>
							<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.none"/>
						</p>
				<%
					} else {
				%>
						<form action="<%= request.getContextPath() + "/missionProcess.do" %>" method="post">
							<html:hidden property="method" value="aproveDislocations"/>
							<table class="tstyle3 thleft tdleft mbottom2" width="100%">
								<tr>
									<th></th>
									<th><bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.participant"/></th>
									<th><bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.destination"/></th>
									<th><bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.start"/></th>
									<th><bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.duration"/></th>
									<th><bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.objective"/></th>
									<th><bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.totalCost"/></th>
									<th><bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.process"/></th>
								</tr>
								<%
									final SortedSet<module.mission.domain.PersonMissionAuthorization> sortedSet = new TreeSet<module.mission.domain.PersonMissionAuthorization>(module.mission.domain.PersonMissionAuthorization.COMPARATOR_BY_DEPARTURE_DATE);
									sortedSet.addAll(personMissionAuthorizations);
									for (final module.mission.domain.PersonMissionAuthorization personMissionAuthorization : sortedSet) {
					    				final Mission mission = personMissionAuthorization.getAssociatedMission();
					    				final MissionProcess missionProcess = mission.getMissionProcess();
					    				request.setAttribute("missionProcess", missionProcess);
					    				final Person subject = personMissionAuthorization.getSubject();
								%>
									<tr>
										<td>
											<input type="checkbox" name="personMissionAuthorizationIds" value="<%= personMissionAuthorization.getExternalId() %>" checked="checked"/>
										</td>
										<td>
											<a href="<%= request.getContextPath() + "/missionOrganization.do?method=showPersonById&amp;personId=" + subject.getExternalId() %>" class="secondaryLink">
												<%= subject.getFirstAndLastName() %>
											</a>
										</td>
										<td>
											<% if (mission instanceof NationalMission) { %>
												<%= mission.getLocation() %>
											<% } else { %>
												<%= mission.getCountry() == null ? mission.getLocation() : mission.getCountry().getName() %>
											<% } %> 
										</td>
										<td>
											<%= mission.getDaparture().toString("yyyy/MM/dd") %>
										</td>
										<td>
											<%= mission.getDurationInDays() %>
											<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.days"/>
										</td>
										<td><%= mission.getObjective() %></td>
										<td><%= mission.getTotalPrevisionaryCost().toFormatString() %></td>
										<td>
											<html:link action="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="missionProcess" paramProperty="externalId">
												<bean:write name="missionProcess" property="processIdentification"/>
											</html:link>
										</td>
									</tr>
								<%
										}
								%>
							</table>
							<html:submit styleClass="inputbutton"><bean:message key="button.aprove" bundle="MISSION_RESOURCES"/></html:submit>
						</form>
				<%
					}
				%>
			</logic:present>
		</logic:iterate>
	</div>	
	<div style="clear: left;"></div>
<% } %>


<div style="float: left; width: 100%">
	<table style="width: 100%; margin: 1em 0;">
		<tr>
			<td style="border: 1px dotted #aaa; padding: 10px 15px; width: 48%; vertical-align: top;">
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.pending.my.aproval"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="missionContext" property="missionYear.pendingAproval"/>
				<jsp:include page="processList.jsp"/>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.pending.my.authorization"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="missionContext" property="missionYear.pendingAuthorization"/>
				<jsp:include page="processList.jsp"/>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.pending.fund.allocation"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="missionContext" property="missionYear.pendingFundAllocation"/>
				<bean:define id="directProcessList" toScope="request" name="missionContext" property="missionYear.directPendingFundAllocation"/>
				<jsp:include page="processListDirect.jsp"/>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.pending.processing.personel.information"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="missionContext" property="missionYear.pendingProcessingPersonelInformation"/>
				<bean:define id="directProcessList" toScope="request" name="missionContext" property="missionYear.pendingDirectProcessingPersonelInformation"/>
				<jsp:include page="processListDirect.jsp"/>
			</td>
			<td style="border: none; width: 2%; padding: 0;"></td>
			<td style="border: 1px dotted #aaa; padding: 10px 15px; width: 48%; vertical-align: top;">
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.taken"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="missionContext" property="missionYear.taken"/>
				<jsp:include page="processList.jsp"/>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.requested"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="missionContext" property="missionYear.requested"/>
				<jsp:include page="processList.jsp"/>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.aprovalResponsible"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="missionContext" property="missionYear.aprovalResponsible"/>
				<jsp:include page="processList.jsp"/>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.participate"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="missionContext" property="missionYear.participate"/>
				<jsp:include page="processList.jsp"/>
			</td>
		</tr>
	</table>
</div>

<div style="clear: both;"></div>
