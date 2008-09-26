<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<bean:define id="currentState" name="acquisitionProcess" property="acquisitionProcessStateType"/>
 
<fr:view name="acquisitionProcess"> 
	<fr:layout name="process-state">
		<fr:property name="stateParameterName" value="state"/>
		<fr:property name="url" value="/viewLogs.do?method=viewOperationLog&acquisitionProcessOid=${OID}"/>
		<fr:property name="contextRelative" value="true"/>
		<fr:property name="currentStateClass" value=""/>
	</fr:layout>
</fr:view>

<div class="wrapper">

<h2><bean:message key="acquisitionProcess.title.viewAcquisitionRequest" bundle="ACQUISITION_RESOURCES"/></h2>

<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<div class="infoop1">
	<ul>
	<logic:iterate id="activity" name="acquisitionProcess" property="activeActivitiesForRequest">
		<bean:define id="activityName" name="activity" property="class.simpleName"/> 
		<li>
			<html:link page='<%= "/acquisitionProcess.do?method=execute" + activityName %>' paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
				<fr:view name="activity" property="class">
					<fr:layout name="label">
						<fr:property name="bundle" value="ACQUISITION_RESOURCES"/>
					</fr:layout>
				</fr:view>
			</html:link>
		</li>
	</logic:iterate>
		<li>
			<html:link page="/viewLogs.do?method=viewOperationLog&amp;module=acquisitions" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
				<bean:message key="label.log.view" bundle="ACQUISITION_RESOURCES"/>
			</html:link>
		</li>

	</ul>
	<logic:empty name="acquisitionProcess" property="activeActivitiesForRequest">
		<p>
			<em>
				<bean:message key="messages.info.noOperatesAvailabeATM" bundle="EXPENDITURE_RESOURCES"/>.
			</em>
		</p>
	</logic:empty>
</div>



<div class="expenditures">
	<logic:equal name="acquisitionProcess" property="allowedToViewSupplierExpenditures" value="true">
		<logic:present name="acquisitionProcess" property="acquisitionRequest.supplier">
			<p>
			<bean:message key="supplier.message.info.totalAllocated" bundle="ORGANIZATION_RESOURCES"/>:
			<fr:view name="acquisitionProcess" property="acquisitionRequest.supplier.totalAllocated"/>
			</p>
		</logic:present>
	</logic:equal>
</div>

<bean:define id="acquisitionProcess" name="acquisitionProcess" toScope="request"/>
<jsp:include page="viewAcquisitionRequest.jsp" flush="true"/>

<logic:notEmpty name="acquisitionProcess" property="acquisitionRequest.totalAmountsForEachPayingUnit">
	<fr:view name="acquisitionProcess" property="acquisitionRequest.totalAmountsForEachPayingUnit"
			schema="viewPayingUnitWithTotalAmount">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5"/>
			<fr:property name="columnClasses" value="aleft,nowrap,,"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>


<div class="documents">
	<p>
		<bean:message key="acquisitionProcess.label.proposalDocument" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
			<html:link action="/acquisitionProcess.do?method=downloadAcquisitionProposalDocument" paramId="acquisitionProposalDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.acquisitionProposalDocument.OID">
				<bean:write name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument.filename"/>
			</html:link>	
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.acquisitionProposalDocument">
			<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
		</logic:notPresent>
	</p>
	<p>
		<bean:message key="acquisitionProcess.label.requestDocument" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestDocument">
			<html:link action="/acquisitionProcess.do?method=downloadAcquisitionRequestDocument" paramId="acquisitionRequestDocumentOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.acquisitionRequestDocument.OID">
				<bean:write name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestDocument.filename"/>
			</html:link>	
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestDocument">
			<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
		</logic:notPresent>
	</p>
	<p>
		<bean:message key="acquisitionProcess.label.invoice" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="acquisitionProcess" property="acquisitionRequest.invoice">
			<logic:present name="acquisitionProcess" property="acquisitionRequest.invoice.content">
				<html:link action="/acquisitionProcess.do?method=downloadInvoice" paramId="invoiceOid" paramName="acquisitionProcess" paramProperty="acquisitionRequest.invoice.OID">
					<bean:write name="acquisitionProcess" property="acquisitionRequest.invoice.filename"/>
				</html:link>
			</logic:present>	
			<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.invoice">
				<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
			</logic:notPresent>
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="acquisitionRequest.invoice">
			<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
		</logic:notPresent>
	</p>
</div>


<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet">

	<logic:equal  name="acquisitionProcess" property="acquisitionProcessState.acquisitionProcessStateType"  value="INVOICE_RECEIVED">		
		<logic:equal name="acquisitionProcess" property="acquisitionRequest.realValueLessThanTotalValue" value="false">
			<div class="infoop4">
				<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.realValueLessThanTotalValue" bundle="ACQUISITION_RESOURCES"/>
			</div>
		</logic:equal>
		<logic:equal name="acquisitionProcess" property="acquisitionRequest.realUnitShareValueLessThanUnitShareValue" value="false">
			<div class="infoop4">
				<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.realUnitShareValueLessThanUnitShareValue" bundle="ACQUISITION_RESOURCES"/>
			</div>
		</logic:equal>
		<logic:equal name="acquisitionProcess" property="acquisitionRequest.realTotalValueEqualsRealShareValue" value="false">
			<div class="infoop4">
				<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.realTotalValueEqualsRealShareValue" bundle="ACQUISITION_RESOURCES"/>
			</div>
		</logic:equal>
	</logic:equal>


	<bean:size id="totalItems" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet"/>
	<logic:iterate id="acquisitionRequestItem" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet" indexId="index">
		<div class="item">
			<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
			<strong><bean:message key="acquisitionRequestItem.label.item" bundle="ACQUISITION_RESOURCES"/></strong> (<fr:view name="currentIndex"/>/<fr:view name="totalItems"/>)
			<bean:define id="itemOID" name="acquisitionRequestItem" property="OID"/>
			
			<logic:iterate id="activity" name="acquisitionProcess" property="activeActivitiesForItem" indexId="index">
				<logic:greaterThan name="index" value="0"> | </logic:greaterThan>
				<bean:define id="activityName" name="activity" property="class.simpleName"/> 
					<html:link page='<%= "/acquisitionProcess.do?method=execute" + activityName + "&acquisitionRequestItemOid=" + itemOID%>' paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
						<fr:view name="activity" property="class">
							<fr:layout name="label">
								<fr:property name="bundle" value="ACQUISITION_RESOURCES"/>
							</fr:layout>
						</fr:view>
					</html:link>
			</logic:iterate>

			<logic:equal name="acquisitionRequestItem" property="valueFullyAttributedToUnits" value="false">
				<div class="infoop4">
					<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/>:</strong> <bean:message key="acquisitionRequestItem.message.info.valueNotFullyAttributed" bundle="ACQUISITION_RESOURCES"/>
				</div>
			</logic:equal>
			
			<logic:equal  name="acquisitionProcess" property="acquisitionProcessState.acquisitionProcessStateType"  value="INVOICE_CONFIRMED">		
				<logic:equal name="acquisitionRequestItem" property="filledWithRealValues" value="false">
					<div class="infoop4">
						<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/></strong>: <bean:message key="acquisitionRequestItem.message.info.valuesNotFilled" bundle="ACQUISITION_RESOURCES"/>
					</div>
				</logic:equal>
			</logic:equal>
		
			<bean:define id="acquisitionRequestItem" name="acquisitionRequestItem" toScope="request"/>
			<jsp:include page="./acquisitionItemDisplay.jsp" flush="false"/>
	
		</div>
	</logic:iterate>
	
	
</logic:present>

</div>


