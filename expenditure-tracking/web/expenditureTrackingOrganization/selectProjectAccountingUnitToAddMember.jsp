<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="projectAccountingUnit.title.add.member" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="infoop2">
	<fr:view name="person"
			type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
			schema="viewPerson">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<logic:empty name="accountingUnits">
	<bean:message key="accountingUnit.message.none.defined" bundle="ORGANIZATION_RESOURCES"/>
</logic:empty>
<logic:notEmpty name="accountingUnits">
	<bean:define id="selectUrl">/expenditureTrackingOrganization.do?method=addToProjectAccountingUnit&amp;personOid=<bean:write name="person" property="OID"/></bean:define>
	<fr:view name="accountingUnits" schema="accountingUnits">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop05"/>
			<fr:property name="columnClasses" value="aleft,,,,aright,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="<%= selectUrl %>"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.select"/>
			<fr:property name="param(view)" value="OID/accountingUnitOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
