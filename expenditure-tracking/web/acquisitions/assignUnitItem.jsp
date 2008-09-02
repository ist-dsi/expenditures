<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<h2>
	<bean:message key="label.pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.AssignPayingUnitToItem" bundle="ACQUISITION_RESOURCES"/>
</h2>

<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/calculator.js" %>"></script> 

<bean:define id="processOID" name="acquisitionProcess" property="OID"/>
<bean:define id="acquisitionRequestItemOid" name="acquisitionRequestItem" property="OID"/>

<bean:define id="outOfLabel">
	<bean:message key="acquisitionRequestItem.label.outOf" bundle="ACQUISITION_RESOURCES"/>
</bean:define>

<bean:define id="acquisitionRequestItem" name="acquisitionRequestItem" toScope="request"/>
<jsp:include page="./acquisitionItemDisplay.jsp" flush="false"/>

<bean:define id="maxValue" name="acquisitionRequestItem" property="totalItemValue.value"/>
<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<div class="dinline forminline">
	<fr:form action="<%="/acquisitionProcess.do?acquisitionProcessOid=" + processOID + "&acquisitionRequestItemOid=" + acquisitionRequestItemOid%>">
		<html:hidden  property="method" value="executeAssignPayingUnitToItemCreation"/>
		
		<fr:edit id="unitItemBeans" name="unitItemBeans" visible="false"/>
		<bean:size id="maxElements" name="unitItemBeans"/>
			
		<table class="tstyle4">
			<tr>
				<th>

				</th>				
				<th>
					<strong><bean:message key="acquisitionProcess.label.payingUnit" bundle="ACQUISITION_RESOURCES"/></strong>
				</th>
				<th>
					<strong><bean:message key="unitItem.label.shareValue" bundle="ACQUISITION_RESOURCES"/></strong>
				</th>
			</tr>
			<logic:iterate id="unitItemBean" name="unitItemBeans" indexId="id">
					<tr id="<%= "tr" + id %>" onKeyUp="<%= "javascript:calculate(" + maxElements + ", 'sum', '" + maxValue + "', '" + outOfLabel+ "')" %>">
						<td onclick="<%= "javascript:calculate(" + maxElements + ", 'sum', '" + maxValue + "', '" + outOfLabel+ "')" %>">
							<fr:edit  id="<%= "assigned" + id %>" name="unitItemBean" slot="assigned"/>
						</td>
						<td>
							<fr:view name="unitItemBean" property="unit.name"/>
						</td>
						<td>
							<fr:edit id="<%= "shareValue" + id %>" name="unitItemBean" slot="shareValue"/>
						</td>
					</tr>
			</logic:iterate>
					<tr>
					<td>
					</td>
					<td>
						<strong><bean:message key="label.total" bundle="EXPENDITURE_RESOURCES"/></strong>:
					</td>
					<td>
						<span id="sum">
								
						</span> 
					</td>
					</tr>
		</table>
		
		<script type="text/javascript">
			<%= "calculate(" + maxElements + ", 'sum', '" + maxValue + "', '" + outOfLabel+ "')" %>
		</script>
		
		
		<p class="mtop05 mbottom2">
			<a href="javascript:document.forms[0].method.value='calculateShareValuePostBack'; document.forms[0].submit();"> <bean:message key="acquisitionRequestItem.link.autoDistribute" bundle="ACQUISITION_RESOURCES"/> </a>
		</p>
		
			<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>

	<fr:form action="<%="/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid=" + processOID + "&acquisitionRequestItemOid=" + acquisitionRequestItemOid%>">
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>