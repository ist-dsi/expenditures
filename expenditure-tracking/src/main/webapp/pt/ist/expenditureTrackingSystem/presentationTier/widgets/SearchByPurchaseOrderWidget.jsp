<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@page import="pt.ist.expenditureTrackingSystem.presentationTier.widgets.SearchByPurchaseOrderWidget"%>

<bean:define id="widget" name="widget" toScope="request" type="module.dashBoard.domain.DashBoardWidget"/>
<bean:define id="widgetId" name="widget" property="externalId" type="java.lang.String" />


<logic:notPresent name="multipleProcessesFound" scope="request">
	<fr:form id="searchByPurchaseOrderForm"
		action="<%="/dashBoardManagement.do?method=widgetSubmition&dashBoardWidgetId=" + widgetId %>">
		<table class="quicksearch">
			<tr>
				<td><fr:edit id="searchByPurchaseOrderBean" name="searchBean">
					<fr:layout>
						<fr:property name="classes" value="thnowrap" />
						<fr:property name="columnClasses" value=",,dnone" />
					</fr:layout>
					<fr:schema
						type="pt.ist.expenditureTrackingSystem.domain.dto.SearchByPurchaseOrderBean"
						bundle="EXPENDITURE_RESOURCES">
						<fr:slot name="purchaseOrderId" key="label.purchaseOrderId.short"
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

	<script type="text/javascript">
		var searchByPurchaseOrderWarningDiv =$("#searchByPurchaseOrderForm > div");
		
		function searchByPurchaseOrderSpinner(formData, jqForm, options) {
			searchByPurchaseOrderWarningDiv.empty();
			searchByPurchaseOrderWarningDiv.append('<bean:message key="widget.SearchByPurchaseOrderWidget.searching" bundle="EXPENDITURE_RESOURCES"/>...');
		}
		
		function searchByPurchaseOrderDecide(responseText, statusText) {
			if (responseText == '<%=SearchByPurchaseOrderWidget.NOT_FOUND%>') {
					searchByPurchaseOrderWarningDiv.empty();
					searchByPurchaseOrderWarningDiv.append('<p class="mtop0"><em><bean:message key="widget.SearchByPurchaseOrderWidget.noProcessFound" bundle="EXPENDITURE_RESOURCES"/>.</em></p>');
			} else if (responseText.indexOf('<%=SearchByPurchaseOrderWidget.SINGLE_FOUND%>') == 0) {
					window.location.replace(<%= "\"" + request.getContextPath() + "\" + responseText.substring(" + SearchByPurchaseOrderWidget.SINGLE_FOUND.length() + ")" %>);
			} else {
					searchByPurchaseOrderWarningDiv.empty();
					searchByPurchaseOrderWarningDiv.append('<p><bean:message key="widget.SearchByPurchaseOrderWidget.processesFound" bundle="EXPENDITURE_RESOURCES"/></p>' + responseText);
			}	
		}
		
		$("#searchByPurchaseOrderForm").ajaxForm({beforeSubmit : searchByPurchaseOrderSpinner, success : searchByPurchaseOrderDecide});
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
