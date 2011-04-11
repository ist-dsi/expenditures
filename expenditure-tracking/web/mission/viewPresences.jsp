<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/chart.tld" prefix="chart" %>

<bean:define id="unit" name="searchUnitMemberPresence" property="unit" type="module.organization.domain.Unit"/>
<h2>
	<fr:view name="searchUnitMemberPresence" property="unit.presentationName"/>
</h2>

<div class="infobox">
	<fr:edit id="searchUnitMemberPresence" name="searchUnitMemberPresence"
			action="/missionOrganization.do?method=viewPresences">
		<fr:schema type="module.mission.domain.util.SearchUnitMemberPresence" bundle="MISSION_RESOURCES">
			<fr:slot name="day" key="label.date" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
			<fr:slot name="includeSubUnits" key="label.includeSubUnits"/>
			<fr:slot name="onMission" key="label.onMission"/>
			<fr:slot name="accountabilityTypes" layout="option-select" key="label.mission.participants.accountabilityTypes" bundle="MISSION_RESOURCES">
        		<fr:property name="providerClass" value="module.mission.presentationTier.provider.MissionAccountabilityTypesRequireingAuthorizationProvider" />
	        	<fr:property name="eachSchema" value="accountabilityType-name"/>
    	    	<fr:property name="eachLayout" value="values"/>
        		<fr:property name="classes" value="nobullet"/>
				<fr:property name="saveOptions" value="true"/>
				<fr:property name="sortBy" value="name"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form listInsideClear" />
			<fr:property name="columnClasses" value="width100px,,tderror" />
		</fr:layout>
		<fr:destination name="cancel" path='<%="/missionOrganization.do?method=showUnitById&&unitId=" + unit.getExternalId()%>' />
	</fr:edit>
</div>

<br/>

<logic:present name="people">
	<logic:empty name="people">
		<logic:equal name="searchUnitMemberPresence" property="onMission" value="true">
			<bean:message key="label.day.on.mission.none" bundle="MISSION_RESOURCES"/>
		</logic:equal>
		<logic:notEqual name="searchUnitMemberPresence" property="onMission" value="true">
			<bean:message key="label.day.not.on.mission.none" bundle="MISSION_RESOURCES"/>
		</logic:notEqual>
	</logic:empty>
	<logic:notEmpty name="people">
		<table class="tstyle3" style="text-align:left;">
			<tr>
				<th style="color: graytext;">
				</th>
				<th>
					<bean:message key="label.person" bundle="ORGANIZATION_RESOURCES"/>
				</th>
				<th>
				</th>
			</tr>
			<logic:iterate id="person" name="people">
				<tr>
					<td>
						<bean:define id="url" type="java.lang.String">https://fenix.ist.utl.pt/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="person" property="user.username"/></bean:define>
						<img src="<%= url %>">
					</td>
					<td>
						<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="person" paramProperty="externalId">
							<fr:view name="person" property="presentationName"/>
						</html:link>
					</td>
					<td>
						<html:link page="/missionOrganization.do?method=searchMission" paramId="personId" paramName="person" paramProperty="externalId">
							<bean:message key="label.search.missions" bundle="MISSION_RESOURCES"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>