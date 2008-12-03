<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="title.listImports" bundle="ACQUISITION_RESOURCES"/></h2>

<table class="tstyle3">
	<tr>
		<td><bean:message key="label.filename" bundle="ACQUISITION_RESOURCES"/></td>
		<td><bean:message key="label.processCount" bundle="ACQUISITION_RESOURCES"/></td>
	</tr>
<logic:iterate id="file" name="files" indexId="index">
	<tr>
		<td>
			<bean:define id="fileOID" name="file" property="OID"/>
			<html:link page="<%= "/acquisitionAfterTheFactAcquisitionProcess.do?method=downloadImportFile&fileOID=" + fileOID %>">
			<fr:view name="file" property="displayName" type="java.lang.String">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="values"/>
					</fr:layout>
				</fr:view>
				</html:link>
		</td>
		<td><fr:view name="file" property="afterTheFactAcquisitionProcessesCount"/></td>
	</tr>
</logic:iterate>

</table>