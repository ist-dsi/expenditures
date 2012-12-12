<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present name="process" property="currentOwner">
	<bean:define id="ownerName" name="process" property="currentOwner.expenditurePerson.firstAndLastName"/>
	<div class="infobox_warning">
		<bean:message key="acquisitionProcess.message.info.currentOwnerIs" bundle="ACQUISITION_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>

<bean:define id="processId" name="process" property="externalId"/>
<bean:define id="name" name="information" property="activityName"/>

<bean:define id="urlActivity" value='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'/>
<bean:define id="urlView" value='<%= "/workflowProcessManagement.do?method=viewProcess&amp;processId=" + processId%>'/>

<div class="forminline mbottom2">

<fr:form id="allocationForm" action="<%= urlActivity %>">

<fr:edit id="activityBeanPaymentReference" name="information" schema="activityInformation.PayAcquisition"
	type="pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.PayAcquisitionActivityInformation">
</fr:edit>

	<fr:edit  
		id="activityBean"
		name="information" visible="false"/>
	<html:hidden property="financerOID" value=""/>
	<html:hidden property="index" value=""/>


<%--
<p class="mtop15 mbottom0"><bean:message key="acquisitionProcess.label.diaryNumbers" bundle="ACQUISITION_RESOURCES"/></p>

<table class="tstyle2">
<logic:iterate id="financerBean" name="information" property="beans" indexId="index">
	<tr>
		<bean:define id="usedClass" value="" toScope="request"/>
		<th class="<%= usedClass %>">			
			<h4 class="dinline"><fr:view name="financerBean" property="financer.unit.presentationName"/></h4>
			<span style="padding-left: 1em;">(<bean:message key="financer.label.diaryNumber" bundle="ACQUISITION_RESOURCES"/>: <fr:view name="financerBean" property="diaryNumber" type="java.lang.String"/>)</span>
		</th>
	</tr>
	<tr>
		<td class="aleft">
			<fr:edit id="<%= "id" + index %>" name="financerBean" slot="diaryNumber" type="java.lang.String"/>
			<bean:define id="financerOID" name="financerBean" property="financer.externalId" type="java.lang.String"/>
				<a href="javascript:addFundAllocation('<%= financerOID %>','<%= index %>', 'allocationForm');">
					<bean:message key="financer.link.addEffectiveAllocationId" bundle="ACQUISITION_RESOURCES"/>
				</a>
		</td>
	</tr>
</logic:iterate>
</table>

 --%>
 
<html:submit styleClass="inputbutton"><bean:message key="button.submit" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
</fr:form>

<fr:form id="allocationForm" action="<%= urlView %>">
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
</fr:form>

<a id="addFundAllocationLink" style="display: none;" href="<%= request.getContextPath() + "/expenditureProcesses.do?method=addDiaryNumber&processId=" + processId + "#allocationForm" %>"></a>
<a id="removeFundAllocationLink" style="display: none;" href="<%= request.getContextPath() + "/expenditureProcesses.do?method=removeDiaryNumber&processId=" + processId + "#allocationForm" %>"></a>

<script type="text/javascript">

	function addFundAllocation(financerOid,index,formId) {
		var form = $("#" + formId);
		form.children("input[name=financerOID]").attr('value',financerOid);
		form.children("input[name=index]").attr('value',index);
		form.attr('action',$("#addFundAllocationLink").attr('href'));
		form.submit();
	}

	function removeFundAllocation(financerOid,index,formId) {
		var form = $("#" + formId);
		form.children("input[name=financerOID]").attr('value',financerOid);
		form.children("input[name=index]").attr('value',index);
		form.attr('action',$("#removeFundAllocationLink").attr('href'));
		form.submit();
	}

</script>
</div>
