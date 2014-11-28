<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page import="pt.ist.expenditureTrackingSystem.util.PhotoTool"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/chart" prefix="chart" %>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/moment.min.js"%>">
</script>
<script type="text/javascript">

var currentDate = moment('2013-03-27','YYYY-MM-DD');

function inactiveEntities(func) {
	$('.filterableInactiveAccountabilityTable').each(function(index) {
		$(this).children().children().each(function(tableRowIndex) {
			if(tableRowIndex > 0) {
				var endDateStr = $($(this).find('.endDateColumn')).children().html().trim();
				if(endDateStr.length > 0 && !moment(endDateStr,'DD-MM-YYYY').isAfter(currentDate)) {
					func($(this));
				}
			}
		});
	});
}

function toggleInactive(elem){
	if( $('#toggleInactiveChbox').attr('checked')) {
		elem.show();
	} else {
		elem.hide();
	}
}

function highlightInactive(elem){
	elem.find('.endDateColumn').children().css({'background-color': '#fcfbad','padding':'3px'});
}
</script>
<script type="text/javascript">
$(function() {
	inactiveEntities(toggleInactive);
	inactiveEntities(highlightInactive);
});
</script>

<h2>
	<fr:view name="unit" property="presentationName"/>
</h2>

<div class="infobox">
	<table align="center" style="width: 100%; text-align: center;">
		<tr>
			<td align="center">
				<chart:orgChart id="party" name="chart" type="java.lang.Object">
					<div class="orgTBox orgTBoxLight">
						<%
							if (party == request.getAttribute("unit")) {
						%>
								<strong>
									<bean:write name="party" property="partyName"/>
								</strong>
						<%			    
							} else {
						%>
								<html:link page="/missionOrganization.do?method=showUnitById" paramId="unitId" paramName="party" paramProperty="externalId" styleClass="secondaryLink">
									<bean:write name="party" property="partyName"/>
								</html:link>
						<%			    
							}
						%>
					</div>
				</chart:orgChart>
			</td>
		</tr>
	</table>
</div>

<% if (ExpenditureTrackingSystem.isManager()) { %>
	<logic:notPresent name="unit" property="missionSystemFromUnitWithResumedAuthorizations">
		<p>
			<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.unitWithResumedAuthorizations.not"/>
			<html:link page="/missionOrganization.do?method=addUnitWithResumedAuthorizations" paramId="unitId" paramName="unit" paramProperty="externalId">
				<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.unitWithResumedAuthorizations.add.summary"/>
			</html:link>
		</p>
	</logic:notPresent>
	<logic:present name="unit" property="missionSystemFromUnitWithResumedAuthorizations">
		<p style="color: green;">
			<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.unitWithResumedAuthorizations"/>
			<html:link page="/missionOrganization.do?method=removeUnitWithResumedAuthorizations" paramId="unitId" paramName="unit" paramProperty="externalId">
				<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.unitWithResumedAuthorizations.remove.summary"/>
			</html:link>
		</p>
	</logic:present>
<% } %>

<html:link page="/missionOrganization.do?method=viewPresences" paramId="unitId" paramName="unit" paramProperty="externalId">
	<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.view.member.presence"/>
</html:link>
<html:messages id="message" message="true">
	<span class="error0"> <bean:write name="message" /> </span>
</html:messages>
<br/>
<bean:define id="urlByMonth" type="java.lang.String">/vaadinContext.do?method=forwardToVaadin#MissionParticipationMap?unit=<bean:write name="unit" property="externalId"/></bean:define>
<html:link page="<%= urlByMonth %>">
	<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.view.member.presence.by.month"/>
</html:link>

<div style="background-color: rgb(245, 245, 245);
border-bottom-color: rgb(221, 221, 221);
border-bottom-style: solid;
border-bottom-width: 1px;
border-collapse: collapse;
border-left-color: rgb(68, 68, 68);
border-left-style: none;
border-left-width: 0px;
border-right-color: rgb(68, 68, 68);
border-right-style: none;
border-right-width: 0px;
border-top-color: rgb(221, 221, 221);
border-top-style: solid;
border-top-width: 1px;
color: rgb(68, 68, 68);
padding: 4px 0px 4px 4px;
margin-top:15px;
font-weight: bold;
width: 719px;
">
<label for="toggleInactiveChbox">
Mostrar pessoas inactivas </label>
<input style="vertical-align: bottom;" type="checkbox" name="toggleInactive" id="toggleInactiveChbox" onclick="inactiveEntities(toggleInactive)">
</div>


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
	<table class="tstyle3 filterableInactiveAccountabilityTable">
		<tr>
			<th>
			</th>
			<th>
				<bean:message key="label.person" bundle="ORGANIZATION_RESOURCES"/>
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
			<th>
			</th>
		</tr>
		<logic:iterate id="authorityAccountability" name="authorityAccountabilities" type="module.organization.domain.Accountability">
			<% if (authorityAccountability.getChild().isPerson()) { %>
			<tr>
				<td>
					<bean:define id="username" type="java.lang.String" name="authorityAccountability" property="child.user.username"/>
					<img src="<%= PhotoTool.getPhotoUrl(username, request.getContextPath()) %>"/>
				</td>
				<td>
					<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="authorityAccountability" paramProperty="child.externalId">
						<fr:view name="authorityAccountability" property="child.presentationName"/>
					</html:link>
				</td>
				<td>
					<logic:notPresent name="authorityAccountability" property="functionDelegationDelegator">
						<fr:view name="authorityAccountability" property="accountabilityType.name"/>
					</logic:notPresent>
					<logic:present name="authorityAccountability" property="functionDelegationDelegator">
						<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showDelegationsForAuthorization" paramId="authorizationId" paramName="authorityAccountability" paramProperty="functionDelegationDelegator.accountabilityDelegator.externalId">
							<fr:view name="authorityAccountability" property="accountabilityType.name"/>
							<br/>
							<bean:message key="label.delegation.by" bundle="MISSION_RESOURCES"/>
							<fr:view name="authorityAccountability" property="functionDelegationDelegator.accountabilityDelegator.child.presentationName"/>
						</html:link>
					</logic:present>
				</td>
				<td>
					<fr:view name="authorityAccountability" property="beginDate"/>
				</td>
				<td class="endDateColumn">
					<span>
					<logic:present name="authorityAccountability" property="endDate">
						<fr:view name="authorityAccountability" property="endDate"/>
					</logic:present>
					</span>
				</td>
				<td>
					<html:link page="/missionOrganization.do?method=showDelegationsForAuthorization" paramId="authorizationId" paramName="authorityAccountability" paramProperty="externalId">
						<bean:size id="numberDelegations" name="authorityAccountability" property="functionDelegationDelegated"/>
						<bean:message key="label.delegations" bundle="MISSION_RESOURCES" arg0="<%= numberDelegations.toString() %>"/>
					</html:link>
				</td>
			</tr>
			<% } %>
		</logic:iterate>
	</table>
</logic:notEmpty>

<br/>

<h3>
	<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.unit.members"/>
</h3>
<br/>
<logic:empty name="workerAccountabilities">
	<p>
		<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.unit.members.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="workerAccountabilities">
	<table class="tstyle3 filterableInactiveAccountabilityTable">
		<tr>
			<th>
			</th>
			<th>
				<bean:message key="label.person" bundle="ORGANIZATION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.mission.member.type" bundle="MISSION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.mission.authority.beginDate" bundle="MISSION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.mission.authority.endDate" bundle="MISSION_RESOURCES"/>
			</th>
		</tr>
		<logic:iterate id="authorityAccountability" name="workerAccountabilities" type="module.organization.domain.Accountability">
			<% if (authorityAccountability.getChild().isPerson()) { %>
			<tr>
				<td>
					<bean:define id="username" type="java.lang.String" name="authorityAccountability" property="child.user.username"/>
					<img src="<%= PhotoTool.getPhotoUrl(username, request.getContextPath()) %>"/>
				</td>
				<td>
					<html:link styleClass="secondaryLink" page="/missionOrganization.do?method=showPersonById" paramId="personId" paramName="authorityAccountability" paramProperty="child.externalId">
						<fr:view name="authorityAccountability" property="child.presentationName"/>
					</html:link>
				</td>
				<td>
					<fr:view name="authorityAccountability" property="accountabilityType.name"/>
				</td>
				<td>
					<fr:view name="authorityAccountability" property="beginDate"/>
				</td>
				<td class="endDateColumn">
					<span>
					<logic:present name="authorityAccountability" property="endDate">
						<fr:view name="authorityAccountability" property="endDate"/>
					</logic:present>
					</span>
				</td>
			</tr>
			<% } %>
		</logic:iterate>
	</table>
</logic:notEmpty>
