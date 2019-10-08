<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@page import="pt.ist.expenditureTrackingSystem.presentationTier.widgets.SearchByPurchaseOrderWidget"%>

<bean:define id="widget" name="widget" toScope="request" type="module.dashBoard.domain.DashBoardWidget"/>
<bean:define id="widgetId" name="widget" property="externalId" type="java.lang.String" />


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
					<fr:slot name="purchaseOrderId" key="label.purchaseOrderId.short" bundle="EXPENDITURE_RESOURCES">
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
		var processes = JSON.parse(responseText);
		if (processes.length == 0) {
			searchByPurchaseOrderWarningDiv.empty();
			searchByPurchaseOrderWarningDiv.append('<p class="mtop0"><em><bean:message key="widget.SearchByInvoiceWidget.noProcessFound" bundle="EXPENDITURE_RESOURCES"/>.</em></p>');
		} else {
			window.location.replace(processes[0].url);
		}
	}

	$("#searchByPurchaseOrderForm").ajaxForm({beforeSubmit : searchByPurchaseOrderSpinner, success : searchByPurchaseOrderDecide});
</script>