<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<h2><bean:message key="label.pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.AddPayingUnit" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="processOID" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="processClass" name="process" property="class.simpleName"/>
<bean:define id="actionMapping" value='<%= "/acquisition" + processClass%>'/>

<bean:define id="processRequest" name="process" property="request" toScope="request"/>
<bean:define id="requestClass" name="processRequest" property="class.simpleName" toScope="request"/>
<jsp:include page="<%= "view" + requestClass + ".jsp"%>" flush="true"/>


<p class="mtop15 mbottom1"><bean:message key="message.help.selectNumberOrName.costCenterProject" bundle="ACQUISITION_RESOURCES"/></p>

<fr:edit id="unitToAdd" name="domainObjectBean" schema="select.unit.from.domain.object.bean" 
action='<%= actionMapping + ".do?method=addPayingUnit&processOid=" + processOID%>'>
	<fr:destination name="cancel" path='<%= actionMapping + ".do?method=viewProcess&processOid=" + processOID %>'/>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form mtop05"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:edit>