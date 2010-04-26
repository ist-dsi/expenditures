<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="title.accounting.unit" bundle="EXPENDITURE_RESOURCES"/> <bean:write name="accountingUnit" property="name"/></h2>

<h3 class="mbottom05"><bean:message key="title.accounting.unit.responsibles" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<logic:empty name="accountingUnit" property="responsiblePeople">
	<p class="mtop05">
		<em><bean:message key="accountingUnit.message.responsibles.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="accountingUnit" property="responsiblePeople">
	<fr:view name="accountingUnit" property="responsiblePeople"
			schema="viewPeopleInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value=",,aleft,,,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewPerson"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/personOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h3 class="mbottom05"><bean:message key="title.accounting.unit.members" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<logic:empty name="accountingUnit" property="people">
	<p class="mtop05">
		<em><bean:message key="accountingUnit.message.members.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="accountingUnit" property="people">
	<fr:view name="accountingUnit" property="people"
			schema="viewPeopleInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value=",,aleft,,,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewPerson"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/personOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h3 class="mbottom05"><bean:message key="title.project.accounting.unit.responsibles" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<logic:empty name="accountingUnit" property="responsibleProjectAccountants">
	<p class="mtop05">
		<em><bean:message key="accountingUnit.message.responsibles.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="accountingUnit" property="responsibleProjectAccountants">
	<fr:view name="accountingUnit" property="responsibleProjectAccountants"
			schema="viewPeopleInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value=",,aleft,,,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewPerson"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/personOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h3 class="mbottom05"><bean:message key="title.project.accounting.unit.members" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<logic:empty name="accountingUnit" property="projectAccountants">
	<p class="mtop05">
		<em><bean:message key="accountingUnit.message.members.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="accountingUnit" property="projectAccountants">
	<fr:view name="accountingUnit" property="projectAccountants"
			schema="viewPeopleInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value=",,aleft,,,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewPerson"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/personOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h3 class="mbottom05"><bean:message key="title.treasury.accounting.unit.members" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<logic:empty name="accountingUnit" property="treasuryMembers">
	<p class="mtop05">
		<em><bean:message key="accountingUnit.message.members.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="accountingUnit" property="treasuryMembers">
	<fr:view name="accountingUnit" property="treasuryMembers"
			schema="viewPeopleInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value=",,aleft,,,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewPerson"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/personOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>


<h3 class="mtop15 mbottom05"><bean:message key="title.accounting.unit.units" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
	<p class="mtop05">
		<html:link action="/expenditureTrackingOrganization.do?method=prepareAddUnitToAccountingUnit" paramId="accountingUnitOid" paramName="accountingUnit" paramProperty="externalId">
			<bean:message key="unit.link.add.accounting.unit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
		</html:link>
	</p>
</logic:present>


<logic:empty name="accountingUnit" property="units">
	<p>
		<em><bean:message key="accountingUnit.message.units.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>
<logic:notEmpty name="accountingUnit" property="units">
	<fr:view name="accountingUnit" property="units"
			schema="unitList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop05"/>
			<fr:property name="columnClasses" value=",,aleft,,,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewOrganization"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/unitOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
