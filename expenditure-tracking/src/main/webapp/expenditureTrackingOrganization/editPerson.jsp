<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="person.label.edit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h2>

<bean:define id="url">/expenditureTrackingOrganization.do?method=viewPerson&amp;personOid=<bean:write name="person" property="externalId"/></bean:define>
<fr:edit id="person"
		name="person"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
		schema="editPerson"
		action="<%= url %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:edit>
