<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="authorizations.title.editAuthorization" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="infoop2">
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
