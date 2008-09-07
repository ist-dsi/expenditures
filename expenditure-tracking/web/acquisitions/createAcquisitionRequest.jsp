<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.createRequestDocument" bundle="ACQUISITION_RESOURCES"/></h2>

<p class="mtop15"><strong><bean:message key="label.requester" bundle="EXPENDITURE_RESOURCES"/></strong></p>
<div class="infoop2" style="width: 360px">
<fr:view name="acquisitionProcess" property="acquisitionRequest.requester"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
		schema="viewAcquisitionRequester">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
</fr:view>
</div>

<p class="mtop15"><strong><bean:message key="label.supplier" bundle="EXPENDITURE_RESOURCES"/></strong></p>
<div class="infoop2" style="width: 360px">
<fr:view name="acquisitionProcess" property="acquisitionRequest.supplier"
		schema="viewAcquisitionSupplier">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
</fr:view>
<bean:define id="acquisitionProcessOID" name="acquisitionProcess" property="OID"/>
<html:link action="/acquisitionProcess.do?method=editSupplierAddress" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID"><bean:message key="acquisitionRequestDocument.link.editSupplierAddress" bundle="ACQUISITION_RESOURCES"/></html:link>
</div>

<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet">
	<bean:size id="totalItems" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet"/>
	<logic:iterate id="acquisitionRequestItem" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet" indexId="index">
		<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
		<p class="mtop15"><strong><bean:message key="acquisitionRequestItem.label.item" bundle="ACQUISITION_RESOURCES"/></strong> (<fr:view name="currentIndex"/>/<fr:view name="totalItems"/>)</p>
		
		<bean:define id="acquisitionRequestItem" name="acquisitionRequestItem" toScope="request"/>
		<jsp:include page="./acquisitionItemDisplay.jsp" flush="false"/>
	</logic:iterate>
</logic:present>

<p class="mvert15">
	<html:link action="/acquisitionProcess.do?method=createAcquisitionRequestDocument" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
		<bean:message key="acquisitionProcess.link.createRequestDocument" bundle="ACQUISITION_RESOURCES"/>
	</html:link>
</p>
	
<bean:define id="acquisitionProcessOid" name="acquisitionProcess" property="OID"/>

<fr:form action='<%= "/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid=" +  acquisitionProcessOid %>'>
	<html:submit styleClass="inputbutton">
		« <bean:message key="button.back" bundle="EXPENDITURE_RESOURCES"/>
	</html:submit>
</fr:form>