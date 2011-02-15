<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<logic:present name="process" property="processCanceled">
	<logic:equal name="process" property="processCanceled" value="true">
		<div class="infobox_warning">
			<bean:message key="label.process.canceled" bundle="MISSION_RESOURCES"/>:
		</div>
	</logic:equal>
</logic:present>

<bean:define id="missionProcess" name="process" type="module.mission.domain.MissionProcess"/>
<% if (!missionProcess.getIsCanceled()
		&& missionProcess.isApproved()
		&& missionProcess.getMission().hasAnyFinancer()
		&& !missionProcess.hasAllAllocatedFunds()) { %>
	<div class="infobox_warning">
		<bean:message key="label.process.isPendingFundAllocation" bundle="MISSION_RESOURCES"/>:
	</div>
<% } %>

<bean:define id="missionProcessMessages" name="process" property="mission.consistencyMessages"/>
<logic:notEmpty name="missionProcessMessages">
	<div class="highlightBox">
		<logic:iterate id="missionProcessMessage" name="missionProcessMessages" length="1">
			<bean:write name="missionProcessMessage" filter="false"/>
		</logic:iterate>
		<logic:iterate id="missionProcessMessage" name="missionProcessMessages" offset="1">
			<br/>
			<br/>
			<bean:write name="missionProcessMessage" filter="false"/>
		</logic:iterate>
	</div>
</logic:notEmpty>

<logic:present name="process" property="mission.missionVersion.changesAfterArrival">
	<div style="padding: 5px 10px; margin: 1em 0; border:1px solid #E0E0E0;">
		<h4>
			<bean:message key="label.module.mission.SendForProcessTerminationActivity" bundle="MISSION_RESOURCES"/>:
		</h4>
		<div class="infobox">
			<p>
				<logic:equal name="process" property="mission.missionVersion.changesAfterArrival" value="true">
					<bean:message key="label.module.mission.SendForProcessTerminationWithChangesActivity.confirmation" bundle="MISSION_RESOURCES"/>:
					<br/>
					<br/>
					<fr:view name="process" property="mission.missionVersion.descriptionOfChangesAfterArrival"/>
				</logic:equal>
				<logic:notEqual name="process" property="mission.missionVersion.changesAfterArrival" value="true">
					<bean:message key="label.module.mission.SendForProcessTerminationActivity.confirmation" bundle="MISSION_RESOURCES"/>:
				</logic:notEqual>
				<br/>
				<br/>
				<span class="smalltxt">
					<fr:view name="process" property="mission.missionVersion.sentForTermination"/>
					-
					<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="process" paramProperty="mission.missionVersion.terminator.externalId">
						<fr:view name="process" property="mission.missionVersion.terminator.name"/>
					</html:link>
				</span>
			</p>
		</div>
	</div>
</logic:present>

<bean:define id="missionChangeDescriptions" name="process" property="mission.sortedMissionChangeDescriptions"/>
<logic:notEmpty name="missionChangeDescriptions">
	<logic:iterate id="missionChangeDescription" name="missionChangeDescriptions">
		<div style="padding: 5px 10px; margin: 1em 0; border:1px solid #E0E0E0;">
			<h4>
				<bean:message key="label.change.justification.description" bundle="MISSION_RESOURCES"/> (<fr:view name="missionChangeDescription" property="revertInstant"/>):
			</h4>
			<div class="infobox">
				<p>
					<span class="smalltxt">
						<fr:view name="missionChangeDescription" property="description"/>
					</span>
				</p>
			</div>
		</div>
	</logic:iterate>
</logic:notEmpty>

<logic:notPresent name="process" property="mission.missionVersion.changesAfterArrival">
<logic:notEmpty name="process" property="missionProcessLateJustifications">
	<div style="padding: 5px 10px; margin: 1em 0; border:1px solid #E0E0E0;">
		<h4>
			<bean:message key="label.late.justification.motive" bundle="MISSION_RESOURCES"/>:
		</h4>
		<logic:iterate id="justification" name="process" property="orderedMissionProcessLateJustificationsSet">
			<div class="infobox">
				<p>
					<fr:view name="justification" property="justification"/>
					<br/>
					<span class="smalltxt">
						<fr:view name="justification" property="justificationDateTime"/>
						-
						<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="justification" paramProperty="person.externalId">
							<fr:view name="justification" property="person.name"/>
						</html:link>
					</span>
				</p>
			</div>
		</logic:iterate>
	</div>
</logic:notEmpty>
</logic:notPresent>

<logic:present name="process" property="mission.serviceGaranteePerson">
	<div style="padding: 5px 10px; margin: 1em 0; border:1px solid #E0E0E0;">
		<div class="infobox">
			<bean:message key="label.module.mission.approve.confirm.service.is.assured" bundle="MISSION_RESOURCES"/>
			<br/>
			<fr:view name="process" property="mission.serviceGaranteeInstante"/>
			-
			<bean:write name="process" property="mission.serviceGaranteePerson.name"/>
		</div>
	</div>
</logic:present>
