<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="title.accounting.unit.add.unit" bundle="ORGANIZATION_RESOURCES"/> <bean:write name="accountingUnit" property="name"/></h2>

<bean:define id="urlExpand" type="java.lang.String">/organization.do?method=prepareAddUnitToAccountingUnit&amp;accountingUnitOid=<bean:write name="accountingUnit" property="OID"/></bean:define>
<fr:edit id="unitBean"
		name="unitBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.UnitBean"
		schema="unitBean"
		action="<%= urlExpand %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:edit>


<bean:define id="urlSelect" type="java.lang.String">/organization.do?method=addUnitToAccountingUnit&amp;accountingUnitOid=<bean:write name="accountingUnit" property="OID"/></bean:define>
<p>
	<html:link action="<%= urlExpand %>">
		<bean:message key="link.top" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
</p>

<logic:present name="unitBean" property="unit">
	<bean:define id="unit" name="unitBean" property="unit"/>
	<logic:present name="unit" property="parentUnit">
		<bean:write name="unit" property="parentUnit.name"/>
		<html:link action="<%= urlExpand %>" paramId="unitOid" paramName="unit" paramProperty="parentUnit.OID">
			<bean:message key="link.expand" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<html:link action="<%= urlSelect %>" paramId="unitOid" paramName="unit" paramProperty="parentUnit.OID">
			<bean:message key="link.select" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<br/>	
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</logic:present>
	<bean:write name="unit" property="name"/>
	<html:link action="<%= urlExpand %>" paramId="unitOid" paramName="unit" paramProperty="OID">
		<bean:message key="link.expand" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
	<html:link action="<%= urlSelect %>" paramId="unitOid" paramName="unit" paramProperty="OID">
		<bean:message key="link.select" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
	<br/>	
</logic:present>
<logic:present name="units">
	<logic:iterate id="u" name="units">
		<logic:present name="unit">
			<logic:present name="unit" property="parentUnit">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</logic:present>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</logic:present>
		<bean:write name="u" property="name"/>
		<html:link action="<%= urlExpand %>" paramId="unitOid" paramName="u" paramProperty="OID">
			<bean:message key="link.expand" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<html:link action="<%= urlSelect %>" paramId="unitOid" paramName="u" paramProperty="OID">
			<bean:message key="link.select" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<br/>
	</logic:iterate>
</logic:present>