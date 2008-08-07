<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="currentState" name="acquisitionProcess" property="acquisitionProcessStateType"/>

<fr:view name="acquisitionProcess"> 
	<fr:layout name="process-state">
		<fr:property name="stateParameterName" value="state"/>
		<fr:property name="url" value="/viewLogs.do?method=viewOperationLog&acquisitionProcessOid=${OID}"/>
		<fr:property name="contextRelative" value="true"/>
	</fr:layout>
</fr:view>
<%-- 
<img src="<%= request.getContextPath() + "/CSS/processImages/" + currentState.toString() + ".png"%>" style="float: right"/>
--%>
 
<h2><bean:message key="label.view.acquisition.process" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="infoop1" style="width: 360px">
	<ul>
	<logic:iterate id="activity" name="acquisitionProcess" property="activeActivitiesForRequest">
		<bean:define id="activityName" name="activity" property="class.simpleName"/> 
		<li>
			<html:link page="<%= "/acquisitionProcess.do?method=execute" + activityName %>" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
				<fr:view name="activity" property="class">
					<fr:layout name="label">
						<fr:property name="bundle" value="ACQUISITION_RESOURCES"/>
					</fr:layout>
				</fr:view>
			</html:link>
		</li>
	</logic:iterate>
	</ul>
	<logic:empty name="acquisitionProcess" property="activeActivities">
		<em>
			<bean:message key="label.no.operations.available.at.the.moment" bundle="EXPENDITURE_RESOURCES"/>
		</em>
	</logic:empty>
</div>

<logic:equal name="acquisitionProcess" property="allowedToViewCostCenterExpenditures" value="true">
	<bean:message key="label.unit.total.allocated" bundle="ORGANIZATION_RESOURCES"/>
	<bean:write name="acquisitionProcess" property="unit.totalAllocated"/>
	<br/>
</logic:equal>
<logic:equal name="acquisitionProcess" property="allowedToViewSupplierExpenditures" value="true">
	<logic:present name="acquisitionProcess" property="acquisitionRequest.supplier">
		<bean:message key="label.supplier.total.allocated" bundle="ORGANIZATION_RESOURCES"/>
		<bean:write name="acquisitionProcess" property="acquisitionRequest.supplier.totalAllocated"/>
		<br/>
	</logic:present>
</logic:equal>

<div class="infoop2" style="width: 360px">
	<fr:view name="acquisitionProcess" property="acquisitionRequest"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest"
			schema="viewAcquisitionRequest">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<div class="documents">
	<p>
		<bean:message key="label.acquisition.proposal.document" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
			<html:link action="/acquisitionProcess.do?method=downloadAcquisitionProposalDocument" paramId="acquisitionProposalDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.acquisitionProposalDocument.OID">
				<bean:write name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument.filename"/>
			</html:link>	
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
			<em><bean:message key="label.document.not.available" bundle="ACQUISITION_RESOURCES"/></em>
		</logic:notPresent>
	</p>
	
	<p>
		<bean:message key="label.acquisition.request.document" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestDocument">
			<html:link action="/acquisitionProcess.do?method=downloadAcquisitionRequestDocument" paramId="acquisitionRequestDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.acquisitionRequestDocument.OID">
				<bean:write name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestDocument.filename"/>
			</html:link>	
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestDocument">
			<em><bean:message key="label.document.not.available" bundle="ACQUISITION_RESOURCES"/></em>
		</logic:notPresent>
	</p>
	
	<p>
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
	</p>
</div>


<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet">

	<bean:size id="totalItems" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet"/>
	<logic:iterate id="acquisitionRequestItem" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet" indexId="index">
		<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
		<strong><bean:message key="label.view.acquisition.request.item" bundle="ACQUISITION_RESOURCES"/></strong> (  <fr:view name="currentIndex"/> / <fr:view name="totalItems"/> )
		<bean:define id="itemOID" name="acquisitionRequestItem" property="OID"/>
		
	<div class="infoop1" style="width: 360px">
	<ul>
	<logic:iterate id="activity" name="acquisitionProcess" property="activeActivitiesForItem">
		<bean:define id="activityName" name="activity" property="class.simpleName"/> 
		<li>
			<html:link page="<%= "/acquisitionProcess.do?method=execute" + activityName + "&acquisitionRequestItemOid=" + itemOID%>" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
				<fr:view name="activity" property="class">
					<fr:layout name="label">
						<fr:property name="bundle" value="ACQUISITION_RESOURCES"/>
					</fr:layout>
				</fr:view>
			</html:link>
		</li>
	</logic:iterate>
	</ul>
	<logic:empty name="acquisitionProcess" property="activeActivities">
		<em>
			<bean:message key="label.no.operations.available.at.the.moment" bundle="EXPENDITURE_RESOURCES"/>
		</em>
	</logic:empty>
</div>
		
		<div class="infoop2" style="width: 360px">
			<fr:view name="acquisitionRequestItem"
					schema="viewAcquisitionRequestItem">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1"/>
				</fr:layout>
			</fr:view>
		</div>
	</logic:iterate>
</logic:present>


