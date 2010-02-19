<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
	<bean:message key="supplier.link.list.cpv" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
</h2>

<jsp:include page="suppliersHeader.jsp"/>

<fr:view name="cvpReferences">
	<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.CreateRefundProcessBean" bundle="EXPENDITURE_ORGANIZATION_RESOURCES">
   		<fr:slot name="code" key="label.cvpReferences.code"/>
   		<fr:slot name="description" key="label.cvpReferences.description"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="columnClasses" value="aleft,,,,aright,"/>
	</fr:layout>
</fr:view>

<!-- 
		<fr:property name="sortBy" value="invoiceDate,invoiceNumber=asc"/>
 -->