<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="process" name="information" property="process"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<div class="dinline forminline">

	<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>

		<fr:edit id="activityBean" name="information">
			<fr:schema type="module.workingCapital.domain.activity.RegisterWorkingCapitalAcquisitionActivityInformation" bundle="WORKING_CAPITAL_RESOURCES">
				<fr:slot name="supplier" key="label.module.workingCapital.acquisition.supplier" layout="autoComplete"
						validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="labelField" value="presentationName" />
					<fr:property name="format" value="${presentationName}" />
					<fr:property name="minChars" value="1" />
					<fr:property name="args" value="provider=pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider.NIFSupplierAutoCompleteProvider" />
					<fr:property name="classes" value="inputsize300px" />
				</fr:slot>
				<fr:slot name="documentNumber" key="label.module.workingCapital.acquisition.documentNumber" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="description" key="label.module.workingCapital.acquisition.description" layout="longText"
						validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="acquisitionClassification" key="label.module.workingCapital.acquisition.acquisitionClassification" layout="menu-select"
						validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="module.workingCapital.presentationTier.provider.AcquisitionClassificationProvider"/>
					<fr:property name="format" value="${description}"/>
					<fr:property name="key" value="true"/>
					<fr:property name="saveOptions" value="true"/>
					<fr:property name="classes" value="nobullet"/>
				</fr:slot>
				<fr:slot name="valueWithoutVat" key="label.module.workingCapital.acquisition.valueWithoutVat" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="money" key="label.module.workingCapital.acquisition.money" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
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