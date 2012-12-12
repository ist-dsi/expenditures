<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/chart" prefix="chart" %>

<bean:define id="accountability" name="functionDelegationBean" property="accountability" toScope="request" type="module.organization.domain.Accountability"/>

<jsp:include page="delegationForAuthorizationHeader.jsp"/>

<html:messages id="message" message="true">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<h3>
	<bean:message key="label.delegations.add" bundle="MISSION_RESOURCES"/>
</h3>

<fr:edit id="functionDelegationBean" name="functionDelegationBean" action="/missionOrganization.do?method=addDelegationsForAuthorization">
	<fr:schema type="module.mission.domain.util.FunctionDelegationBean" bundle="ORGANIZATION_RESOURCES">
		<fr:slot name="unit" layout="autoComplete" key="label.unit" bundle="ORGANIZATION_RESOURCES" validator="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator">
    	    <fr:property name="labelField" value="partyName.content"/>
			<fr:property name="format" value="${presentationName}"/>
			<fr:property name="minChars" value="3"/>		
			<fr:property name="args" value="provider=module.organization.presentationTier.renderers.providers.PartiesAutoCompleteProvider"/>
			<fr:property name="size" value="60"/>
		</fr:slot>
		<fr:slot name="person" layout="autoComplete" key="label.person" bundle="ORGANIZATION_RESOURCES" validator="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator">
    	    <fr:property name="labelField" value="partyName.content"/>
			<fr:property name="format" value="${presentationName}"/>
			<fr:property name="minChars" value="3"/>		
			<fr:property name="args" value="provider=module.organization.presentationTier.renderers.providers.PartiesAutoCompleteProvider"/>
			<fr:property name="size" value="60"/>
		</fr:slot>
		<fr:slot name="beginDate" key="label.begin" required="true"/>
		<fr:slot name="endDate" key="label.end"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
	<fr:destination name="cancel" path='<%="/missionOrganization.do?method=showDelegationsForAuthorization&authorizationId=" + accountability.getExternalId()%>' />
</fr:edit>
