<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="module.organization.domain.Person"%>
<%@page import="module.mission.domain.MissionSystem"%>

<br/>

<h3>
	<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.person.mission.responsibilities"/>
</h3>
<br/>
<logic:empty name="authorityAccountabilities">
	<p>
		<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.person.mission.responsibilities.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="authorityAccountabilities">
	<table class="tstyle3">
		<tr>
			<th>
				<bean:message key="label.unit" bundle="ORGANIZATION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.mission.authority.type" bundle="MISSION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.mission.authority.beginDate" bundle="MISSION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.mission.authority.endDate" bundle="MISSION_RESOURCES"/>
			</th>
		</tr>
		<logic:iterate id="authorityAccountability" name="authorityAccountabilities" type="module.organization.domain.Accountability">
			<tr>
				<td>
					<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showUnitById" paramId="unitId" paramName="authorityAccountability" paramProperty="parent.externalId">
						<fr:view name="authorityAccountability" property="parent.presentationName"/>
					</html:link>
				</td>
				<td>
					<fr:view name="authorityAccountability" property="accountabilityType.name"/>
				</td>
				<td>
					<fr:view name="authorityAccountability" property="beginDate"/>
				</td>
				<td>
					<logic:present name="authorityAccountability" property="endDate">
						<fr:view name="authorityAccountability" property="endDate"/>
					</logic:present>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
