<%@page import="module.internalrequest.domain.util.InternalRequestProcessCreationBean"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>

<h2><bean:message key="link.internalRequest.process.new" bundle="INTERNAL_REQUEST_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="INTERNAL_REQUEST_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<fr:form id="createForm" action="/createInternalRequestProcess.do?method=newInternalRequestCreation">

	<fr:edit name="internalRequestProcessCreationBean">
		<fr:schema type="module.internalrequest.domain.util.InternalRequestProcessCreationBean" bundle="INTERNAL_REQUEST_RESOURCES">
			<fr:slot name="requestingUnit" layout="autoComplete" key="label.internalRequest.requestingUnit" required="true">
				<fr:property name="labelField" value="name" />
				<fr:property name="minChars" value="3" />
				<fr:property name="args"
					value="provider=module.internalrequest.presentationTier.renderers.autoCompleteProvider.InternalUnitAutoCompleteProvider"/>
				<fr:property name="classes" value="" />
			</fr:slot>
			<fr:slot name="requestedUnit" layout="autoComplete" key="label.internalRequest.requestedUnit" required="true">
				<fr:property name="labelField" value="name" />
				<fr:property name="minChars" value="3" />
				<fr:property name="args"
					value="provider=module.internalrequest.presentationTier.renderers.autoCompleteProvider.InternalUnitAutoCompleteProvider"/>
				<fr:property name="classes" value="" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form" />
			<fr:property name="columnClasses" value=",,tderror" />
			<fr:property name="requiredMarkShown" value="true"/>
		</fr:layout>
	</fr:edit>

	<html:submit styleClass="inputbutton"><bean:message key="button.submit" bundle="EXPENDITURE_RESOURCES"/></html:submit>
</fr:form>
