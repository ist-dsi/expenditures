<%@page import="module.finance.domain.Supplier"%>
<%@page import="module.workingCapital.domain.WorkingCapitalSystem"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="module.workflow.presentationTier.WorkflowLayoutContext"%>
<bean:define id="process" name="information" property="process" toScope="request"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="adquisitionWarning">
<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.adquisition.exceptionalAdquisitionWarning"/>
</bean:define>
<bean:define id="adquisitionWarningTitle">
<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.adquisition.exceptionalAdquisitionWarning.title"/>
</bean:define>
<bean:define id="name" name="information" property="activityName" />
<bean:define id="activityInformationClass" name="information" property="activityClass.simpleName"/>

 <script src="<%= request.getContextPath() + "/javaScript/jquery.alerts.js"%>" type="text/javascript"></script> 
 <script src="<%= request.getContextPath() + "/javaScript/alertHandlers.js"%>" type="text/javascript"></script> 
<script type="text/javascript"> 
	$(function() {
		
		// selecting :first because submit is the first button on the page, and no id attribute are being assigned to the buttons
		$('.inputbutton:first').click(function(){
			if(parseInt($('#[id*=money]').val())><%= Supplier.SOFT_SUPPLIER_LIMIT.getValue().toString()%>){
				 requestConfirmation("activityBean","<%= adquisitionWarning %>",  "<%= adquisitionWarningTitle %>");
			}
			else {
				$("#activityBean").submit();
			}
			return false;
		});	
	});
</script> 

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

<fr:edit id="activityBean" name="information" action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'
schema="<%= "activityInformation."+activityInformationClass%>">
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
		<fr:property name="requiredMarkShown" value="true"/>
	</fr:layout>
	
	<fr:destination name="cancel" path='<%="/workflowProcessManagement.do?method=viewProcess&processId=" + processId%>'/>
</fr:edit>

