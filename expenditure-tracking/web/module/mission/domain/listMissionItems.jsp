<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>
							
<%@page import="myorg.presentationTier.servlets.filters.contentRewrite.ContentContextInjectionRewriter"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>
			
<bean:size id="transportationItemsSize" name="process" property="mission.transportationItems"/>
<bean:size id="personelExpenseItemsSize" name="process" property="mission.personelExpenseItems"/>
<bean:size id="accommodationItemsSize" name="process" property="mission.accommodationItems"/>
<bean:size id="otherItemsSize" name="process" property="mission.otherItems"/>

<h3 class="mtop15 mbottom1"><bean:message key="label.mission.items" bundle="MISSION_RESOURCES"/></h3>

<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.BLOCK_HAS_CONTEXT_PREFIX %>

<table class="tstyle3 thleft tdleft mbottom2" style="width: 100%;" id="itemResume">
	<tr>
		<th><bean:message key="label.mission.item" bundle="MISSION_RESOURCES"/></th>
		<th><bean:message key="label.mission.item.type" bundle="MISSION_RESOURCES"/></th>
		<th><bean:message key="label.itemDescription" bundle="MISSION_RESOURCES"/></th>
		<th><bean:message key="label.mission.value" bundle="MISSION_RESOURCES"/></th>
	</tr>
	
	<logic:iterate id="item" indexId="counter1" name="process" property="mission.transportationItems">
		<bean:define id="itemOID" name="item" property="externalId" type="java.lang.String"/>
		<tr>
			<td>
			<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= "#item" + itemOID %>">
				<bean:message key="label.mission.item" bundle="MISSION_RESOURCES"/> <%= counter1 + 1 %>
			</a>
			</td>
			<td><fr:view name="item" property="localizedName"/></td>
			<td><fr:view name="item" property="itemDescription"/></td>
			<td><fr:view name="item" property="value"/></td>
		</tr>
	</logic:iterate>
	
	<logic:iterate id="item" indexId="counter2" name="process" property="mission.personelExpenseItems">
		<bean:define id="itemOID" name="item" property="externalId" type="java.lang.String"/>
		<tr>
			<td>
			<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= "#item" + itemOID %>">
				<bean:message key="label.mission.item" bundle="MISSION_RESOURCES"/> <%= transportationItemsSize + counter2 + 1 %>
			</a>
		 	</td>
			<td><fr:view name="item" property="localizedName"/></td>
			<td><fr:view name="item" property="itemDescription"/></td>
			<td><fr:view name="item" property="value"/></td>
		</tr>
	</logic:iterate>
	
	<logic:iterate id="item" indexId="counter3" name="process" property="mission.accommodationItems">
		<tr>
			<td>
			<bean:define id="itemOID" name="item" property="externalId" type="java.lang.String"/>
			<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= "#item" + itemOID %>">
				<bean:message key="label.mission.item" bundle="MISSION_RESOURCES"/> <%= transportationItemsSize +  personelExpenseItemsSize + counter3 + 1%>
			</a>
			</td>
			<td><fr:view name="item" property="localizedName"/></td>
			<td><fr:view name="item" property="itemDescription"/></td>
			<td><fr:view name="item" property="value"/></td>
		</tr>
	</logic:iterate>
	
	<logic:iterate id="item" indexId="counter4" name="process" property="mission.otherItems">
		<bean:define id="itemOID" name="item" property="externalId" type="java.lang.String"/>
		<tr>
			<td>
			<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= "#item" + itemOID %>">
				<bean:message key="label.mission.item" bundle="MISSION_RESOURCES"/> <%= transportationItemsSize +  personelExpenseItemsSize +  accommodationItemsSize + counter4 + 1%>
			</a>
			</td>
			<td><fr:view name="item" property="localizedName"/></td>
			<td><fr:view name="item" property="itemDescription"/></td>
			<td><fr:view name="item" property="value"/></td>
		</tr>
	</logic:iterate>
	
</table>

<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>

<wf:activityLink processName="process" activityName="AddItemActivity" scope="request">
	<bean:message bundle="MISSION_RESOURCES" key="activity.AddItemActivity"/>
</wf:activityLink>

<br/>
<br/>
<br/>

<logic:equal name="process" property="mission.hasVehicleItem" value="true">
	<logic:equal name="process" property="underConstruction" value="true">

			<div class="highlightBox">
				<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.domain.VehiclItem.requirements.message"/>
			</div>

	</logic:equal>
</logic:equal>

<bean:define id="totalItems" toScope="request" value="<%= Integer.valueOf(transportationItemsSize + personelExpenseItemsSize +  accommodationItemsSize + otherItemsSize).toString() %>"/>

<bean:define id="missionItemTypeHeaderKey" toScope="request" value="label.mission.items.transportation"/>
<bean:define id="missionItems" toScope="request" name="process" property="mission.transportationItems"/>
<jsp:include page="viewMissionItems.jsp"/>

<bean:define id="shift" value="<%= Integer.valueOf(transportationItemsSize).toString() %>" toScope="request"/>
<bean:define id="missionItemTypeHeaderKey" toScope="request" value="label.mission.items.personel.expense"/>
<bean:define id="missionItems" toScope="request" name="process" property="mission.personelExpenseItems"/>
<jsp:include page="viewMissionItems.jsp"/>

<bean:define id="shift" value="<%= Integer.valueOf(transportationItemsSize + personelExpenseItemsSize).toString() %>" toScope="request"/>
<bean:define id="missionItemTypeHeaderKey" toScope="request" value="label.mission.items.accomodation"/>
<bean:define id="missionItems" toScope="request" name="process" property="mission.accommodationItems"/>
<jsp:include page="viewMissionItems.jsp"/>

<bean:define id="shift" value="<%= Integer.valueOf(transportationItemsSize + personelExpenseItemsSize + accommodationItemsSize).toString() %>" toScope="request"/>
<bean:define id="missionItemTypeHeaderKey" toScope="request" value="label.mission.items.other"/>
<bean:define id="missionItems" toScope="request" name="process" property="mission.otherItems"/>
<jsp:include page="viewMissionItems.jsp"/>
