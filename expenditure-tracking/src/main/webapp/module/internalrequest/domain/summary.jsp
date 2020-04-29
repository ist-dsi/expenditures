<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="module.organization.domain.OrganizationalModel"%>
<%@page import="org.fenixedu.bennu.core.domain.Bennu"%>

<div class="infobox">
	<table width="100%">
		<tr>
			<td width="20%">	
				<span class="processNumber">
					<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="title.internalRequest.process"/>:</b>
				</span>
			</td>
			<td width="80%">
				<bean:write name="process" property="processIdentification"/>
			</td>
		</tr>
		<tr>
			<td width="20%">	
				<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.requestingUnit"/>:</b>
			</td>
			<td width="80%">
				<logic:present name="process" property="internalRequest.requestingUnit"><fr:view name="process" property="internalRequest.requestingUnit.name"/></logic:present>
			</td>
		</tr>
		<tr>
			<td width="20%">
				<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.requestedUnit"/>:</b>
			</td>
			<td width="80%">
				<logic:present name="process" property="internalRequest.requestedUnit"><fr:view name="process" property="internalRequest.requestedUnit.name"/></logic:present>
			</td>
		</tr>
	</table>
	<logic:present name="process" property="internalRequest.approvedBy">
		<hr>
	</logic:present>
	<table width="100%">
		<logic:present name="process" property="internalRequest.approvedBy">
			<tr>
				<td width="20%">	
					<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.approvedBy"/>:</b>
				</td>
				<td width="50%">
					<fr:view name="process" property="internalRequest.approvedBy.name"/>
				</td>
				<td width="20%">
					<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.approvalDate"/>:</b>
				</td>
				<td width="10%">
					<fr:view name="process" property="internalRequest.approvalDate"/>
				</td>
			</tr>
		</logic:present>
		<logic:present name="process" property="internalRequest.budgetedBy">
			<tr>
				<td width="20%">	
					<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.budgetedBy"/>:</b>
				</td>
				<td width="50%">
					<fr:view name="process" property="internalRequest.budgetedBy.name"/>
				</td>
				<td width="20%">
					<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.budgetDate"/>:</b>
				</td>
				<td width="10%">
					<fr:view name="process" property="internalRequest.budgetDate"/>
				</td>
			</tr>
		</logic:present>
		<logic:present name="process" property="internalRequest.authorizedBy">
			<tr>
				<td width="20%">	
					<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.authorizedBy"/>:</b>
				</td>
				<td width="50%">
					<fr:view name="process" property="internalRequest.authorizedBy.name"/>
				</td>
				<td width="20%">
					<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.authorizationDate"/>:</b>
				</td>
				<td width="10%">
					<fr:view name="process" property="internalRequest.authorizationDate"/>
				</td>
			</tr>
		</logic:present>
		<logic:present name="process" property="internalRequest.processedBy">
			<tr>
				<td width="20%">	
					<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.processedBy"/>:</b>
				</td>
				<td width="50%">
					<fr:view name="process" property="internalRequest.processedBy.name"/>
				</td>
				<td width="20%">
					<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.processDate"/>:</b>
				</td>
				<td width="10%">
					<fr:view name="process" property="internalRequest.processDate"/>
				</td>
			</tr>
		</logic:present>
		<logic:present name="process" property="internalRequest.deliveredBy">
			<tr>
				<td width="20%">	
					<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.deliveredBy"/>:</b>
				</td>
				<td width="50%">
					<fr:view name="process" property="internalRequest.deliveredBy.name"/>
				</td>
				<td width="20%">
					<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.deliveryDate"/>:</b>
				</td>
				<td width="10%">
					<fr:view name="process" property="internalRequest.deliveryDate"/>
				</td>
			</tr>
		</logic:present>
		<logic:present name="process" property="internalRequest.imputedBy">
			<tr>
				<td width="20%">	
					<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.imputedBy"/>:</b>
				</td>
				<td width="50%">
					<fr:view name="process" property="internalRequest.imputedBy.name"/>
				</td>
				<td width="20%">
					<b><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="label.internalRequest.costImputationDate"/>:</b>
				</td>
				<td width="10%">
					<fr:view name="process" property="internalRequest.costImputationDate"/>
				</td>
			</tr>
		</logic:present>

	</table>
</div>
