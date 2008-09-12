<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="afterTheFactAcquisitionProcess.title.viewAfterTheFactAcquisitionProcess" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="urlCancel">/afterTheFactAcquisitionProcess.do?method=viewAfterTheFactAcquisitionProcess&amp;acquisitionAfterTheFactOid=<bean:write name="afterTheFactAcquisitionProcessBean" property="afterTheFactAcquisitionProcess.acquisitionAfterTheFact.OID"/></bean:define>
<fr:form action="/afterTheFactAcquisitionProcess.do?method=editAfterTheFactAcquisitionProcess">
	<fr:destination name="cancel" path="<%= urlCancel %>"/>
	<fr:edit id="afterTheFactAcquisitionProcessBean"
			name="afterTheFactAcquisitionProcessBean"
			type="pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionProcessBean"
			schema="afterTheFactAcquisitionProcessBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:edit>
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	<html:cancel styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:cancel>
</fr:form>