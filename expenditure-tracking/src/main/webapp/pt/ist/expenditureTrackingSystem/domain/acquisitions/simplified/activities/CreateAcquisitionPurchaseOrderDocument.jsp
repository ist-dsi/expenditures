<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<h3>
	<bean:message key="label.select.address" bundle="ACQUISITION_RESOURCES"/>
</h3>

	<logic:iterate id="supplierContact" name="process" property="request.supplier.supplierContactSet" type="module.finance.domain.SupplierContact">
		<blockquote>
			<br/>
			<bean:define id="address" name="supplierContact" property="address"/>
			<bean:write name="address" property="line1"/>
			<br/>
			<logic:notEmpty name="address" property="line2">
				<bean:write name="address" property="line2"/>
				<br/>
			</logic:notEmpty>
			<bean:write name="address" property="postalCode"/> - <bean:write name="address" property="location"/>
			<br/>
			<bean:write name="address" property="country"/>
			<br/>
		</blockquote>
		<div class="ruler1">
			<bean:define id="url" type="java.lang.String">/workflowProcessManagement.do?method=actionLink&amp;activity=CreateAcquisitionPurchaseOrderDocument&amp;processId=<bean:write name="process" property="externalId"/>&amp;parameters=supplierContact&amp;supplierContact=<bean:write name="supplierContact" property="externalId"/></bean:define>
			<html:link action="<%= url %>">
				<bean:message key="link.select" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
			<br/>
			<br/>
		</div>
	</logic:iterate>
