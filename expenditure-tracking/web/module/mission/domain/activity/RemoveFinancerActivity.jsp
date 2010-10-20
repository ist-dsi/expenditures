<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

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
