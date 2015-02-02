<%@page import="module.mission.domain.MissionProcess"%>
<%@page import="module.mission.domain.PersonalVehiclItem"%>
<%@page import="module.mission.domain.NationalMission"%>
<%@page import="module.mission.domain.Mission"%>
<%@page import="module.organization.domain.Person"%>
<%@page import="module.mission.domain.VehiclItem"%>
<%@page import="java.util.Collection"%>
<%@page import="module.mission.domain.MissionSystem"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<div style="border: 1px dotted #aaa; background: #E6E6E6; padding: 10px;  margin-top: 10px;">
	<h3 class="mtop0">
		<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.vehicle.authorization"/>
	</h3>

	<logic:present name="noLongerActiveVehicleItems">
		<div class="infobox_warning">
			<bean:message bundle="MISSION_RESOURCES" key="label.vehicle.items.not.authorized.because.of.state.change"/>:
			<ul>
				<logic:iterate id="vehicleItem" name="noLongerActiveVehicleItems">
					<li>
						<html:link action="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="vehicleItem" paramProperty="mission.missionProcess.externalId">
							<bean:write name="vehicleItem" property="mission.missionProcess.processIdentification"/>
						</html:link>
					</li>
				</logic:iterate>
			</ul>
		</div>
	</logic:present>

	<%
	Collection<VehiclItem> pendingVehicles = MissionSystem.getInstance().getVehicleItemsPendingAuthorization();
	if (pendingVehicles.isEmpty()) {
	%>
		<p>
			<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.none"/>
		</p>
	<% } else { %>
		<form action="<%= request.getContextPath() + "/missionProcess.do" %>" method="post">
			<html:hidden property="method" value="massAuthorizeVehicles"/>
			<table class="tstyle3 thleft tdleft mbottom2" width="100%">
				<tr>
					<th></th>
					<th><bean:message bundle="MISSION_RESOURCES" key="label.mission.item.driver.short"/></th>
					<th><bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.destination"/></th>
					<th><bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.start"/></th>
					<th><bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.duration.short"/></th>
					<th><bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.objective"/></th>
					<th><bean:message bundle="MISSION_RESOURCES" key="label.mission.item.type"/></th>
					<th><bean:message bundle="MISSION_RESOURCES" key="label.mission.item.vehicle.kms.short"/></th>
					<th><bean:message bundle="MISSION_RESOURCES" key="label.mission.item.value"/></th>
					<th><bean:message bundle="MISSION_RESOURCES" key="label.module.mission.front.page.list.process"/></th>
				</tr>
				<%
					for (VehiclItem vehicle : pendingVehicles) {
		    			final Person driver = vehicle.getDriver();
		    			final Mission mission = vehicle.getMission();
	    				final MissionProcess missionProcess = mission.getMissionProcess();
	    				request.setAttribute("missionProcess", missionProcess);
				%>
						<tr>
							<td>
								<input type="checkbox" name="vehicleItemIds" value="<%= vehicle.getExternalId() %>" checked="checked"/>
							</td>
							<td>
								<% if (driver != null) { %>
									<a href="<%= request.getContextPath() + "/missionOrganization.do?method=showPersonById&amp;personId=" + driver.getExternalId() %>" class="secondaryLink">
										<%= driver.getFirstAndLastName() %>
									</a>
								<% } else { %>
									-
								<% } %>
							</td>
							<td>
								<% if (mission instanceof NationalMission) { %>
									<%= mission.getLocation() %>
								<% } else { %>
									<%= mission.getCountry() == null ? mission.getLocation() : mission.getCountry().getName().getContent() %>
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
							<td><%= vehicle.getLocalizedName() %></td>
							<td>
								<% if (vehicle instanceof PersonalVehiclItem) { %>
									<%= ((PersonalVehiclItem) vehicle).getKms() %>
								<% } else { %>
									-
								<% } %> 
							</td>
							<td><%= vehicle.getValue().toFormatString() %></td>
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
			<html:submit styleClass="inputbutton"><bean:message key="button.authorize.vehicles" bundle="MISSION_RESOURCES"/></html:submit>
		</form>
	<% } %>
</div>