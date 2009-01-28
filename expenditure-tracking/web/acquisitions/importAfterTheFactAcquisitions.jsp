<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="link.sideBar.importAfterTheFactAcquisitions" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="infoop2">
	<bean:message key="message.help.afterTheFactAcquisition.import" bundle="ACQUISITION_RESOURCES"/>
</div>

<fr:edit id="afterTheFactAcquisitionsImportBean"
		name="afterTheFactAcquisitionsImportBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionsImportBean"
		schema="afterTheFactAcquisitionsImportBean"
		action="/acquisitionAfterTheFactAcquisitionProcess.do?method=processImport">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="/wizard.do?method=afterTheFactOperationsWizard"/>
</fr:edit>
