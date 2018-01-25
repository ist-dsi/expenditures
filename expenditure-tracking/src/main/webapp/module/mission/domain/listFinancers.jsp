<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

<table class="tstyle3 mvert1 width100pc tdmiddle punits">
	<tr>	
		<th class="aleft">
			<bean:message bundle="MISSION_RESOURCES" key="label.mission.financers"/>
		</th>
		<th></th>
		<logic:notEmpty name="process" property="mission.financerSet">
			<th class="acenter">
				<bean:message bundle="MISSION_RESOURCES" key="label.mission.financer.accounting.unit"/>
			</th>
			<th class="acenter">
				<bean:message bundle="MISSION_RESOURCES" key="label.mission.financer.fundAllocationId"/>
			</th>
			<th class="acenter">
				<bean:message bundle="EXPENDITURE_RESOURCES" key="label.commitmentNumbers"/>
			</th>
			<th class="aright">
				<bean:message bundle="MISSION_RESOURCES" key="label.mission.financer.amount"/>
			</th>
		</logic:notEmpty>
	</tr>
	<logic:empty name="process" property="mission.financerSet">
		<tr>
			<td class="aleft">
				<i><strong>
					<bean:message bundle="MISSION_RESOURCES" key="label.mission.financers.none"/>
				</strong></i>
			</td>
		</td>
	</logic:empty>
	<logic:iterate id="financer" name="process" property="mission.financerSet">
		<tr>
			<td class="aleft">
				<logic:present name="financer" property="unit">
					<html:link styleClass="secondaryLink" page="/expenditureTrackingOrganization.do?method=viewOrganization" paramId="unitOid" paramName="financer" paramProperty="unit.externalId">
						<fr:view name="financer" property="unit.presentationName"/>
					</html:link>
				</logic:present>
			</td>
			<td> 
				
				<bean:define id="financerOID" name="financer" property="externalId" type="java.lang.String"/>
				<wf:activityLink processName="process" activityName="RemoveFinancerActivity" scope="request" paramName0="financer" paramValue0="<%= financerOID %>">
					<bean:message bundle="MISSION_RESOURCES" key="link.remove"/>
				</wf:activityLink>
			</td>
			<td class="acenter">
				<logic:present name="financer" property="unit">
					<logic:present name="financer" property="accountingUnit">
						<html:link styleClass="secondaryLink" page="/expenditureTrackingOrganization.do?method=viewAccountingUnit" paramId="accountingUnitOid" paramName="financer" paramProperty="accountingUnit.externalId">
							<fr:view name="financer" property="accountingUnit.name"/>
						</html:link>
						<br/>
						<wf:activityLink processName="process" activityName="ChangeAccountingUnitActivity" scope="request" paramName0="financer" paramValue0="<%= financerOID %>">
							<bean:message bundle="MISSION_RESOURCES" key="link.change"/>
						</wf:activityLink>
					</logic:present>
				</logic:present>
			</td>
			<td class="acenter">
				<logic:present name="financer" property="projectFundAllocationId">
					<logic:notEmpty name="financer" property="projectFundAllocationId">
						<logic:notEqual name="financer" property="projectFundAllocationId" value="null">
							[
								<bean:message bundle="MISSION_RESOURCES" key="label.mission.financer.fundAllocationId.mgp.prefix"/>
								<bean:write name="financer" property="projectFundAllocationId"/>
							]
						</logic:notEqual>
					</logic:notEmpty>
				</logic:present>
				<logic:present name="financer" property="fundAllocationId">
					<logic:notEmpty name="financer" property="fundAllocationId">
						<logic:notEqual name="financer" property="fundAllocationId" value="null">
							[
								<bean:message bundle="MISSION_RESOURCES" key="label.mission.financer.fundAllocationId.giaf.prefix"/>
								<bean:write name="financer" property="fundAllocationId"/>
							]
						</logic:notEqual>
					</logic:notEmpty>
				</logic:present>
			</td>
			<td class="acenter">
				<bean:write name="financer" property="commitmentNumber"/>
<%--
				<wf:activityLink processName="process" activityName="UnCommitFundsActivity" scope="request" paramName0="missionFinancer" paramValue0="<%= financerOID %>">
					<br/>
					<bean:message bundle="MISSION_RESOURCES" key="link.remove"/>
				</wf:activityLink>
 --%>
			</td>
			<td class="aright">
				<fr:view name="financer" property="amount"/>
			</td>
		</tr>
	</logic:iterate>
</table>

<wf:activityLink processName="process" activityName="AddFinancerActivity" scope="request">
	<bean:message bundle="MISSION_RESOURCES" key="activity.AddFinancerActivity"/>
</wf:activityLink>
