<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.create.acquisition.request" bundle="EXPENDITURE_RESOURCES"/></h2>


<fr:edit id="acquisitionProcessBean"
		name="acquisitionProcessBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean"
		schema="createAcquisitionRequest"
		action="/acquisitionProcess.do?method=createNewAcquisitionProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
	</fr:layout>
	<fr:destination name="cancel" path="/acquisitionProcess.do?method=showPendingProcesses"/>
</fr:edit>
