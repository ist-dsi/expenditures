<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:notEmpty name="simplifiedCounters-priority">
	<table>
		<logic:iterate id="counter" name="simplifiedCounters-priority">
			<tr>
				<bean:define id="state" name="counter" property="countableObject" />
				<td><fr:view name="counter" property="countableObject" /></td>
				<td class="aright">
				<bean:define id="showResponsabilities" value="false" toScope="request" />
				<logic:present name="person">
					<logic:notEmpty name="person" property="authorizations">
						<bean:define id="showResponsabilities" value="true" toScope="request" />
					</logic:notEmpty>
				</logic:present>
				<html:link
					page="<%="/search.do?method=searchJump&searchProcessValue=ACQUISITIONS&hasAvailableAndAccessibleActivityForUser=true&showPriorityOnly=true&responsibleUnitSetOnly="
									    + showResponsabilities.toString() + "&acquisitionProcessStateType=" + state%>">
					<fr:view name="counter" property="value" />
				</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
<logic:empty name="simplifiedCounters-priority">
	<p><em><bean:message key="label.no.pendingProcesses" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
</logic:empty>
