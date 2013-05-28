<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>
<%@page import="module.mission.domain.util.MissionStateProgress"%>

<table style="text-align: center; width: 100%;">
	<tr>
		<td align="center">
			<table style="border-collapse: separate; border-spacing: 10px;">
				<tr>
					<bean:define id="process" name="process" type="module.mission.domain.MissionProcess"/>
					<logic:iterate id="missionState" name="process" property="missionStates" type="module.mission.domain.util.MissionState">
						<%
						    String colorStyle = "";
					    	if (missionState.getStateProgress(process) == MissionStateProgress.COMPLETED) {
						        colorStyle = "background-color: #CEF6CE; border-color: #04B404;";
					    	} else if (missionState.getStateProgress(process) == MissionStateProgress.PENDING) {
						        colorStyle = "background-color: #F6E3CE; border-color: #B45F04;";
					    	}
						%>
						<td style="<%=colorStyle + "border-style: solid; border-width: thin; width: 120px; padding: 5px; border-radius: 2em; -moz-border-radius: 2em;"%>" align="center" title="<%=missionState.getLocalizedDescription()%>">
							<%=missionState.getLocalizedName()%>
						</td>
					</logic:iterate>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td align="center">
			<table style="border-collapse: separate; border-spacing: 10px; border-style: dotted; border-width: thin; background-color: #FEFEFE;">
				<tr>
					<td align="center">
						<strong>
							<bean:message bundle="MISSION_RESOURCES" key="label.mission.state.view.label"/>
						</strong>
					</td>
					<td style="border-style: solid; border-width: thin; width: 12px; padding: 5px; border-radius: 2em; -moz-border-radius: 2em;">
					</td>
					<td>
						<%=MissionStateProgress.IDLE.getLocalizedName()%>
					</td>
					<td style="background-color: #F6E3CE; border-color: #B45F04; border-style: solid; border-width: thin; width: 12px; padding: 5px; border-radius: 2em; -moz-border-radius: 2em;">
					</td>
					<td>
						<%=MissionStateProgress.PENDING.getLocalizedName()%>
					</td>
					<td style="background-color: #CEF6CE; border-color: #04B404; border-style: solid; border-width: thin; width: 12px; padding: 5px; border-radius: 2em; -moz-border-radius: 2em;">
					</td>
					<td>
						<%=MissionStateProgress.COMPLETED.getLocalizedName()%>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
