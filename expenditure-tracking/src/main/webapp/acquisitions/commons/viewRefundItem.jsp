<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>


<bean:define id="itemID" name="item" property="externalId" type="java.lang.String"/>
<bean:define id="totalItems" name="allItems" />
<bean:define id="currentIndex" name="currentIndex"/>


	<tr>
		<th>Item</th>
		<th colspan="2">Descrição</th>
		<th></th>
		<th></th>
		<th name="operations"></th>
	</tr>
	<tr>
		<td rowspan="2">
			<%= currentIndex %>/<%= totalItems %>
		</td>
		<td rowspan="2" colspan="2" class="aleft">
			<p class="mvert0"><fr:view name="item" property="description"/></p>
			<ul>
				<logic:present name="item" property="material">
					<li>
						<bean:message key="refundItem.label.material" bundle="ACQUISITION_RESOURCES"/>:
						<fr:view name="item" property="material" >
							<fr:layout name="format">
								<fr:property name="format" value="\${materialSapId} - \${description}"/>
							</fr:layout>
						</fr:view>
					</li>
				</logic:present>
				<li>
					<bean:message key="refundItem.label.salesCode" bundle="ACQUISITION_RESOURCES"/>:
					<fr:view name="item" property="CPVReference" >
						<fr:layout name="format">
							<fr:property name="format" value="\${code} - \${description}"/>
						</fr:layout>
					</fr:view>
				</li>
				<li>
					<bean:message key="label.pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionItemClassification" bundle="ACQUISITION_RESOURCES"/>:
					<logic:present name="item" property="classification">
						<fr:view name="item" property="classification"/>
					</logic:present>
					<logic:notPresent name="item" property="classification">
						-
					</logic:notPresent>
				</li>
                <li>
                    <bean:message key="refundItem.label.refundItemNature" bundle="ACQUISITION_RESOURCES"/>:
                    <logic:present name="item" property="refundItemNature">
                        <fr:view name="item" property="refundItemNature.type"/>
                    </logic:present>
                    <logic:notPresent name="item" property="refundItemNature">
                        -
                    </logic:notPresent>
                </li>
                <li>
                    <bean:message key="refundItem.label.supplier" bundle="ACQUISITION_RESOURCES"/>:
                    <logic:present name="item" property="supplier">
                        <fr:view name="item" property="supplier.presentationName"/>
                    </logic:present>
                    <logic:notPresent name="item" property="supplier">
                        -
                    </logic:notPresent>
                </li>
				<logic:notEmpty name="item" property="unitItems">
					<li>
						<bean:message key="acquisitionProcess.label.payingUnits" bundle="ACQUISITION_RESOURCES"/>:<br/>
						<logic:iterate id="unitItem" name="item" property="sortedUnitItems">
							<fr:view name="unitItem" property="unit.presentationName"/>
							<logic:present name="unitItem" property="realShareValue">
								<fr:view name="unitItem" property="realShareValue"/>
							</logic:present>
							<logic:notPresent name="unitItem" property="realShareValue">
								<fr:view name="unitItem" property="shareValue"/>
							</logic:notPresent>
						</logic:iterate>
					</li>
				</logic:notEmpty>
			</ul>
		</td>
		
		<td class="nowrap aleft"><bean:message key="label.value" bundle="EXPENDITURE_RESOURCES"/>:</td>
		<td class="nowrap aright"><fr:view name="item" property="value"/></td>
	
		<td rowspan="2" class="nowrap aleft" name="operations">
			<ul style="padding-top: 0;">
				<wf:activityLink id='<%= "edit-" + itemID %>' processName="process" activityName="EditRefundItem" scope="request" paramName0="item" paramValue0="<%= itemID %>">
					<bean:define id="needsSeparator" value="true" toScope="request"/>
					<li><wf:activityName processName="process" activityName="EditRefundItem" scope="request"/></li>
				</wf:activityLink>
				
				<wf:activityLink id='<%= "edit-" + itemID %>' processName="process" activityName="EditRefundItemWithMaterial" scope="request" paramName0="item" paramValue0="<%= itemID %>">
					<bean:define id="needsSeparator" value="true" toScope="request"/>
					<li><wf:activityName processName="process" activityName="EditRefundItemWithMaterial" scope="request"/></li>
				</wf:activityLink>

				<wf:activityLink id='<%= "assignPayingUnits-" + itemID %>' processName="process" activityName="GenericAssignPayingUnitToItem" scope="request" paramName0="item" paramValue0="<%= itemID %>">
					<bean:define id="needsSeparator" value="true" toScope="request"/>
					<li><wf:activityName processName="process" activityName="GenericAssignPayingUnitToItem" scope="request"/></li>
				</wf:activityLink>

				<wf:activityLink id='<%= "createRefundInvoice-" + itemID %>' processName="process" activityName="CreateRefundInvoice" scope="request" paramName0="item" paramValue0="<%= itemID %>">
					<bean:define id="needsSeparator" value="true" toScope="request"/>
					<li><wf:activityName processName="process" activityName="CreateRefundInvoice" scope="request"/></li>
				</wf:activityLink>

				<wf:activityLink id='<%= "distributeRealValuesForPayingUnits-" + itemID %>' processName="process" activityName="DistributeRealValuesForPayingUnits" scope="request" paramName0="item" paramValue0="<%= itemID %>">
						<bean:define id="needsSeparator" value="true" toScope="request"/>
						<li><wf:activityName processName="process" activityName="DistributeRealValuesForPayingUnits" scope="request"/></li>
				</wf:activityLink>

				<wf:activityLink id='<%= "deleteRefundItem-" + itemID %>' processName="process" activityName="DeleteRefundItem" scope="request" paramName0="item" paramValue0="<%= itemID %>">
					<bean:define id="needsSeparator" value="true" toScope="request"/>
					<li><wf:activityName processName="process" activityName="DeleteRefundItem" scope="request"/></li>
				</wf:activityLink>
				<wf:activityLink id='<%= "change-" + itemID %>' processName="process" activityName="ChangeRefundItemClassification" scope="request" paramName0="item" paramValue0="<%= itemID %>">
					<bean:define id="needsSeparator" value="true" toScope="request"/>
					<li><wf:activityName processName="process" activityName="ChangeRefundItemClassification" scope="request"/></li>
				</wf:activityLink>

                <logic:empty name="item" property="invoicesFilesSet">
                    <wf:activityLink id='<%= "selectSupplierForItem-" + itemID %>' processName="process" activityName="SelectSupplierForItem" scope="request" paramName0="item" paramValue0="<%= itemID %>">
                        <bean:define id="needsSeparator" value="true" toScope="request"/>
                        <li><wf:activityName processName="process" activityName="SelectSupplierForItem" scope="request"/></li>
                    </wf:activityLink>
                </logic:empty>
			</ul>
			
			<script type="text/javascript">
				<bean:define id="hideOperations" value="true" toScope="request"/>

				<wf:isActive processName="process" activityName="EditRefundItem" scope="request">
					<bean:define id="hideOperations" value="false" toScope="request"/>
				</wf:isActive>
				<wf:isActive processName="process" activityName="EditRefundItemWithMaterial" scope="request">
					<bean:define id="hideOperations" value="false" toScope="request"/>
				</wf:isActive>
				<wf:isActive processName="process" activityName="GenericAssignPayingUnitToItem" scope="request">
					<bean:define id="hideOperations" value="false" toScope="request"/>
				</wf:isActive>
				<wf:isActive processName="process" activityName="CreateRefundInvoice" scope="request">
					<bean:define id="hideOperations" value="false" toScope="request"/>
				</wf:isActive>
				<wf:isActive processName="process" activityName="DistributeRealValuesForPayingUnits" scope="request">
					<bean:define id="hideOperations" value="false" toScope="request"/>
				</wf:isActive>
				<wf:isActive processName="process" activityName="DeleteRefundItem" scope="request">
					<bean:define id="hideOperations" value="false" toScope="request"/>
				</wf:isActive>
				<wf:isActive processName="process" activityName="ChangeRefundItemClassification" scope="request">
					<bean:define id="hideOperations" value="false" toScope="request"/>
				</wf:isActive>
                <wf:isActive processName="process" activityName="SelectSupplierForItem" scope="request">
                    <bean:define id="hideOperations" value="false" toScope="request"/>
                </wf:isActive>
		
				<logic:equal name="hideOperations" value="true">
					$("[name='operations']").hide();
				</logic:equal>
			</script>
		</td>
	</tr>
	<tr>

		<td class="nowrap aleft"><bean:message key="label.refundValue" bundle="EXPENDITURE_RESOURCES"/>:</td>
		<td class="nowrap aright"><fr:view name="item" property="realValue" type="module.finance.util.Money" layout="null-as-label"/></td>
	</tr>
