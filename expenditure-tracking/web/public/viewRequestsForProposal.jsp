<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- public/viewRequestsForProposal.jsp -->


<h2><bean:message key="process.requestForProposal.title.openRequests" bundle="EXPENDITURE_RESOURCES"/></h2>


<logic:empty name="activeRequests">
	<p><em><bean:message key="process.messages.info.noProcessesToOperate" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
</logic:empty>


<logic:iterate id="requestProcess" name="activeRequests" indexId="Id">
	<div class="infoop2">
		<table class="tstyle1">
			<tr>
				<th><bean:message key="label.title" bundle="EXPENDITURE_RESOURCES"/></th>
				<td><fr:view name="requestProcess" property="requestForProposal.title"/></td>
			</tr>
			<tr>
				<th><bean:message key="label.expireDate" bundle="REQUEST_RESOURCES"/></th>
				<td><fr:view name="requestProcess" property="requestForProposal.expireDate"/></td>
			</tr>
			<tr>
				<th><bean:message key="label.description" bundle="EXPENDITURE_RESOURCES"/></th>
				<td>
				
				<fr:view name="requestProcess" property="requestForProposal.description">
						<fr:layout name="short">
								<fr:property name="length" value="120"/>
						</fr:layout>
				</fr:view>
				
				
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<html:link page="<%= "/home.do?method=viewRequestForProposalProcess" %>" paramId="requestForProposalProcessOid" paramName="requestProcess" paramProperty="OID">
					<bean:message key="link.viewMore" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</td>
			</tr>
		</table>
	</div>
</logic:iterate>

<table>
		
		
		<tr>
			<th></th>

		</tr>

</table>
