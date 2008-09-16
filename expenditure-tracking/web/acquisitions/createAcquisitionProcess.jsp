<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.createAcquisitionRequest" bundle="ACQUISITION_RESOURCES"/></h2>

<div class="infoop2">
	<bean:message key="acquisitionProcess.message.note" bundle="ACQUISITION_RESOURCES"/>
</div>


<fr:form action="/acquisitionProcess.do?method=createNewAcquisitionProcess">
	<fr:edit id="acquisitionProcessBean"
			name="acquisitionProcessBean"
			type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean"
			schema="createAcquisitionRequest">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
		<fr:destination name="cancel" path="/acquisitionProcess.do?method=showPendingProcesses"/>
	</fr:edit>
	<html:submit styleClass="inputbutton"><bean:message key="button.create" bundle="EXPENDITURE_RESOURCES"/></html:submit>
	<html:cancel styleClass="inputbutton"><bean:message key="button.cancel" bundle="EXPENDITURE_RESOURCES"/></html:cancel>
</fr:form>