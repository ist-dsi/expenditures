<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="myorg.domain.VirtualHost"%>
<%@page import="myorg.domain.MyOrg"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	final ExpenditureTrackingSystem expenditureTrackingSystem = virtualHost.getExpenditureTrackingSystem();
%>

<h2><bean:message key="link.topBar.configuration" bundle="EXPENDITURE_RESOURCES"/></h2>

<h3><bean:message key="link.topBar.configuration.virtual.hosts" bundle="EXPENDITURE_RESOURCES"/></h3>

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
		<th>
		</th>
	</tr>
	<%
		for (final VirtualHost someVirtualHost : MyOrg.getInstance().getVirtualHostsSet()) {
	%>
			<tr>
				<td <% if (virtualHost == someVirtualHost) { %>style="background-color: #99FF66;"<% } %>>	
					<%= someVirtualHost.getApplicationTitle() %>
					<br/>
					<%= someVirtualHost.getHostname() %>
				</td>
				<% 	final ExpenditureTrackingSystem someExpenditureTrackingSystem = someVirtualHost.getExpenditureTrackingSystem();
					if (someExpenditureTrackingSystem != null) {
				%>
						<td <% if (virtualHost == someVirtualHost) { %>style="background-color: #99FF66;"<% } %>>
							<%= someExpenditureTrackingSystem.getExternalId() %>
						</td>
						<td <% if (virtualHost == someVirtualHost) { %>style="background-color: #99FF66;"<% } %>>
							<%
								for (final Unit unit : someExpenditureTrackingSystem.getTopLevelUnitsSet()) {
							%>
								    <%= unit.getPresentationName() %>
							<%
								}
							%>
						</td>
				<%
					} else {
				%>
						<td <% if (virtualHost == someVirtualHost) { %>style="background-color: #99FF66;"<% } %>>
							--
						</td>
						<td <% if (virtualHost == someVirtualHost) { %>style="background-color: #99FF66;"<% } %>>
							--
						</td>
				<%
					}
				%>
				<td <% if (virtualHost == someVirtualHost) { %>style="background-color: #99FF66;"<% } %>>
					<%
						if (someExpenditureTrackingSystem != null && someExpenditureTrackingSystem != expenditureTrackingSystem) {
					%>
							<html:link action="<%= "/expenditureConfiguration.do?method=useSystem&amp;systemId=" + someExpenditureTrackingSystem.getExternalId() %>">
								<bean:message key="link.topBar.configuration.virtual.hosts.use.system" bundle="EXPENDITURE_RESOURCES"/>
							</html:link>
					<%
						}
						if (someVirtualHost == virtualHost) {
					%>
							<html:link action="/expenditureConfiguration.do?method=createNewSystem">
								<bean:message key="link.topBar.configuration.virtual.hosts.create.new.system" bundle="EXPENDITURE_RESOURCES"/>
							</html:link>
					<%
						}
						if (someExpenditureTrackingSystem != null && !someExpenditureTrackingSystem.hasAnyTopLevelUnits()) {
					%>
							<br/>
							<html:link action="<%= "/expenditureConfiguration.do?method=prepareCreateTopLevelUnits&amp;systemId=" + someExpenditureTrackingSystem.getExternalId() %>">
								<bean:message key="link.topBar.configuration.virtual.hosts.create.top.level.units" bundle="EXPENDITURE_RESOURCES"/>
							</html:link>
					<%
						}
					%>
				</td>
			</tr>
	<%
		}
	%>
</table>


<%
	if (expenditureTrackingSystem != null) {
%>

<form action="<%= request.getContextPath() + "/expenditureConfiguration.do" %>" method="post">
	<html:hidden property="method" value="saveConfiguration"/>

	<h4>
		<bean:message key="label.configuration.process.institutionalProcessNumberPrefix" bundle="EXPENDITURE_RESOURCES"/>
	</h4>
	<input type="text" name="institutionalProcessNumberPrefix" size="50"
		<%
			if (expenditureTrackingSystem.getInstitutionalProcessNumberPrefix() != null && !expenditureTrackingSystem.getInstitutionalProcessNumberPrefix().isEmpty()) {
		%>
				value="<%= expenditureTrackingSystem.getInstitutionalProcessNumberPrefix() %>"
		<%
			}
		%>
	/>

	<h4>
		<bean:message key="label.configuration.process.creation.interface" bundle="EXPENDITURE_RESOURCES"/>
	</h4>
	<input type="text" name="acquisitionCreationWizardJsp" size="50"
		<%
			if (expenditureTrackingSystem.getAcquisitionCreationWizardJsp() != null && !expenditureTrackingSystem.getAcquisitionCreationWizardJsp().isEmpty()) {
		%>
				value="<%= expenditureTrackingSystem.getAcquisitionCreationWizardJsp() %>"
		<%
			}
		%>
	/>

	<h4>
		<bean:message key="label.configuration.process.search.types" bundle="EXPENDITURE_RESOURCES"/>
	</h4>
	<table>
	<%
		for (final SearchProcessValues value : SearchProcessValues.values()) {
	%>
			<tr>
				<td>
					<%= value.getLocalizedName() %>
				</td>
				<td>
					<input type="checkbox" name="<%= value.name() %>"
						<% if (expenditureTrackingSystem.contains(value)) {%>
							checked="checked"
						<% } %>
					/>
				</td>
			</tr>
	<%
		}
	%>
	</table>

	<h4>
		<bean:message key="label.configuration.process.flow" bundle="EXPENDITURE_RESOURCES"/>
	</h4>
	<table>
		<tr>
			<td>
				<bean:message key="label.configuration.process.flow.start.with.invoive" bundle="EXPENDITURE_RESOURCES"/>
			</td>
			<td>
				<input type="checkbox" name="invoiveAllowedToStartAcquisitionProcess"
					<% if (expenditureTrackingSystem.getInvoiveAllowedToStartAcquisitionProcess() != null
								&& expenditureTrackingSystem.getInvoiveAllowedToStartAcquisitionProcess().booleanValue()) {%>
							checked="checked"
					<% } %>
				/>
			</td>
		</tr>
	</table>

	<html:submit styleClass="inputbutton">
		<bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/>
	</html:submit>
</form>

<% } %>
