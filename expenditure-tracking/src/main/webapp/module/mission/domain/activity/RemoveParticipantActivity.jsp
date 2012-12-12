<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

<bean:define id="process" name="information" property="process"/>

<logic:notEmpty name="process" property="mission.participantes">
<table class="tstyle3 mtop1 mbottom1">
	<tr>	
		<th class="aleft"><bean:message bundle="MISSION_RESOURCES" key="label.mission.participants"/></th>
		<th class="aleft"></th>
	</tr>
	<logic:iterate id="person" name="process" property="mission.participantes">
		<tr>
			<td class="aleft">
				<html:link styleClass="secondaryLink" page="/expenditureTrackingOrganization.do?method=viewPerson" paramId="personOid" paramName="person" paramProperty="externalId">
					<fr:view name="person" property="name"/>
				</html:link>
			</td>
			<td class="aleft" style="padding-left: 1em;">
				
				<bean:define id="personOID" name="person" property="externalId" type="java.lang.String"/>
				
				<wf:activityLink processName="process" activityName="RemoveParticipantActivity" scope="request" paramName0="person" paramValue0="<%= personOID %>">
					<bean:message bundle="MISSION_RESOURCES" key="link.remove"/>
				</wf:activityLink>
			</td>
		</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="process" property="mission.participantes">
	<p class="mtop15"><em><bean:message bundle="MISSION_RESOURCES" key="label.mission.participants.none"/></em></p>
</logic:empty>
