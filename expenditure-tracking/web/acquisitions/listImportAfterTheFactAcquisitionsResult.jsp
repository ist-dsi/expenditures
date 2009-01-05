<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="title.listImports" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="action" value="/acquisitionAfterTheFactAcquisitionProcess.do"/>

<table class="tstyle3">
	<tr>
		<td><bean:message key="label.filename" bundle="ACQUISITION_RESOURCES"/></td>
		<td><bean:message key="label.processCount" bundle="ACQUISITION_RESOURCES"/></td>
		<td><bean:message key="afterTheFactAcquisitionProcess.label.active" bundle="ACQUISITION_RESOURCES"/></td>
		<td></td>
		
	</tr>
<logic:iterate id="file" name="files" indexId="index">
	<bean:define id="fileOID" name="file" property="OID"/>
	<tr>
		<td>
			<html:link page='<%= action + "?method=downloadImportFile&fileOID=" + fileOID %>'>
			<fr:view name="file" property="displayName" type="java.lang.String">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value=""/>
					</fr:layout>
				</fr:view>
				</html:link>
		</td>
		<td><fr:view name="file" property="afterTheFactAcquisitionProcessesCount"/></td>
		<td><fr:view name="file" property="active"/></td>
		<td>
				<logic:equal name="file" property="active" value="true">
					<html:link page='<%= action + "?method=cancelImportFile&fileOID=" + fileOID %>'>
						<bean:message key="link.cancel" bundle="EXPENDITURE_RESOURCES"/>
					 </html:link>
				 </logic:equal>
				 <logic:equal name="file" property="active" value="false">
					<html:link page='<%= action + "?method=enableImportFile&fileOID=" + fileOID %>'>
						<bean:message key="link.reenable" bundle="EXPENDITURE_RESOURCES"/>
					 </html:link>
				 </logic:equal>
		</td>
	</tr>
</logic:iterate>

</table>