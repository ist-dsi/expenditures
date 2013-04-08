<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

<bean:define id="process" name="information" property="process"/>


<table class="tstyle3 mtop1">
	<tr>	
		<th class="aleft"><bean:message bundle="MISSION_RESOURCES" key="label.mission.financers"/></th>
		<th class="aleft"></th>
	</tr>
	<logic:empty name="process" property="mission.financer">
		<tr>
			<td class="aleft" colspan="2">
				<i><strong>
					<bean:message bundle="MISSION_RESOURCES" key="label.mission.financers.none"/>
				</strong></i>
			</td>
		</td>
	</logic:empty>
	<logic:iterate id="financer" name="process" property="mission.financer">
		<tr>
			<td class="aleft">
				<html:link styleClass="secondaryLink" page="/expenditureTrackingOrganization.do?method=viewOrganization" paramId="unitOid" paramName="financer" paramProperty="unit.externalId">
					<fr:view name="financer" property="unit.presentationName"/>
				</html:link>
			</td>
			<td class="aleft">
				
				<bean:define id="financerOID" name="financer" property="externalId" type="java.lang.String"/>
				
				<span style="padding-left: 1em;">
					<wf:activityLink processName="process" activityName="RemoveFinancerActivity" scope="request" paramName0="financer" paramValue0="<%= financerOID %>">
						<bean:message bundle="MISSION_RESOURCES" key="link.remove"/>
					</wf:activityLink>
				</span>
			</td>
		</tr>
	</logic:iterate>
</table>
