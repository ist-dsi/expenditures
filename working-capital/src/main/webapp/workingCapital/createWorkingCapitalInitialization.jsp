<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page"/>
</h2>

<html:messages id="message" message="true">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<fr:edit id="workingCapitalInitializationBean" name="workingCapitalInitializationBean" action="/workingCapital.do?method=createWorkingCapitalInitialization">
	<fr:schema type="module.workingCapital.domain.util.WorkingCapitalInitializationBean" bundle="WORKING_CAPITAL_RESOURCES">
		<fr:slot name="year" key="label.module.workingCapital.year" required="true"/>
		<fr:slot name="unit" layout="autoComplete" key="label.module.workingCapital.unit" validator="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator">
        	<fr:property name="labelField" value="presentationName"/>
			<fr:property name="format" value="\${presentationName}"/>
			<fr:property name="minChars" value="3"/>
			<fr:property name="args" value="provider=module.workingCapital.presentationTier.renderers.UnitAutoCompleteProvider"/>
			<fr:property name="size" value="60"/>
		</fr:slot>
		<fr:slot name="person" layout="autoComplete" key="label.module.workingCapital.movementResponsible"
				validator="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator"
				help="label.module.workingCapital.movementResponsible.help">
        	<fr:property name="labelField" value="name"/>
			<fr:property name="format" value="\${user.name} (\${user.username})"/>
			<fr:property name="minChars" value="3"/>
			<fr:property name="args" value="provider=module.organization.presentationTier.renderers.providers.PersonAutoCompleteProvider" />
			<fr:property name="size" value="60"/>
		</fr:slot>
		<fr:slot name="requestedMonthlyValue" key="label.module.workingCapital.requestedMonthlyValue.requested" required="true"/>
		<fr:slot name="fiscalId" key="label.module.workingCapital.fiscalId"
				required="true"
				help="label.module.workingCapital.fiscalId.help">
 			<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.NumberRangeValidator">
       			<fr:property name="upperBound" value="999999999"/>
       			<fr:property name="lowerBound" value="1"/>
   			</fr:validator>
		</fr:slot>
		<fr:slot name="internationalBankAccountNumber" key="label.module.workingCapital.ibanOrBan"
				help="label.module.workingCapital.internationalBankAccountNumber.help">
 			<fr:validator name="module.workingCapital.presentationTier.validator.IbanOfPTBanValidator"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form" />
		<fr:property name="columnClasses" value=",,tderror" />
		<fr:property name="requiredMarkShown" value="true"/>
	</fr:layout>
	<fr:destination name="cancel" path="/workingCapital.do?method=frontPage"/>
</fr:edit>

