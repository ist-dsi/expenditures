<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/chart" prefix="chart" %>

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
