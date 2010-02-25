<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<bean:define id="workingCapital" name="process" property="workingCapital"/>

<h3>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transactions"/>
</h3>

<logic:empty name="workingCapital" property="workingCapitalTransactions">
	<p>
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transactions.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="workingCapital" property="workingCapitalTransactions">
	<br/>
	<table class="tstyle2">
		<tr>
			<th>
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transaction.number"/>
			</th>
			<th>
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transaction.description"/>
			</th>
			<th>
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transaction.value"/>
			</th>
			<th>
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transaction.accumulatedValue"/>
			</th>
			<th>
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transaction.balance"/>
			</th>
			<th>
				<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.transaction.debt"/>
			</th>
			<th>
			</th>
		</tr>
		<logic:iterate id="workingCapitalTransaction" name="workingCapital" property="sortedWorkingCapitalTransactions">
			<tr>
				<td>
					<fr:view name="workingCapitalTransaction" property="number"/>
				</td>
				<td>
					<fr:view name="workingCapitalTransaction" property="description"/>
				</td>
				<td>
					<fr:view name="workingCapitalTransaction" property="value"/>
				</td>
				<td>
					<fr:view name="workingCapitalTransaction" property="accumulatedValue"/>
				</td>
				<td>
					<fr:view name="workingCapitalTransaction" property="balance"/>
				</td>
				<td>
					<fr:view name="workingCapitalTransaction" property="debt"/>
				</td>
				<td>
					<html:link action="/workingCapital.do?method=viewWorkingCapitalTransaction" paramId="workingCapitalTransactionOid" paramName="workingCapitalTransaction" paramProperty="externalId">
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="link.view"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
