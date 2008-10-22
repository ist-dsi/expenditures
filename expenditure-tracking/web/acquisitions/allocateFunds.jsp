<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.fundAllocation" bundle="ACQUISITION_RESOURCES"/></h2>

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


<p class="mtop15 mbottom025"><bean:message key="acquisitionProcess.label.fundAllocation.insert" bundle="ACQUISITION_RESOURCES"/>:</p>

<bean:define id="urlActivity">/acquisitionProcess.do?method=allocateFunds&amp;acquisitionProcessOid=<bean:write name="acquisitionProcess" property="OID"/></bean:define>
<bean:define id="urlView">/acquisitionProcess.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<bean:write name="acquisitionProcess" property="OID"/></bean:define>
<fr:edit action="<%= urlActivity %>" 
		id="financerFundAllocationId" 
		schema="editFinancerFundAllocationId" 
		name="fundAllocationBeans" >
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle2 mtop05"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>

<div class="item mtop2">
	<bean:size id="totalItems" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet"/>
	<logic:iterate id="acquisitionRequestItem" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet" indexId="index">
		<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
		<strong><bean:message key="acquisitionRequestItem.label.item" bundle="ACQUISITION_RESOURCES"/></strong> (<fr:view name="currentIndex"/>/<fr:view name="totalItems"/>)

		<bean:define id="acquisitionRequestItem" name="acquisitionRequestItem" toScope="request"/>
		<jsp:include page="./acquisitionItemDisplay.jsp" flush="false"/>
	
	</logic:iterate>
</div>