<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="module.workingCapital.domain.WorkingCapitalSystem"%>

<h2>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration"/>
</h2>

<h3><bean:message key="link.topBar.configuration.all.virtual.hosts" bundle="WORKING_CAPITAL_RESOURCES"/></h3>

<%
	final WorkingCapitalSystem currentWorkingCapitalSystem = WorkingCapitalSystem.getInstanceForCurrentHost();
%>

<p>
<h3><bean:message key="link.topBar.configuration.this.virtual.host" bundle="WORKING_CAPITAL_RESOURCES"/></h3>

<logic:present name="currentWorkingCapitalSystem">
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
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.management"/>
			</th>
			<td>
				<logic:present name="currentWorkingCapitalSystem" property="managementUnit">
					<bean:write name="currentWorkingCapitalSystem" property="managementUnit.presentationName"/>
				</logic:present>
			</td>
			<td>
				<logic:present name="currentWorkingCapitalSystem" property="managingAccountabilityType">
					<fr:view name="currentWorkingCapitalSystem" property="managingAccountabilityType.name"/>
				</logic:present>
			</td>
			<td>
				<html:link action="/workingCapitalConfiguration.do?method=configureManagementUnit">
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.management.configure"/>
				</html:link>
			</td>
		</tr>
	</table>
	
	<logic:present name="currentWorkingCapitalSystem" property="managementUnit">
		<br/>
		<h3>
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.management.members"/>
		</h3>
		<fr:view name="currentWorkingCapitalSystem" property="managementMembers" schema="module.organization.domain.Accountability.with.child.info">
			<fr:schema type="module.organization.domain.Accountability" bundle="ORGANIZATION_RESOURCES">
				<fr:slot name="child.partyName" key="label.name"/>
				<fr:slot name="child.user.username" key="label.username"/>
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
		<html:link action="/workingCapitalConfiguration.do?method=prepareAddAcquisitionClassification">
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.acquisition.classifications.add"/>
		</html:link>
	</p>
	<logic:notPresent name="currentWorkingCapitalSystem" property="acquisitionClassifications">
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.acquisition.classifications.none"/>
	</logic:notPresent>
	<logic:present name="currentWorkingCapitalSystem" property="acquisitionClassifications">
		<fr:view name="currentWorkingCapitalSystem" property="acquisitionClassifications">
			<fr:schema type="module.workingCapital.domain.AcquisitionClassification" bundle="WORKING_CAPITAL_RESOURCES">
				<fr:slot name="description" key="label.module.workingCapital.configuration.acquisition.classifications.description"/>
				<fr:slot name="economicClassification" key="label.module.workingCapital.configuration.acquisition.classifications.economicClassification"/>
				<fr:slot name="pocCode" key="label.module.workingCapital.configuration.acquisition.classifications.pocCode" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 tdleft thleft"/>
				<fr:property name="link(delete)" value="/workingCapital.do?method=deleteAcquisitionClassification"/>
				<fr:property name="bundle(delete)" value="WORKING_CAPITAL_RESOURCES"/>
				<fr:property name="key(delete)" value="link.delete"/>
				<fr:property name="param(delete)" value="externalId/acquisitionClassificationOid"/>
				<fr:property name="order(delete)" value="1"/>
			</fr:layout>
		</fr:view>
		
	<h3>
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.acquisition.limit"/>
	</h3>
	
	<table><tr>
	<td>
		<logic:present name="currentWorkingCapitalSystem" property="acquisitionValueLimit">
			<fr:view name="currentWorkingCapitalSystem">
				<fr:schema bundle="WORKING_CAPITAL_RESOURCES" type="module.workingCapital.domain.WorkingCapitalSystem">
					<fr:slot name="acquisitionValueLimit" key="label.module.workingCapital.configuration.acquisition.limit.short" bundle="WORKING_CAPITAL_RESOURCES"/>
				</fr:schema>
			</fr:view>
		</logic:present>
		<logic:notPresent name="currentWorkingCapitalSystem" property="acquisitionValueLimit">
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.acquisition.no.limit"/>
		</logic:notPresent>
	</td>
	<td>-</td>
	<td>
		<html:link action="/workingCapitalConfiguration.do?method=configureAcquisitionLimit">
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.management.configure"/>
		</html:link>
	</td>
	</tr></table>
	
	</logic:present>
</logic:present>
