<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="currentState" name="acquisitionProcess" property="acquisitionProcessStateType"/>

<img src="<%= request.getContextPath() + "/CSS/processImages/" + currentState.toString() + ".png"%>" style="float: right"/>

<h2><bean:message key="label.view.acquisition.process" bundle="EXPENDITURE_RESOURCES"/></h2>
<br />

<div style="border: 1px dotted; width: 300px;">
 <strong><bean:message key="label.available.operations" bundle="EXPENDITURE_RESOURCES"/>:</strong>
 
 	<logic:equal name="acquisitionProcess" property="personAbleToExecuteActivities" value="true">
	 <ul>
		<logic:equal name="acquisitionProcess" property="submitForApprovalAvailable" value="true">
			<li><html:link action="/acquisitionProcess.do?method=submitForApproval" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
				<bean:message key="link.submit.for.approval" bundle="ACQUISITION_RESOURCES"/>
			</html:link>
			</li>
		</logic:equal>
		<logic:equal name="acquisitionProcess" property="approveAvailable" value="true">
			<li>
				<html:link action="/acquisitionProcess.do?method=approve" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
					<bean:message key="link.approve" bundle="ACQUISITION_RESOURCES"/>
				</html:link>
			</li>
		</logic:equal>
		<logic:equal name="acquisitionProcess" property="fundAllocationIdAvailable" value="true">
			<li>
				<html:link action="/acquisitionProcess.do?method=allocateFunds" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
					<bean:message key="link.allocate.funds" bundle="ACQUISITION_RESOURCES"/>
				</html:link>
			</li>
		</logic:equal>
		<logic:equal name="acquisitionProcess" property="fundAllocationExpirationDateAvailable" value="true">
			<li>
				<html:link action="/acquisitionProcess.do?method=allocateFundsToServiceProvider" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
					<bean:message key="link.allocate.funds.to.service.provider" bundle="ACQUISITION_RESOURCES"/>
				</html:link>
			</li>
		</logic:equal>	 
		<logic:equal name="acquisitionProcess" property="acquisitionProcessed" value="true">
			<li>
				<html:link action="/acquisitionProcess.do?method=receiveInvoice" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
					<bean:message key="link.receive.invoice" bundle="ACQUISITION_RESOURCES"/>
				</html:link>
			</li>
		</logic:equal>
		<logic:equal name="acquisitionProcess" property="deleteAvailable" value="true">
			<li>
				<html:link action="/acquisitionProcess.do?method=deleteAcquisitionProcess" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
					<bean:message key="label.delete.acquisiton" bundle="ACQUISITION_RESOURCES"/>
				</html:link>
			</li>
		</logic:equal>
		<logic:equal name="acquisitionProcess" property="acquisitionProposalDocumentAvailable" value="true">
			<li>
				<html:link action="/acquisitionProcess.do?method=prepareAddAcquisitionProposalDocument" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
					<bean:message key="link.add.acquisition.proposal.document" bundle="ACQUISITION_RESOURCES"/>
				</html:link>
			</li>
		</logic:equal>
		<logic:equal name="acquisitionProcess" property="createAcquisitionRequestItemAvailable" value="true">
			<li>
				<html:link action="/acquisitionProcess.do?method=createNewAcquisitionRequestItem" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
					<bean:message key="link.create.new.acquisition.request.item" bundle="ACQUISITION_RESOURCES"/>
				</html:link>
			</li>
		</logic:equal>
		<logic:equal name="acquisitionProcess" property="editRequestItemAvailable" value="true">
		<li>
			<html:link action="/acquisitionProcess.do?method=editAcquisitionRequest" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
				<bean:message key="label.edit.acquisiton" bundle="ACQUISITION_RESOURCES"/>
			</html:link>
		</li>
		</logic:equal>
	 </logic:equal>
	 <logic:equal name="acquisitionProcess" property="personAbleToExecuteActivities" value="false">
	 	<br/><em><bean:message key="label.no.operations.available.at.the.moment" bundle="EXPENDITURE_RESOURCES"/></em>
	 </logic:equal>
	<logic:equal name="acquisitionProcess" property="receiveInvoiceAvailable" value="true">
		<li>
			<html:link action="/acquisitionProcess.do?method=receiveInvoice" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
				<bean:message key="link.receive.invoice" bundle="ACQUISITION_RESOURCES"/>
			</html:link>
		</li>
	</logic:equal>
	<logic:equal name="acquisitionProcess" property="confirmInvoiceAvailable" value="true">
		<li>
			<html:link action="/acquisitionProcess.do?method=confirmInvoice" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
				<bean:message key="link.confirm.invoice" bundle="ACQUISITION_RESOURCES"/>
			</html:link>
		</li>
	</logic:equal>
	</ul>
</div>

<br/>
<br/>
<fr:view name="acquisitionProcess" property="acquisitionRequest"
		type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest"
		schema="viewAcquisitionRequest">
	<fr:layout name="tabular">
		<fr:property name="style" value="border: 1px solid #000; text-align: left;"/>
	</fr:layout>
</fr:view>
<br/>

<bean:message key="label.acquisition.proposal.document" bundle="ACQUISITION_RESOURCES"/>:

<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
	<html:link action="/acquisitionProcess.do?method=downloadAcquisitionProposalDocument" paramId="acquisitionProposalDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.acquisitionProposalDocument.OID">
		<bean:write name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument.filename"/>
	</html:link>	
</logic:present>
<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
	<em><bean:message key="label.document.not.available" bundle="ACQUISITION_RESOURCES"/></em>
</logic:notPresent>

<br/>
<br/>
<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet">
	<fr:view name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet"
			schema="viewAcquisitionRequestItemInList">
		<fr:layout name="tabular">
			<fr:property name="style" value="border: 1px solid #000; "/>

			<fr:property name="link(view)" value="/acquisitionProcess.do?method=viewAcquisitionRequestItem"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="OID/acquisitionRequestItemOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:present>
<br/>

<bean:message key="label.invoice" bundle="ACQUISITION_RESOURCES"/>:

<logic:present name="acquisitionProcess" property="acquisitionRequest.invoice">
	<logic:present name="acquisitionProcess" property="acquisitionRequest.invoice.content">
		<html:link action="/acquisitionProcess.do?method=downloadInvoice" paramId="invoiceOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.invoice.OID">
			<bean:write name="acquisitionProcess" property="acquisitionRequest.invoice.filename"/>
		</html:link>
	</logic:present>	
	<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.invoice">
		<em><bean:message key="label.document.not.available" bundle="ACQUISITION_RESOURCES"/></em>
	</logic:notPresent>
</logic:present>
<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.invoice">
	<em><bean:message key="label.document.not.available" bundle="ACQUISITION_RESOURCES"/></em>
</logic:notPresent>
