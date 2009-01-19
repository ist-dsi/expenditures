<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<h2><bean:message key="acquisitionRequestItem.title.editRealValues" bundle="ACQUISITION_RESOURCES"/></h2>

<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/calculator.js" %>"></script> 

<bean:define id="processOID" name="item" property="request.process.OID"/>
<bean:define id="processClass" name="item" property="request.process.class.simpleName"/>
<bean:define id="actionMapping" value="<%= "/acquisition" + processClass%>"/>

<bean:define id="outOfLabel">
	<bean:message key="acquisitionRequestItem.label.outOf" bundle="ACQUISITION_RESOURCES"/>
</bean:define>
<bean:size id="maxElements" name="unitItemBeans"/>

<bean:define id="itemOID" name="item" property="OID"/>
<bean:define id="itemClass" name="item" property="class.simpleName"/>

<jsp:include page='<%= "view" + itemClass + ".jsp"%>'/>

<logic:equal name="item" property="filledWithRealValues" value="false">
	<div class="infoop4">
		<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/></strong>: <bean:message key="acquisitionRequestItem.message.warn.mustDefineRealValuesFirst" bundle="ACQUISITION_RESOURCES"/>
	</div>
	<html:link page='<%= actionMapping + ".do?method=viewProcess&processOid="  + processOID %>'>« <bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/></html:link>
</logic:equal>
			
<logic:equal name="item" property="filledWithRealValues" value="true">
	
<bean:define id="maxValue" name="item" property="realValue.roundedValue"/>
<jsp:include page="../../commons/defaultErrorDisplay.jsp"/>

<div class="dinline forminline">	
<fr:form action='<%= actionMapping + ".do?processOid="  + processOID + "&itemOid=" + itemOID%>'>
	<html:hidden property="method" value="executeDistributeRealValuesForPayingUnitsEdition"/>
	<fr:edit name="unitItemBeans" id="unitItemBeans" visible="false"/>
	
	<table class="tstyle4">	
		<tr>
			<th></th>
			<th><bean:message key="acquisitionProcess.label.payingUnit" bundle="ACQUISITION_RESOURCES"/></th>
			<th><bean:message key="acquisitionRequestItem.label.effectiveValue" bundle="ACQUISITION_RESOURCES"/></th>
			<th><bean:message key="acquisitionRequestItem.label.estimatedValue" bundle="ACQUISITION_RESOURCES"/></th>
		</tr>
		<logic:iterate id="bean" name="unitItemBeans" indexId="id">
			<tr  id='<%= "tr" + id %>' onKeyUp="<%= "javascript:calculate('" + maxElements + "', 'sum', '" + maxValue + "', '" + outOfLabel+ "');" %>">
				<td><input type="checkbox" checked="true" disabled="disabled"/></td>
				<td><fr:view name="bean" property="unit.presentationName"/></td>
				<td><fr:edit name="bean" slot="realShareValue"/></td>
				<td><fr:view name="bean" property="shareValue"/></td>
			</tr>
		</logic:iterate>
			<tr>
				<td colspan="2" class="aright">
					<strong><bean:message key="label.total" bundle="EXPENDITURE_RESOURCES"/></strong>
				</td>
				<td>
					<span id="sum">
					</span> 
				</td>
				<td>
				</td>
			</tr>
	</table>
		<script type="text/javascript">
			 <%= "calculate('" + maxElements + "', 'sum', '" + maxValue + "', '" + outOfLabel+ "');" %> 
		</script>
		
			<p class="mtop05 mbottom2">
				<a href="javascript:document.forms[0].method.value='calculateRealShareValuePostBack'; document.forms[0].submit();">
				 <bean:message key="acquisitionRequestItem.link.autoDistribute" bundle="ACQUISITION_RESOURCES"/> </a>
			</p>
		
			<html:submit styleClass="inputbutton"><bean:message key="button.atribute" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
	</fr:form>

	<fr:form action='<%=actionMapping + ".do?method=viewProcess&processOid=" + processOID + "&itemOid=" + itemOID%>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>


</logic:equal>