<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.create.acquisition.request.document" bundle="EXPENDITURE_RESOURCES"/></h2>

<bean:message key="label.acquisition.requester" bundle="ACQUISITION_RESOURCES"/>
<div class="infoop2" style="width: 360px">
<fr:view name="acquisitionProcess" property="acquisitionRequest.requester"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
		schema="viewAcquisitionRequester">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
</fr:view>
</div>

<bean:message key="label.acquisition.supplier" bundle="ACQUISITION_RESOURCES"/>
<div class="infoop2" style="width: 360px">
<fr:view name="acquisitionProcess" property="acquisitionRequest.supplier"
		schema="viewAcquisitionSupplier">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
</fr:view>
</div>

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
	<html:submit styleClass="inputbutton">
		<bean:message key="button.back" bundle="ACQUISITION_RESOURCES"/>
	</html:submit>
</fr:form>