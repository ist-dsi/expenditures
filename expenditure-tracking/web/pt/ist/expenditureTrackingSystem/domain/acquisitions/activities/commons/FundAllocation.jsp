<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="process" property="currentOwner">
	<bean:define id="ownerName" name="process" property="currentOwner.expenditurePerson.firstAndLastName"/>
	<div class="infobox_warning">
		<bean:message key="acquisitionProcess.message.info.currentOwnerIs" bundle="ACQUISITION_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>

<bean:define id="name" name="information" property="activityName"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>

<p class="mtop15 mbottom025"><bean:message key="acquisitionProcess.label.fundAllocation.insert" bundle="ACQUISITION_RESOURCES"/>:</p>

<bean:define id="urlActivity" value='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'/>
<bean:define id="urlView" value='<%= "/workflowProcessManagement.do?method=viewProcess&amp;processId=" + processId%>'/>

<div class="dinline forminline">
	<fr:form action="<%= urlActivity %>">
		<fr:edit  id="activityBean" name="information" visible="false"/>
	
		<fr:edit id="funds"	schema="editFinancerFundAllocationId" name="information" property="beans" >
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle2 mtop05 mbottom15"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%= urlView %>" />
		</fr:edit>
		
		<html:submit styleClass="inputbutton"><bean:message key="button.atribute" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
	</fr:form>
	
	<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>


