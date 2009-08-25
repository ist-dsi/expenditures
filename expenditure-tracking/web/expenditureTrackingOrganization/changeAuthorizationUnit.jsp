<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="authorizations.title.grant" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h2>

<div class="infobox">
	<fr:view name="person"
			type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
			schema="viewPerson">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<bean:define id="urlExpand" type="java.lang.String">/expenditureTrackingOrganization.do?method=expandAuthorizationUnit&amp;personOid=<bean:write name="person" property="externalId"/></bean:define>
<bean:define id="urlCancel" type="java.lang.String">/expenditureTrackingOrganization.do?method=viewPerson&amp;personOid=<bean:write name="person" property="externalId"/></bean:define>
<fr:edit id="unitBean"
		name="unitBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.UnitBean"
		schema="unitBean"
		action="<%= urlExpand %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlCancel %>" />
</fr:edit>


<bean:define id="urlSelect" type="java.lang.String">/expenditureTrackingOrganization.do?method=prepareCreateAuthorizationUnit&amp;personOid=<bean:write name="person" property="externalId"/></bean:define>
<p>
	<html:link action="<%= urlExpand %>">
		<bean:message key="link.top" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
</p>


<logic:present name="unit">
	<logic:present name="unit" property="parentUnit">
		<bean:write name="unit" property="parentUnit.name"/>
		<html:link action="<%= urlExpand %>" paramId="unitOid" paramName="unit" paramProperty="parentUnit.externalId">
			<bean:message key="link.expand" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<html:link action="<%= urlSelect %>" paramId="unitOid" paramName="unit" paramProperty="parentUnit.externalId">
			<bean:message key="link.select" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<br/>	
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</logic:present>
	<bean:write name="unit" property="name"/>
	<html:link action="<%= urlExpand %>" paramId="unitOid" paramName="unit" paramProperty="externalId">
		<bean:message key="link.expand" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
	<html:link action="<%= urlSelect %>" paramId="unitOid" paramName="unit" paramProperty="externalId">
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
		<html:link action="<%= urlExpand %>" paramId="unitOid" paramName="u" paramProperty="externalId">
			<bean:message key="link.expand" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<html:link action="<%= urlSelect %>" paramId="unitOid" paramName="u" paramProperty="externalId">
			<bean:message key="link.select" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<br/>
	</logic:iterate>
</logic:present>