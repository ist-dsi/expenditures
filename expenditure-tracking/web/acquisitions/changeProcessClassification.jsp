<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.ChangeProcessClassification" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="processRequest" name="acquisitionProcess" property="request" toScope="request"/>
<jsp:include page="commons/viewAcquisitionRequest.jsp" flush="true"/>

<bean:define id="urlActivity">/acquisitionSimplifiedProcedureProcess.do?method=setClassification&amp;acquisitionProcessOid=<bean:write name="acquisitionProcess" property="externalId"/></bean:define>
<bean:define id="urlView">/acquisitionSimplifiedProcedureProcess.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<bean:write name="acquisitionProcess" property="externalId"/></bean:define>

<fr:edit name="bean" id="bean" schema="changeProcessClassification" action="<%= urlActivity %>">
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>
