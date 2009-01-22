<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<div class="infoop2">	
	<table class="tstyle1" style="width: 100%;">
		<tr>
				<th colspan="2" style="background: #eaeaea;"><bean:message key="acquisitionProcess.title.description" bundle="ACQUISITION_RESOURCES"/></th>
		</tr>
		<tr>
			<td><bean:message key="refundItem.label.salesCode" bundle="ACQUISITION_RESOURCES"/></td>
			<td>
				<fr:view name="item" property="CPVReference" >
					<fr:layout name="format">
						<fr:property name="format" value="${code} - ${description}"/>
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		<tr>
			<td><bean:message key="refundItem.label.description" bundle="ACQUISITION_RESOURCES"/></td><td><fr:view name="item" property="description"/></td>
		</tr>

		<tr>
			<th colspan="2" style="background: #eaeaea;"><bean:message key="acquisitionProcess.title.quantityAndCosts.lowercase" bundle="ACQUISITION_RESOURCES"/></th>
		</tr>	
		<tr>
			<td><bean:message key="label.value" bundle="EXPENDITURE_RESOURCES"/></td><td><fr:view name="item" property="value"/></td>
		</tr>
		<tr>
			 <td><bean:message key="label.refundValue" bundle="EXPENDITURE_RESOURCES"/></td><td>
			 
			 	<fr:view name="item" property="realValue" type="pt.ist.expenditureTrackingSystem.domain.util.Money" layout="null-as-label"/>
			 
			  </td>
		</tr>

		<logic:notEmpty name="item" property="unitItems">
			<tr>
				<th colspan="2" style="background: #eaeaea;"><bean:message key="acquisitionProcess.label.payingUnits" bundle="ACQUISITION_RESOURCES"/></th>
			</tr>
			<tr>
				<td colspan="2">
					<table class="payingunits">
						<logic:iterate id="unitItem" name="item" property="sortedUnitItems">
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
				</td>
			</tr>
		</logic:notEmpty>
	</table>
</div>