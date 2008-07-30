<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.receive.invoice" bundle="ACQUISITION_RESOURCES"/></h2>
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
<br/>
<br/>
<bean:define id="urlView">/acquisitionProcess.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<bean:write name="acquisitionProcess" property="OID"/></bean:define>
<fr:edit id="acquisitionProcess"
		name="acquisitionProcess"
		type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess"
		schema="allocateFunds"
		action="<%= urlView %>">
	<fr:layout name="tabular">
		<fr:destination name="cancel" path="<%= urlView %>" />
	</fr:layout>
</fr:edit>
