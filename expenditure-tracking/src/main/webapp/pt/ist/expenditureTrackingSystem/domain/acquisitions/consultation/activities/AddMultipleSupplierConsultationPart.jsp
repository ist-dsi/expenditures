<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<bean:define id="processId" name="process" property="externalId"/>
<bean:define id="name" name="information" property="activityName"/>
<bean:define id="activityInformationClass" name="information" property="activityClass.simpleName"/>

<div class="infobox">
	<p><strong><bean:message key="acquisitionRequestItem.label.researchAndDevelopmentPurpose" bundle="ACQUISITION_RESOURCES" /></strong></p>
	<div id="researchAndDevelopmentPurposeExtraInfo" style="display: none" >
		<bean:message key="message.info.help.researchAndDevelopmentPurpose" bundle="ACQUISITION_RESOURCES" />
	</div>
	<p class="mver1"><span id="showExplanation" class="btn-link"><bean:message key="label.showExplanation" bundle="EXPENDITURE_RESOURCES"/></span></p>
	<p class="mver1"><span id="hideExplanation" style="display: none" class="btn-link"><bean:message key="label.hideExplanation" bundle="EXPENDITURE_RESOURCES"/></span></p>	
	<script type="text/javascript">
		$("#showExplanation").click(
				function() {
					$('#researchAndDevelopmentPurposeExtraInfo').slideToggle();
					$('#showExplanation').hide();
					$('#hideExplanation').show();
				}
			);
		$("#hideExplanation").click(
				function() {
					$('#researchAndDevelopmentPurposeExtraInfo').slideToggle();
					$('#hideExplanation').hide();
					$('#showExplanation').show();
				}
			);	
	</script>
</div>

<div class="forminline mbottom2">
	<fr:edit id="activityBean" name="information" schema="<%= "activityInformation."+activityInformationClass%>" action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>
	<fr:destination name="cancel" path='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'/>
	</fr:edit>


<%-- 
<fr:form id="form" action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>
	<fr:edit id="activityBean" name="information" visible="false"/>

	<fr:edit 
		id="financersAccountingUnits" 
		schema="activityInformation.ChangeFinancersAccountingUnit" 
		name="information" property="beans">
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:edit>
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
</fr:form>

<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>--%>
</div>

