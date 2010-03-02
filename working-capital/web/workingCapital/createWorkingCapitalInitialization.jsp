<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page"/>
</h2>

<html:messages id="message" message="true" bundle="COFFEE_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<fr:edit id="workingCapitalInitializationBean" name="workingCapitalInitializationBean" action="/workingCapital.do?method=createWorkingCapitalInitialization">
	<fr:schema type="module.workingCapital.domain.util.WorkingCapitalInitializationBean" bundle="WORKING_CAPITAL_RESOURCES">
		<fr:slot name="year" key="label.module.workingCapital.year" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
		<fr:slot name="unit" layout="autoComplete" key="label.module.workingCapital.unit" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
        	<fr:property name="labelField" value="presentationName"/>
			<fr:property name="format" value="${presentationName}"/>
			<fr:property name="minChars" value="3"/>
			<fr:property name="args" value="provider=pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider.UnitAutoCompleteProvider"/>
			<fr:property name="size" value="60"/>
		</fr:slot>
		<fr:slot name="person" layout="autoComplete" key="label.module.workingCapital.movementResponsible"
				validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"
				help="label.module.workingCapital.movementResponsible.help">
        	<fr:property name="labelField" value="name"/>
			<fr:property name="format" value="${partyName} (${user.username})"/>
			<fr:property name="minChars" value="3"/>
			<fr:property name="args" value="provider=module.organization.presentationTier.renderers.providers.PersonAutoCompleteProvider" />
			<fr:property name="size" value="60"/>
		</fr:slot>
		<fr:slot name="requestedAnualValue" key="label.module.workingCapital.requestedAnualValue" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
		<fr:slot name="fiscalId" key="label.module.workingCapital.fiscalId"
				validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"
				help="label.module.workingCapital.fiscalId.help"/>
		<fr:slot name="bankAccountId" key="label.module.workingCapital.bankAccountId"
				help="label.module.workingCapital.bankAccountId.help"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form" />
		<fr:property name="columnClasses" value=",,tderror" />
		<fr:property name="requiredMarkShown" value="true"/>
	</fr:layout>
	<fr:destination name="cancel" path="/workingCapital.do?method=frontPage"/>
</fr:edit>

