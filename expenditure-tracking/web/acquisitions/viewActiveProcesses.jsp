<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:empty name="activeProcesses">
	<em><bean:message key="label.no.processes.that.user.can.operate" bundle="EXPENDITURE_RESOURCES"/>.</em>
</logic:empty>


<fr:view name="activeProcesses" schema="viewAcquisitionProcessInList">
	<fr:layout name="tabular">
			
			<fr:property name="link(uploadProposalDocument)" value="/acquisitionProcess.do?method=prepareAddAcquisitionProposalDocument"/>
			<fr:property name="bundle(uploadProposalDocument)" value="ACQUISITION_RESOURCES"/>
			<fr:property name="key(uploadProposalDocument)" value="link.add.acquisition.proposal.document"/>
			<fr:property name="param(uploadProposalDocument)" value="OID/acquisitionProcessOid"/>
			<fr:property name="visibleIf(uploadProposalDocument)" value="acquisitionProposalDocumentAvailable"/>
			<fr:property name="order(uploadProposalDocument)" value="1"/>
	
			<fr:property name="link(createAcquisitionRequestItem)" value="/acquisitionProcess.do?method=createNewAcquisitionRequestItem"/>
			<fr:property name="bundle(createAcquisitionRequestItem)" value="ACQUISITION_RESOURCES"/>
			<fr:property name="key(createAcquisitionRequestItem)" value="link.create.new.acquisition.request.item"/>
			<fr:property name="param(createAcquisitionRequestItem)" value="OID/acquisitionProcessOid"/>
			<fr:property name="visibleIf(createAcquisitionRequestItem)" value="createAcquisitionRequestItemAvailable"/>
			<fr:property name="order(createAcquisitionRequestItem)" value="2"/>
	
			<fr:property name="link(submitForApproval)" value="/acquisitionProcess.do?method=submitForApproval"/>
			<fr:property name="bundle(submitForApproval)" value="ACQUISITION_RESOURCES"/>
			<fr:property name="key(submitForApproval)" value="link.submit.for.approval"/>
			<fr:property name="param(submitForApproval)" value="OID/acquisitionProcessOid"/>
			<fr:property name="visibleIf(submitForApproval)" value="submitForApprovalAvailable"/>
			<fr:property name="order(submitForApproval)" value="3"/>
	
			<fr:property name="link(approve)" value="/acquisitionProcess.do?method=approve"/>
			<fr:property name="bundle(approve)" value="ACQUISITION_RESOURCES"/>
			<fr:property name="key(approve)" value="link.approve"/>
			<fr:property name="param(approve)" value="OID/acquisitionProcessOid"/>
			<fr:property name="visibleIf(approve)" value="approveAvailable"/>
			<fr:property name="order(approve)" value="4"/>
	
			<fr:property name="link(delete)" value="/acquisitionProcess.do?method=deleteAcquisitionProcess"/>
			<fr:property name="bundle(delete)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(delete)" value="link.delete"/>
			<fr:property name="param(delete)" value="OID/acquisitionProcessOid"/>
			<fr:property name="visibleIf(delete)" value="deleteAvailable"/>
			<fr:property name="order(delete)" value="5"/>
	
			<fr:property name="link(fundAllocationId)" value="/acquisitionProcess.do?method=allocateFunds"/>
			<fr:property name="bundle(fundAllocationId)" value="ACQUISITION_RESOURCES"/>
			<fr:property name="key(fundAllocationId)" value="link.allocate.funds"/>
			<fr:property name="param(fundAllocationId)" value="OID/acquisitionProcessOid"/>
			<fr:property name="visibleIf(fundAllocationId)" value="fundAllocationIdAvailable"/>
			<fr:property name="order(fundAllocationId)" value="6"/>
			
			<fr:property name="link(fundAllocationExpirationDate)" value="/acquisitionProcess.do?method=allocateFundsToServiceProvider"/>
			<fr:property name="bundle(fundAllocationExpirationDate)" value="ACQUISITION_RESOURCES"/>
			<fr:property name="key(fundAllocationExpirationDate)" value="link.allocate.funds.to.service.provider"/>
			<fr:property name="param(fundAllocationExpirationDate)" value="OID/acquisitionProcessOid"/>
			<fr:property name="visibleIf(fundAllocationExpirationDate)" value="fundAllocationExpirationDateAvailable"/>
			<fr:property name="order(fundAllocationExpirationDate)" value="7"/>
	
	</fr:layout>
</fr:view>

