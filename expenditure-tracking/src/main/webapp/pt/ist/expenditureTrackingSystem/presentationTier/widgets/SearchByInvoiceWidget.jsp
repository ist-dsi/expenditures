<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@page import="pt.ist.expenditureTrackingSystem.presentationTier.widgets.SearchByInvoiceWidget"%>

<bean:define id="widget" name="widget" toScope="request" type="module.dashBoard.domain.DashBoardWidget"/>
<bean:define id="widgetId" name="widget" property="externalId" type="java.lang.String" />


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
					<fr:slot name="invoiceId" key="label.invoiceId.short" bundle="EXPENDITURE_RESOURCES">
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
	var resultsDiv = $("#searchByInvoiceForm > div");

	function spinner(formData, jqForm, options) {
		resultsDiv.empty();
		resultsDiv.append('<bean:message key="widget.SearchByInvoiceWidget.searching" bundle="EXPENDITURE_RESOURCES"/>...');
	}

	function decide(responseText, statusText) {
		var processes = JSON.parse(responseText);

		if (processes.length == 0) {
			resultsDiv.empty();
			resultsDiv.append('<p class="mtop0"><em><bean:message key="widget.SearchByInvoiceWidget.noProcessFound" bundle="EXPENDITURE_RESOURCES"/>.</em></p>');
		} else if (processes.length == 1) {
			window.location.replace(processes[0].url);
		} else {
			resultsDiv.empty();
			resultsDiv.append('<p><bean:message key="widget.SearchByInvoiceWidget.processesFound" bundle="EXPENDITURE_RESOURCES"/></p>');

			var table = '<table class="width100pc">';
			for (var i = 0; i < processes.length; i++) {
				table += '<tr>';
				table += '<td><a href' + '="' + processes[i].url + '">' + processes[i].id + '</a></td>'; // the weird seperation right after href is there so a checksum isn't automatically added - the correct checksum should be delivered in the response text
				table += '<td>' + processes[i].name + '</td>';
				table += '</tr>';
			}
			table += '</table>';
			resultsDiv.append(table);
		}
	}

	$("#searchByInvoiceForm").ajaxForm({beforeSubmit : spinner, success : decide});
</script>