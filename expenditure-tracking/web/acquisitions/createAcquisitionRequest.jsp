<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.createRequestDocument" bundle="ACQUISITION_RESOURCES"/></h2>

<p class="mtop15"><strong><bean:message key="label.requester" bundle="EXPENDITURE_RESOURCES"/></strong></p>
<bean:define id="acquisitionProcess" name="acquisitionProcess" toScope="request"/>
<jsp:include page="viewAcquisitionRequest.jsp" flush="true"/>

<p class="mtop15"><strong><bean:message key="label.supplier" bundle="EXPENDITURE_RESOURCES"/></strong></p>
<div class="infoop2" style="width: 360px">
<fr:view name="acquisitionProcess" property="acquisitionRequest.supplier"
		schema="viewAcquisitionSupplier">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
</fr:view>
<bean:define id="acquisitionProcessOID" name="acquisitionProcess" property="OID"/>
<html:link action="/acquisitionProcess.do?method=editSupplierAddress" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID"><bean:message key="supplier.link.edit" bundle="ORGANIZATION_RESOURCES"/></html:link>
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

<div class="documents">
	<p>
		<bean:message key="acquisitionProcess.label.requestDocument" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestDocument">
			<html:link action="/acquisitionProcess.do?method=downloadAcquisitionRequestDocument" paramId="acquisitionRequestDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.acquisitionRequestDocument.OID">
				<bean:write name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestDocument.filename"/>
			</html:link>	
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestDocument">
			<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
		</logic:notPresent>
	</p>
</div>

<bean:define id="url">/acquisitionProcess.do?method=createAcquisitionRequestDocument&amp;acquisitionProcessOid=<%= acquisitionProcessOID %></bean:define>
<p>
	<html:link action="<%= url %>" onclick="setTimeout('document.location.reload(true)',10500)"><bean:message key="acquisitionProcess.link.createRequestDocument" bundle="ACQUISITION_RESOURCES"/></html:link>
</p>
<bean:define id="urlAdd">/acquisitionProcess.do?method=addAcquisitionRequestDocument&amp;acquisitionProcessOid=<%= acquisitionProcessOID %></bean:define>
<bean:define id="urlView">/acquisitionProcess.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<%= acquisitionProcessOID %></bean:define>

<fr:form action="<%= urlView %>">
	<html:submit styleClass="inputbutton">
		<bean:message key="button.back" bundle="EXPENDITURE_RESOURCES"/>
	</html:submit>
</fr:form>

