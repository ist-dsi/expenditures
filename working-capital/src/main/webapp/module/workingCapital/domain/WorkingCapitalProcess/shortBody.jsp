<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="workingCapital" name="process" property="workingCapital"/>

<div class="infobox mtop1 mbottom1">
	<table>
		<tr>
			<td>
				<bean:message key="label.module.workingCapital" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td class="aleft">
				<bean:define id="unitOID" name="workingCapital" property="unit.externalId" type="java.lang.String"/>
				<html:link styleClass="secondaryLink" page="<%= "/expenditureTrackingOrganization.do?method=viewOrganization&unitOid=" + unitOID%>" target="_blank">
					<bean:write name="workingCapital" property="unit.presentationName"/>
				</html:link>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.module.workingCapital.requestingDate" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td class="aleft">
				<fr:view name="workingCapital" property="workingCapitalInitialization.requestCreation"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.module.workingCapital.requester" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
			</td>
			<td class="aleft">
				<bean:write name="workingCapital" property="workingCapitalInitialization.requestor.name"/>
			</td>
		</tr>
		<logic:present name="workingCapital" property="movementResponsible">
			<tr>
				<td>
					<bean:message key="label.module.workingCapital.movementResponsible" bundle="WORKING_CAPITAL_RESOURCES" />:&nbsp;
				</td>
				<td class="aleft">
					<bean:write name="workingCapital" property="movementResponsible.name"/>
				</td>
			</tr>
		</logic:present>
	</table>
</div>
