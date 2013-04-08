<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>
<%@page import="pt.ist.bennu.core.presentationTier.actions.ContextBaseAction"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="module.mission.domain.MissionSystem"%>

<h4>
	<bean:message bundle="MISSION_RESOURCES" key="label.mission.associated.mission.processes"/>
</h4>
<logic:empty name="process" property="associatedMissionProcesses">
	<bean:message bundle="MISSION_RESOURCES" key="label.mission.associated.mission.processes.none"/>
</logic:empty>
<logic:notEmpty name="process" property="associatedMissionProcesses">
	<ul>
		<logic:iterate id="associatedProcess" name="process" property="associatedMissionProcesses" type="module.mission.domain.MissionProcess">
		<li>
			<a href="<%=
					"https://" + associatedProcess.getMissionSystem().getVirtualHost().get(0).getHostname()
					+ "/ForwardToProcess/" + associatedProcess.getExternalId()
					%>">
				<bean:write name="associatedProcess" property="processIdentification"/></a>
			(<%= associatedProcess.getMissionSystem().getFirstTopLevelUnitFromExpenditureSystem().getPresentationName() %>)
			<wf:activityLink id="disassociate" processName="process" activityName="DisassociateMissionProcessActivity" paramName0="missionProcessToAssociate" paramValue0="<%=associatedProcess.getExternalId() %>" scope="request">
			<bean:message bundle="MISSION_RESOURCES" key="activity.DisassociateMissionProcessActivity"/>
			</wf:activityLink>
		</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>
<wf:activityLink id="associate" processName="process" activityName="AssociateMissionProcessActivity" scope="request">
	<bean:message bundle="MISSION_RESOURCES" key="activity.AssociateMissionProcessActivity"/>
</wf:activityLink>
<br/>
