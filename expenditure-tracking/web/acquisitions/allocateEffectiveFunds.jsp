<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.viewAcquisitionRequest" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="acquisitionProcessOID" name="acquisitionProcess"  property="OID"/>
<bean:define id="acquisitionProcess" name="acquisitionProcess" toScope="request"/>
<jsp:include page="viewAcquisitionRequest.jsp" flush="true"/>

<div class="documents">
	<p>
		<bean:message key="acquisitionProcess.label.proposalDocument" bundle="ACQUISITION_RESOURCES"/>: 
		<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
			<html:link action="/acquisitionProcess.do?method=downloadAcquisitionProposalDocument" paramId="acquisitionProposalDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.acquisitionProposalDocument.OID">
				<bean:write name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument.filename"/>
			</html:link>	
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
			-
		</logic:notPresent>
	</p>
</div>

<bean:define id="urlActivity">/acquisitionProcess.do?acquisitionProcessOid=<bean:write name="acquisitionProcess" property="OID"/></bean:define>
<bean:define id="urlView">/acquisitionProcess.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<bean:write name="acquisitionProcess" property="OID"/></bean:define>
<div class="dinline forminline">
<fr:form id="allocationForm" action="<%= urlActivity %>">
	<fr:edit  
		id="financerFundAllocationId"
		name="fundAllocationBeans" visible="false"/>
		
	<html:hidden  property="method" value="allocateFundsPermanently"/>
	<html:hidden property="financerOID" value=""/>
	<html:hidden property="index" value=""/>


<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<logic:iterate id="financerBean" name="fundAllocationBeans" indexId="index">
	
	<bean:define id="usedClass" value="" toScope="request"/>
	<logic:equal name="financerBean" property="allowedToAddNewFund" value="false">
			<bean:define id="usedClass" value="dnone" toScope="request"/>
	</logic:equal>
	
	<span class="<%= usedClass %>">
		<fr:view name="financerBean" property="financer.unit.presentationName"/> (<bean:message key="financer.label.fundAllocation.identification" bundle="ACQUISITION_RESOURCES"/>: <fr:view name="financerBean" property="fundAllocationId" type="java.lang.String"/>)
	</span> 
	<div>
		<fr:edit id="<%= "id" + index %>" name="financerBean" slot="effectiveFundAllocationId" type="java.lang.String"/>
			<bean:define id="financerOID" name="financerBean" property="financer.OID"/>
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
	</div>
</logic:iterate>

	<html:submit styleClass="inputbutton"><bean:message key="button.atribute" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
</fr:form>
<fr:form id="allocationForm" action="<%= urlView %>">
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
</fr:form>
</div>

<div class="item">
	<bean:size id="totalItems" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet"/>
	<logic:iterate id="acquisitionRequestItem" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet" indexId="index">
		<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
		<strong><bean:message key="acquisitionRequestItem.label.item" bundle="ACQUISITION_RESOURCES"/></strong> (<fr:view name="currentIndex"/>/<fr:view name="totalItems"/>)

		<bean:define id="acquisitionRequestItem" name="acquisitionRequestItem" toScope="request"/>
		<jsp:include page="./acquisitionItemDisplay.jsp" flush="false"/>
	
	</logic:iterate>
</div>