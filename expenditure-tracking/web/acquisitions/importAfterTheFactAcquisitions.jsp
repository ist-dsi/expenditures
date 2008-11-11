<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="link.sideBar.importAfterTheFactAcquisitions" bundle="EXPENDITURE_RESOURCES"/></h2>

<fr:edit id="afterTheFactAcquisitionsImportBean"
		name="afterTheFactAcquisitionsImportBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionsImportBean"
		schema="afterTheFactAcquisitionsImportBean"
		action="/acquisitionAfterTheFactAcquisitionProcess.do?method=importAcquisitions">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="/acquisitionProcess.do?method=showPendingProcesses"/>
</fr:edit>
