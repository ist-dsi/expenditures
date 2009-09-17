<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="user.label.view" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/> <bean:write name="person" property="username"/></h2>

<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL_MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACCOUNTING_MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.PROJECT_ACCOUNTING_MANAGER">
	<div class="infobox_dotted">
		<ul>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL_MANAGER">
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=attributeAuthorization" paramId="personOid" paramName="person" paramProperty="externalId">
						<bean:message key="authorizations.link.grant" bundle="EXPENDITURE_RESOURCES"/>
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
		</ul>
	</div>
</logic:present>


<div class="infobox">
	<table style="width: 100%;">
		<tr>
			<td style="vertical-align: top;">
				<fr:view name="person"
						type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
						schema="viewPerson">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
						<fr:property name="columnClasses" value=",,tderror"/>
					</fr:layout>
				</fr:view>
			</td>
			<td style="text-align: right;">
				<html:img src="https://fenix.ist.utl.pt/publico/viewHomepage.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage"
					paramId="uuid" paramName="person" paramProperty="username"
					align="middle" styleClass="float: right; border: 1px solid #aaa; padding: 3px;" />
			</td>
		</tr>
	</table>
</div>

<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
	<logic:iterate id="role" name="availableRoles">
		<p class="mvert05">
			<span>
				<fr:view name="role"/>: 
				<html:link action='<%= "/expenditureTrackingOrganization.do?method=addRole&role=" + role %>' paramId="personOid" paramName="person" paramProperty="externalId">
					<bean:message key="role.label.add" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</html:link>
				|
				<html:link action='<%= "/expenditureTrackingOrganization.do?method=removeRole&role=" + role %>' paramId="personOid" paramName="person" paramProperty="externalId">
					<bean:message key="role.label.remove" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</html:link>
			</span>
		</p>
	</logic:iterate>
</logic:present>


<h3 class="mtop2 mbottom05"><bean:message key="authorizations.label.person.responsible" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<bean:define id="authorizations" name="person" property="authorizations"/>
<logic:notEmpty name="authorizations">
	<fr:view name="authorizations"
			schema="viewAuthorizations">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="aleft,,,,aright,"/>

			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewAuthorization"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/authorizationOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="authorizations">
	<p>
		<em><bean:message key="authorizations.label.person.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>.</em>
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
