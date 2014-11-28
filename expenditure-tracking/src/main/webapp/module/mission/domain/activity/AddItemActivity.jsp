<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="pt.ist.fenixWebFramework.renderers.utils.RenderUtils"%>

<bean:define id="information" name="information" type="module.mission.domain.activity.ItemActivityInformation"/>
<bean:define id="process" name="information" property="process"/>

<h3>
	<bean:message bundle="MISSION_RESOURCES" key="label.mission.item.type"/>
</h3>

	<table class="tstyle2">
		<tr>
			<th><bean:message bundle="MISSION_RESOURCES" key="label.mission.items.transportation"/></th>
			<th><bean:message bundle="MISSION_RESOURCES" key="label.mission.items.personel.expense"/></th>
			<th><bean:message bundle="MISSION_RESOURCES" key="label.mission.items.accomodation"/></th>
			<th><bean:message bundle="MISSION_RESOURCES" key="label.mission.items.other"/></th>
		</tr>
		<tr>
			<td valign="top" style="text-align: left;">
				<ul>
					<li>
						<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.PlaneItem"
								paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.PlaneItem"/>
						</html:link>
					</li>
					<li>
						<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.BusItem"
								paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.BusItem"/>
						</html:link>
					</li>
					<li>
						<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.TrainItem"
								paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.TrainItem"/>
						</html:link>
					</li>
					<li>
						<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.BoatItem"
								paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.BoatItem"/>
						</html:link>
					</li>
					<li>
						<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.RentedVehiclItem"
								paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.RentedVehiclItem"/>
						</html:link>
					</li>
					<li>
						<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.PersonalVehiclItem"
								paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.PersonalVehiclItem"/>
						</html:link>
					</li>
					<li>
						<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.OtherTransportationItem"
								paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.OtherTransportationItem"/>
						</html:link>
					</li>
				</ul>
			</td>
			<td valign="top" style="text-align: left;">
				<logic:equal name="process" property="personelExpenseItemsAvailable" value="true">
					<ul>
						<li>
							<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.FullPersonelExpenseItem"
									paramId="processId" paramName="process" paramProperty="externalId">
								<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.FullPersonelExpenseItem"/>
							</html:link>
						</li>
						<logic:equal name="process" property="class.name" value="module.mission.domain.ForeignMissionProcess">
							<li>
								<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.WithAccommodationPersonelExpenseItem"
										paramId="processId" paramName="process" paramProperty="externalId">
									<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.WithAccommodationPersonelExpenseItem"/>
								</html:link>
							</li>
							<li>
								<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.WithAccommodationAndOneMealPersonelExpenseItem"
										paramId="processId" paramName="process" paramProperty="externalId">
									<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.WithAccommodationAndOneMealPersonelExpenseItem"/>
								</html:link>
							</li>
							<li>
								<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.WithAccommodationAndTwoMealsPersonelExpenseItem"
										paramId="processId" paramName="process" paramProperty="externalId">
									<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.WithAccommodationAndTwoMealsPersonelExpenseItem"/>
								</html:link>
							</li>
						</logic:equal>
						<li>
							<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.NoPersonelExpenseItem"
									paramId="processId" paramName="process" paramProperty="externalId">
								<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.NoPersonelExpenseItem"/>
							</html:link>
						</li>
						<li>
							<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.OtherPersonelExpenseItem"
									paramId="processId" paramName="process" paramProperty="externalId">
								<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.OtherPersonelExpenseItem"/>
							</html:link>
						</li>
					</ul>
				</logic:equal>
				<logic:notEqual name="process" property="personelExpenseItemsAvailable" value="true">
					<p align="center">
						<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.AccommodationItem.not.available"/>
					</p>
				</logic:notEqual>
			</td>
			<td valign="top" style="text-align: left;">
				<logic:equal name="process" property="areAccomodationItemsAvailable" value="true">
					<ul>
						<li>
							<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.AccommodationItem"
									paramId="processId" paramName="process" paramProperty="externalId">
								<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.AccommodationItem"/>
							</html:link>
						</li>
					</ul>
				</logic:equal>
				<logic:notEqual name="process" property="areAccomodationItemsAvailable" value="true">
					<p align="center">
						<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.AccommodationItem.not.available"/>
					</p>
				</logic:notEqual>
			</td>
			<td valign="top" style="text-align: left;">
				<ul>
					<li>
						<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.ConferenceItem"
								paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.ConferenceItem"/>
						</html:link>
					</li>
					<li>
						<html:link page="/missionProcess.do?method=addMissionItemSelectType&amp;missionItemType=module.mission.domain.OtherMissionItem"
								paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.OtherMissionItem"/>
						</html:link>
					</li>
				</ul>
			</td>
		</tr>
	</table>

<br/>

<logic:present name="information" property="missionItem">
<logic:present name="information" property="concreteMissionItemType">
	<h3>
		<bean:define id="labelKey" type="java.lang.String">label.<bean:write name="information" property="concreteMissionItemType.name"/></bean:define>
		<bean:message bundle="MISSION_RESOURCES" key="<%= labelKey %>"/>
	</h3>

		<logic:equal name="information" property="concreteMissionItemType.name" value="module.mission.domain.RentedVehiclItem">
			<div class="highlightBox">
				<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.VehiclItem.requirements.message"/>
			</div>
		</logic:equal>
		<logic:equal name="information" property="concreteMissionItemType.name" value="module.mission.domain.PersonalVehiclItem">
			<div class="highlightBox">
				<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.VehiclItem.requirements.message"/>
			</div>
		</logic:equal>

	<logic:equal name="information" property="concreteMissionItemType.name" value="module.mission.domain.AccommodationItem">
		<div class="highlightBox">
			<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.AccommodationItem.requirements.message"/>
		</div>
	</logic:equal>
	<fr:form action="/missionProcess.do">
		<html:hidden  property="method" value="addMissionItem"/>
		<bean:define id="processId" type="java.lang.String" name="process" property="externalId"/>
		<html:hidden property="processId" value="<%= processId %>"/>

		<logic:present name="information" property="missionItem">
			<bean:define id="schemaName" type="java.lang.String" name="information" property="missionItem.class.name"/>
			<fr:edit id="missionItem" name="information" property="missionItem" schema="<%= schemaName %>">
				<fr:layout name="tabular">
					<fr:property name="classes" value="form listInsideClear" />
					<fr:property name="columnClasses" value=",,tderror" />
				</fr:layout>
				<fr:destination name="invalid" path="/missionProcess.do?method=activityInformationPostback"/>
			</fr:edit>

			<bean:size id="numberPeople" name="information" property="process.mission.participantes"/>
			<logic:notEqual name="numberPeople" value="1">
				<h4>
					<bean:message bundle="MISSION_RESOURCES" key="label.mission.item.participants"/>
				</h4>

				<fr:edit id="information.participants" name="information">
					<fr:schema type="module.mission.domain.activity.ItemActivityInformation" bundle="MISSION_RESOURCES">
						<fr:slot name="people" layout="option-select" key="label.mission.item.people">
							<fr:property name="eachSchema" value="person-name"/>
							<fr:property name="eachLayout" value="values-dash"/>
					        <fr:property name="providerClass" value="module.mission.presentationTier.provider.MissionItemParticipantProvider" />
					        <fr:property name="classes" value="nobullet noindent"/>
					        <fr:property name="sortBy" value="user.name"/>
					    </fr:slot>
					<% if(information.getMissionItem().isVehicleItem()) { %>
						<fr:slot name="driver" layout="radio-select" key="label.mission.item.driver" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
							<fr:property name="eachSchema" value="person-name"/>
							<fr:property name="eachLayout" value="values-dash"/>
						    <fr:property name="providerClass" value="module.mission.presentationTier.provider.MissionItemParticipantProvider" />
						    <fr:property name="classes" value="nobullet noindent"/>
						    <fr:property name="sortBy" value="user.name"/>
						</fr:slot>
					<% } %>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="form listInsideClear" />
						<fr:property name="columnClasses" value="width100px,,tderror" />
					</fr:layout>
				</fr:edit>
			</logic:notEqual>
		</logic:present>

		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>

		<fr:edit id="information" name="information" schema="module.mission.domain.activity.ItemActivityInformation.empty">
		</fr:edit>
	</fr:form>
</logic:present>
</logic:present>
