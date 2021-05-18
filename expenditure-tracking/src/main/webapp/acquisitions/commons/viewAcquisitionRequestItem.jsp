<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

<style>
@media print {
	td[title]:after{
		content: "("attr(title)")";
	}
	a[href]:after {
	   	content: none !important;
	}
}
</style>


<bean:define id="acquisitionRequestItem" name="item" type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem"/>
<bean:define id="itemId" name="item" property="externalId" type="java.lang.String"/>
<bean:define id="processId" name="item" property="request.process.externalId" type="java.lang.String"/>

<bean:define id="needsSeparator" value="false" toScope="request"/>	
	
	<bean:define id="currentIndex" name="currentIndex" />
	<bean:define id="totalItems" name="totalItems" />

	<tbody id='<%= "item" + currentIndex %>'>
		<tr>
			<th>Item</th>
			<th>Descrição</th>
			<th></th>
			<th></th>
			<th name="effectiveValues">Efectivos</th>
			<th name="operations"></th>
		</tr>

		<tr>
			<td rowspan="7"><fr:view name="currentIndex"/>/<fr:view name="totalItems"/></td>
			<td rowspan="7" class="aleft">
				<p class="mvert0"><fr:view name="acquisitionRequestItem" property="description"/></p>
				<ul>
					<li>
						<bean:message key="acquisitionRequestItem.label.proposalReference" bundle="ACQUISITION_RESOURCES"/>:
						<fr:view name="acquisitionRequestItem" property="proposalReference"/>
					</li>
					<logic:notEmpty name="acquisitionRequestItem" property="material">
						<li>
							<bean:message key="acquisitionRequestItem.label.material" bundle="ACQUISITION_RESOURCES"/>:
							<fr:view name="acquisitionRequestItem" property="material">
								<fr:layout name="format">
									<fr:property name="format" value="\${materialSapId} - \${description}"/>
								</fr:layout>
							</fr:view>
						</li>
					</logic:notEmpty>
					<li>
						<bean:message key="acquisitionRequestItem.label.salesCode" bundle="ACQUISITION_RESOURCES"/>:
						<fr:view name="acquisitionRequestItem" property="CPVReference">
							<fr:layout name="format">
								<fr:property name="format" value="\${code} - \${description}"/>
							</fr:layout>
						</fr:view>
					</li>
					<li>
						<% if(acquisitionRequestItem.getResearchAndDevelopmentPurpose()!=null && acquisitionRequestItem.getResearchAndDevelopmentPurpose()) {%>
							<strong><bean:message key="acquisitionRequestItem.label.researchAndDevelopmentPurpose" bundle="ACQUISITION_RESOURCES" />:
							<fr:view name="acquisitionRequestItem" property="researchAndDevelopmentPurpose"/>
							</strong>
						<% } else { %>
							<bean:message key="acquisitionRequestItem.label.researchAndDevelopmentPurpose" bundle="ACQUISITION_RESOURCES" />:
							<fr:view name="acquisitionRequestItem" property="researchAndDevelopmentPurpose" type="java.lang.Boolean">
								<fr:layout name="null-as-label">
									<fr:property name="subLayout" value="default" />
								</fr:layout>
							</fr:view>
						<% } %>
						
					</li>
					<li class="extraInfo">
						<bean:message key="label.address" bundle="ACQUISITION_RESOURCES"/>:
						<fr:view name="acquisitionRequestItem" property="address">
							<fr:layout name="format">
									<fr:property name="format" value="\${country}, \${line2}, \${location}, \${postalCode}"/>
							</fr:layout>
						</fr:view>
					</li>
					<li class="extraInfo">
						<bean:message key="label.pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionItemClassification" bundle="ACQUISITION_RESOURCES"/>:
						<logic:present name="acquisitionRequestItem" property="classification">
							<fr:view name="acquisitionRequestItem" property="classification"/>
						</logic:present>
						<logic:notPresent name="acquisitionRequestItem" property="classification">
							-
						</logic:notPresent>
					</li>
					<li>
						<bean:message key="acquisitionProcess.label.payingUnits" bundle="ACQUISITION_RESOURCES"/>:
						<logic:notEmpty name="acquisitionRequestItem" property="unitItems">
							<logic:iterate id="unitItem" name="acquisitionRequestItem" property="sortedUnitItems">
								<p class="mvert0">
									<fr:view name="unitItem" property="unit.presentationName"/>:
									<logic:present name="unitItem" property="realShareValue">
										<fr:view name="unitItem" property="realShareValue"/>
									</logic:present>
									<logic:notPresent name="unitItem" property="realShareValue">
										<fr:view name="unitItem" property="shareValue"/>
									</logic:notPresent>
								</p>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="acquisitionRequestItem" property="unitItems">
							<em><bean:message key="label.notDefined" bundle="EXPENDITURE_RESOURCES"/></em>
						</logic:empty>
					</li>
					<% if(acquisitionRequestItem.isServiceProvisionProjectItem()) {%>
					     <li>
						     <bean:message key="unit.label.type" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
						     <strong><bean:message key="label.servicesProvision" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></strong>
					     </li>
					<% } %>
				</ul>
			</td>
			
			
			<td class="nowrap aleft"><bean:message key="acquisitionRequestItem.label.quantity" bundle="ACQUISITION_RESOURCES"/>:</td>
			<td class="nowrap aright"><fr:view name="acquisitionRequestItem" property="quantity"/></td>
			<td class="nowrap aright" name="effectiveValues">
				<fr:view name="acquisitionRequestItem" property="realQuantity" type="java.lang.Integer">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="default"/>
					</fr:layout>
				</fr:view>
			</td>
			
			<td rowspan="7" class="nowrap aleft" name="operations">
				<ul style="padding-top: 0;">
					<wf:activityLink id='<%= "edit-" + itemId %>' processName="process" activityName="EditAcquisitionRequestItem" scope="request" paramName0="item" paramValue0="<%= itemId %>">
						<bean:define id="needsSeparator" value="true" toScope="request"/>
						<li>
							<wf:activityName processName="process" activityName="EditAcquisitionRequestItem" scope="request"/>
						</li>
					</wf:activityLink>
					<wf:activityLink id='<%= "edit-" + itemId %>' processName="process" activityName="EditAcquisitionRequestItemWithMaterial" scope="request" paramName0="item" paramValue0="<%= itemId %>">
						<bean:define id="needsSeparator" value="true" toScope="request"/>
						<li>
							<wf:activityName processName="process" activityName="EditAcquisitionRequestItemWithMaterial" scope="request"/>
						</li>
					</wf:activityLink>
					<wf:activityLink id='<%= "gaput-" + itemId %>' processName="process" activityName="GenericAssignPayingUnitToItem" scope="request" paramName0="item" paramValue0="<%= itemId %>">
						<bean:define id="needsSeparator" value="true" toScope="request"/>
						<li>
							<wf:activityName processName="process" activityName="GenericAssignPayingUnitToItem" scope="request"/>
						</li>
					</wf:activityLink>		
					
					<wf:activityLink id='<%= "realEdit-" + itemId %>' processName="process" activityName="EditAcquisitionRequestItemRealValues" scope="request" paramName0="item" paramValue0="<%= itemId %>">
						<bean:define id="needsSeparator" value="true" toScope="request"/>
						<li>
							<wf:activityName processName="process" activityName="EditAcquisitionRequestItemRealValues" scope="request"/>
						</li>
					</wf:activityLink>	
					
					<wf:activityLink id='<%= "realDistribute-" + itemId %>' processName="process" activityName="DistributeRealValuesForPayingUnits" scope="request" paramName0="item" paramValue0="<%= itemId %>">
						<bean:define id="needsSeparator" value="true" toScope="request"/>
						<li>
							<wf:activityName processName="process" activityName="DistributeRealValuesForPayingUnits" scope="request"/>
						</li>
					</wf:activityLink>
					<wf:activityLink id='<%= "delete-" + itemId %>' processName="process" activityName="DeleteAcquisitionRequestItem" scope="request" paramName0="item" paramValue0="<%= itemId %>">
						<bean:define id="needsSeparator" value="true" toScope="request"/>
						<li>
							<wf:activityName processName="process" activityName="DeleteAcquisitionRequestItem" scope="request"/>
						</li>
					</wf:activityLink>
					<wf:activityLink id='<%= "change-" + itemId %>' processName="process" activityName="ChangeAcquisitionRequestItemClassification" scope="request" paramName0="item" paramValue0="<%= itemId %>">
						<bean:define id="needsSeparator" value="true" toScope="request"/>
						<li>
							<wf:activityName processName="process" activityName="ChangeAcquisitionRequestItemClassification" scope="request"/>
						</li>
					</wf:activityLink>
                    <wf:activityLink id='<%= "changeMaterial-" + itemId %>' processName="process" activityName="ChangeAcquisitionRequestItemMaterial" scope="request" paramName0="item" paramValue0="<%= itemId %>">
                        <bean:define id="needsSeparator" value="true" toScope="request"/>
                        <li>
                            <wf:activityName processName="process" activityName="ChangeAcquisitionRequestItemMaterial" scope="request"/>
                        </li>
                    </wf:activityLink>
				</ul>
			</td>
		</tr>
		<tr>
			<td class="nowrap aleft"><bean:message key="acquisitionRequestItem.label.unitValue" bundle="ACQUISITION_RESOURCES"/>:</td>
			<td class="nowrap aright" title="<%= acquisitionRequestItem.getUnitValue().getValue() %>"><fr:view name="acquisitionRequestItem" property="unitValue"/>
			</td>
			<td class="nowrap aright" name="effectiveValues">
				<fr:view name="acquisitionRequestItem" property="realUnitValue" type="module.finance.util.Money">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="default"/>
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		<tr>
			<td class="nowrap aleft"><bean:message key="acquisitionRequestItem.label.totalValue" bundle="ACQUISITION_RESOURCES"/>:</td>
			<td class="nowrap aright" title="<%= acquisitionRequestItem.getTotalItemValue().getValue() %>"><span><fr:view name="acquisitionRequestItem" property="totalItemValue"/></span></td>
			<td class="nowrap aright" name="effectiveValues">
				<span>
					<fr:view name="acquisitionRequestItem" property="totalRealValue" type="module.finance.util.Money">
						<fr:layout name="null-as-label">
							<fr:property name="subLayout" value="default"/>
						</fr:layout>
					</fr:view>
				</span>
			</td>
		</tr>
		<tr>
			<td class="nowrap aleft"><bean:message key="acquisitionRequestItem.label.vatValue" bundle="ACQUISITION_RESOURCES"/></td>
			<td class="nowrap aright"><fr:view name="acquisitionRequestItem" property="vatValue"/></td>
			<td class="nowrap aright" name="effectiveValues">
				<fr:view name="acquisitionRequestItem" property="realVatValue" type="java.lang.String">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="default"/>
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		<tr>
			<td class="nowrap aleft"><bean:message key="acquisitionRequestItem.label.vat" bundle="ACQUISITION_RESOURCES"/></td>
			<td class="nowrap aright" title="<%= acquisitionRequestItem.getTotalVatValue().getValue() %>"><fr:view name="acquisitionRequestItem" property="totalVatValue"/></td>
			<td class="nowrap aright" name="effectiveValues">
				<fr:view name="acquisitionRequestItem" property="totalRealVatValue" type="module.finance.util.Money">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="default"/>
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		<tr class="extraInfo">
			<td class="nowrap aleft"><bean:message key="acquisitionRequestItem.label.additionalCostValue" bundle="ACQUISITION_RESOURCES"/>:</td>
			
			<td class="nowrap aright" <%if(acquisitionRequestItem.getAdditionalCostValue()!=null){ %>title="<%= acquisitionRequestItem.getAdditionalCostValue().getValue() %>"<%} %>>
				<fr:view name="acquisitionRequestItem" property="additionalCostValue" type="module.finance.util.Money">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="default"/>
					</fr:layout>
				</fr:view>
			</td>
			<td class="nowrap aright" name="effectiveValues">
				<fr:view name="acquisitionRequestItem" property="realAdditionalCostValue" type="module.finance.util.Money">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="default"/>
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		<tr>
			<td class="nowrap aleft"><bean:message key="acquisitionRequestItem.label.totalValueWithAdditionalCostsAndVat" bundle="ACQUISITION_RESOURCES"/>:</td>
			<td class="nowrap aright" title="<%= acquisitionRequestItem.getTotalItemValueWithAdditionalCostsAndVat().getValue() %>"><span><fr:view name="acquisitionRequestItem" property="totalItemValueWithAdditionalCostsAndVat"/></span></td>
			<td class="nowrap aright" name="effectiveValues">
				<span>
					<fr:view name="acquisitionRequestItem" property="totalRealValueWithAdditionalCostsAndVat" type="module.finance.util.Money">
						<fr:layout name="null-as-label">
							<fr:property name="subLayout" value="default"/>
						</fr:layout>
					</fr:view>
				</span>
			</td>
		</tr>
	</tbody>	
	

	<script type="text/javascript">
		<logic:equal name="acquisitionRequestItem" property="filledWithRealValues" value="false">
			$("[name='effectiveValues']").hide();
		</logic:equal>
		<logic:equal name="acquisitionRequestItem" property="filledWithRealValues" value="true">
			$("[name='effectiveValues']").show();
		</logic:equal>

		<bean:define id="hideOperations" value="true" toScope="request"/>

		<wf:isActive processName="process" activityName="EditAcquisitionRequestItem" scope="request">
			<bean:define id="hideOperations" value="false" toScope="request"/>
		</wf:isActive>
		<wf:isActive processName="process" activityName="GenericAssignPayingUnitToItem" scope="request">
			<bean:define id="hideOperations" value="false" toScope="request"/>
		</wf:isActive>
		<wf:isActive processName="process" activityName="EditAcquisitionRequestItemRealValues" scope="request">
			<bean:define id="hideOperations" value="false" toScope="request"/>
		</wf:isActive>
		<wf:isActive processName="process" activityName="DistributeRealValuesForPayingUnits" scope="request">
			<bean:define id="hideOperations" value="false" toScope="request"/>
		</wf:isActive>
		<wf:isActive processName="process" activityName="DeleteAcquisitionRequestItem" scope="request">
			<bean:define id="hideOperations" value="false" toScope="request"/>
		</wf:isActive>
		<wf:isActive processName="process" activityName="ChangeAcquisitionRequestItemClassification" scope="request">
			<bean:define id="hideOperations" value="false" toScope="request"/>
		</wf:isActive>
        <wf:isActive processName="process" activityName="ChangeAcquisitionRequestItemMaterial" scope="request">
            <bean:define id="hideOperations" value="false" toScope="request"/>
        </wf:isActive>

		<logic:equal name="hideOperations" value="true">
			$("[name='operations']").hide();
		</logic:equal>
	</script>
	
	

	
