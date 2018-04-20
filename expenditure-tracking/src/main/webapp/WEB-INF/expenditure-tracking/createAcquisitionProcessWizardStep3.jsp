<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="java.util.ResourceBundle"%>
<%@page import="org.fenixedu.bennu.core.i18n.BundleUtil"%>
<%@page import="module.mission.domain.MissionSystem"%>

<h2>Criar novo Processo de Aquisição</h2>

<p class="mvert05">
	STEP 3
</p>

<html:messages id="message" message="true" bundle="MISSION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<fr:form id="createForm" action="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcess">
	<p>
		<bean:message key="label.message.create.suppliers.instructions" bundle="ACQUISITION_RESOURCES"
				arg0="<%= ExpenditureTrackingSystem.getInstance().getCreateSupplierUrl() %>"
				arg1="<%= ExpenditureTrackingSystem.getInstance().getCreateSupplierLabel() %>"
		/>
	</p>
	<bean:define id="type" name="acquisitionProcessBean" property="classification"/>
	<bean:define id="schema" value="createAcquisitionRequest.SupplierOnly"/>
	<fr:edit id="acquisitionProcessBeanSupplier"
			name="acquisitionProcessBean"
			type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean"
			schema="<%= schema %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:edit>
	<html:submit styleClass="inputbutton">Next &gt;</html:submit>
</fr:form>
	
<logic:equal name="type" value="CCP">
<logic:notEqual name="acquisitionProcessBean" property="isUnderMandatorySupplierScope" value="true">
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

		function formatString(string, formatTokens) {
			var text = string;
			for (i=0; i < formatTokens.length ; i++) {
				text = text.replace(new RegExp("\\{" + i + "\\}"),formatTokens[i]);
			}
			return text;
		}
	</script>
</logic:notEqual>
</logic:equal>