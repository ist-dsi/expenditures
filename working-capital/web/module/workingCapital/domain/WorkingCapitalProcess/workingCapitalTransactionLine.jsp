<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<bean:define id="workingCapitalTransactionOid" type="java.lang.String" name="workingCapitalTransaction" property="externalId"/>

<bean:define id="process" name="workingCapitalTransaction" property="workingCapital.workingCapitalProcess" toScope="request"/>

<td>
	<fr:view name="workingCapitalTransaction" property="number"/>
</td>
<td>
	<logic:notEqual name="workingCapitalTransaction" property="canceledOrRejected" value="true">
		<fr:view name="workingCapitalTransaction" property="description"/>
	</logic:notEqual>
	<logic:equal name="workingCapitalTransaction" property="canceledOrRejected" value="true">
		<del>
			<fr:view name="workingCapitalTransaction" property="description"/>
		</del>
	</logic:equal>
</td>
<td>
	<logic:equal name="workingCapitalTransaction" property="acquisition" value="true">
		<logic:notEmpty name="workingCapitalTransaction" property="workingCapitalAcquisition.approved">
			<img src="<%= request.getContextPath() + "/workingCapital/image/accept.gif" %>">
		</logic:notEmpty>
		<logic:notEmpty name="workingCapitalTransaction" property="workingCapitalAcquisition.rejectedApproval">
			<img src="<%= request.getContextPath() + "/workingCapital/image/incorrect.gif" %>">
		</logic:notEmpty>
		<logic:notPresent name="viewWorkingCapitalTransaction">
			<logic:equal name="workingCapitalTransaction" property="pendingApprovalByUser" value="true">
				<!-- 
				<html:link action="/workingCapital.do?method=viewWorkingCapitalTransaction" paramId="workingCapitalTransactionOid" paramName="workingCapitalTransaction" paramProperty="externalId">
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.ApproveWorkingCapitalAcquisitionActivity"/>
				</html:link>
				 -->
				<wf:activityLink processName="process" activityName="ApproveWorkingCapitalAcquisitionActivity" scope="request" paramName0="workingCapitalTransaction" paramValue0="<%= workingCapitalTransactionOid %>">
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.ApproveWorkingCapitalAcquisitionActivity"/>
				</wf:activityLink>
			</logic:equal>
		</logic:notPresent>
	</logic:equal>
	<logic:equal name="workingCapitalTransaction" property="acquisition" value="false">
	-
	</logic:equal>
</td>
<td>
	<logic:equal name="workingCapitalTransaction" property="acquisition" value="true">
		<logic:notEmpty name="workingCapitalTransaction" property="workingCapitalAcquisition.verified">
			<img src="<%= request.getContextPath() + "/workingCapital/image/accept.gif" %>">
		</logic:notEmpty>
		<logic:notEmpty name="workingCapitalTransaction" property="workingCapitalAcquisition.notVerified">
			<img src="<%= request.getContextPath() + "/workingCapital/image/incorrect.gif" %>">
		</logic:notEmpty>
		<logic:notPresent name="viewWorkingCapitalTransaction">
			<logic:equal name="workingCapitalTransaction" property="pendingVerificationByUser" value="true">
				<html:link action="/workingCapital.do?method=viewWorkingCapitalTransaction" paramId="workingCapitalTransactionOid" paramName="workingCapitalTransaction" paramProperty="externalId">
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="activity.VerifyWorkingCapitalAcquisitionActivity"/>
				</html:link>
			</logic:equal>
		</logic:notPresent>
	</logic:equal>
	<logic:equal name="workingCapitalTransaction" property="acquisition" value="false">
	-
	</logic:equal>
</td>
<td>
	<logic:notEqual name="workingCapitalTransaction" property="canceledOrRejected" value="true">
		<fr:view name="workingCapitalTransaction" property="value"/>
	</logic:notEqual>
	<logic:equal name="workingCapitalTransaction" property="canceledOrRejected" value="true">
		<del>
			<fr:view name="workingCapitalTransaction" property="value"/>
		</del>
	</logic:equal>
</td>
<td>
	<logic:notEqual name="workingCapitalTransaction" property="canceledOrRejected" value="true">
		<fr:view name="workingCapitalTransaction" property="accumulatedValue"/>
	</logic:notEqual>
	<logic:equal name="workingCapitalTransaction" property="canceledOrRejected" value="true">
		<del>
			<fr:view name="workingCapitalTransaction" property="accumulatedValue"/>
		</del>
	</logic:equal>
</td>
<td>
	<logic:notEqual name="workingCapitalTransaction" property="canceledOrRejected" value="true">
		<fr:view name="workingCapitalTransaction" property="balance"/>
	</logic:notEqual>
	<logic:equal name="workingCapitalTransaction" property="canceledOrRejected" value="true">
		<del>
			<fr:view name="workingCapitalTransaction" property="balance"/>
		</del>
	</logic:equal>
</td>
<td>
	<logic:notEqual name="workingCapitalTransaction" property="canceledOrRejected" value="true">
		<fr:view name="workingCapitalTransaction" property="debt"/>
	</logic:notEqual>
	<logic:equal name="workingCapitalTransaction" property="canceledOrRejected" value="true">
		<del>
			<fr:view name="workingCapitalTransaction" property="debt"/>
		</del>
	</logic:equal>
</td>