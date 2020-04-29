<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="pt.ist.fenixWebFramework.renderers.utils.RenderUtils"%>

<bean:define id="information" name="information" type="module.internalrequest.domain.activity.ItemActivityInformation"/>
<bean:define id="process" name="information" property="process"/>

<logic:present name="information" property="internalRequestItem">
	<h3>
		<bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="activity.AddItemActivity"/>
	</h3>

	<fr:form action="/internalRequestProcess.do">
		<html:hidden  property="method" value="addItem"/>
		<bean:define id="processId" type="java.lang.String" name="process" property="externalId"/>
		<html:hidden property="processId" value="<%= processId %>"/>

		<fr:edit name="information">
			<fr:layout name="tabular">
				<fr:property name="classes" value="form" />
				<fr:property name="columnClasses" value=",,tderror" />
				<fr:property name="requiredMarkShown" value="true"/>
			</fr:layout>
		</fr:edit>
	
		<html:submit styleClass="inputbutton"><bean:message key="button.submit" bundle="EXPENDITURE_RESOURCES"/></html:submit>
	</fr:form>
</logic:present>
