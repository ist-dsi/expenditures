<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="process" name="information" property="process"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<div class="dinline forminline">

	<h3>
		<bean:message key="label.module.workingCapital.acquisition.details" bundle="WORKING_CAPITAL_RESOURCES"/>
	</h3>

	<div class="infobox mtop1 mbottom1">
	<fr:view name="information" property="workingCapitalAcquisitionTransaction.workingCapitalAcquisition">
		<fr:schema type="module.workingCapital.domain.WorkingCapitalAcquisition" bundle="WORKING_CAPITAL_RESOURCES">
			<fr:slot name="supplier.presentationName" key="label.module.workingCapital.acquisition.supplier"/>
			<fr:slot name="documentNumber" key="label.module.workingCapital.acquisition.documentNumber"/>
			<fr:slot name="description" key="label.module.workingCapital.acquisition.description"/>
			<fr:slot name="acquisitionClassification.description" key="label.module.workingCapital.acquisition.acquisitionClassification"/>
			<fr:slot name="acquisitionClassification.economicClassification" key="label.module.workingCapital.configuration.acquisition.classifications.economicClassification"/>
			<fr:slot name="acquisitionClassification.pocCode" key="label.module.workingCapital.configuration.acquisition.classifications.pocCode"/>
			<fr:slot name="valueWithoutVat" key="label.module.workingCapital.acquisition.valueWithoutVat"/>
			<fr:slot name="workingCapitalAcquisitionTransaction.value" key="label.module.workingCapital.acquisition.money"/>
		</fr:schema>
	</fr:view>
	</div>
	
	<p>
	
	<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>

		<h4>
			<bean:message key="activity.CorrectWorkingCapitalAcquisitionClassificationActivity" bundle="WORKING_CAPITAL_RESOURCES"/>
		</h4>
		
		<fr:edit id="activityBean" name="information">
			<fr:schema type="module.workingCapital.domain.activity.EditWorkingCapitalActivityInformation" bundle="WORKING_CAPITAL_RESOURCES">
				<fr:slot name="acquisitionClassification" key="label.module.workingCapital.acquisition.acquisitionClassification" layout="menu-select">
					<fr:property name="providerClass" value="module.workingCapital.presentationTier.provider.AcquisitionClassificationProvider"/>
					<fr:property name="nullOptionHidden" value="true"/>
					<fr:property name="format" value="\${description}"/>
					<fr:property name="key" value="true"/>
					<fr:property name="saveOptions" value="true"/>
					<fr:property name="classes" value="nobullet"/>
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form listInsideClear" />
				<fr:property name="columnClasses" value="width100px,,tderror" />
			</fr:layout>
		</fr:edit>

		<html:submit styleClass="inputbutton"><bean:message key="button.atribute" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
	
	</fr:form>

	<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>

</div>
