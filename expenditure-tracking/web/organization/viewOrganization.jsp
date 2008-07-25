<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="link.view.organization" bundle="EXPENDITURE_RESOURCES"/></h2>
<br />
<logic:notPresent name="unit">
	<html:link action="/organization.do?method=createNewUnit">
		<bean:message key="link.organization.create.new.unit" bundle="ORGANIZATION_RESOURCES"/>
	</html:link>
</logic:notPresent>
<logic:present name="unit">
	<html:link action="/organization.do?method=createNewUnit" paramId="unitOid" paramName="unit" paramProperty="OID">
		<bean:message key="link.organization.create.new.unit" bundle="ORGANIZATION_RESOURCES"/>
	</html:link>
</logic:present>
