<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%
	final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();
%>

<h2><bean:message key="link.topBar.configuration" bundle="EXPENDITURE_RESOURCES"/></h2>

<form action="<%= request.getContextPath() + "/expenditureConfiguration.do" %>" method="post">
	<html:hidden property="method" value="saveConfiguration"/>

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
	<html:submit styleClass="inputbutton">
		<bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/>
	</html:submit>
</form>
