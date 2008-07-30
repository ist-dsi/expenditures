<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.view.acquisition.process" bundle="EXPENDITURE_RESOURCES"/></h2>
<br />
<logic:equal name="acquisitionProcess" property="submitForApprovalAvailable" value="true">
	<html:link action="/acquisitionProcess.do?method=submitForApproval" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
		<bean:message key="link.submit.for.approval" bundle="ACQUISITION_RESOURCES"/>
	</html:link>
	<br />
</logic:equal>

<logic:equal name="acquisitionProcess" property="approveAvailable" value="true">
	<html:link action="/acquisitionProcess.do?method=approve" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
		<bean:message key="link.approve" bundle="ACQUISITION_RESOURCES"/>
	</html:link>
	<br />
</logic:equal>

<logic:equal name="acquisitionProcess" property="fundAllocationIdAvailable" value="true">
	<html:link action="/acquisitionProcess.do?method=allocateFunds" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
		<bean:message key="link.allocate.funds" bundle="ACQUISITION_RESOURCES"/>
	</html:link>
	<br />
</logic:equal>

<logic:equal name="acquisitionProcess" property="fundAllocationExpirationDateAvailable" value="true">
	<html:link action="/acquisitionProcess.do?method=allocateFundsToServiceProvider" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
		<bean:message key="link.allocate.funds.to.service.provider" bundle="ACQUISITION_RESOURCES"/>
	</html:link>
	<br />
</logic:equal>

<logic:equal name="acquisitionProcess" property="acquisitionProcessed" value="true">
</logic:equal>
	<html:link action="/acquisitionProcess.do?method=receiveInvoice" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
		<bean:message key="link.receive.invoice" bundle="ACQUISITION_RESOURCES"/>
	</html:link>
	<br />

<br/>
<fr:view name="acquisitionProcess" property="acquisitionRequest"
		type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest"
		schema="viewAcquisitionRequest">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:view>
<br/>
<html:link action="/acquisitionProcess.do?method=editAcquisitionRequest" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
	<bean:message key="link.edit" bundle="EXPENDITURE_RESOURCES"/>
</html:link>

<logic:equal name="acquisitionProcess" property="deleteAvailable" value="true">
	<html:link action="/acquisitionProcess.do?method=deleteAcquisitionProcess" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
		<bean:message key="link.delete" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
</logic:equal>

<br/>
<br/>
<bean:message key="label.acquisition.proposal.document" bundle="ACQUISITION_RESOURCES"/>
<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
	<html:link action="/acquisitionProcess.do?method=downloadAcquisitionProposalDocument" paramId="acquisitionProposalDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.acquisitionProposalDocument.OID">
		<bean:write name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument.filename"/>
	</html:link>	
</logic:present>
<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
	--
</logic:notPresent>
<br/>
<br/>
<logic:equal name="acquisitionProcess" property="acquisitionProposalDocumentAvailable" value="true">
	<html:link action="/acquisitionProcess.do?method=prepareAddAcquisitionProposalDocument" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
		<bean:message key="link.add.acquisition.proposal.document" bundle="ACQUISITION_RESOURCES"/>
	</html:link>
</logic:equal>
<br/>
<br/>
<logic:equal name="acquisitionProcess" property="createAcquisitionRequestItemAvailable" value="true">
	<html:link action="/acquisitionProcess.do?method=createNewAcquisitionRequestItem" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
		<bean:message key="link.create.new.acquisition.request.item" bundle="ACQUISITION_RESOURCES"/>
	</html:link>
</logic:equal>
<br/>
<br/>
<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet">
	<fr:view name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet"
			schema="viewAcquisitionRequestItemInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>

			<fr:property name="link(view)" value="/acquisitionProcess.do?method=viewAcquisitionRequestItem"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="OID/acquisitionRequestItemOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:present>
