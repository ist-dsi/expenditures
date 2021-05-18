<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>

<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="processClass" name="process" property="class.simpleName"/>
<bean:define id="outOfLabel">
	<bean:message key="acquisitionRequestItem.label.outOf" bundle="ACQUISITION_RESOURCES"/>
</bean:define>
<bean:define id="name" name="information" property="activityName"/>
<bean:define id="requestItemId" name="information" property="item.externalId"/>

<h3 class="mbottom05"><bean:message key="acquisitionProcess.label.assignPayingUnitToItem" bundle="ACQUISITION_RESOURCES"/></h3>
<p class="mvert05"><bean:message key="acquisitionProcess.label.assignPayingUnitToItem.description" bundle="ACQUISITION_RESOURCES"/></p>
<span id="maxValue" style="display: none;"><fr:view name="information" property="item.valueForDistribution"/></span>
<div class="dinline forminline">
	<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>
	
		<fr:edit id="activityBean" name="information" visible="false"/>
				
		<bean:size id="maxElements" name="information" property="beans"/>
			
		<table id="assign" class="tstyle3 inputaright">
			<tr>
				<th>

				</th>				
				<th>
					<bean:message key="acquisitionProcess.label.payingUnit" bundle="ACQUISITION_RESOURCES"/>
				</th>
				<th>
					<bean:message key="unitItem.label.shareValue" bundle="ACQUISITION_RESOURCES"/>
				</th>
			</tr>
			
			<logic:iterate id="unitItemBean" name="information" property="beans" indexId="id">
				<bean:define id="unitId" name="unitItemBean" property="unit.externalId"/>
					<tr id='<%= "tr" + id %>'>
						<td id='<%= unitId %>'>
							<fr:edit  id='<%= "assigned" + unitId %>' name="unitItemBean" slot="assigned"/>
						</td>
						<td class="aleft">
							<fr:view name="unitItemBean" property="unit.presentationName"/>
						</td>
						<td class="aright">
							<fr:edit id='<%= "shareValue" + unitId %>' name="unitItemBean" slot="shareValue"/>
						</td>
					</tr>
			</logic:iterate>
					<tr>
						<td colspan="2" class="aright">
							<strong><bean:message key="label.total" bundle="EXPENDITURE_RESOURCES"/></strong>
						</td>
						<td class="aright">
							<span id="sum">
									
							</span> 
						</td>
					</tr>
		</table>
		
		<p>	
		<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#" id="distribute"><bean:message key="acquisitionRequestItem.link.autoDistribute" bundle="ACQUISITION_RESOURCES"/></a>
		</p>
		
		<script type="text/javascript" src='<%=  request.getContextPath() + "/javaScript/valueDistribution.js"%>'></script>
		<script type="text/javascript">

			var url = '<%= request.getContextPath() +  "/acquisition" + processClass + ".do?method=calculateShareValuesViaAjax" %>';
			var urlValue = '<%= request.getContextPath() +  "/acquisition" + processClass + ".do?method=calculateValueForDistribution" %>';
		
			$("#assign input[type='checkbox']").click(function() {
					if(!$(this).is(':checked')) {
						$(this).parent("td").siblings("td:last").find("input").val("0");
					}
					<%= "getMaxValue('" + requestItemId + "', '" + outOfLabel + "',urlValue);" %>
			});

			$("#distribute").click(function() {
				<%= "getShares('" + outOfLabel + "',url);" %>
			});
			
			$("input[type=text]").keyup(function() {
				<%= "writeSum('" + outOfLabel + "',url);" %>
			});

			<%= "writeSum('" + outOfLabel + "',url);" %>
		</script>
				
		<html:submit styleClass="inputbutton"><bean:message key="button.atribute" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
	</fr:form>

	<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>
