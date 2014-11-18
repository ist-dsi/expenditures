<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="supplier.link.merge" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h2>

<p class="mvert05"><strong><bean:message key="label.supplier.to.transfer" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></strong></p>

	<div class="infobox">
		<fr:view name="supplierToTransfer"
				type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"
				schema="viewSupplier">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="rowClasses" value=",tdbold,,,,,,,"/>
			</fr:layout>
		</fr:view>
	</div>

	<div class="infobox">
		<fr:view name="supplierToTransfer"
				type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"
				schema="viewSupplierAcquisitionInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
			</fr:layout>
		</fr:view>
	</div>

<div class="mbottom15">
	<bean:define id="url">/expenditureTrackingOrganization.do?method=prepareMergeSupplier&amp;supplierToTransferOID=<bean:write name="supplierToTransfer" property="externalId"/></bean:define>
	<fr:form action="<%= url %>">
		<fr:edit id="supplierBean" 
				name="supplierBean"
				type="pt.ist.expenditureTrackingSystem.domain.dto.SupplierBean"
				schema="supplierBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="form mtop05 mbottom1"/>
				<fr:property name="columnClasses" value=",,tderror"/>
			</fr:layout>
		</fr:edit>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
	</fr:form>
</div>


<logic:present name="supplierBean" property="supplier">
	<p class="mvert05"><strong><bean:message key="label.supplier.destination" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></strong></p>

	<div class="infobox">
		<fr:view name="supplierBean" property="supplier"
				type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"
				schema="viewSupplier">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="rowClasses" value=",tdbold,,,,,,,"/>
			</fr:layout>
		</fr:view>
	</div>

	<div class="infobox">
		<fr:view name="supplierBean" property="supplier"
				type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"
				schema="viewSupplierAcquisitionInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
			</fr:layout>
		</fr:view>
	</div>

	<p>
		<bean:define id="supplierToTransferOID" name="supplierToTransfer" property="externalId" type="java.lang.String"/>
		<bean:define id="supplierDestinationOID" name="supplierBean" property="supplier.externalId" type="java.lang.String"/>
		<html:link action='<%= "/expenditureManageSuppliers.do?method=mergeSupplier&amp;supplierToTransferOID=" + supplierToTransferOID + "&amp;supplierDestinationOID=" + supplierDestinationOID%>'>
			<bean:message key="supplier.link.merge.into.this" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
		</html:link>
	</p>

</logic:present>
