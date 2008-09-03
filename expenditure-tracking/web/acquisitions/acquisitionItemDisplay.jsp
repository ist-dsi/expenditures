<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
			
			
<div class="infoop2">
	
	<table class="tstyle1">

		<tr>
			<th><bean:message key="acquisitionRequestItem.label.proposalReference" bundle="ACQUISITION_RESOURCES"/>:</th>
			<td colspan="3"><fr:view name="acquisitionRequestItem" property="proposalReference"/></td>
		</tr>
		
		<tr>
			<th><bean:message key="acquisitionRequestItem.label.quantity" bundle="ACQUISITION_RESOURCES"/>:</th>
			<td><fr:view name="acquisitionRequestItem" property="quantity"/></td>
			<th style="padding-left: 1em;"><bean:message key="acquisitionRequestItem.label.realQuantity" bundle="ACQUISITION_RESOURCES"/>:</th>
			<td><fr:view name="acquisitionRequestItem" property="realQuantity" type="java.lang.Integer">
						<fr:layout name="null-as-label">
							<fr:property name="subLayout" value="default"/>
						</fr:layout>
					</fr:view>
			</td>
		</tr>
		
		<tr>
			<th><bean:message key="acquisitionRequestItem.label.unitValue" bundle="ACQUISITION_RESOURCES"/>:</th>
			<td><fr:view name="acquisitionRequestItem" property="unitValue"/></td>
			<th style="padding-left: 1em;"><bean:message key="acquisitionRequestItem.label.realUnitValue" bundle="ACQUISITION_RESOURCES"/>:</th>
			<td>
				<fr:view name="acquisitionRequestItem" property="realUnitValue" type="pt.ist.expenditureTrackingSystem.domain.util.Money">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="default"/>
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		
		<tr>
			<th><bean:message key="acquisitionRequestItem.label.vatValue" bundle="ACQUISITION_RESOURCES"/></th>
			<td><fr:view name="acquisitionRequestItem" property="vatValue"/></td>
			<th style="padding-left: 1em;"><bean:message key="acquisitionRequestItem.label.realVatValue" bundle="ACQUISITION_RESOURCES"/>:</th>
			<td>
				<fr:view name="acquisitionRequestItem" property="realVatValue" type="java.lang.String">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="default"/>
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		
		<tr>
			<th><bean:message key="acquisitionRequestItem.label.additionalCostValue" bundle="ACQUISITION_RESOURCES"/>:</th>
			<td>
				<fr:view name="acquisitionRequestItem" property="additionalCostValue" type="pt.ist.expenditureTrackingSystem.domain.util.Money">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="default"/>
					</fr:layout>
				</fr:view>
			</td>
			<th style="padding-left: 1em;"><bean:message key="acquisitionRequestItem.label.realAdditionalCostValue" bundle="ACQUISITION_RESOURCES"/>:</th>
			<td>
				<fr:view name="acquisitionRequestItem" property="realAdditionalCostValue" type="pt.ist.expenditureTrackingSystem.domain.util.Money">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="default"/>
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		
		<tr>
			<th><bean:message key="acquisitionRequestItem.label.totalValue" bundle="ACQUISITION_RESOURCES"/>:</th>
			<td><span><fr:view name="acquisitionRequestItem" property="totalItemValue"/></span></td>
			<th style="padding-left: 1em;"><bean:message key="acquisitionRequestItem.label.totalRealValue" bundle="ACQUISITION_RESOURCES"/>:</th>
			<td>
				<span>
					<fr:view name="acquisitionRequestItem" property="totalRealValue" type="pt.ist.expenditureTrackingSystem.domain.util.Money">
						<fr:layout name="null-as-label">
							<fr:property name="subLayout" value="default"/>
						</fr:layout>
					</fr:view>
				</span>
			</td>
		</tr>
		
		<tr>
			<th><bean:message key="acquisitionProcess.label.payingUnits" bundle="ACQUISITION_RESOURCES"/>:</th>
			<td colspan="3">
				<logic:notEmpty name="acquisitionRequestItem" property="unitItems">
				<table class="payingunits">
				<logic:iterate id="unitItem" name="acquisitionRequestItem" property="sortedUnitItems">
					<tr>
						<td>
							<fr:view name="unitItem" property="unit.presentationName"/>										
						</td>
						<td class="nowrap vatop">
							<logic:present name="unitItem" property="realShareValue">
								<fr:view name="unitItem" property="realShareValue"/>
							</logic:present>
							<logic:notPresent name="unitItem" property="realShareValue">
								<fr:view name="unitItem" property="shareValue"/>
							</logic:notPresent>
						</td>
					</tr>
				</logic:iterate>
				</table>
				</logic:notEmpty>
				<logic:empty name="acquisitionRequestItem" property="unitItems">
					<em><bean:message key="label.notDefined" bundle="EXPENDITURE_RESOURCES"/></em>
				</logic:empty>
			</td>
		</tr>
		
		<tr>
			<th><bean:message key="acquisitionRequestItem.label.salesCode" bundle="ACQUISITION_RESOURCES"/>:</th>
			<td colspan="3">
				<fr:view name="acquisitionRequestItem" property="CPVReference">
					<fr:layout name="format">
						<fr:property name="format" value="${code} - ${description}"/>
					</fr:layout>
				</fr:view>
			</td>
		</tr>
	
		<tr>
			<th><bean:message key="acquisitionRequestItem.label.description" bundle="ACQUISITION_RESOURCES"/>:</th>
			<td colspan="3"><fr:view name="acquisitionRequestItem" property="description"/></td>
		</tr>
		
	</table>
		
</div>