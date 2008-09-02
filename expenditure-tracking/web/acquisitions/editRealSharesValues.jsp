<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<h2><bean:message key="acquisitionRequestItem.title.editRealValues" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="processOID" name="item" property="acquisitionRequest.acquisitionProcess.OID"/>

<bean:define id="itemOID" name="item" property="OID"/>


<logic:equal name="item" property="filledWithRealValues" value="false">
	<div class="infoop4">
			<strong><bean:message key="label.attention" bundle="EXPENDITURE_RESOURCES"/></strong>: <bean:message key="acquisitionRequestItem.message.warn.mustDefineRealValuesFirst" bundle="ACQUISITION_RESOURCES"/>
	</div>
	
	<html:link page="<%= "/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid="  + processOID %>"><bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/></html:link>
</logic:equal>
			
<logic:equal name="item" property="filledWithRealValues" value="true">
	
<jsp:include page="../commons/defaultErrorDisplay.jsp"/>
	
	<fr:edit id="beans" name="beans" schema="editSchemaRealShareValues" 
		action="<%= "/acquisitionProcess.do?method=executeDistributeRealValuesForPayingUnitsEdition&acquisitionProcessOid="  + processOID + "&acquisitionRequestItemOid=" + itemOID%>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle3"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%= "/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid="  + processOID %>"/>
	</fr:edit>
</logic:equal>