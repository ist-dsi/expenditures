<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="unit.link.edit" bundle="ORGANIZATION_RESOURCES"/></h2>

<bean:define id="unit"
		name="unit"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Unit"
		/>
<bean:define id="urlView">/expenditureTrackingOrganization.do?method=viewOrganization&amp;unitOid=<%= unit.getOID() %></bean:define>
<fr:edit id="unit"
		name="unit"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Unit"
		schema="editUnit"
		action="<%= urlView %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:edit>
