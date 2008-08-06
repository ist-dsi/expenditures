<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:iterate id="unit" name="payingUnits">
	<bean:define id="unitOID" name="unit" property="OID"/>
	<bean:define id="processOID" name="acquisitionProcess" property="OID"/>
	<html:link page="<%= "/acquisitionProcess.do?method=removePayingUnit&unitOID=" + unitOID + "&acquisitionProcessOid=" + processOID %>">
		<fr:view name="unit" property="name"/>
	</html:link>
</logic:iterate>