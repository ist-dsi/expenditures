<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

<h3 class="mtop15 mbottom1"><bean:message key="title.internalRequest.items" bundle="INTERNAL_REQUEST_RESOURCES"/></h3>


<wf:activityLink processName="process" activityName="AddItemActivity" scope="request">
	<bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="activity.AddItemActivity"/>
</wf:activityLink>

<logic:empty name="process" property="internalRequest.itemsSet">
	<div class="alert alert-warning" role="alert">
		<bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="info.items.none"/>
	</div>
</logic:empty>

<logic:notEmpty name="process" property="internalRequest.itemsSet">
	<table class="tstyle3 thleft tdleft mbottom2" style="width: 100%;" id="itemResume">
		<tr>
			<th class="col-sm-1"><bean:message key="label.internalRequest.item" bundle="INTERNAL_REQUEST_RESOURCES"/></th>
			<th class="col-sm-1"><bean:message key="label.internalRequest.item.qty" bundle="INTERNAL_REQUEST_RESOURCES"/></th>
			<th class="col-sm-3"><bean:message key="label.internalRequest.item.description" bundle="INTERNAL_REQUEST_RESOURCES"/></th>
			<th class="col-sm-2"><bean:message key="label.internalRequest.item.price" bundle="INTERNAL_REQUEST_RESOURCES"/></th>
			<th class="col-sm-3"><bean:message key="label.internalRequest.item.observations" bundle="INTERNAL_REQUEST_RESOURCES"/></th>
			<th class="col-sm-2"></th>
		</tr>

		<logic:iterate id="item" indexId="counter" name="process" property="internalRequest.sortedItemsSet">
			<bean:define id="itemOID" name="item" property="externalId" type="java.lang.String"/>
			<tr>
				<td>
					<bean:message key="label.internalRequest.item" bundle="INTERNAL_REQUEST_RESOURCES"/>
					<%= counter + 1 %>
				</td>
				<td><fr:view name="item" property="quantity"/></td>
				<td><fr:view name="item" property="description"/></td>
				<td>
					<logic:present name="item" property="price">
						<fr:view name="item" property="price"/>
					</logic:present>
				</td>
				<td>
					<logic:present name="item" property="observations">
						<fr:view name="item" property="observations"/>
					</logic:present>
				</td>
				<td>
					<wf:activityLink id='<%= "removeLink" + itemOID %>' processName="process" activityName="RemoveItemActivity" scope="request" paramName0="item" paramValue0="<%= itemOID %>">
						<bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="activity.RemoveItemActivity"/>
					</wf:activityLink>

					<wf:activityLink id='<%= "budgetLink" + itemOID %>' processName="process" activityName="BudgetItemActivity" scope="request" paramName0="item" paramValue0="<%= itemOID %>">
						<bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="activity.BudgetItemActivity"/>
					</wf:activityLink>
				</td>
			</tr>
		</logic:iterate>
			<tr>
				<th></th>
				<th></th>
				<th style="text-align: right !important;"><bean:message key="label.internalRequest.item.total" bundle="INTERNAL_REQUEST_RESOURCES"/>:</th>
				<th><fr:view name="process" property="internalRequest.totalPrice"/></th>
				<th></th>
				<th></th>
			</tr>
	</table>
</logic:notEmpty>
