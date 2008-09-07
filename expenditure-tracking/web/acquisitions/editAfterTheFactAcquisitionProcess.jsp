<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="afterTheFactAcquisitionProcess.title.createAfterTheFactAcquisitionProcess" bundle="ACQUISITION_RESOURCES"/></h2>

<fr:edit id="afterTheFactAcquisitionProcessBean"
		name="afterTheFactAcquisitionProcessBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionProcessBean"
		schema="afterTheFactAcquisitionProcessBean"
		action="/afterTheFactAcquisitionProcess.do?method=editAfterTheFactAcquisitionProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="/acquisitionProcess.do?method=showPendingProcesses"/>
</fr:edit>
