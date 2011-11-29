<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="process" name="information" property="process"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/jquery.ui.draggable.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/jquery.alerts.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/alertHandlers.js" %>"></script>

<div class="dinline forminline">
	
	<fr:view name="information">
		<fr:schema type="module.workingCapital.domain.activity.SubmitForValidationActivityInformation" bundle="WORKING_CAPITAL_RESOURCES">
			<fr:slot name="process.workingCapital.lastTransaction.accumulatedValue" key="label.module.workingCapital.transaction.accumulatedValue"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form listInsideClear" />
			<fr:property name="columnClasses" value="width100px" />
		</fr:layout>
	</fr:view>

	<fr:form id="submitForValidationForm" action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>

		<fr:edit id="activityBean" name="information">
			<fr:schema type="module.workingCapital.domain.activity.SubmitForValidationActivityInformation" bundle="WORKING_CAPITAL_RESOURCES">
				<fr:slot name="lastSubmission" key="label.module.workingCapital.lastSubmission"/>
				<fr:slot name="paymentRequired" key="label.module.workingCapital.paymentRequired"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form listInsideClear" />
				<fr:property name="columnClasses" value="width100px,,tderror" />
			</fr:layout>
		</fr:edit>

		<bean:define id="submitButton"><bean:message key="button.atribute" bundle="EXPENDITURE_RESOURCES"/></bean:define>
		<input type="button" id="submitButton" class="inputbutton" value="<%= submitButton %>"
				onclick="requestConfirmationSubmitForValidationActivityForm()"/>

	</fr:form>

	<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>

</div>

<script type="text/javaScript">

$("#submitButton").click(function() {
	var formToSubmit = $("#submitForValidationForm");
	if ($("#submitForValidationForm input[type=checkbox]").attr("checked") == true) {
		requestConfirmationForJQueryForm(formToSubmit,'<bean:message key="label.message.SubmitForValidationActivity.confirm" bundle="WORKING_CAPITAL_RESOURCES"/>', '<bean:message key="label.message.SubmitForValidationActivity.confirm.title" bundle="WORKING_CAPITAL_RESOURCES"/>');
	}
	else {
		formToSubmit.submit();
	}	
});

</script>