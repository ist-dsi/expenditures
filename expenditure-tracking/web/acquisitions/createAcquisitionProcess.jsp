<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@page import="java.util.ResourceBundle"%>
<%@page import="myorg.util.BundleUtil"%>
<%@page import="module.mission.domain.MissionSystem"%>

<h2><bean:message key="acquisitionProcess.title.createAcquisitionRequest" bundle="ACQUISITION_RESOURCES"/></h2>

<div class="infobox">
	<bean:message key="acquisitionProcess.message.note" bundle="ACQUISITION_RESOURCES" />
</div>

<html:messages id="message" message="true" bundle="MISSION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<p class="mtop15 mbottom05"><strong><fr:view name="acquisitionProcessBean" property="classification"/></strong></p>

<% if (MissionSystem.getInstance().hasAnyMissions()) { %>
	<fr:form id="selectMissionBeanForm" action="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcess">
		<fr:edit id="selectMissionBean" name="acquisitionProcessBean">
			<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean" bundle="ACQUISITION_RESOURCES">
   				<fr:slot name="isForMission" key="label.aquisition.process.create.is.for.mission" layout="radio-postback">
    				<fr:property name="classes" value="liinline"/>
   				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form"/>
				<fr:property name="columnClasses" value=",,tderror"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
<% } %>

<fr:form id="createForm" action="/acquisitionSimplifiedProcedureProcess.do?method=createNewAcquisitionProcess">
	<logic:equal name="acquisitionProcessBean" property="isForMission" value="true">
		<fr:edit id="acquisitionProcessBeanMissionProcess" name="acquisitionProcessBean">
			<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean" bundle="ACQUISITION_RESOURCES">
    			<fr:slot name="missionProcess" layout="autoComplete" key="label.mission.process" bundle="ACQUISITION_RESOURCES"
    					validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
        			<fr:property name="args" value="provider=module.mission.presentationTier.provider.MissionProcessProvider" />
        			<fr:property name="labelField" value="processIdentification"/>
        			<fr:property name="format" value="${processIdentification}"/>
        			<fr:property name="classes" value="inputsize100px"/>
        			<fr:property name="minChars" value="1"/>
        			<fr:property name="sortBy" value="processIdentification"/>
					<fr:property name="saveOptions" value="true"/>
    			</fr:slot>
    		</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form"/>
				<fr:property name="columnClasses" value=",,tderror"/>
			</fr:layout>
		</fr:edit>
	</logic:equal>
	<bean:define id="type" name="acquisitionProcessBean" property="classification"/>
	
	<bean:define id="schema" value="<%= "createAcquisitionRequest." + type %>"/>
	
	<fr:edit id="acquisitionProcessBean"
			name="acquisitionProcessBean"
			type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean"
			schema="<%= schema %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:edit>
	<html:submit styleClass="inputbutton"><bean:message key="button.submit" bundle="EXPENDITURE_RESOURCES"/></html:submit>
</fr:form>

<logic:equal name="type" value="CCP">
	<script type="text/javascript">
		$("input[id$='supplier_AutoComplete']").change(function() {
					<%= "$.getJSON(\"" + request.getContextPath() + "/acquisitionSimplifiedProcedureProcess.do?method=checkSupplierLimit&supplierOid=\" + $(this).attr('value'),function(data, textStatus) {dealWith(data)})" %>
				}); 
	
		function dealWith(data) {
	
			$("#limitInformation").remove();
			
	
			if(data['status'] == 'SOK') {
	
				<%
					String message = BundleUtil.getStringFromResourceBundle("resources.AcquisitionResources","label.supplier.allocationInfo.notFormatted");
				%>
				
				var text = "<%= message %>";
				text = formatString(text,[data['softLimit'], data['supplierLimit']]);
			
				$("#createForm").before("<div id=\"limitInformation\"><p class=\"mbottom05\"><span>" + text + "</span><br/>");
			} else {
	
				<%
					String message2 = BundleUtil.getStringFromResourceBundle("resources.AcquisitionResources","label.attention.supplier.supplierOverLimit.notFormatted");
				%>
			
				var text = "<%= message2 %>";
				text = formatString(text,[data['softLimit'],data['supplierLimit']]);
		
				<bean:define id="messageExtra">
					<bean:message key="label.attention.supplier.explanation" bundle="ACQUISITION_RESOURCES"/>
				</bean:define>
	
				$("#createForm").before("<div id=\"limitInformation\"><div class=\"infobox_warning\">" + text + "</p><p><%= messageExtra %></p>");
			}
		}	
	</script>
</logic:equal>

	