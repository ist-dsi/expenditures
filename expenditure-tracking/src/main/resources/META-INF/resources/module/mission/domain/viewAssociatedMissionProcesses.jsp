<%@page import="myorg.presentationTier.actions.ContextBaseAction"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="module.mission.domain.MissionSystem"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

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
		</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>
<wf:activityLink id="associate" processName="process" activityName="AssociateMissionProcessActivity" scope="request">
	<bean:message bundle="MISSION_RESOURCES" key="activity.AssociateMissionProcessActivity"/>
</wf:activityLink>
<br/>
