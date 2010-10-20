<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<bean:define id="process" name="information" property="process"/>
<table class="tstyle3 mervt1">
	<tr>	
		<th class="aleft"><bean:message bundle="MISSION_RESOURCES" key="label.mission.items"/></th>
		<th class="aleft"></th>
	</tr>
	<logic:empty name="process" property="mission.missionItems">
		<tr>
			<td class="aleft" colspan="2">
				<i><strong>
					<bean:message bundle="MISSION_RESOURCES" key="label.mission.items.none"/>
				</strong></i>
			</td>
		</td>
	</logic:empty>
	<logic:iterate id="missionItem" name="process" property="mission.missionItems">
		<tr>
			<td class="aleft">
				<bean:define id="schemaName"><bean:write name="missionItem" property="class.name"/>.view</bean:define>
				<fr:view name="missionItem" schema="<%= schemaName %>"/>
			</td>
			<td class="aleft">
				
				<bean:define id="itemOID" name="missionItem" property="externalId" type="java.lang.String"/>
				
				<bean:message key="label.activities" bundle="WORKFLOW_RESOURCES"/>: 
				<wf:activityLink processName="process" activityName="RemoveItemActivity" scope="request" paramName0="missionItem" paramValue0="<%= itemOID %>">
					<bean:message bundle="MISSION_RESOURCES" key="link.remove"/>
				</wf:activityLink>
			</td>
		</tr>
	</logic:iterate>
</table>
