<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<h2><bean:message key="supplier.title.manage" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h2>

<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL_MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL,pt.ist.expenditureTrackingSystem.domain.RoleType.SUPPLIER_MANAGER">
	<div class="infoop1">
		<ul>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.SUPPLIER_MANAGER">
			<li>
				<html:link action="/expenditureTrackingOrganization.do?method=prepareCreateSupplier">
					<bean:message key="supplier.link.create" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</html:link>		
			</li>
			</logic:present>
			<li>
				<html:link action="/expenditureTrackingOrganization.do?method=listSuppliers">
					<bean:message key="supplier.link.list" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</html:link>
			</li>
		</ul>
	</div>
</logic:present>

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

	<div class="infoop2">
		<fr:view name="supplierBean" property="supplier"
				type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"
				schema="viewSupplier">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="rowClasses" value=",tdbold,,,,,,,"/>
			</fr:layout>
		</fr:view>
	</div>

	<bean:define id="supplierOID" name="supplier" property="OID"/>
	<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.SUPPLIER_MANAGER">
		<p>
			<html:link action='<%= "/expenditureTrackingOrganization.do?method=prepareEditSupplier&supplierOid=" + supplierOID%>'>
				<bean:message key="supplier.link.edit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL">
			| 
				<html:link action='<%= "/expenditureTrackingOrganization.do?method=editSupplierLimit&supplierOid=" + supplierOID%>'>
					<bean:message key="supplier.link.edit.limit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
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
	</logic:present>

	<div class="infoop2">
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
				<fr:property name="link(view)" value="/acquisitionAfterTheFactAcquisitionProcess.do?method=viewAcquisitionProcess"/>
				<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="param(view)" value="afterTheFactAcquisitionProcess.OID/acquisitionProcessOid"/>
				<fr:property name="order(view)" value="1"/>
			</fr:layout>
		</fr:view>
	</logic:present>
</logic:present>
