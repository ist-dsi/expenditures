<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="module.organization.domain.OrganizationalModel"%>
<%@page import="org.fenixedu.bennu.core.domain.Bennu"%>

<%--
<logic:equal name="process" property="mission.readyToHaveAssociatedPaymentProcesses" value="true">
 --%>
	<h4>
		<bean:message bundle="MISSION_RESOURCES" key="label.mission.associated.payment.processes"/>
	</h4>
	<logic:empty name="process" property="paymentProcess">
		<bean:message bundle="MISSION_RESOURCES" key="label.mission.associated.payment.processes.none"/>
	</logic:empty>
	<logic:notEmpty name="process" property="paymentProcess">
		<table class="tstyle3 mvert1 width100pc tdmiddle punits">
			<tr>
				<th>
					<bean:message bundle="MISSION_RESOURCES" key="label.mission.associated.payment.processes.identification"/>
				</th>
				<th>
					<bean:message bundle="MISSION_RESOURCES" key="label.mission.associated.payment.processes.type"/>
				</th>
				<th>
					<bean:message bundle="MISSION_RESOURCES" key="label.mission.associated.payment.processes.state"/>
				</th>
				<th>
					<bean:message bundle="MISSION_RESOURCES" key="label.mission.associated.payment.processes.totalValue"/>
				</th>
			</tr>
			<logic:iterate id="paymentProcess" name="process" property="paymentProcess">
				<tr>
					<td>
						<html:link page="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="paymentProcess" paramProperty="externalId">
							<bean:write name="paymentProcess" property="processNumber"/>
						</html:link>
					</td>
					<td>
						<bean:write name="paymentProcess" property="localizedName"/>
					</td>
					<td>
						<logic:equal name="paymentProcess" property="active" value="true">
							<logic:equal name="paymentProcess" property="currentProcessState.inFinalStage" value="true">
								<span style="background-color: #CEF6CE;">
									<bean:write name="paymentProcess" property="processStateDescription"/>
								</span>
							</logic:equal>
							<logic:notEqual name="paymentProcess" property="currentProcessState.inFinalStage" value="true">
								<span class="error0">
									<bean:write name="paymentProcess" property="processStateDescription"/>
								</span>
							</logic:notEqual>
						</logic:equal>
						<logic:notEqual name="paymentProcess" property="active" value="true">
							<bean:write name="paymentProcess" property="processStateDescription"/>
						</logic:notEqual>
					</td>
					<td>
						<fr:view name="paymentProcess" property="totalValue"/>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
	<br/>
<%-- 
</logic:equal>
 --%>
