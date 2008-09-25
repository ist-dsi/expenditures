<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="title.accounting.unit" bundle="EXPENDITURE_RESOURCES"/> <bean:write name="accountingUnit" property="name"/></h2>

<logic:empty name="accountingUnit" property="people">
	<p>
		<em><bean:message key="accountingUnit.message.members.none" bundle="ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>
<logic:notEmpty name="accountingUnit" property="people">
	<fr:view name="accountingUnit" property="people"
			schema="viewPeopleInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="aleft,,,,aright,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="/organization.do?method=viewPerson"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="OID/personOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
