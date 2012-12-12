<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="link.sideBar.afterTheFactAcquisitionProcess.create" bundle="EXPENDITURE_RESOURCES"/></h2>

<fr:edit id="afterTheFactAcquisitionProcessBean"
		name="afterTheFactAcquisitionProcessBean"
		schema="activityInformation.EditAfterTheFactAcquisition"
		action="/acquisitionAfterTheFactAcquisitionProcess.do?method=createNewAfterTheFactAcquisitionProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="/wizard.do?method=afterTheFactOperationsWizard"/>
</fr:edit>
