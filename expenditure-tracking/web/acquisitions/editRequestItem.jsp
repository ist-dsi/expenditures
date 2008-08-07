<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<bean:define id="processOID" name="itemBean" property="acquisitionRequest.acquisitionProcess.OID"/>
<fr:edit id="itemBean" name="itemBean" schema="createAcquisitionRequestItem" 
	action="<%= "/acquisitionProcess.do?method=executeAcquisitionRequestItemEdition&acquisitionProcessOid="  + processOID %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle3"/>
		</fr:layout>
		<fr:destination name="cancel" path="<%= "/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid="  + processOID %>"/>
</fr:edit>