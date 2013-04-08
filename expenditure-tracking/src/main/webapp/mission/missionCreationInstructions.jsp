<%@page import="module.mission.domain.MissionSystem"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="title.mission.process.new" bundle="MISSION_RESOURCES"/></h2>

<p class="mvert05">
	<bean:message key="label.mission.selectProcessType" bundle="MISSION_RESOURCES"/>:
</p>

<table class="mvert1" width="100%">
	<tr>
		<td style="vertical-align: top; padding-right: 10px; width: 49%; border: 1px solid #e0e0e0; background: #f7f7f7; padding: 0.5em 1em 1em 1em;">
			<div>
				<p class="mtop0">
					<strong>
						<bean:message key="title.mission.process.type.dislocation" bundle="MISSION_RESOURCES"/>
					</strong>
				</p>
				<p>
					<bean:message key="label.mission.process.type.dislocation.description" bundle="MISSION_RESOURCES"/>
				</p>
				<ul class="mvert05" style="padding-left: 0em; list-style: none;">
					<li class="mbottom1">
						<p class="mvert025">
							<strong>
								<html:link action="/missionProcess.do?method=prepareNewMissionCreation&amp;grantOwnerEquivalence=false">
									<bean:message key="title.mission.process.type.dislocation" bundle="MISSION_RESOURCES"/>
								</html:link>
							</strong>
						</p>
					</li>
				</ul>
			</div>
		</td>
		
		<td style="width: 2%;"></td>

		<%
			if (MissionSystem.getInstance().allowGrantOwnerEquivalence()) {
		%>
		<td style="vertical-align: top; padding-right: 10px; width: 49%; border: 1px solid #e0e0e0; background: #f7f7f7; padding: 0.5em 1em 1em 1em;">
			<div>
				<p class="mtop0">
					<strong>
						<bean:message key="title.mission.process.type.grantOwnerEquivalence" bundle="MISSION_RESOURCES"/>
					</strong>
				</p>
				<p>
					<bean:message key="label.mission.process.type.grantOwnerEquivalence.description" bundle="MISSION_RESOURCES"/>
				</p>
				
				<ul class="mvert05" style="padding-left: 0em; list-style: none;">
					<li class="mbottom1">
						<p class="mvert025">
							<strong>
								<html:link action="/missionProcess.do?method=prepareNewMissionCreation&amp;grantOwnerEquivalence=true">
									<bean:message key="title.mission.process.type.grantOwnerEquivalence" bundle="MISSION_RESOURCES"/>
								</html:link>
							</strong>
						</p>
					</li>
			</div>
		</td>
		<% } %>

	</tr>
</table>
