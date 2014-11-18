<%@page import="pt.ist.fenixframework.FenixFramework"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/chart" prefix="chart" %>
<%@page import="pt.ist.bennu.core.presentationTier.component.OrganizationChart"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>

<logic:messagesPresent property="message" message="true">
	<div class="error1">
		<html:messages id="errorMessage" property="message" message="true"> 
			<span><fr:view name="errorMessage"/></span>
		</html:messages>
	</div>
</logic:messagesPresent>

<bean:define id="systemId" name="systemId" type="java.lang.String"/>
<%
	final ExpenditureTrackingSystem currentExpenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();
	final ExpenditureTrackingSystem chosenExpenditureTrackingSystem = FenixFramework.getDomainObject(systemId);
%>

<h2><bean:message key="label.models" bundle="ORGANIZATION_RESOURCES"/></h2>

<p><bean:message key="message.organization.choose.model.for.system" bundle="EXPENDITURE_RESOURCES"/></p>

<logic:empty name="organizationalModels">
	<bean:message key="label.models.none" bundle="ORGANIZATION_RESOURCES"/>
</logic:empty>

<logic:notEmpty name="organizationalModels">
	<chart:orgChart id="organizationalModel" name="organizationalModelChart" type="java.lang.Object">
		<div class="orgTBox orgTBoxLight">
			<bean:define id="orgModelOID" name="organizationalModel" property="externalId" type="java.lang.String"/>
			<html:link action="<%= "/expenditureConfiguration.do?method=createTopLevelUnits&systemId=" + systemId + "&organizationalModelOid=" + orgModelOID %>">
				<bean:write name="organizationalModel" property="name.content"/>
			</html:link>
		</div>
	</chart:orgChart>
</logic:notEmpty>
