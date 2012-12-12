<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="accountingUnit.title.add.member" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="infobox">
	<fr:view name="person"
			type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
			schema="viewPerson">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<logic:empty name="accountingUnits">
	<bean:message key="accountingUnit.message.none.defined" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
</logic:empty>
<logic:notEmpty name="accountingUnits">
	<bean:define id="selectUrl">/expenditureTrackingOrganization.do?method=addToAccountingUnit&amp;personOid=<bean:write name="person" property="externalId"/></bean:define>
	<fr:view name="accountingUnits" schema="accountingUnits">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop05"/>
			<fr:property name="columnClasses" value="aleft,,,,aright,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="<%= selectUrl %>"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.select"/>
			<fr:property name="param(view)" value="externalId/accountingUnitOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
