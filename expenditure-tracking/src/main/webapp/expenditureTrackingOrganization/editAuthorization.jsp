<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="authorizations.title.editAuthorization" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="infobox">
	<fr:view name="authorization" schema="viewAuthorizationBeingEdited">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:view>
</div>

<bean:define id="url" type="java.lang.String">/expenditureTrackingOrganization.do?method=viewAuthorization&amp;authorizationOid=<bean:write name="authorization" property="externalId"/></bean:define>
<fr:edit name="authorization" schema="editAuthorization"
		action="<%= url %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:edit>
