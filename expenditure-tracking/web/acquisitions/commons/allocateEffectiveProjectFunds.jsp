<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.projectFundAllocation" bundle="ACQUISITION_RESOURCES"/></h2>

<logic:present name="process" property="currentOwner">
	<bean:define id="ownerName" name="process" property="currentOwner.firstAndLastName"/>
	<div class="infoop4">
		<bean:message key="acquisitionProcess.message.info.currentOwnerIs" bundle="ACQUISITION_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>

<bean:define id="processRequest" name="process" property="request" toScope="request"/>
<bean:define id="requestClass" name="processRequest" property="class.simpleName"/>
<bean:define id="processClass" name="process" property="class.simpleName"/>
<jsp:include page='<%=  "view" + requestClass + ".jsp" %>' flush="true"/>

<p class="mtop15 mbottom025"><bean:message key="acquisitionProcess.label.projectFundAllocation.insert" bundle="ACQUISITION_RESOURCES"/>:</p>

<bean:define id="urlActivity">/acquisition<%= processClass %>.do?method=allocateProjectFundsPermanently&amp;processOid=<bean:write name="process" property="OID"/></bean:define>
<bean:define id="urlView">/acquisition<%= processClass %>.do?method=viewAcquisitionProcess&amp;processOid=<bean:write name="process" property="OID"/></bean:define>
<fr:edit action="<%= urlActivity %>" 
		id="financerFundAllocationId" 
		schema="editProjectFinancerFundAllocationId" 
		name="fundAllocationBeans" >
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle2 mtop05"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>

