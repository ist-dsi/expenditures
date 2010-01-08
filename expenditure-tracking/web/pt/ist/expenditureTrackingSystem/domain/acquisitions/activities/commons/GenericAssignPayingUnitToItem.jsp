<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<%@page import="myorg.presentationTier.servlets.filters.contentRewrite.ContentContextInjectionRewriter"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%><script type="text/javascript" src='<%= request.getContextPath() + "/javaScript/calculator.js" %>'></script> 


<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="processClass" name="process" property="class.simpleName"/>
<bean:define id="outOfLabel">
	<bean:message key="acquisitionRequestItem.label.outOf" bundle="ACQUISITION_RESOURCES"/>
</bean:define>
<bean:define id="maxValue" name="information" property="item.value.roundedValue"/>
<bean:define id="name" name="information" property="activityName"/>

<h3 class="mbottom05"><bean:message key="acquisitionProcess.label.assignPayingUnitToItem" bundle="ACQUISITION_RESOURCES"/></h3>
<p class="mvert05"><bean:message key="acquisitionProcess.label.assignPayingUnitToItem.description" bundle="ACQUISITION_RESOURCES"/></p>

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
					<tr id='<%= "tr" + id %>'>
						<td>
							<fr:edit  id='<%= "assigned" + id %>' name="unitItemBean" slot="assigned"/>
						</td>
						<td class="aleft">
							<fr:view name="unitItemBean" property="unit.presentationName"/>
						</td>
						<td class="aright">
							<fr:edit id='<%= "shareValue" + id %>' name="unitItemBean" slot="shareValue"/>
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
		<%= ContentContextInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
		<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#" id="distribute"><bean:message key="acquisitionRequestItem.link.autoDistribute" bundle="ACQUISITION_RESOURCES"/></a>
		<%= ContentContextInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>
		</p>
		
		<script type="text/javascript" src='<%=  request.getContextPath() + "/javaScript/valueDistribution.js"%>'></script>
		<script type="text/javascript">

			var url = '<%= request.getContextPath() +  "/acquisition" + processClass + ".do?method=calculateShareValuesViaAjax" %>';
		
			$("#assign input[type='checkbox']").click(function() {
					if(!$(this).attr('checked')) {
						$(this).parent("td").siblings("td:last").children("input").attr('value','0');
					}
					<%= "getShares('" + maxValue + "', '" + outOfLabel + "',url);" %>
			});

			$("#distribute").click(function() {
				<%= "getShares('" + maxValue + "', '" + outOfLabel + "',url);" %>
			});
			
			$("input[type=text]").keyup(function() {
				<%= "writeSum('" + maxValue + "', '" + outOfLabel + "',url);" %>
			});

			<%= "writeSum('" + maxValue + "', '" + outOfLabel + "',url);" %>
		</script>
				
		<html:submit styleClass="inputbutton"><bean:message key="button.atribute" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
	</fr:form>

	<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>