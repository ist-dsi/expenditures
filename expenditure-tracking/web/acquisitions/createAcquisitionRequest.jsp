<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.view.acquisition.process" bundle="EXPENDITURE_RESOURCES"/></h2>

<bean:message key="label.acquisition.requester" bundle="ACQUISITION_RESOURCES"/>
<fr:view name="acquisitionProcess" property="acquisitionRequest.requester"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
		schema="viewAcquisitionRequester">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:view>

<bean:message key="label.acquisition.supplier" bundle="ACQUISITION_RESOURCES"/>
<fr:view name="acquisitionProcess" property="acquisitionRequest.supplier"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"
		schema="viewAcquisitionSupplier">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:view>

<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet">
	<fr:view name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet"
			schema="viewAcquisitionRequestItemInListFull">
		<fr:layout name="tabular">
		</fr:layout>
	</fr:view>
</logic:present>

<html:link action="/acquisitionProcess.do?method=createAcquisitionRequestDocument" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
	<bean:message key="link.create.acquisition.request.document" bundle="ACQUISITION_RESOURCES"/>
</html:link>
	
<bean:define id="acquisitionProcessOid" name="acquisitionProcess" property="OID"/>

<fr:form action='<%= "/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid=" +  acquisitionProcessOid %>'>
	<html:submit>
		<bean:message key="button.back" bundle="ACQUISITION_RESOURCES"/>
	</html:submit>
</fr:form>