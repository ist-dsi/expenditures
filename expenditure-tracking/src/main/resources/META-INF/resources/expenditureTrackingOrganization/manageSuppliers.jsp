<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<h2><bean:message key="supplier.title.manage" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h2>


<jsp:include page="suppliersHeader.jsp"/>


<p class="mvert05"><strong><bean:message key="label.search" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></strong></p>

<div class="mbottom15">
	<fr:form action="/expenditureTrackingOrganization.do?method=manageSuppliers">
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
	<bean:define id="supplier" name="supplierBean" property="supplier" type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"/>
	<bean:message key="supplier.soft.limit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>: <%= supplier.getSupplierLimit().toFormatString() %>

	<div class="infobox">
		<fr:view name="supplierBean" property="supplier"
				type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"
				schema="viewSupplier">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="rowClasses" value=",tdbold,,,,,,,"/>
			</fr:layout>
		</fr:view>
		<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL_MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL,pt.ist.expenditureTrackingSystem.domain.RoleType.SUPPLIER_MANAGER">
			<logic:present name="supplierBean" property="supplier.giafKey">
				<logic:notEmpty name="supplierBean" property="supplier.giafKey">
					<bean:message key="label.supplier.giaf.key" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
					<bean:write name="supplierBean" property="supplier.giafKey"/>
				</logic:notEmpty>
				<logic:empty name="supplierBean" property="supplier.giafKey">
					<font color="red">
						<bean:message key="label.supplier.giaf.key.does.not.exist" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
					</font>
				</logic:empty>
			</logic:present>
			<logic:notPresent name="supplierBean" property="supplier.giafKey">
				<font color="red">
					<bean:message key="label.supplier.giaf.key.does.not.exist" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
				</font>
			</logic:notPresent>
		</logic:present>
		<br/>
		<br/>
		<h3><bean:message key="supplier.contacts.title" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
		<logic:empty name="supplierBean" property="supplier.supplierContactSet">
			<span>
				<bean:message key="supplier.contacts.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</span>
		</logic:empty>
		<logic:iterate id="supplierContact" name="supplierBean" property="supplier.supplierContactSet">
			<div class="ruler1">
			<fr:view name="supplierContact" type="module.finance.domain.SupplierContact">
				<fr:schema type="module.finance.domain.SupplierContact" bundle="EXPENDITURE_ORGANIZATION_RESOURCES">
					<fr:slot name="address" key="supplier.label.address" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
					<fr:slot name="phone" key="supplier.label.phone" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
					<fr:slot name="fax" key="supplier.label.fax" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
					<fr:slot name="email" key="supplier.label.email" bundle="EXPENDITURE_ORGANIZATION_RESOURCES">
						<fr:property name="size" value="40"/>
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1"/>
					<fr:property name="rowClasses" value=",,,,,,,,"/>
				</fr:layout>
			</fr:view>
			</div>
		</logic:iterate>
	</div>

	<bean:define id="supplierOID" name="supplier" property="externalId" type="java.lang.String"/>
	<p>
		<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.SUPPLIER_MANAGER">
			<html:link action='<%= "/expenditureTrackingOrganization.do?method=prepareEditSupplier&supplierOid=" + supplierOID%>'>
				<bean:message key="supplier.link.edit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</logic:present>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL">
				<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
					| 
				</logic:present>
				<html:link action='<%= "/expenditureTrackingOrganization.do?method=editSupplierLimit&supplierOid=" + supplierOID%>'>
					<bean:message key="supplier.link.edit.limit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</html:link>
					| 
				<html:link action='<%= "/expenditureTrackingOrganization.do?method=downloadSupplierAcquisitionInformation&supplierOid=" + supplierOID%>'>
					<bean:message key="supplier.link.export.aquisition.information" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</html:link>
			</logic:present>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
				| 
				<html:link action='<%= "/expenditureTrackingOrganization.do?method=deleteSupplier&supplierOid=" + supplierOID%>'>
					<bean:message key="supplier.link.delete" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</html:link>
			</logic:present>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
				| 
				<html:link action='<%= "/expenditureTrackingOrganization.do?method=prepareMergeSupplier&supplierToTransferOID=" + supplierOID%>'>
					<bean:message key="supplier.link.merge" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</html:link>
			</logic:present>
	</p>

	<div class="infobox">
		<fr:view name="supplierBean" property="supplier"
				type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"
				schema="viewSupplierAcquisitionInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
			</fr:layout>
		</fr:view>
	</div>

	<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL_MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL">
		<bean:define id="aquisitions" name="supplierBean" property="supplier.acquisitionsAfterTheFactSet"/>
		<fr:view name="aquisitions"
				schema="acquisitionAfterTheFact">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2"/>
				<fr:property name="columnClasses" value="aleft,,,,aright,"/>
				<fr:property name="sortBy" value="invoiceDate,invoiceNumber=asc"/>
				<fr:property name="link(view)" value="/workflowProcessManagement.do?method=viewProcess"/>
				<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="param(view)" value="afterTheFactAcquisitionProcess.externalId/processId"/>
				<fr:property name="order(view)" value="1"/>
			</fr:layout>
		</fr:view>
	</logic:present>
</logic:present>
