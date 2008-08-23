<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<h2>
	<bean:message key="label.pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.AssignPayingUnitToItem" bundle="ACQUISITION_RESOURCES"/>
</h2>

<bean:define id="processOID" name="acquisitionProcess" property="OID"/>

<bean:define id="acquisitionRequestItemOid" name="acquisitionRequestItem" property="OID"/>

<div class="infoop2" style="width: 500px">
	<fr:view name="acquisitionRequestItem"
			schema="viewAcquisitionRequestItem">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<messages:hasMessages>
	<div class="infoop4" style="width: 500px">
		<messages:showMessages/>
	</div>
</messages:hasMessages>

<div class="dinline forminline">
	<fr:form action="<%="/acquisitionProcess.do?acquisitionProcessOid=" + processOID + "&acquisitionRequestItemOid=" + acquisitionRequestItemOid%>">
		<html:hidden  property="method" value="executeAssignPayingUnitToItemCreation"/>
		
		<fr:edit id="unitItemBeans" name="unitItemBeans" visible="false"/>
			
		<table class="tstyle4">
			<tr>
				<th>
					<strong><bean:message key="label.payingUnit" bundle="ACQUISITION_RESOURCES"/></strong>
				</th>
				<th>
					<strong><bean:message key="label.payingUnit" bundle="ACQUISITION_RESOURCES"/></strong>
				</th>				
				<th>
					<strong><bean:message key="label.shareValue" bundle="ACQUISITION_RESOURCES"/></strong>
				</th>
			</tr>
			<logic:iterate id="unitItemBean" name="unitItemBeans" indexId="id">
					<tr>
						<td>
							<fr:view name="unitItemBean" property="unit.name"/>
						</td>
						<td>
							<fr:edit  id="<%= "assigned" + id %>" name="unitItemBean" slot="assigned"/>
						</td>
						<td>
							<fr:edit id="<%= "shareValue" + id %>" name="unitItemBean" slot="shareValue"/>
						</td>
					</tr>
			</logic:iterate>
		</table>

		<p class="mtop05 mbottom2">
			<a href="javascript:document.forms[0].method.value='calculateShareValuePostBack'; document.forms[0].submit();"> <bean:message key="label.auto.distribute" bundle="ACQUISITION_RESOURCES"/> </a>
		</p>
		
			<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>

	<fr:form action="<%="/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid=" + processOID + "&acquisitionRequestItemOid=" + acquisitionRequestItemOid%>">
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>