<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="title.preferences" bundle="EXPENDITURE_RESOURCES"/></h2>

<logic:present name="options" property="cascadingStyleSheet">
	<html:link action="/CSS/user/layout.css">
		<bean:write name="options" property="cascadingStyleSheet.filename"/>
	</html:link>
	&nbsp;&nbsp;
	<html:link action="/customize.do?method=deleteCss">
		<bean:message key="link.delete" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
</logic:present>

<fr:edit id="fileUploadBean"
		name="fileUploadBean"
		type="pt.ist.expenditureTrackingSystem.presentationTier.util.FileUploadBean"
		schema="interfaceOptionsCssUpload"
		action="/customize.do?method=uploadCss">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
	</fr:layout>
</fr:edit>
