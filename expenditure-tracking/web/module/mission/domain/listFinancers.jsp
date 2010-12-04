<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

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
					<logic:present name="financer" property="unit.accountingUnit">
						<html:link styleClass="secondaryLink" page="/expenditureTrackingOrganization.do?method=viewAccountingUnit" paramId="accountingUnitOid" paramName="financer" paramProperty="unit.accountingUnit.externalId">
							<fr:view name="financer" property="unit.accountingUnit.name"/>
						</html:link>
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
			<td class="aright">
				<fr:view name="financer" property="amount"/>
			</td>
		</tr>
	</logic:iterate>
</table>

<wf:activityLink processName="process" activityName="AddFinancerActivity" scope="request">
	<bean:message bundle="MISSION_RESOURCES" key="activity.AddFinancerActivity"/>
</wf:activityLink>
