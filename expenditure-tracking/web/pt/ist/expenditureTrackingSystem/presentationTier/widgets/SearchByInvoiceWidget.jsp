<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="pt.ist.expenditureTrackingSystem.presentationTier.widgets.SearchByInvoiceWidget"%>
<bean:define id="widget" name="widget" toScope="request" type="module.dashBoard.domain.DashBoardWidget"/>
<bean:define id="widgetId" name="widget" property="externalId" type="java.lang.String" />
	
<fr:form id="searchByInvoiceForm" action="<%="/dashBoardManagement.do?method=widgetSubmition&dashBoardWidgetId=" + widgetId %>">
	<table class="quicksearch">
		<tr>
			<td>
			<fr:edit id="searchByInvoiceBean" name="searchBean">
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
			</fr:edit>
			</td>
			<td>
				<html:submit><bean:message key="link.view" bundle="MYORG_RESOURCES"/></html:submit>
			</td>
		</tr>
	</table>
	<div>

	</div>
</fr:form>

<bean:define id="theme" name="virtualHost" property="theme.name"/>

<script type="text/javascript">

function spinner(formData, jqForm, options) {
	var warningDiv =$("#searchByInvoiceForm > div");
	warningDiv.empty();
	warningDiv.append('<bean:message key="widget.SearchByInvoiceWidget.searching" bundle="EXPENDITURE_RESOURCES"/>...<img src="<%= request.getContextPath() + "/CSS/" + theme + "/images/autocomplete.gif"%>"/>');
}

function decide(responseText, statusText) {
	if (responseText == '<%= SearchByInvoiceWidget.NOT_FOUND %>') {
		var warningDiv =$("#searchByInvoiceForm > div");
		warningDiv.empty();
		warningDiv.append('<p class="mtop0"><em><bean:message key="widget.SearchByInvoiceWidget.noProcessFound" bundle="EXPENDITURE_RESOURCES"/>.</em></p>');
	}
	else {
		window.location.replace(<%= "\"" + request.getContextPath() + "\" + responseText" %>);
	}
}

$("#searchByInvoiceForm").ajaxForm({beforeSubmit: spinner, success: decide});
</script>
