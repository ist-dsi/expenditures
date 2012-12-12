<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="link.sideBar.importAfterTheFactAcquisitions" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="infobox">
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
