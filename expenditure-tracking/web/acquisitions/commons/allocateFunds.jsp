<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.fundAllocation" bundle="ACQUISITION_RESOURCES"/></h2>

<logic:present name="process" property="currentOwner">
	<bean:define id="ownerName" name="process" property="currentOwner.firstAndLastName"/>
	<div class="infobox_warning">
		<bean:message key="acquisitionProcess.message.info.currentOwnerIs" bundle="ACQUISITION_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>

<bean:define id="processRequest" name="process" property="request" toScope="request"/>

<bean:define id="processOID" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="requestClass" name="processRequest" property="class.simpleName"/>
<bean:define id="processClass" name="process" property="class.simpleName"/>
<jsp:include page='<%=  "view" + requestClass + ".jsp" %>' flush="true"/>

<p class="mtop15 mbottom025"><bean:message key="acquisitionProcess.label.fundAllocation.insert" bundle="ACQUISITION_RESOURCES"/>:</p>

<bean:define id="urlActivity" value='<%= "/acquisition" + processClass   + ".do?method=allocateFunds&amp;processOid=" + processOID%>'/>
<bean:define id="urlView" value='<%= "/acquisition" + processClass + ".do?method=viewProcess&amp;processOid=" + processOID%>'/>

<fr:edit action="<%= urlActivity %>" 
		id="financerFundAllocationId" 
		schema="editFinancerFundAllocationId" 
		name="fundAllocationBeans" >
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle2 mtop05 mbottom15"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>
