<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<strong><bean:message key="label.available.operations" bundle="EXPENDITURE_RESOURCES"/>:</strong>
<logic:equal name="acquisitionProcess" property="personAbleToExecuteActivities" value="true">
	<ul>
		<logic:equal name="acquisitionProcess" property="submitForApprovalAvailable" value="true">
			<li>
				<html:link action="/acquisitionProcess.do?method=submitForApproval" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
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
		<logic:equal name="acquisitionProcess" property="createAcquisitionRequestAvailable" value="true">
			<html:link action="/acquisitionProcess.do?method=prepareCreateAcquisitionRequest" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
				<bean:message key="link.create.acquisition.request" bundle="ACQUISITION_RESOURCES"/>
			</html:link>
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
</logic:equal>
<logic:equal name="acquisitionProcess" property="personAbleToExecuteActivities" value="false">
	<br/>
	<em>
		<bean:message key="label.no.operations.available.at.the.moment" bundle="EXPENDITURE_RESOURCES"/>
	</em>
</logic:equal>
