<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="java.util.ResourceBundle"%>
<%@page import="org.fenixedu.bennu.core.i18n.BundleUtil"%>
<%@page import="module.mission.domain.MissionSystem"%>

<h2>Criar novo Processo de Aquisição/Reembolso</h2>

<p class="mvert05">
	STEP 2
</p>

<html:messages id="message" message="true" bundle="MISSION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<fr:form id="isForRefundForm" action="/acquisitionSimplifiedProcedureProcess.do?method=acquisitionWizardStep2">
	<fr:edit id="isForRefundBean" name="acquisitionProcessBean">
		<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean" bundle="ACQUISITION_RESOURCES">
				<fr:slot name="isForRefund" key="label.aquisition.process.create.is.for.refund" layout="radio-postback">
    				<fr:property name="classes" value="liinline"/>
   				</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<fr:form id="createForm" action="/acquisitionSimplifiedProcedureProcess.do?method=acquisitionWizardStep3">
	<fr:edit id="acquisitionProcessBeanMissionProcessType" name="acquisitionProcessBean">
		<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean" bundle="ACQUISITION_RESOURCES">

		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:edit>
	<html:submit styleClass="inputbutton">Next &gt;</html:submit>
</fr:form>
	
