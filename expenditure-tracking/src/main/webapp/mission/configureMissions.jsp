<%@page import="module.mission.domain.MissionSystem"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/collection-pager" prefix="cp"%>

<h2><bean:message key="label.mission.missionConfiguration" bundle="MISSION_RESOURCES"/></h2>

<br/>

<h3><bean:message key="label.mission.missionConfiguration.country.for.national.missions" bundle="MISSION_RESOURCES"/></h3>

<logic:notPresent name="missionSystem" property="country">
	<p>
		<bean:message key="label.mission.missionConfiguration.country.for.national.missions.not.defined" bundle="MISSION_RESOURCES"/>
		<br/>
		<html:link action="/configureMissions.do?method=selectCountry">
			<bean:message key="label.mission.missionConfiguration.select.country" bundle="MISSION_RESOURCES"/>
		</html:link>
	</p>
</logic:notPresent>
<logic:present name="missionSystem" property="country">
	<br/>
	<div class="orgTBox orgTBoxLight">
		<bean:write name="missionSystem" property="country.name.content"/>
	</div>	
	<p>
		<html:link action="/configureMissions.do?method=selectCountry">
			<bean:message key="label.mission.missionConfiguration.change.country" bundle="MISSION_RESOURCES"/>
		</html:link>
	</p>
</logic:present>

<br/>

<h3><bean:message key="label.mission.missionConfiguration.model.for.mission.authorizations" bundle="MISSION_RESOURCES"/></h3>

<logic:notPresent name="missionSystem" property="organizationalModel">
	<p>
		<bean:message key="label.mission.missionConfiguration.model.for.mission.authorizations.not.defined" bundle="MISSION_RESOURCES"/>
		<br/>
		<html:link action="/configureMissions.do?method=prepareSelectOrganizationalModel">
			<bean:message key="label.mission.missionConfiguration.select.organizationalModel" bundle="MISSION_RESOURCES"/>
		</html:link>
	</p>
</logic:notPresent>
<logic:present name="missionSystem" property="organizationalModel">
	<br/>
	<div class="orgTBox orgTBoxLight">
		<html:link action="/organizationModel.do?method=viewModel" paramId="organizationalModelOid" paramName="missionSystem" paramProperty="organizationalModel.externalId">
			<bean:write name="missionSystem" property="organizationalModel.name.content"/>
		</html:link>
	</div>	
	<p>
		<html:link action="/configureMissions.do?method=prepareSelectOrganizationalModel">
			<bean:message key="label.mission.missionConfiguration.change.organizationalModel" bundle="MISSION_RESOURCES"/>
		</html:link>
	</p>
	<br/>
	<h3><bean:message key="label.mission.missionConfiguration.accountabilityTypes.for.authorization" bundle="MISSION_RESOURCES"/></h3>
	<p>
		<html:link action="/configureMissions.do?method=prepareAddMissionAuthorizationAccountabilityType">
			<bean:message key="label.mission.missionConfiguration.add.accountability.type" bundle="MISSION_RESOURCES"/>
		</html:link>
	</p>
	<logic:notEmpty name="missionSystem" property="missionAuthorizationAccountabilityTypesSet">
		<fr:view name="missionSystem" property="missionAuthorizationAccountabilityTypesSet">
			<fr:schema type="module.mission.domain.util.MissionAuthorizationAccountabilityTypeBean" bundle="ORGANIZATION_RESOURCES">
    			<fr:slot name="accountabilityType.name.content" key="label.accountability.type" bundle="ORGANIZATION_RESOURCES"/>
    			<fr:slot name="accountabilityTypesAsString" key="label.accountability.types.for.mission.authorization" bundle="MISSION_RESOURCES"/>
			</fr:schema>    				
			<fr:layout name="tabular">		
				<fr:property name="classes" value="tview1"/>

				<fr:property name="linkFormat(delete)" value="/configureMissions.do?method=deleteMissionAuthorizationAccountabilityType&missionAuthorizationAccountabilityTypeOid=\${externalId}"/>
				<fr:property name="bundle(delete)" value="MISSION_RESOURCES"/>
				<fr:property name="key(delete)" value="link.delete"/>
				<fr:property name="order(delete)" value="1"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>


<form action="<%= request.getContextPath() + "/configureMissions.do" %>" method="post">
	<html:hidden property="method" value="togleAllowGrantOwnerMissionProcessNature"/>

	<bean:message key="label.configuration.allowGrantOwnerEquivalence" bundle="MISSION_RESOURCES"/>

	<input type="checkbox" name="allowGrantOwnerEquivalence"
		<% if (MissionSystem.getInstance().allowGrantOwnerEquivalence()) {%>
				checked="checked"
		<% } %>
	/>

	<html:submit styleClass="inputbutton">
		<bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/>
	</html:submit>
</form>

<br/>

<form action="<%= request.getContextPath() + "/configureMissions.do" %>" method="post">
	<html:hidden property="method" value="togleUseWorkingPlaceAuthorizationChain"/>

	<bean:message key="label.configuration.useWorkingPlaceAuthorizationChain" bundle="MISSION_RESOURCES"/>

	<input type="checkbox" name="useWorkingPlaceAuthorizationChain"
		<% if (MissionSystem.getInstance().useWorkingPlaceAuthorizationChain()) {%>
				checked="checked"
		<% } %>
	/>

	<html:submit styleClass="inputbutton">
		<bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/>
	</html:submit>
</form>


<br/>

<h3><bean:message key="label.mission.missionConfiguration.daily.personel.expense.tables" bundle="MISSION_RESOURCES"/></h3>
<p>
	<html:link action="/configureMissions.do?method=prepareCreateDailyPersonelExpenseTable">
		<bean:message key="label.mission.missionConfiguration.daily.personel.expense.table.create" bundle="MISSION_RESOURCES"/>
	</html:link>
</p>
<logic:empty name="missionSystem" property="dailyPersonelExpenseTablesSet">
	<p>
		<bean:message key="label.mission.missionConfiguration.daily.personel.expense.tables.none" bundle="MISSION_RESOURCES"/>
	</p>
</logic:empty>
<logic:notEmpty name="missionSystem" property="dailyPersonelExpenseTablesSet">
	<fr:view name="missionSystem" property="currentDailyExpenseTables">
		<fr:schema type="module.mission.domain.util.MissionAuthorizationAccountabilityTypeBean" bundle="MISSION_RESOURCES">
   			<fr:slot name="aplicableToMissionClass" layout="name-resolver" key="label.mission.type" bundle="MISSION_RESOURCES"/>
		</fr:schema>    				
		<fr:layout name="tabular">
			<fr:property name="classes" value="tview1"/>
			<fr:property name="linkFormat(view)" value="/configureMissions.do?method=viewDailyPersonelExpenseTable&dailyPersonelExpenseTableOid=\${externalId}"/>
			<fr:property name="bundle(view)" value="MISSION_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>


<br/>

<h3><bean:message key="missionConfiguration.verificationQueue" bundle="MISSION_RESOURCES"/></h3>
<fr:form id="missionSystemVerificationQueueForm">
	<fr:edit id="missionSystemVerificationQueue" name="missionSystem">
		<fr:schema type="module.mission.domain.MissionSystem" bundle="MISSION_RESOURCES">
	    	<fr:slot name="verificationQueue" layout="menu-select-postback" key="label.queue" bundle="MISSION_RESOURCES">
	        	<fr:property name="providerClass" value="module.workflow.presentationTier.provider.WorkflowQueueProvider" />
	        	<fr:property name="eachSchema" value="workflowQueue-name"/>
	        	<fr:property name="eachLayout" value="values"/>
	        	<fr:property name="classes" value="nobullet noindent"/>
	        	<fr:property name="sortBy" value="name"/>
				<fr:property name="saveOptions" value="true"/>
	    	</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form listInsideClear" />
			<fr:property name="columnClasses" value="width100px,,tderror" />
		</fr:layout>
	</fr:edit>
</fr:form>


<br/>

<h3><bean:message key="missionConfiguration.selectEmploymentAccountabilityType" bundle="MISSION_RESOURCES"/></h3>
<fr:form id="missionSystemEmploymentAccountabilityTypeForm">
	<fr:edit id="missionSystemEmploymentAccountabilityType" name="missionSystem">
		<fr:schema type="module.mission.domain.MissionSystem" bundle="MISSION_RESOURCES">
	    	<fr:slot name="employmentAccountabilityType" layout="menu-select-postback" key="label.accountabilityType" bundle="MISSION_RESOURCES">
	        	<fr:property name="providerClass" value="module.mission.presentationTier.provider.MissionAccountabilityTypeEmploymentProvider" />
	        	<fr:property name="format" value="\${name}" />
	        	<fr:property name="classes" value="nobullet noindent"/>
	        	<fr:property name="sortBy" value="name"/>
				<fr:property name="saveOptions" value="true"/>
	    	</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form listInsideClear" />
			<fr:property name="columnClasses" value="width100px,,tderror" />
		</fr:layout>
	</fr:edit>
</fr:form>


<br/>



<h3><bean:message key="label.mission.missionConfiguration.accountabilityType.queues" bundle="MISSION_RESOURCES"/></h3>
<p>
	<html:link action="/configureMissions.do?method=prepareAddQueueForAccountabilityType">
		<bean:message key="label.mission.missionConfiguration.accountabilityType.queues.add" bundle="MISSION_RESOURCES"/>
	</html:link>
</p>
<logic:empty name="missionSystem" property="accountabilityTypeQueues">
	<p>
		<bean:message key="label.mission.missionConfiguration.accountabilityType.queues.none" bundle="MISSION_RESOURCES"/>
	</p>
</logic:empty>
<logic:notEmpty name="missionSystem" property="accountabilityTypeQueues">
	<fr:view name="missionSystem" property="accountabilityTypeQueues">
		<fr:schema type="module.mission.domain.AccountabilityTypeQueue" bundle="MISSION_RESOURCES">
			<fr:slot name="accountabilityType.name" key="label.accountability.type" bundle="ORGANIZATION_RESOURCES"/>
			<fr:slot name="workflowQueue.name" key="label.queue" bundle="MISSION_RESOURCES"/>
		</fr:schema>    				
		<fr:layout name="tabular">
			<fr:property name="classes" value="tview1"/>

			<fr:property name="linkFormat(delete)" value="/configureMissions.do?method=deleteAccountabilityTypeQueue&accountabilityTypeQueue=\${externalId}"/>
			<fr:property name="bundle(delete)" value="MISSION_RESOURCES"/>
			<fr:property name="key(delete)" value="link.delete"/>
			<fr:property name="order(delete)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<br/>

<h3><bean:message bundle="MISSION_RESOURCES" key="label.module.mission.unitsWithResumedAuthorizations"/></h3>
<logic:empty name="missionSystem" property="unitsWithResumedAuthorizations">
	<p>
		<bean:message key="label.module.mission.unitsWithResumedAuthorizations.none" bundle="MISSION_RESOURCES"/>
	</p>
</logic:empty>
<logic:notEmpty name="missionSystem" property="unitsWithResumedAuthorizations">
	<br/>
	<table>
		<tr>
			<logic:iterate id="unit" name="missionSystem" property="orderedUnitsWithResumedAuthorizations">
				<td>
					<div class="orgTBox orgTBoxLight">
						<html:link page="/missionOrganization.do?method=showUnitById" paramId="unitId" paramName="unit" paramProperty="externalId" styleClass="secondaryLink">
							<bean:write name="unit" property="partyName"/>
						</html:link>
					</div>
				</td>
			</logic:iterate>
		</tr>
	</table>
</logic:notEmpty>

<br/>

<h3><bean:message key="label.mission.missionConfiguration.users.who.can.cancelMissions" bundle="MISSION_RESOURCES"/></h3>
<p>
	<html:link action="/configureMissions.do?method=prepareAddUserWhoCanCancelMissions">
		<bean:message key="label.mission.missionConfiguration.users.who.can.cancelMissions.add" bundle="MISSION_RESOURCES"/>
	</html:link>
</p>
<logic:empty name="missionSystem" property="usersWhoCanCancelMission">
	<p>
		<bean:message key="label.mission.missionConfiguration.users.who.can.cancelMissions.none" bundle="MISSION_RESOURCES"/>
	</p>
</logic:empty>
<logic:notEmpty name="missionSystem" property="usersWhoCanCancelMission">
	<fr:view name="missionSystem" property="usersWhoCanCancelMission">
		<fr:schema type="module.organization.domain.Person" bundle="MISSION_RESOURCES">
			<fr:slot name="displayName" key="label.user" bundle="ORGANIZATION_RESOURCES"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tview1"/>

			<fr:property name="linkFormat(remove)" value="/configureMissions.do?method=removeUserWhoCanCancelMissions&userOid=\${externalId}"/>
			<fr:property name="bundle(remove)" value="MISSION_RESOURCES"/>
			<fr:property name="key(remove)" value="label.mission.missionConfiguration.users.who.can.cancelMissions.remove"/>
			<fr:property name="order(remove)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<br/>

<h3><bean:message key="label.mission.missionConfiguration.vehicle.authorizers" bundle="MISSION_RESOURCES"/></h3>
<p>
	<html:link action="/configureMissions.do?method=prepareAddVehicleAuthorizer">
		<bean:message key="label.mission.missionConfiguration.vehicle.authorizers.addUser" bundle="MISSION_RESOURCES"/>
	</html:link>
</p>
<logic:empty name="missionSystem" property="vehicleAuthorizers">
	<p>
		<bean:message key="label.mission.missionConfiguration.vehicle.authorizers.none" bundle="MISSION_RESOURCES"/>
	</p>
</logic:empty>
<logic:notEmpty name="missionSystem" property="vehicleAuthorizers">
	<fr:view name="missionSystem" property="vehicleAuthorizers">
		<fr:schema type="module.organization.domain.Person" bundle="MISSION_RESOURCES">
			<fr:slot name="displayName" key="label.user" bundle="ORGANIZATION_RESOURCES"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tview1"/>
			<fr:property name="linkFormat(remove)" value="/configureMissions.do?method=removeVehicleAuthorizer&userOid=\${externalId}"/>
			<fr:property name="bundle(remove)" value="MISSION_RESOURCES"/>
			<fr:property name="key(remove)" value="label.mission.missionConfiguration.vehicle.authorizers.remove"/>
			<fr:property name="order(remove)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h3><bean:message key="label.mission.missionConfiguration.mandatorySupplier" bundle="MISSION_RESOURCES"/></h3>

<fr:form id="missionSystemMandatorySupplierForm" action="/configureMissions.do?method=addMandatorySupplier">
	<fr:edit id="missionSystemMandatorySupplier" name="supplierBean">
		<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.SupplierBean" bundle="MISSION_RESOURCES">
			<fr:slot name="supplier" layout="autoComplete" key="label.supplier" bundle="EXPENDITURE_RESOURCES">
				<fr:property name="labelField" value="presentationName" />
				<fr:property name="format" value="\${presentationName}" />
				<fr:property name="minChars" value="1" />
				<fr:property name="args"
					value="provider=pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider.NIFSupplierAutoCompleteProvider" />
				<fr:property name="size" value="60" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form mtop05 mbottom1"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<fr:view name="missionSystem" property="mandatorySupplierSet">
	<fr:schema type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier" bundle="MISSION_RESOURCES">
		<fr:slot name="presentationName" key="label.supplier" bundle="EXPENDITURE_RESOURCES"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tview1"/>
		<fr:property name="linkFormat(remove)" value="/configureMissions.do?method=removeMandatorySupplier&supplierOid=\${externalId}"/>
		<fr:property name="bundle(remove)" value="MISSION_RESOURCES"/>
		<fr:property name="key(remove)" value="label.mission.missionConfiguration.vehicle.authorizers.remove"/>
		<fr:property name="order(remove)" value="1"/>
	</fr:layout>
</fr:view>

<fr:edit id="missionSystemMandatorySupplierErrorMessage" name="missionSystem">
	<fr:schema type="module.mission.domain.MissionSystem" bundle="MISSION_RESOURCES">
		<fr:slot name="mandatorySupplierNotUsedErrorMessageArg" key="label.mission.system.mandatory.supplier.error.message.arg" bundle="MISSION_RESOURCES"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form mtop05 mbottom1"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:edit>
