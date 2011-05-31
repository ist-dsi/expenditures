<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/chart.tld" prefix="chart" %>
<%@page import="myorg.presentationTier.component.OrganizationChart"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page import="myorg.domain.VirtualHost"%>

<%@page import="pt.ist.fenixframework.pstm.AbstractDomainObject"%>

<logic:messagesPresent property="message" message="true">
	<div class="error1">
		<html:messages id="errorMessage" property="message" message="true"> 
			<span><fr:view name="errorMessage"/></span>
		</html:messages>
	</div>
</logic:messagesPresent>

<bean:define id="systemId" name="systemId" type="java.lang.String"/>
<%
	final VirtualHost currentVirtualHost = VirtualHost.getVirtualHostForThread();
	final ExpenditureTrackingSystem currentExpenditureTrackingSystem = currentVirtualHost.getExpenditureTrackingSystem();
	final ExpenditureTrackingSystem chosenExpenditureTrackingSystem = ExpenditureTrackingSystem.fromExternalId(systemId);
%>

<h2><bean:message key="link.topBar.configuration.virtual.hosts.title" bundle="EXPENDITURE_RESOURCES"/></h2>

<table class="tstyle2">
	<tr>
		<th>
			<bean:message key="link.topBar.configuration.virtual.hosts.title" bundle="EXPENDITURE_RESOURCES"/>
		</th>
		<th>
			<bean:message key="link.topBar.configuration.virtual.hosts.system" bundle="EXPENDITURE_RESOURCES"/>
		</th>
		<th>
			<bean:message key="link.topBar.configuration.virtual.hosts.units" bundle="EXPENDITURE_RESOURCES"/>
		</th>
	</tr>
	<%
		for (final VirtualHost virtualHost : chosenExpenditureTrackingSystem.getVirtualHostSet()) {
	%>
		<tr>
			<td <% if (virtualHost == currentVirtualHost) { %>style="background-color: #99FF66;"<% } %>>	
				<%= virtualHost.getApplicationTitle() %>
				<br/>
				<%= virtualHost.getHostname() %>
			</td>
			<td <% if (virtualHost == currentVirtualHost) { %>style="background-color: #99FF66;"<% } %>>
				<%= chosenExpenditureTrackingSystem.getExternalId() %>
			</td>
			<td <% if (virtualHost == currentVirtualHost) { %>style="background-color: #99FF66;"<% } %>>
				<%
					for (final Unit unit : chosenExpenditureTrackingSystem.getTopLevelUnitsSet()) {
				%>
					    <%= unit.getPresentationName() %>
				<%
					}
				%>
			</td>
		</tr>
	<%
		}
	%>
</table>

<h2><bean:message key="label.models" bundle="ORGANIZATION_RESOURCES"/></h2>

<p><bean:message key="message.organization.choose.model.for.system" bundle="EXPENDITURE_RESOURCES"/></p>

<logic:empty name="organizationalModels">
	<bean:message key="label.models.none" bundle="ORGANIZATION_RESOURCES"/>
</logic:empty>

<logic:notEmpty name="organizationalModels">
	<chart:orgChart id="organizationalModel" name="organizationalModelChart" type="java.lang.Object">
		<div class="orgTBox orgTBoxLight">
			<html:link action="<%= "/expenditureConfiguration.do?method=createTopLevelUnits&systemId=" + systemId + "&organizationalModelOid=" + ((AbstractDomainObject) organizationalModel).getExternalId() %>">
				<bean:write name="organizationalModel" property="name.content"/>
			</html:link>
		</div>
	</chart:orgChart>
</logic:notEmpty>
