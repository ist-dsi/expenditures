<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@page import="pt.ist.expenditureTrackingSystem.presentationTier.widgets.SearchByInvoiceWidget"%>

<bean:define id="widget" name="widget" toScope="request" type="module.dashBoard.domain.DashBoardWidget"/>
<bean:define id="widgetId" name="widget" property="externalId" type="java.lang.String" />


<logic:notPresent name="multipleProcessesFound" scope="request">
	<fr:form id="searchByInvoiceForm"
		action="<%="/dashBoardManagement.do?method=widgetSubmition&dashBoardWidgetId=" + widgetId %>">
		<table class="quicksearch">
			<tr>
				<td><fr:edit id="searchByInvoiceBean" name="searchBean">
					<fr:layout>
						<fr:property name="classes" value="thnowrap" />
						<fr:property name="columnClasses" value=",,dnone" />
					</fr:layout>
					<fr:schema
						type="pt.ist.expenditureTrackingSystem.domain.dto.SearchByInvoiceBean"
						bundle="EXPENDITURE_RESOURCES">
						<fr:slot name="invoiceId" key="label.invoiceId.short"
							bundle="EXPENDITURE_RESOURCES">
							<fr:property name="size" value="10" />
						</fr:slot>
					</fr:schema>
				</fr:edit></td>
				<td><html:submit>
					<bean:message key="link.view" bundle="MYORG_RESOURCES" />
				</html:submit></td>
			</tr>
		</table>
		<div></div>
	</fr:form>

	<bean:define id="theme" name="virtualHost" property="theme.name"/>

	<script type="text/javascript">
		var warningDiv =$("#searchByInvoiceForm > div");
		
		function spinner(formData, jqForm, options) {
			warningDiv.empty();
			warningDiv.append('<bean:message key="widget.SearchByInvoiceWidget.searching" bundle="EXPENDITURE_RESOURCES"/>...<img src="<%=request.getContextPath() + "/CSS/" + theme + "/images/autocomplete.gif"%>"/>');
		}
		
		function decide(responseText, statusText) {
			if (responseText == '<%=SearchByInvoiceWidget.NOT_FOUND%>') {
					warningDiv.empty();
					warningDiv.append('<p class="mtop0"><em><bean:message key="widget.SearchByInvoiceWidget.noProcessFound" bundle="EXPENDITURE_RESOURCES"/>.</em></p>');
			} else if (responseText.indexOf('<%=SearchByInvoiceWidget.SINGLE_FOUND%>') == 0) {
					window.location.replace(<%= "\"" + request.getContextPath() + "\" + responseText.substring(" + SearchByInvoiceWidget.SINGLE_FOUND.length() + ")" %>);
			} else {
					warningDiv.empty();
					warningDiv.append('<p><bean:message key="widget.SearchByInvoiceWidget.processesFound" bundle="EXPENDITURE_RESOURCES"/></p>' + responseText);
			}	
		}
		
		$("#searchByInvoiceForm").ajaxForm({beforeSubmit : spinner, success : decide});
	</script>
</logic:notPresent>

<logic:present name="multipleProcessesFound" scope="request">
	<logic:notEmpty name="multipleProcessesFound">
		<table class="width100pc">
			<logic:iterate id="process" name="multipleProcessesFound">
				<bean:define id="oid" name="process" property="externalId" type="java.lang.String"/>
				<tr>
					<td>
						<html:link page="<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + oid %>">
							<fr:view name="process" property="acquisitionProcessId" />
						</html:link>
					</td>
					<td><fr:view name="process" property="suppliersDescription" /></td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>
