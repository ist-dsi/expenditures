<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2>
	<bean:message key="supplier.link.list.cpv" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
</h2>

<jsp:include page="suppliersHeader.jsp"/>

<fr:view name="cvpReferences">
	<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.CreateRefundProcessBean" bundle="EXPENDITURE_ORGANIZATION_RESOURCES">
   		<fr:slot name="code" key="label.cvpReferences.code"/>
   		<fr:slot name="description" key="label.cvpReferences.description"/>
   		<fr:slot name="eligibleForDeductibleVat" key="label.cvpReferences.eligibleForDeductibleVat"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="columnClasses" value="aleft,,,,aright,"/>
	</fr:layout>
</fr:view>

<!-- 
		<fr:property name="sortBy" value="invoiceDate,invoiceNumber=asc"/>
 -->
