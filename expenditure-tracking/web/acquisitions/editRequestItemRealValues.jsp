<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionRequestItem.title.insertRealValues" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="processOID" name="itemBean" property="acquisitionRequest.acquisitionProcess.externalId" type="java.lang.String"/>
<bean:define id="processClass" name="itemBean" property="acquisitionRequest.acquisitionProcess.class.simpleName"/>
<bean:define id="actionMapping" value="<%= "/acquisition" + processClass %>"/>

<bean:define id="itemOID" name="itemBean" property="item.externalId" type="java.lang.String"/>

<bean:define id="processRequest" name="itemBean" property="acquisitionRequest" toScope="request"/>
<jsp:include page="commons/viewAcquisitionRequest.jsp" flush="true"/>



<div class="dinline forminline">
<fr:form action='<%= actionMapping + ".do?method=executeAcquisitionRequestItemRealValuesEdition&acquisitionProcessOid="  + processOID %>'>
	<fr:edit id="acquisitionRequestItem" name="itemBean" visible="false"/>
			<table class="tstyle3 thright inputaright">
				<tr>
					<th></th>
					<th>
						<bean:message key="acquisitionRequestItem.label.estimatedValue" bundle="ACQUISITION_RESOURCES"/>
					</th>
					<th class="acenter">
						<bean:message key="acquisitionRequestItem.label.effectiveValue" bundle="ACQUISITION_RESOURCES"/>
					</th>
					<td class="tderror">
					</td>
				</tr>
				<tr>
					<td class="aleft nowrap">
						<bean:message key="acquisitionRequestItem.label.quantity" bundle="ACQUISITION_RESOURCES"/>:
					</td>
					<td class="aright">
						<fr:view name="itemBean" property="quantity"/>
					</td>
					<td class="aleft">
						<fr:edit id="realQuantity" name="itemBean" slot="realQuantity" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					</td>
					<td class="tderror">
						<fr:hasMessages for="realQuantity"><span><fr:message for="realQuantity"/></span></fr:hasMessages>
					</td>
				</tr>
				<tr>
					<td class="aleft nowrap">
						<bean:message key="acquisitionRequestItem.label.unitValue" bundle="ACQUISITION_RESOURCES"/>:
					</td>
					<td class="aright">
						<fr:view name="itemBean" property="unitValue"/>
					</td>
					<td class="aleft">
						<fr:edit id="realUnitValue" name="itemBean" slot="realUnitValue" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/> €
					</td>
					<td class="tderror">
						<fr:hasMessages for="realUnitValue"><span><fr:message for="realUnitValue"/></span></fr:hasMessages>
					</td>
				</tr>
				<tr>
					<td class="aleft nowrap">
						<bean:message key="acquisitionRequestItem.label.vatValue" bundle="ACQUISITION_RESOURCES"/>:
					</td>
					<td class="aright">
						<fr:view name="itemBean" property="vatValue"/>%
					</td>
					<td class="aleft">
						<fr:edit id="realVatValue" name="itemBean" slot="realVatValue" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/> %
					</td>
					<td class="tderror">
						<fr:hasMessages for="realVatValue"><span><fr:message for="realVatValue"/></span></fr:hasMessages>
					</td>
				</tr>
				<tr>
					<td class="aleft nowrap">
						<bean:message key="acquisitionRequestItem.label.additionalCosts" bundle="ACQUISITION_RESOURCES"/>:
					</td>
					<td class="aright">
						<fr:view name="itemBean" property="additionalCostValue" type="myorg.domain.util.Money">
							<fr:layout name="null-as-label">
								<fr:property name="subLayout" value="default"/>
							</fr:layout>
						</fr:view>
					</td>
					<td class="aleft">
						<fr:edit name="itemBean" slot="shipment" />
					</td>
					<td class="tderror"></td>
				</tr>
			</table>
			<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>

	<fr:form action='<%= actionMapping + ".do?method=viewAcquisitionProcess&acquisitionProcessOid=" + processOID + "&acquisitionRequestItemOid=" + itemOID%>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>

 