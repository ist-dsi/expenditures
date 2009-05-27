<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="link.sideBar.afterTheFactAcquisitionProcess.create" bundle="EXPENDITURE_RESOURCES"/></h2>

<jsp:include page="../commons/defaultErrorDisplay.jsp"/>
asfasfasfasfasfasfasfas

<fr:edit id="afterTheFactAcquisitionProcessBean"
		name="afterTheFactAcquisitionProcessBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionProcessBean"
		schema="afterTheFactAcquisitionProcessBean"
		action="/acquisitionAfterTheFactAcquisitionProcess.do?method=createNewAfterTheFactAcquisitionProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="/wizard.do?method=afterTheFactOperationsWizard"/>
</fr:edit>
