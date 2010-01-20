<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<fr:view name="process" property="processStatesSet">
	<fr:schema type="module.mission.domain.util.MissionAuthorizationAccountabilityTypeBean" bundle="ACQUISITION_RESOURCES">
    	<fr:slot name="whenDateTime" key="label.whenOperationWasRan" bundle="WORKFLOW_RESOURCES"/>
    	<fr:slot name="acquisitionProcessStateType" key="label.acquisitionProcessStateType"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="columnClasses" value="smalltxt, smalltxt aleft,smalltxt"/>
		<fr:property name="sortBy" value="whenDateTime"/>
	</fr:layout>
</fr:view>

<div class="dinline forminline">
	<fr:edit id="activityBean" name="information"
			action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'
			schema="activityInformation.JumpToProcessState"
			>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:edit>
</div>

