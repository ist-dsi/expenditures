<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="java.util.ResourceBundle"%>
<%@page import="org.fenixedu.bennu.core.i18n.BundleUtil"%>
<%@page import="module.mission.domain.MissionSystem"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>

<h2><bean:message key="acquisitionProcess.title.createAcquisitionRequest" bundle="ACQUISITION_RESOURCES"/></h2>

<div class="infobox">
	<bean:message key="acquisitionProcess.message.note.normal" bundle="ACQUISITION_RESOURCES" />
</div>

<html:messages id="message" message="true" bundle="MISSION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<p class="mtop15 mbottom05"><strong><fr:view name="acquisitionProcessBean" property="classification"/></strong></p>

<fr:form id="createForm" action="/acquisitionSimplifiedProcedureProcess.do?method=createNewAcquisitionProcess">
	<logic:equal name="acquisitionProcessBean" property="isForMission" value="true">
		<fr:edit id="acquisitionProcessBeanMissionProcess" name="acquisitionProcessBean">
			<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean" bundle="ACQUISITION_RESOURCES">
    			<fr:slot name="missionProcess" layout="autoComplete" key="label.mission.process" bundle="ACQUISITION_RESOURCES"
    					required="true">
        			<fr:property name="args" value="provider=module.mission.presentationTier.provider.MissionProcessProvider" />
        			<fr:property name="labelField" value="processIdentification"/>
        			<fr:property name="format" value="\${processIdentification}"/>
        			<fr:property name="classes" value="inputsize100px"/>
        			<fr:property name="minChars" value="1"/>
        			<fr:property name="sortBy" value="processIdentification"/>
					<fr:property name="saveOptions" value="true"/>
    			</fr:slot>
    			<% if (!MissionSystem.getInstance().getMandatorySupplierSet().isEmpty()) { %>
    				<fr:slot name="isUnderMandatorySupplierScope" key="label.aquisition.process.create.for.mission.is.under.mandatory.supplier.scope" layout="radio-postback">
    					<fr:property name="classes" value="liinline"/>
   					</fr:slot>
   				<% } %>
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
					String message = BundleUtil.getString("resources.AcquisitionResources","label.supplier.allocationInfo.notFormatted");
				%>
				
				var text = "<%= message %>";
				text = formatString(text,[data['softLimit'], data['supplierLimit']]);
			
				$("#createForm").before("<div id=\"limitInformation\"><p class=\"mbottom05\"><span>" + text + "</span><br/>");
			} else {
	
				<%
					String message2 = BundleUtil.getString("resources.AcquisitionResources","label.attention.supplier.supplierOverLimit.notFormatted");
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

	
