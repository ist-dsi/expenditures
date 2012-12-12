<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="authorizations.title.grant" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h2>

<div class="infobox">
	<bean:message key="label.unit" bundle="EXPENDITURE_RESOURCES"/>
	:
	<strong>
		<fr:view name="authorizationBean" property="unit.name"/>
	</strong>
</div>


<fr:edit id="bean" name="authorizationBean" action="/expenditureTrackingOrganization.do?method=createAuthorizationUnit">
	<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.AuthorizationBean" bundle="EXPENDITURE_ORGANIZATION_RESOURCES">
		<fr:slot name="person" layout="autoComplete" key="person.label.name"
				validator="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator">
        	<fr:property name="labelField" value="name"/>
			<fr:property name="format" value="${name} (${user.username})"/>
			<fr:property name="minChars" value="3"/>
			<fr:property name="args" value="provider=pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider.PersonAutoComplete" />
			<fr:property name="size" value="60"/>
		</fr:slot>
		<fr:slot name="startDate" key="authorizations.label.startDate" bundle="EXPENDITURE_RESOURCES" layout="null-as-label" required="true">
			<fr:property name="subLayout" value="default"/>
		</fr:slot>
		<fr:slot name="endDate" key="authorizations.label.endDate" bundle="EXPENDITURE_RESOURCES" layout="null-as-label">
			<fr:property name="subLayout" value="default"/>
		</fr:slot>
		<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
			<fr:slot name="canDelegate" key="authorizations.label.canDelegate" bundle="EXPENDITURE_RESOURCES"/>
		</logic:present>
		<fr:slot name="maxAmount" key="authorizations.label.maxAmount" bundle="EXPENDITURE_RESOURCES" />
		<fr:slot name="justification" key="authorizations.label.justification" bundle="EXPENDITURE_RESOURCES" layout="longText">
			<fr:property name="rows" value="3"/>
			<fr:property name="columns" value="50"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:edit>
