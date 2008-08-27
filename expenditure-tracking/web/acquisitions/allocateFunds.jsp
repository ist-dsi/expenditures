<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.view.acquisition.process" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="infoop2">
	<fr:view name="acquisitionProcess" property="acquisitionRequest"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest"
			schema="viewAcquisitionRequest">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thmiddle thlight mtop05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
</div>

<bean:message key="label.acquisition.proposal.document" bundle="ACQUISITION_RESOURCES"/>
<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
	<html:link action="/acquisitionProcess.do?method=downloadAcquisitionProposalDocument" paramId="acquisitionProposalDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.acquisitionProposalDocument.OID">
		<bean:write name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument.filename"/>
	</html:link>	
</logic:present>
<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
	--
</logic:notPresent>

<bean:define id="urlActivity">/acquisitionProcess.do?method=allocateFunds&amp;acquisitionProcessOid=<bean:write name="acquisitionProcess" property="OID"/></bean:define>
<bean:define id="urlView">/acquisitionProcess.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<bean:write name="acquisitionProcess" property="OID"/></bean:define>
<fr:edit id="fundAllocationBean"
		name="fundAllocationBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean"
		schema="allocateFunds"
		action="<%= urlActivity %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
		<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>

<p>
	<bean:size id="totalItems" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet"/>
	<logic:iterate id="acquisitionRequestItem" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet" indexId="index">
		<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
		<strong><bean:message key="label.view.acquisition.request.item" bundle="ACQUISITION_RESOURCES"/></strong> (  <fr:view name="currentIndex"/> / <fr:view name="totalItems"/> )

		<div class="infoop2">
			<fr:view name="acquisitionRequestItem"
					schema="viewAcquisitionRequestItem">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1"/>
				</fr:layout>
			</fr:view>
		</div>
	</logic:iterate>
</p>