<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<h2><bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.accounting.configure.title"/></h2>

<fr:edit id="workingCapitalSystem" name="workingCapitalSystem" action="/workingCapital.do?method=configuration">
	<fr:schema type="module.mission.domain.util.MissionAuthorizationAccountabilityTypeBean" bundle="ORGANIZATION_RESOURCES">
		<fr:slot name="accountingUnit" layout="autoComplete" key="label.party" bundle="ORGANIZATION_RESOURCES" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
        	<fr:property name="labelField" value="partyName.content"/>
			<fr:property name="format" value="${presentationName}"/>
			<fr:property name="minChars" value="3"/>
			<fr:property name="args" value="provider=module.organization.presentationTier.renderers.providers.UnitAutoCompleteProvider"/>
			<fr:property name="size" value="60"/>
		</fr:slot>
    	<fr:slot name="accountingAccountabilityType" layout="menu-select" key="label.accountability.type" bundle="ORGANIZATION_RESOURCES">
        	<fr:property name="providerClass" value="module.organization.presentationTier.renderers.providers.AccountabilityTypesProvider"/>
        	<fr:property name="eachSchema" value="accountabilityType-name"/>
        	<fr:property name="eachLayout" value="values"/>
        	<fr:property name="classes" value="nobullet noindent"/>
        	<fr:property name="sortBy" value="name"/>
			<fr:property name="saveOptions" value="true"/>
    	</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>