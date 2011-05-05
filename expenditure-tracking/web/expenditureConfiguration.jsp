<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="link.topBar.configuration" bundle="EXPENDITURE_RESOURCES"/></h2>

<h4>
	<bean:message key="label.configuration.process.search.types" bundle="EXPENDITURE_RESOURCES"/>
</h4>

<form action="<%= request.getContextPath() + "/expenditureConfiguration.do" %>" method="post">
	<html:hidden property="method" value="saveSelectedSearchProcessValues"/>

	<table>
	<%
		final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();
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
