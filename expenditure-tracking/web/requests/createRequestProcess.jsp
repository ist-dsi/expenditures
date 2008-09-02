<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- requests/createRequestProcess.jsp -->
requests/createRequestProcess.jsp

<h2><bean:message key="label.create.requestForProposal" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="dinline forminline">
		<fr:edit id="requestForProposalBean"
				name="requestForProposalBean"
				schema="requestForProposalBean" action="/requestForProposalProcess.do?method=createNewRequestForProposalProcess">
				
			<fr:layout name="tabular">
				<fr:property name="classes" value=""/>
				<fr:property name="columnClasses" value=",,tderror"/>
				<fr:property name="requiredMarkShown" value="true"/>
			</fr:layout>
			<fr:destination name="cancel" path="/requestForProposalProcess.do?method=showPendingRequests"/>
		</fr:edit>
	
		<p>
		<em><bean:message key="label.fieldsWith" bundle="EXPENDITURE_RESOURCES"/><span class="required">*</span><bean:message key="label.areRequired" bundle="EXPENDITURE_RESOURCES"/></em>
		</p>

</div>