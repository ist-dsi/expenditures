<%@page import="pt.ist.bennu.core.domain.util.Money"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference"%>
<%@page import="java.util.Map.Entry"%>
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
	<bean:define id="supplierOID" name="supplier" property="externalId" type="java.lang.String"/>

	<div class="infobox">
		<h3>
			<bean:message key="label.supplier" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
		</h3>
		<p>
			<bean:message key="supplier.label.fiscalCode" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
			<span style="font-weight: bold;">
				<bean:write name="supplier" property="fiscalIdentificationCode"/>
			</span>
			&nbsp;&nbsp;&nbsp;
			<bean:message key="supplier.label.name" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
			<span style="font-weight: bold;">
				<bean:write name="supplier" property="name"/>
			</span>
			&nbsp;&nbsp;&nbsp;
			<bean:message key="supplier.label.abbreviatedName" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
			<bean:write name="supplier" property="abbreviatedName"/>
		</p>
		<p>
			<bean:message key="supplier.soft.limit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
			<span style="font-weight: bold;">
				<%= supplier.getSupplierLimit().toFormatString() %>
			</span>
			&nbsp;&nbsp;&nbsp;
			<bean:message key="supplier.label.nib" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
			<bean:write name="supplier" property="nib"/>
			&nbsp;&nbsp;&nbsp;
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
		</p>
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

		<h4 style="background: #EEE; padding: 5px 10px 5px 10px; margin: -5px -10px 0 -10px;">
			<bean:message key="supplier.contacts.title" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
		</h4>
		<logic:empty name="supplierBean" property="supplier.supplierContactSet">
			<span>
				<bean:message key="supplier.contacts.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</span>
		</logic:empty>
		<logic:iterate id="supplierContact" name="supplierBean" property="supplier.supplierContactSet">
			<div class="ruler1">
				<table>
					<tr>
						<td style="padding-left: 25px;">
			<fr:view name="supplierContact" type="module.finance.domain.SupplierContact">
				<fr:schema type="module.finance.domain.SupplierContact" bundle="EXPENDITURE_ORGANIZATION_RESOURCES">
					<fr:slot name="address" key="supplier.label.address" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1"/>
					<fr:property name="rowClasses" value=",,,,,,,,"/>
				</fr:layout>
			</fr:view>
						</td>
						<td style="padding-left: 25px;">
			<fr:view name="supplierContact" type="module.finance.domain.SupplierContact">
				<fr:schema type="module.finance.domain.SupplierContact" bundle="EXPENDITURE_ORGANIZATION_RESOURCES">
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
						</td>
					</tr>
				</table>
			</div>
		</logic:iterate>

		<br/>
		<h4 style="background: #EEE; padding: 5px 10px 5px 10px; margin: -5px -10px 0 -10px;">
			<bean:message key="supplier.message.info.totalAllocated" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
		</h4>
		<br/>
		<table class="tstyle2">
			<tr>
				<th>
					<bean:message key="label.cvpReferences.code" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.cvpReferences.description" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.value" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</th>
			</tr>
		<%
			Money total = Money.ZERO;
			for (final Entry<CPVReference, Money> entry : supplier.getAllocationsByCPVReference().entrySet()) {
			    final CPVReference cpvReference = entry.getKey();
			    final Money value = entry.getValue();
			    total = total.add(value);
		%>
			<tr>
				<td style="text-align: left;">
					<%= cpvReference == null ? "" : cpvReference.getCode() %>
				</td>
				<td style="text-align: left;">
					<%= cpvReference == null ? "" : cpvReference.getDescription() %>
				</td>
				<td style="text-align: right;">
					<%= value.toFormatString() %>
				</td>
			</tr>				
		<% } %>
			<tr style=" font-weight: bold;">
				<td style="text-align: left;" colspan="2">
					<bean:message key="label.total" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</td>
				<td style="text-align: right;">
					<%= total.toFormatString() %>
				</td>
			</tr>
		</table>

		<br/>
		<h4 style="background: #EEE; padding: 5px 10px 5px 10px; margin: -5px -10px 0 -10px;">
			<bean:message key="label.supplier.manually.registered.allocations" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
		</h4>
		<br/>
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
			<br/>
		</logic:present>
	</div>
</logic:present>
