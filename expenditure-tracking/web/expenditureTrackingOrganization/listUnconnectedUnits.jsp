<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/chart.tld" prefix="chart" %>

<h2>
	<bean:message key="label.listUnconnectedUnits" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
</h2>


<logic:iterate id="entry" name="counterMap">
	<bean:write name="entry" property="key"/>
	:
	<bean:write name="entry" property="value"/>
	<br/>
</logic:iterate>

<br/>
<br/>

<logic:iterate id="unit" name="unconnectedUnits">
	<bean:define id="urlSelectUnit">/connectUnits.do?method=showUnits</bean:define>
	<html:link action="<%= urlSelectUnit %>" paramId="unitId" paramName="unit" paramProperty="externalId">
		<bean:write name="unit" property="presentationName"/>
	</html:link>
	<br/>
</logic:iterate>
