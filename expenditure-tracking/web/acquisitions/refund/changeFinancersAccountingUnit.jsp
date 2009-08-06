<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.ChangeFinancersAccountingUnit" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="processRequest" name="process" property="request" toScope="request"/>

<bean:define id="processClass" name="process" property="class.simpleName"/>

<bean:define id="urlActivity">/acquisition<%=  processClass %>.do?method=changeFinancersAccountingUnit&amp;processOid=<bean:write name="process" property="externalId"/></bean:define>
<bean:define id="urlView">/acquisition<%= processClass %>.do?method=viewProcess&amp;processOid=<bean:write name="process" property="externalId"/></bean:define>
<fr:edit action="<%= urlActivity %>" 
		id="financersAccountingUnits" 
		schema="changeFinancersAccountingUnit" 
		name="financersBean" >
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>
