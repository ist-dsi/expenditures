<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="user.label.view" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/> <bean:write name="person" property="username"/></h2>

<div id="xpto">
	<ul>
		<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL_MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACCOUNTING_MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.PROJECT_ACCOUNTING_MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.AQUISITIONS_UNIT_MANAGER">
			<script type="text/javascript">
				$("#xpto").attr("class","infobox_dotted");
			</script>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.AQUISITIONS_UNIT_MANAGER">
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=attributeAuthorization" paramId="personOid" paramName="person" paramProperty="externalId">
						<bean:message key="authorizations.link.grant" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
			</logic:present>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.AQUISITIONS_UNIT_MANAGER">
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=prepareAddResponsibleAccountingUnit" paramId="personOid" paramName="person" paramProperty="externalId">
						<bean:message key="accountingUnit.link.add.responsible" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
			</logic:present>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACCOUNTING_MANAGER">
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=prepareAddToAccountingUnit" paramId="personOid" paramName="person" paramProperty="externalId">
						<bean:message key="accountingUnit.link.add.member" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
			</logic:present>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.AQUISITIONS_UNIT_MANAGER">
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=prepareAddResponsibleProjectAccountingUnit" paramId="personOid" paramName="person" paramProperty="externalId">
						<bean:message key="project.accountingUnit.link.add.responsible" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
			</logic:present>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.PROJECT_ACCOUNTING_MANAGER">
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=prepareAddToProjectAccountingUnit" paramId="personOid" paramName="person" paramProperty="externalId">
						<bean:message key="project.accountingUnit.link.add.member" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
			</logic:present>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACCOUNTING_MANAGER">
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=prepareAddToTreasuryAccountingUnit" paramId="personOid" paramName="person" paramProperty="externalId">
						<bean:message key="treasury.accountingUnit.link.add.member" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
			</logic:present>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=editPerson" paramId="personOid" paramName="person" paramProperty="externalId">
						<bean:message key="user.link.editUser" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
					</html:link>
				</li>
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=deletePerson" paramId="personOid" paramName="person" paramProperty="externalId">
						<bean:message key="user.link.removeUser" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
					</html:link>
				</li>
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=managePriorityCPVs">
						<bean:message key="link.managePriorityCPVS" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
					</html:link>
				</li>
			</logic:present>
			<li>
				<html:link action="/expenditureTrackingOrganization.do?method=viewAuthorizationLogs" paramId="personOid" paramName="person" paramProperty="externalId">
					<bean:message key="authorizations.link.logs" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
		</logic:present>
		<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL_MANAGER">
			<script type="text/javascript">
				$("#xpto").attr("class","infobox_dotted");
			</script>
			<li>
				<html:link action="/expenditureTrackingOrganization.do?method=viewAcquisitionProcessStatistics" paramId="userOid" paramName="person" paramProperty="user.externalId">
					<bean:message key="user.link.view.acquisition.process.statistics" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
		</logic:present>
		<logic:notPresent role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL_MANAGER">
			<bean:define id="username" name="person" property="username" type="java.lang.String"/>
			<logic:present user="<%= username %>">
					<script type="text/javascript">
						$("#xpto").attr("class","infobox_dotted");
					</script>
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=viewAcquisitionProcessStatistics" paramId="userOid" paramName="person" paramProperty="user.externalId">
						<bean:message key="user.link.view.acquisition.process.statistics" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
			</logic:present>
		</logic:notPresent>
	</ul>
</div>


<div class="infobox">
	<table style="width: 100%;">
		<tr>
			<td>
				<bean:message bundle="EXPENDITURE_ORGANIZATION_RESOURCES" key="person.label.name"/>: 
			</td>
			<td>
				<bean:write name="person" property="name"/>
			</td>
			<td style="text-align: right;" rowspan="3">
				<html:img src="https://fenix.ist.utl.pt/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage"
					paramId="uuid" paramName="person" paramProperty="username"
					align="middle" styleClass="float: right; border: 1px solid #aaa; padding: 3px;" />
			</td>
		</tr>
		<tr>
			<td>
				<logic:present role="myorg.domain.RoleType.MANAGER">
					<bean:message bundle="EXPENDITURE_ORGANIZATION_RESOURCES" key="label.email"/>:
				</logic:present>
			</td>
			<td>
				<logic:present role="myorg.domain.RoleType.MANAGER">
					<bean:write name="person" property="email"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="EXPENDITURE_ORGANIZATION_RESOURCES" key="role.label.roles"/>: 
			</td>
			<td>
				<logic:iterate id="persistentGroup" name="person" property="expenditurePersistentGroups" length="1">
					<bean:write name="persistentGroup" property="name"/>
				</logic:iterate>
				<logic:iterate id="persistentGroup" name="person" property="expenditurePersistentGroups" offset="1">
					<br/>
					<bean:write name="persistentGroup" property="name"/>
				</logic:iterate>
			</td>
		</tr>
	</table>
</div>

<logic:present role="myorg.domain.RoleType.MANAGER">
	<p class="mvert05">
		<span>
			<bean:message bundle="EXPENDITURE_ENUMERATION_RESOURCES" key="RoleType.ACQUISITION_CENTRAL"/>
			<html:link action="/expenditureTrackingOrganization.do?method=addRoleAcquisitionCentralGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.add" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
			|
			<html:link action="/expenditureTrackingOrganization.do?method=removeRoleAcquisitionCentralGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.remove" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</span>
	</p>
	<p class="mvert05">
		<span>
			<bean:message bundle="EXPENDITURE_ENUMERATION_RESOURCES" key="RoleType.ACQUISITION_CENTRAL_MANAGER"/>
			<html:link action="/expenditureTrackingOrganization.do?method=addRoleAcquisitionCentralManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.add" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
			|
			<html:link action="/expenditureTrackingOrganization.do?method=removeRoleAcquisitionCentralManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.remove" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</span>
	</p>
	<p class="mvert05">
		<span>
			<bean:message bundle="EXPENDITURE_ENUMERATION_RESOURCES" key="RoleType.ACCOUNTING_MANAGER"/>
			<html:link action="/expenditureTrackingOrganization.do?method=addRoleAccountingManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.add" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
			|
			<html:link action="/expenditureTrackingOrganization.do?method=removeRoleAccountingManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.remove" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</span>
	</p>
	<p class="mvert05">
		<span>
			<bean:message bundle="EXPENDITURE_ENUMERATION_RESOURCES" key="RoleType.PROJECT_ACCOUNTING_MANAGER"/>
			<html:link action="/expenditureTrackingOrganization.do?method=addRoleProjectAccountingManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.add" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
			|
			<html:link action="/expenditureTrackingOrganization.do?method=removeRoleProjectAccountingManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.remove" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</span>
	</p>
	<p class="mvert05">
		<span>
			<bean:message bundle="EXPENDITURE_ENUMERATION_RESOURCES" key="RoleType.TREASURY_MANAGER"/>
			<html:link action="/expenditureTrackingOrganization.do?method=addRoleTreasuryMemberGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.add" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
			|
			<html:link action="/expenditureTrackingOrganization.do?method=removeRoleTreasuryMemberGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.remove" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</span>
	</p>
	<p class="mvert05">
		<span>
			<bean:message bundle="EXPENDITURE_ENUMERATION_RESOURCES" key="RoleType.SUPPLIER_MANAGER"/>
			<html:link action="/expenditureTrackingOrganization.do?method=addRoleSupplierManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.add" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
			|
			<html:link action="/expenditureTrackingOrganization.do?method=removeRoleSupplierManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.remove" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</span>
	</p>
	<p class="mvert05">
		<span>
			<bean:message bundle="EXPENDITURE_ENUMERATION_RESOURCES" key="RoleType.SUPPLIER_FUND_ALLOCATION_MANAGER"/>
			<html:link action="/expenditureTrackingOrganization.do?method=addRoleSupplierFundAllocationManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.add" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
			|
			<html:link action="/expenditureTrackingOrganization.do?method=removeRoleSupplierFundAllocationManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.remove" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</span>
	</p>
	<p class="mvert05">
		<span>
			<bean:message bundle="EXPENDITURE_ENUMERATION_RESOURCES" key="RoleType.STATISTICS_VIEWER"/>
			<html:link action="/expenditureTrackingOrganization.do?method=addRoleStatisticsViewerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.add" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
			|
			<html:link action="/expenditureTrackingOrganization.do?method=removeRoleStatisticsViewerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.remove" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</span>
	</p>
	<p class="mvert05">
		<span>
			<bean:message bundle="EXPENDITURE_ENUMERATION_RESOURCES" key="RoleType.AQUISITIONS_UNIT_MANAGER"/>
			<html:link action="/expenditureTrackingOrganization.do?method=addRoleAcquisitionsUnitManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.add" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
			|
			<html:link action="/expenditureTrackingOrganization.do?method=removeRoleAcquisitionsUnitManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.remove" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</span>
	</p>
	<p class="mvert05">
		<span>
			<bean:message bundle="EXPENDITURE_ENUMERATION_RESOURCES" key="RoleType.ACQUISITION_PROCESS_AUDITOR"/>
			<html:link action="/expenditureTrackingOrganization.do?method=addRoleAcquisitionsProcessAuditorGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.add" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
			|
			<html:link action="/expenditureTrackingOrganization.do?method=removeRoleAcquisitionsProcessAuditorGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.remove" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</span>
	</p>
	<p class="mvert05">
		<span>
			<bean:message bundle="EXPENDITURE_ENUMERATION_RESOURCES" key="RoleType.FUND_COMMITMENT_MANAGER"/>
			<html:link action="/expenditureTrackingOrganization.do?method=addRoleFundCommitmentManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.add" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
			|
			<html:link action="/expenditureTrackingOrganization.do?method=removeFundCommitmentManagerGroup" paramId="personOid" paramName="person" paramProperty="externalId">
				<bean:message key="role.label.remove" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</span>
	</p>
</logic:present>


<h3 class="mtop2 mbottom05"><bean:message key="authorizations.label.person.responsible" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<bean:define id="authorizations" name="person" property="authorizations"/>
<logic:notEmpty name="authorizations">
	<fr:view name="authorizations"
			schema="viewAuthorizations">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="aleft,,,,aright nowrap,"/>
			<fr:property name="linkGroupSeparator" value=" | "/> 

			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewAuthorization"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/authorizationOid"/>
			<fr:property name="order(view)" value="1"/>
			
			<fr:property name="counter(observers)" value="(${unit.observersCount})"/>
			<fr:property name="link(observers)" value="/expenditureTrackingOrganization.do?method=manageObservers"/>
			<fr:property name="bundle(observers)" value="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			<fr:property name="key(observers)" value="label.observers"/>
			<fr:property name="param(observers)" value="unit.externalId/unitOid"/>
			<fr:property name="order(observers)" value="2"/>
			<fr:property name="visibleIf(observers)" value="validAndIsCurrentUserResponsible"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="authorizations">
	<p>
		<em><bean:message key="authorizations.label.person.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>.</em>
	</p>
</logic:empty>

<h3 class="mtop2 mbottom05"><bean:message key="observableUnits.for.person" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>

<logic:notEmpty name="person" property="observableUnits">
	<ul>
	<logic:iterate name="person" property="observableUnits" id="unit">
		<bean:define id="unitOID" name="unit" property="externalId" type="java.lang.String"/>
		<li>
		<html:link page="<%= "/expenditureTrackingOrganization.do?method=viewOrganization&unitOid=" + unitOID%>">
			<fr:view name="unit" property="presentationName"/>
		</html:link>
		</li>
	</logic:iterate>
	</ul>
</logic:notEmpty>


<logic:empty name="person" property="observableUnits">

</logic:empty>

<h3 class="mtop2 mbottom05"><bean:message key="accountingUnit.list.for.person.responsible" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<bean:define id="removeUrl">/expenditureTrackingOrganization.do?method=removeResponsibleFromAccountingUnit&amp;personOid=<bean:write name="person" property="externalId"/></bean:define>
<logic:notEmpty name="person" property="responsibleAccountingUnits">
	<fr:view name="person" property="responsibleAccountingUnits"
			schema="accountingUnits">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="aleft,,,,aright,"/>

			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewAccountingUnit"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/accountingUnitOid"/>
			<fr:property name="order(view)" value="1"/>

			<fr:property name="link(remove)" value="<%= removeUrl %>"/>
			<fr:property name="bundle(remove)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(remove)" value="link.remove"/>
			<fr:property name="param(remove)" value="externalId/accountingUnitOid"/>
			<fr:property name="order(remove)" value="2"/>
			<fr:property name="confirmationBundle(remove)" value="EXPENDITURE_RESOURCES"/> 
            <fr:property name="confirmationKey(remove)" value="label.removeAuthorization"/> 	
            <fr:property name="confirmationTitleKey(remove)" value="title.removeAuthorization"/> 	
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="person" property="responsibleAccountingUnits">
	<p>
		<em><bean:message key="accountingUnit.message.person.not.responsible" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>.</em>
	</p>
</logic:empty>

<h3 class="mtop2 mbottom05"><bean:message key="accountingUnit.list.for.person" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<bean:define id="removeUrl">/expenditureTrackingOrganization.do?method=removePersonFromAccountingUnit&amp;personOid=<bean:write name="person" property="externalId"/></bean:define>
<logic:notEmpty name="person" property="accountingUnits">
	<fr:view name="person" property="accountingUnits"
			schema="accountingUnits">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="aleft,,,,aright,"/>

			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewAccountingUnit"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/accountingUnitOid"/>
			<fr:property name="order(view)" value="1"/>

			<fr:property name="link(remove)" value="<%= removeUrl %>"/>
			<fr:property name="bundle(remove)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(remove)" value="link.remove"/>
			<fr:property name="param(remove)" value="externalId/accountingUnitOid"/>
			<fr:property name="order(remove)" value="2"/>
			<fr:property name="confirmationBundle(remove)" value="EXPENDITURE_RESOURCES"/> 
            <fr:property name="confirmationKey(remove)" value="label.removeAuthorization"/> 	
            <fr:property name="confirmationTitleKey(remove)" value="title.removeAuthorization"/> 	
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="person" property="accountingUnits">
	<p>
		<em><bean:message key="accountingUnit.message.person.not.associated" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>.</em>
	</p>
</logic:empty>

<h3 class="mtop2 mbottom05"><bean:message key="project.accountingUnit.list.for.person.responsible" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<bean:define id="removeUrl">/expenditureTrackingOrganization.do?method=removePersonResponsibleForProjectAccountingUnit&amp;personOid=<bean:write name="person" property="externalId"/></bean:define>
<logic:notEmpty name="person" property="responsibleProjectAccountingUnits">
	<fr:view name="person" property="responsibleProjectAccountingUnits"
			schema="accountingUnits">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="aleft,,,,aright,"/>

			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewAccountingUnit"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/accountingUnitOid"/>
			<fr:property name="order(view)" value="1"/>

			<fr:property name="link(remove)" value="<%= removeUrl %>"/>
			<fr:property name="bundle(remove)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(remove)" value="link.remove"/>
			<fr:property name="param(remove)" value="externalId/accountingUnitOid"/>
			<fr:property name="order(remove)" value="2"/>
			<fr:property name="confirmationBundle(remove)" value="EXPENDITURE_RESOURCES"/> 
            <fr:property name="confirmationKey(remove)" value="label.removeAuthorization"/> 	
            <fr:property name="confirmationTitleKey(remove)" value="title.removeAuthorization"/> 
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="person" property="responsibleProjectAccountingUnits">
	<p>
		<em><bean:message key="accountingUnit.message.person.not.responsible" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>.</em>
	</p>
</logic:empty>


<h3 class="mtop2 mbottom05"><bean:message key="project.accountingUnit.list.for.person" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<bean:define id="removeUrl">/expenditureTrackingOrganization.do?method=removePersonFromProjectAccountingUnit&amp;personOid=<bean:write name="person" property="externalId"/></bean:define>
<logic:notEmpty name="person" property="projectAccountingUnits">
	<fr:view name="person" property="projectAccountingUnits"
			schema="accountingUnits">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="aleft,,,,aright,"/>

			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewAccountingUnit"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/accountingUnitOid"/>
			<fr:property name="order(view)" value="1"/>

			<fr:property name="link(remove)" value="<%= removeUrl %>"/>
			<fr:property name="bundle(remove)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(remove)" value="link.remove"/>
			<fr:property name="param(remove)" value="externalId/accountingUnitOid"/>
			<fr:property name="order(remove)" value="2"/>
			<fr:property name="confirmationBundle(remove)" value="EXPENDITURE_RESOURCES"/> 
            <fr:property name="confirmationKey(remove)" value="label.removeAuthorization"/> 	
            <fr:property name="confirmationTitleKey(remove)" value="title.removeAuthorization"/> 
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="person" property="projectAccountingUnits">
	<p>
		<em><bean:message key="accountingUnit.message.person.not.associated" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>.</em>
	</p>
</logic:empty>

<h3 class="mtop2 mbottom05"><bean:message key="treasury.accountingUnit.list.for.person" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<bean:define id="removeUrl">/expenditureTrackingOrganization.do?method=removePersonFromTreasuryAccountingUnit&amp;personOid=<bean:write name="person" property="externalId"/></bean:define>
<logic:notEmpty name="person" property="treasuryAccountingUnits">
	<fr:view name="person" property="treasuryAccountingUnits"
			schema="accountingUnits">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="aleft,,,,aright,"/>

			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewAccountingUnit"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/accountingUnitOid"/>
			<fr:property name="order(view)" value="1"/>

			<fr:property name="link(remove)" value="<%= removeUrl %>"/>
			<fr:property name="bundle(remove)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(remove)" value="link.remove"/>
			<fr:property name="param(remove)" value="externalId/accountingUnitOid"/>
			<fr:property name="order(remove)" value="2"/>
			<fr:property name="confirmationBundle(remove)" value="EXPENDITURE_RESOURCES"/> 
            <fr:property name="confirmationKey(remove)" value="label.removeAuthorization"/> 	
            <fr:property name="confirmationTitleKey(remove)" value="title.removeAuthorization"/> 
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="person" property="treasuryAccountingUnits">
	<p>
		<em><bean:message key="accountingUnit.message.person.not.associated" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>.</em>
	</p>
</logic:empty>
