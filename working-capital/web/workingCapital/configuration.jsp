<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration"/>
</h2>

<table class="plist mtop05">
	<tr>
		<td>
		</td>
		<th>
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.unit"/>
		</th>
		<th>
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.accountabilityType"/>
		</th>
		<td>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.accounting"/>
		</th>
		<td>
			<logic:present name="workingCapitalSystem" property="accountingUnit">
				<bean:write name="workingCapitalSystem" property="accountingUnit.presentationName"/>
			</logic:present>
		</td>
		<td>
			<logic:present name="workingCapitalSystem" property="accountingAccountabilityType">
				<fr:view name="workingCapitalSystem" property="accountingAccountabilityType.name"/>
			</logic:present>
		</td>
		<td>
			<html:link action="/workingCapital.do?method=configureAccountingUnit">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.accounting.configure"/>
			</html:link>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.management"/>
		</th>
		<td>
			<logic:present name="workingCapitalSystem" property="managementUnit">
				<bean:write name="workingCapitalSystem" property="managementUnit.presentationName"/>
			</logic:present>
		</td>
		<td>
			<logic:present name="workingCapitalSystem" property="managementAccountabilityType">
				<fr:view name="workingCapitalSystem" property="managementAccountabilityType.name"/>
			</logic:present>
		</td>
		<td>
			<html:link action="/workingCapital.do?method=configureManagementUnit">
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.managemente.configure"/>
			</html:link>
		</td>
	</tr>
</table>

<logic:present name="workingCapitalSystem" property="accountingUnit">
	<br/>
	<h3>
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.accounting.members"/>
	</h3>
	<fr:view name="workingCapitalSystem" property="accountingMembers" schema="module.organization.domain.Accountability.with.child.info">
		<fr:schema type="module.organization.domain.Accountability" bundle="ORGANIZATION_RESOURCES">
			<fr:slot name="child.partyName" key="label.name"/>
			<fr:slot name="beginDate" key="label.begin"/>
			<fr:slot name="endDate" key="label.end" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 tdleft thleft"/>
		</fr:layout>
	</fr:view>
</logic:present>

<logic:present name="workingCapitalSystem" property="managementUnit">
	<br/>
	<h3>
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.management.members"/>
	</h3>
	<fr:view name="workingCapitalSystem" property="managementeMembers" schema="module.organization.domain.Accountability.with.child.info">
		<fr:schema type="module.organization.domain.Accountability" bundle="ORGANIZATION_RESOURCES">
			<fr:slot name="child.partyName" key="label.name"/>
			<fr:slot name="beginDate" key="label.begin"/>
			<fr:slot name="endDate" key="label.end" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 tdleft thleft"/>
		</fr:layout>
	</fr:view>
</logic:present>

<br/>

<h3>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.acquisition.classifications"/>
</h3>
<p>
	<html:link action="/workingCapital.do?method=prepareAddAcquisitionClassification">
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.acquisition.classifications.add"/>
	</html:link>
</p>
<logic:notPresent name="workingCapitalSystem" property="acquisitionClassifications">
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.acquisition.classifications.none"/>
</logic:notPresent>
<logic:present name="workingCapitalSystem" property="acquisitionClassifications">
	<fr:view name="workingCapitalSystem" property="acquisitionClassifications">
		<fr:schema type="module.workingCapital.domain.AcquisitionClassification" bundle="WORKING_CAPITAL_RESOURCES">
			<fr:slot name="description" key="label.module.workingCapital.configuration.acquisition.classifications.description"/>
			<fr:slot name="economicClassification" key="label.module.workingCapital.configuration.acquisition.classifications.economicClassification"/>
			<fr:slot name="pocCode" key="label.module.workingCapital.configuration.acquisition.classifications.pocCode" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 tdleft thleft"/>
		</fr:layout>
	</fr:view>
</logic:present>

