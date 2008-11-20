<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.uploadFile" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="acquisitionProcess" name="acquisitionProcess" toScope="request"/>
<bean:define id="acquisitionProcessOID" name="acquisitionProcess" property="OID" toScope="request"/>

<bean:define id="acquisitionProcessClass" name="acquisitionProcess" property="class.simpleName" toScope="request"/>
<bean:define id="actionMapping" value="<%= "/acquisition" + acquisitionProcessClass%>"/>

<jsp:include page="viewAcquisitionRequest.jsp" flush="true"/>

<bean:define id="urlView"><%= actionMapping %>.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<bean:write name="acquisitionProcess" property="OID"/></bean:define>

<fr:edit name="bean" id="uploadFile" action="<%= actionMapping + ".do?method=genericUpload&acquisitionProcessOid=" + acquisitionProcessOID %>" schema="addGenericFile">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form mtop05"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>