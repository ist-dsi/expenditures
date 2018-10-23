<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page
	import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>

<br/>
<%=ExpenditureTrackingSystem.getInstance().getApprovalTextForRapidAcquisitions().getContent()%>

<bean:define id="processId" name="process" property="externalId" type="java.lang.String" />
<bean:define id="name" name="information" property="activityName" />

<div class="forminline mbottom2">
	<fr:form
		action='<%="/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name%>'>
		<fr:edit id="activityBean" name="information">
			<fr:schema
				type="pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SubmitForFundAllocationActivityInformation"
				bundle="EXPENDITURE_RESOURCES">
				<fr:slot name="acceptAndApprove" key="label.acceptAndApprove" required="true">
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form" />
				<fr:property name="columnClasses" value=",,tderror" />
			</fr:layout>
		</fr:edit>
		<html:submit styleClass="btn btn-primary" disabled="true" property="submitButton">
			<bean:message key="renderers.form.submit.name"
				bundle="RENDERER_RESOURCES" />
		</html:submit>
	</fr:form>
	<fr:form
		action='<%="/workflowProcessManagement.do?method=viewProcess&processId=" + processId%>'>
		<html:submit styleClass="btn btn-default">
			<bean:message key="renderers.form.cancel.name"
				bundle="RENDERER_RESOURCES" />
		</html:submit>
	</fr:form>
</div>


<script type="text/javascript">

$("input[type=checkbox]").click(function() {
	if($(this).prop('checked') == true) {
		$("input[name='submitButton']").prop('disabled',false);
	}else{
		$("input[name='submitButton']").prop('disabled',true);
	}
});
</script>

