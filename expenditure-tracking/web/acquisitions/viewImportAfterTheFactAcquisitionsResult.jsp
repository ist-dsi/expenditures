<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="link.sideBar.importAfterTheFactAcquisitions" bundle="EXPENDITURE_RESOURCES"/></h2>

<bean:define id="afterTheFactAcquisitionsImportBean" name="afterTheFactAcquisitionsImportBean" type="pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionsImportBean"/>

<logic:equal name="afterTheFactAcquisitionsImportBean" property="errorCount" value="0">
	<div class="infoop5">
		<bean:message key="label.afterTheFactAcquisition.import.lines" bundle="ACQUISITION_RESOURCES"/>: <bean:write name="afterTheFactAcquisitionsImportBean" property="importedLines"/>
	</div>
</logic:equal>
<logic:greaterThan name="afterTheFactAcquisitionsImportBean" property="errorCount" value="0">
	<div class="error1">
		<bean:message key="label.afterTheFactAcquisition.import.not.imported" bundle="ACQUISITION_RESOURCES"/>
		<bean:message key="label.afterTheFactAcquisition.import.errors" bundle="ACQUISITION_RESOURCES" arg0="<%= Integer.toString(afterTheFactAcquisitionsImportBean.getErrorCount()) %>"/>
		<table>
			<logic:iterate id="issue" name="afterTheFactAcquisitionsImportBean" property="issues" type="pt.ist.expenditureTrackingSystem.domain.dto.Issue">
				<% if (issue.getIssueType().getIssueTypeLevel().name().equals("ERROR")) { %>
					<tr>
						<td>
							<bean:define id="key">label.afterTheFactAcquisition.import.issueType.<%= issue.getIssueType() %></bean:define>
							<%
								final String[] args = new String[5];
								args[0] = Integer.toString(issue.getLineNumber());
								for (int i = 0; i < issue.getMessageArgs().length; i++) {
								    args[i + 1] = issue.getMessageArgs()[i];
								}
								for (int i = issue.getMessageArgs().length + 1; i < 5; i++) {
								    args[i] = "";
								}
							%>
							<bean:message key="<%= key %>" bundle="ACQUISITION_RESOURCES" arg0="<%= args[0] %>" arg1="<%= args[1] %>" arg2="<%= args[2] %>" arg3="<%= args[3] %>" arg4="<%= args[4] %>"/>
						</td>
					</tr>
				<% } %>
			</logic:iterate>
		</table>
	</div>
</logic:greaterThan>
<logic:greaterThan name="afterTheFactAcquisitionsImportBean" property="warningCount" value="0">
	<div class="warning3">
		<bean:message key="label.afterTheFactAcquisition.import.warnings" bundle="ACQUISITION_RESOURCES" arg0="<%= Integer.toString(afterTheFactAcquisitionsImportBean.getWarningCount()) %>"/>
		<table>
			<logic:iterate id="issue" name="afterTheFactAcquisitionsImportBean" property="issues" type="pt.ist.expenditureTrackingSystem.domain.dto.Issue">
				<% if (issue.getIssueType().getIssueTypeLevel().name().equals("WARNING")) { %>
					<tr>
						<td>
							<bean:define id="key">label.afterTheFactAcquisition.import.issueType.<%= issue.getIssueType() %></bean:define>
							<%
								final String[] args = new String[5];
								args[0] = Integer.toString(issue.getLineNumber());
								for (int i = 1; i < issue.getMessageArgs().length; i++) {
								    args[i] = issue.getMessageArgs()[i];
								}
								for (int i = issue.getMessageArgs().length + 1; i < 5; i++) {
								    args[i] = "";
								}
							%>
							<bean:message key="<%= key %>" bundle="ACQUISITION_RESOURCES" arg0="<%= args[0] %>" arg1="<%= args[1] %>" arg2="<%= args[2] %>" arg3="<%= args[3] %>" arg4="<%= args[4] %>"/>
						</td>
					</tr>
				<% } %>
			</logic:iterate>
		</table>
	</div>
</logic:greaterThan>