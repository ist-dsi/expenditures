<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.view.acquisition.request.item" bundle="ACQUISITION_RESOURCES"/></h2>
<br />

<bean:define id="acquisitionRequestItem"
		name="acquisitionRequestItem"
		type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem"
		/>
<fr:view name="acquisitionRequestItem"
		type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem"
		schema="viewAcquisitionRequestItem">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:view>
<html:link action="/acquisitionProcess.do?method=editAcquisitionRequestItem" paramId="acquisitionRequestItemOid" paramName="acquisitionRequestItem" paramProperty="OID">
	<bean:message key="link.edit" bundle="EXPENDITURE_RESOURCES"/>
</html:link>
<html:link action="/acquisitionProcess.do?method=deleteAcquisitionRequestItem" paramId="acquisitionRequestItemOid" paramName="acquisitionRequestItem" paramProperty="OID">
	<bean:message key="link.delete" bundle="EXPENDITURE_RESOURCES"/>
</html:link>
