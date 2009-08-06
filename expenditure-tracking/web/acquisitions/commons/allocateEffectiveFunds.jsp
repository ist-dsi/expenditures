<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.allocateFundsPermanently" bundle="ACQUISITION_RESOURCES"/></h2>

<logic:present name="process" property="currentOwner">
	<bean:define id="ownerName" name="process" property="currentOwner.firstAndLastName"/>
	<div class="infoop4">
		<bean:message key="acquisitionProcess.message.info.currentOwnerIs" bundle="ACQUISITION_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>

<bean:define id="processClass" name="process" property="class.simpleName"/>
<bean:define id="processRequest" name="process" property="request" toScope="request"/>
<bean:define id="requestclass" name="processRequest" property="class.simpleName"/>
<jsp:include page='<%= "view" + requestclass  + ".jsp"%>' flush="true"/>

<bean:define id="urlActivity">/acquisition<%= processClass %>.do?processOid=<bean:write name="process" property="externalId"/></bean:define>
<bean:define id="urlView">/acquisition<%= processClass %>.do?method=viewProcess&amp;processOid=<bean:write name="process" property="externalId"/></bean:define>


<div class="forminline mbottom2">

<fr:form id="allocationForm" action="<%= urlActivity %>">
	<fr:edit  
		id="financerFundAllocationId"
		name="fundAllocationBeans" visible="false"/>
	<html:hidden  property="method" value="allocateFundsPermanently"/>
	<html:hidden property="financerOID" value=""/>
	<html:hidden property="index" value=""/>


<jsp:include page="../../commons/defaultErrorDisplay.jsp"/>


<p class="mtop15 mbottom0"><bean:message key="acquisitionProcess.label.insertPermanentFunds" bundle="ACQUISITION_RESOURCES"/></p>

<table class="tstyle6">
<logic:iterate id="financerBean" name="fundAllocationBeans" indexId="index">
	<tr>
		<bean:define id="usedClass" value="" toScope="request"/>
		<logic:equal name="financerBean" property="allowedToAddNewFund" value="false">
				<bean:define id="usedClass" value="dnone" toScope="request"/>
		</logic:equal>
		<th class="<%= usedClass %>">			
			<h4 class="dinline"><fr:view name="financerBean" property="financer.unit.presentationName"/></h4>
			<span style="padding-left: 1em;">(<bean:message key="financer.label.fundAllocation.identification" bundle="ACQUISITION_RESOURCES"/>: <fr:view name="financerBean" property="fundAllocationId" type="java.lang.String"/>)</span>
		</th>
	</tr>
	<tr>
		<td>
			<fr:edit id="<%= "id" + index %>" name="financerBean" slot="effectiveFundAllocationId" type="java.lang.String"/>
			<bean:define id="financerOID" name="financerBean" property="financer.externalId" type="java.lang.String"/>
			<logic:equal name="financerBean" property="allowedToAddNewFund" value="true">
				<a href="javascript:document.getElementById('allocationForm').method.value='addAllocationFund'; document.getElementById('allocationForm').financerOID.value='<%= financerOID %>'; document.getElementById('allocationForm').index.value='<%= index %>'; document.getElementById('allocationForm').submit();">
					<bean:message key="financer.link.addEffectiveAllocationId" bundle="ACQUISITION_RESOURCES"/>
				</a>
			</logic:equal>
			<logic:equal name="financerBean" property="allowedToAddNewFund" value="false">
				<a href="javascript:document.getElementById('allocationForm').method.value='removeAllocationFund'; document.getElementById('allocationForm').index.value='<%= index %>'; document.getElementById('allocationForm').submit();">
					<bean:message key="financer.link.removeEffectiveAllocationId" bundle="ACQUISITION_RESOURCES"/>
				</a>
			</logic:equal>
		</td>
	</tr>
</logic:iterate>
</table>

<html:submit styleClass="inputbutton"><bean:message key="button.submit" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
</fr:form>

<fr:form id="allocationForm" action="<%= urlView %>">
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
</fr:form>

</div>

<div class="item">
	<bean:size id="totalItems" name="process" property="request.requestItems"/>
	<logic:iterate id="item" name="process" property="request.requestItems" indexId="index">
		<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
		<strong><bean:message key="acquisitionRequestItem.label.item" bundle="ACQUISITION_RESOURCES"/></strong> (<fr:view name="currentIndex"/>/<fr:view name="totalItems"/>)

		<bean:define id="item" name="item" toScope="request"/>
		<bean:define id="itemClass" name="item" property="class.simpleName"/>
		<jsp:include page='<%= "view" + itemClass + ".jsp" %>' flush="false"/>
	
	</logic:iterate>
</div>