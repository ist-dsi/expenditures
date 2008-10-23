<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<h2><bean:message key="acquisitionRequestItem.title.editRealValues" bundle="ACQUISITION_RESOURCES"/></h2>

<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/calculator.js" %>"></script> 

<bean:define id="processOID" name="item" property="acquisitionRequest.acquisitionProcess.OID"/>

<bean:define id="outOfLabel">
	<bean:message key="acquisitionRequestItem.label.outOf" bundle="ACQUISITION_RESOURCES"/>
</bean:define>
<bean:define id="maxValue" name="item" property="totalRealValueWithAdditionalCostsAndVat.value"/>
<bean:size id="maxElements" name="beans"/>

<bean:define id="itemOID" name="item" property="OID"/>

<bean:define id="acquisitionRequestItem" name="item" toScope="request"/>
<jsp:include page="./acquisitionItemDisplay.jsp" flush="false"/>

<logic:equal name="item" property="filledWithRealValues" value="false">
	<div class="infoop4">
		<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/></strong>: <bean:message key="acquisitionRequestItem.message.warn.mustDefineRealValuesFirst" bundle="ACQUISITION_RESOURCES"/>
	</div>
	<html:link page='<%= "/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid="  + processOID %>'>« <bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/></html:link>
</logic:equal>
			
<logic:equal name="item" property="filledWithRealValues" value="true">
	
<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<div class="dinline forminline">	
<fr:form action='<%= "/acquisitionProcess.do?method=executeDistributeRealValuesForPayingUnitsEdition&acquisitionProcessOid="  + processOID + "&acquisitionRequestItemOid=" + itemOID%>'>
	<fr:edit name="beans" id="beans" visible="false"/>
	
	<table class="tstyle4">	
		<tr>
			<th></th>
			<th><bean:message key="acquisitionProcess.label.payingUnit" bundle="ACQUISITION_RESOURCES"/></th>
			<th><bean:message key="acquisitionRequestItem.label.effectiveValue" bundle="ACQUISITION_RESOURCES"/></th>
			<th><bean:message key="acquisitionRequestItem.label.estimatedValue" bundle="ACQUISITION_RESOURCES"/></th>
		</tr>
		<logic:iterate id="bean" name="beans" indexId="id">
			<tr  id='<%= "tr" + id %>' onKeyUp="<%= "javascript:calculate('" + maxElements + "', 'sum', '" + maxValue + "', '" + outOfLabel+ "');" %>">
				<td><input type="checkbox" checked="true" disabled="disabled"/></td>
				<td><fr:view name="bean" property="unit.presentationName"/></td>
				<td><fr:edit name="bean" slot="realShareValue" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/></td>
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
		
			<html:submit styleClass="inputbutton"><bean:message key="button.atribute" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
	</fr:form>

	<fr:form action='<%="/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid=" + processOID + "&acquisitionRequestItemOid=" + itemOID%>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>


</logic:equal>