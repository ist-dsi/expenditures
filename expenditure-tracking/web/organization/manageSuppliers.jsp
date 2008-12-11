<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="supplier.title.manage" bundle="ORGANIZATION_RESOURCES"/></h2>

<div class="mbottom15">
	<fr:edit id="supplierBean" action="/organization.do?method=manageSuppliers"
			name="supplierBean"
			type="pt.ist.expenditureTrackingSystem.domain.dto.SupplierBean"
			schema="supplierBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
		<fr:destination name="cancel" path="/organization.do?method=manageSuppliers"/>
	</fr:edit>
</div>

<logic:present role="MANAGER,ACQUISITION_CENTRAL_MANAGER,ACQUISITION_CENTRAL,SUPPLIER_MANAGER">
	<p>
		<html:link action="/organization.do?method=prepareCreateSupplier">
			<bean:message key="supplier.link.create" bundle="ORGANIZATION_RESOURCES"/>
		</html:link>
	</p>
	<p>
		<html:link action="/organization.do?method=listSuppliers">
			<bean:message key="supplier.link.list" bundle="ORGANIZATION_RESOURCES"/>
		</html:link>
	</p>
</logic:present>

<logic:present name="supplierBean" property="supplier">
	<div class="infoop2">
		<fr:view name="supplierBean" property="supplier"
				type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"
				schema="viewSupplier">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
			</fr:layout>
		</fr:view>
	</div>
	<bean:define id="supplierOID" name="supplierBean" property="supplier.OID"/>
	<logic:present role="MANAGER,SUPPLIER_MANAGER">
		<p>
			<html:link action='<%= "/organization.do?method=prepareEditSupplier&supplierOid=" + supplierOID%>'>
				<bean:message key="supplier.link.edit" bundle="ORGANIZATION_RESOURCES"/>
			</html:link>
			<logic:present role="MANAGER">
			| 
				<html:link action='<%= "/organization.do?method=deleteSupplier&supplierOid=" + supplierOID%>'>
					<bean:message key="supplier.link.delete" bundle="ORGANIZATION_RESOURCES"/>
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

	<logic:present role="MANAGER,ACQUISITION_CENTRAL_MANAGER,ACQUISITION_CENTRAL">
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
