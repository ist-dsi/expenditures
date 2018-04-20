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
	WIZARD YAY
</p>

<html:messages id="message" message="true" bundle="MISSION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<% if (MissionSystem.getInstance().hasAnyMissionProcesses()) { %>
	<fr:form id="selectMissionBeanForm" action="/acquisitionSimplifiedProcedureProcess.do?method=newAcquisitionWizard">
		<fr:edit id="selectMissionBean" name="acquisitionProcessBean">
			<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean" bundle="ACQUISITION_RESOURCES">
   				<fr:slot name="isForMission" key="label.aquisition.process.create.is.for.mission" layout="radio-postback">
    				<fr:property name="classes" value="liinline"/>
   				</fr:slot>
   				<% if (!MissionSystem.getInstance().getMandatorySupplierSet().isEmpty()) { %>
   					<fr:slot name="isUnderMandatorySupplierScope" key="label.aquisition.process.create.for.mission.is.under.mandatory.supplier.scope" layout="radio-postback">
   						<fr:property name="classes" value="liinline"/>
					</fr:slot>
				<% } %>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form"/>
				<fr:property name="columnClasses" value=",,tderror"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
<% } %>

<fr:form id="createForm" action="/acquisitionSimplifiedProcedureProcess.do?method=acquisitionWizardStep2">
	<logic:equal name="acquisitionProcessBean" property="isForMission" value="true">
		<fr:edit id="acquisitionProcessBeanMissionProcessMission" name="acquisitionProcessBean">
			<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean" bundle="ACQUISITION_RESOURCES">
    			<fr:slot name="missionProcess" layout="autoComplete" key="label.mission.process" bundle="ACQUISITION_RESOURCES"
    					required="true">
        			<fr:property name="args" value="provider=module.mission.presentationTier.provider.MissionProcessProvider" />
        			<fr:property name="labelField" value="processIdentification"/>
        			<fr:property name="format" value="\${processIdentification}"/>
        			<fr:property name="classes" value="inputsize100px"/>
        			<fr:property name="minChars" value="1"/>
        			<fr:property name="sortBy" value="processIdentification"/>
					<fr:property name="saveOptions" value="true"/>
    			</fr:slot>
    		</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form"/>
				<fr:property name="columnClasses" value=",,tderror"/>
			</fr:layout>
		</fr:edit>
	</logic:equal>

	<html:submit styleClass="inputbutton">Next &gt;</html:submit>
</fr:form>
	
