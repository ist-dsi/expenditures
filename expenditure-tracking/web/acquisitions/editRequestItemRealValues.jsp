<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.edit.acquisition.request.real.values" bundle="ACQUISITION_RESOURCES"/></h2>


<bean:define id="processOID" name="itemBean" property="acquisitionRequest.acquisitionProcess.OID"/>
<bean:define id="itemOID" name="itemBean" property="item.OID"/>
<fr:edit id="acquisitionRequestItem" name="itemBean" schema="editSchemaRealValues" 
	action="<%= "/acquisitionProcess.do?method=executeAcquisitionRequestItemRealValuesEdition&acquisitionProcessOid="  + processOID %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle3"/>
		</fr:layout>
		<fr:destination name="cancel" path="<%= "/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid="  + processOID %>"/>
</fr:edit>