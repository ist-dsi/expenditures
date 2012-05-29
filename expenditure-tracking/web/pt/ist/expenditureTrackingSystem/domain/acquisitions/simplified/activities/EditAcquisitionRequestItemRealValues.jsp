<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


	<!--
	<bean:message key="acquisitionRequestItem.title.insertRealValues" bundle="ACQUISITION_RESOURCES"/>
	-->
	
<p class="mtop15 mbottom0">
	<b>Insira os custos efectivos:</b>
</p>

<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<bean:define id="item" name="information" property="item"/>


<div class="dinline forminline">
<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>

	<fr:edit id="activityBean" name="information" visible="false"/>

			<table class="tstyle3 thright inputaright">
				<tr>
					<th></th>
					<th>
						<bean:message key="acquisitionRequestItem.label.estimatedValue" bundle="ACQUISITION_RESOURCES"/>
					</th>
					<th class="aleft" style="padding-left: 20px;">
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
						<fr:view name="item" property="quantity"/>
					</td>
					<td class="aleft">
						<fr:edit id="realQuantity" name="information" slot="realQuantity" required="true">
					 		<fr:layout>
								<fr:property name="size" value="10"/>
							</fr:layout>
				 		</fr:edit>
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
						<fr:view name="item" property="unitValue"/>
					</td>
					<td class="aleft">
						<fr:edit id="realUnitValue" name="information" slot="realUnitValue" required="true">
							<fr:layout>
								<fr:property name="size" value="10"/>
							</fr:layout>
				 		</fr:edit>
				 		€
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
						<fr:view name="item" property="vatValue"/>%
					</td>
					<td class="aleft">
						<fr:edit id="realVatValue" name="information" slot="realVatValue" required="true">
							<fr:layout>
								<fr:property name="size" value="10"/>
							</fr:layout>
				 		</fr:edit>
						%
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
						<fr:view name="item" property="additionalCostValue" type="myorg.domain.util.Money">
							<fr:layout name="null-as-label">
								<fr:property name="subLayout" value="default"/>
							</fr:layout>
						</fr:view>
					</td>
					<td class="aleft">
						<fr:edit name="information" slot="shipment">
							<fr:layout>
								<fr:property name="size" value="10"/>
							</fr:layout>
				 		</fr:edit>
					</td>
					<td class="tderror"></td>
				</tr>
			</table>
			<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>

	<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>

 