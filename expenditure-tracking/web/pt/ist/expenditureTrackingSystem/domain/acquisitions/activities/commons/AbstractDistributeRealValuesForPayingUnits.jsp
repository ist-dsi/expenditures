<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<%@page import="myorg.presentationTier.servlets.filters.contentRewrite.ContentContextInjectionRewriter"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%><bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="processClass" name="process" property="class.simpleName"/>
<bean:define id="outOfLabel">
	<bean:message key="acquisitionRequestItem.label.outOf" bundle="ACQUISITION_RESOURCES"/>
</bean:define>
<bean:define id="name" name="information" property="activityName"/>

<logic:equal name="information" property="item.filledWithRealValues" value="false">
	<div class="infobox_warning">
		<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/></strong>: <bean:message key="acquisitionRequestItem.message.warn.mustDefineRealValuesFirst" bundle="ACQUISITION_RESOURCES"/>
	</div>
	<html:link page='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>« <bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/></html:link>
</logic:equal>
			
<logic:equal name="information" property="item.filledWithRealValues" value="true">
	
<bean:define id="maxValue" name="information" property="item.realValue.roundedValue"/>

<div class="dinline forminline">	
	
	<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>
	
		<fr:edit id="activityBean" name="information" visible="false"/>
		
		<table class="tstyle3 inputaright" id="assign">	
			<tr>
				<th></th>
				<th><bean:message key="acquisitionProcess.label.payingUnit" bundle="ACQUISITION_RESOURCES"/></th>
				<th><bean:message key="acquisitionRequestItem.label.effectiveValue" bundle="ACQUISITION_RESOURCES"/></th>
				<th><bean:message key="acquisitionRequestItem.label.estimatedValue" bundle="ACQUISITION_RESOURCES"/></th>
			</tr>
			<logic:iterate id="bean" name="information" property="beans" indexId="id">
					<tr  id='<%= "tr" + id %>'">
					<td><input type="checkbox" checked="true" disabled="disabled"/></td>
					<td><fr:view name="bean" property="unit.presentationName"/></td>
					<td class="aright"><fr:view name="bean" property="shareValue"/></td>
					<td><fr:edit name="bean" slot="realShareValue"/></td>
				</tr>
			</logic:iterate>
				<tr>
					<td colspan="3" class="aright">
						<strong><bean:message key="label.total" bundle="EXPENDITURE_RESOURCES"/></strong>
					</td>
					<td class="aright">
						<span id="sum">
						</span> 
					</td>
					<td>
					</td>
				</tr>
		</table>
	
		<p>	
		<%= ContentContextInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
		<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#" id="distribute"><bean:message key="acquisitionRequestItem.link.autoDistribute" bundle="ACQUISITION_RESOURCES"/></a>
		<%= ContentContextInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>
		</p>
		
		<html:submit styleClass="inputbutton"><bean:message key="button.atribute" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
	
	</fr:form>

	<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
	
</div>


	<script type="text/javascript" src='<%=  request.getContextPath() + "/javaScript/valueDistribution.js"%>'></script>
	
	<script type="text/javascript">

		var url = '<%= request.getContextPath() +  "/acquisition" + processClass + ".do?method=calculateShareValuesViaAjax" %>';
			
		$("#distribute").click(function() {
				<%= "getShares('" + maxValue + "', '" + outOfLabel + "',url);" %>
		});

		
		$("input[type=text]").keyup(function() {
			<%= "writeSum('" + maxValue + "', '" + outOfLabel + "');" %>
		});

		<%= "writeSum('" + maxValue + "', '" + outOfLabel + "');" %>
		</script>
</logic:equal>