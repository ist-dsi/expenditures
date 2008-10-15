<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionRequestItem.title.insertRealValues" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="processOID" name="itemBean" property="acquisitionRequest.acquisitionProcess.OID"/>
<bean:define id="itemOID" name="itemBean" property="item.OID"/>

<bean:define id="acquisitionProcess" name="itemBean" property="acquisitionRequest.acquisitionProcess" toScope="request"/>
<jsp:include page="viewAcquisitionRequest.jsp" flush="true"/>


<div class="dinline forminline">
<fr:form action='<%= "/acquisitionProcess.do?method=executeAcquisitionRequestItemRealValuesEdition&acquisitionProcessOid="  + processOID %>'>
	<fr:edit id="acquisitionRequestItem" name="itemBean" visible="false"/>
			<table class="formhorizontal">
				<tr>
					<th></th>
					<th>
						<strong><bean:message key="acquisitionRequestItem.label.estimatedValue" bundle="ACQUISITION_RESOURCES"/></strong>
					</th>
					<th>
						<strong><bean:message key="acquisitionRequestItem.label.effectiveValue" bundle="ACQUISITION_RESOURCES"/></strong>
					</th>
				</tr>
				<tr>
					<td><bean:message key="acquisitionRequestItem.label.quantity" bundle="ACQUISITION_RESOURCES"/>:</td>
					<td class="aright">
						<fr:view name="itemBean" property="quantity"/>
					</td>
					<td>
						<fr:edit name="itemBean" slot="realQuantity" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					</td>
				</tr>
				<tr>
					<td><bean:message key="acquisitionRequestItem.label.unitValue" bundle="ACQUISITION_RESOURCES"/>:</td>
					<td class="aright">
						<fr:view name="itemBean" property="unitValue"/>
					</td>
					<td>
						<fr:edit name="itemBean" slot="realUnitValue" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					</td>
				</tr>
				<tr>
					<td><bean:message key="acquisitionRequestItem.label.vatValue" bundle="ACQUISITION_RESOURCES"/>:</td>
					<td class="aright">
						<fr:view name="itemBean" property="vatValue"/>
					</td>
					<td>
						<fr:edit name="itemBean" slot="realVatValue" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					</td>
				</tr>
				<tr>
					<td><bean:message key="acquisitionRequestItem.label.additionalCosts" bundle="ACQUISITION_RESOURCES"/>:</td>
					<td class="aright">
						<fr:view name="itemBean" property="additionalCostValue" type="pt.ist.expenditureTrackingSystem.domain.util.Money">
							<fr:layout name="null-as-label">
								<fr:property name="subLayout" value="default"/>
							</fr:layout>
						</fr:view>
					</td>
					<td>
						<fr:edit name="itemBean" slot="shipment" />
					</td>
				</tr>
			</table>
			<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>

	<fr:form action='<%="/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid=" + processOID + "&acquisitionRequestItemOid=" + itemOID%>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>

 