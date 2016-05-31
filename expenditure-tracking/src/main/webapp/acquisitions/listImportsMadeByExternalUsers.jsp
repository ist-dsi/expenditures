<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess"%>
<%@page import="java.util.Set"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="link.sideBar.listImportsMadeByExternalUsers" bundle="EXPENDITURE_RESOURCES"/></h2>

             
<fr:edit id="afterTheFactAcquisitionsImportBean"
		name="afterTheFactAcquisitionsImportBean"
		action="/acquisitionAfterTheFactAcquisitionProcess.do?method=listImportsMadeByExternalUsers">
	<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionsImportBean"
			bundle="EXPENDITURE_RESOURCES">
		<fr:slot name="year" key="label.year" bundle="ACQUISITION_RESOURCES">
			<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
			<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.NumberValidator" />
		</fr:slot>	 
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:edit>

<% final Set<AfterTheFactAcquisitionProcess> afterTheFactAcquisitionProcesses = (Set<AfterTheFactAcquisitionProcess>) request.getAttribute("afterTheFactAcquisitionProcesses"); %>

<br/>
<br/>

<table class="tstyle2">
	<tr>
		<th>
			<bean:message key="label.process.number" bundle="EXPENDITURE_RESOURCES"/>
		</th>
		<th>
			<bean:message key="label.process.creator" bundle="EXPENDITURE_RESOURCES"/>
		</th>
		<th>
			<bean:message key="label.value" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>*
		</th>
	</tr>
	<% for (final AfterTheFactAcquisitionProcess process : afterTheFactAcquisitionProcesses) { %>
		<tr>
			<td>
				<html:link action='<%= "/workflowProcessManagement.do?method=viewProcess&amp;processId=" + process.getExternalId() %>'>
					<%= process.getProcessNumber() %>
				</html:link>
			</td>
			<td>
				<%  final User creator = process.getProcessCreator();
					if (creator != null) {
				%>
						<%= creator.getDisplayName() %>
				<%
					} else {
				%>
						--
				<%
					}
				%>
			</td>
			<td>
				<%= process.getTotalValue().toFormatString() %>
			</td>
		</tr>
	<% } %>
</table>
