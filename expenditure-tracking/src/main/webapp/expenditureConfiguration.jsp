<%@page import="module.organization.domain.OrganizationalModel"%>
<%@page import="org.fenixedu.bennu.core.util.CoreConfiguration"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="org.fenixedu.bennu.core.domain.Bennu"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%
	final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();
	request.setAttribute("expenditureTrackingSystem", expenditureTrackingSystem);
%>

<h2><bean:message key="link.topBar.configuration" bundle="EXPENDITURE_RESOURCES"/></h2>

<h3><bean:message key="link.topBar.configuration.virtual.hosts" bundle="EXPENDITURE_RESOURCES"/></h3>

<%
	if (expenditureTrackingSystem != null) {
%>

<%
	if (expenditureTrackingSystem.getTopLevelUnitsSet().isEmpty()) {
	    for (final OrganizationalModel model : Bennu.getInstance().getOrganizationalModelsSet()) {	        
%>
			<%= model.getName().getContent() %>
			<html:link action="<%= "/expenditureConfiguration.do?method=selectOrganizationalModel&amp;modelId=" + model.getExternalId() %>">
				<bean:message key="label.select" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
<%
	    }
	}
%>

<br/>

<h2>
	<bean:message key="label.configuration.server" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
	<%= CoreConfiguration.getConfiguration().applicationUrl() %>
</h2>

<h3>
	<bean:message key="label.configuration.general" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
</h3>

<form action="<%= request.getContextPath() + "/expenditureConfiguration.do" %>" method="post">
	<html:hidden property="method" value="saveConfiguration"/>

	<h4>
		<bean:message key="label.configuration.process.institutionalProcessNumberPrefix" bundle="EXPENDITURE_RESOURCES"/>
	</h4>
	<input type="text" name="institutionalProcessNumberPrefix" size="50"
		<%
			if (expenditureTrackingSystem.getInstitutionalProcessNumberPrefix() != null && !expenditureTrackingSystem.getInstitutionalProcessNumberPrefix().isEmpty()) {
		%>
				value="<%= expenditureTrackingSystem.getInstitutionalProcessNumberPrefix() %>"
		<%
			}
		%>
	/>

	<h4>
		<bean:message key="label.configuration.process.institutionalRequestDocumentPrefix" bundle="EXPENDITURE_RESOURCES"/>
	</h4>
	<input type="text" name="institutionalRequestDocumentPrefix" size="50"
		<%
			if (expenditureTrackingSystem.getInstitutionalRequestDocumentPrefix() != null && !expenditureTrackingSystem.getInstitutionalRequestDocumentPrefix().isEmpty()) {
		%>
				value="<%= expenditureTrackingSystem.getInstitutionalRequestDocumentPrefix() %>"
		<%
			}
		%>
	/>

	<h4>
		<bean:message key="label.configuration.process.creation.interface" bundle="EXPENDITURE_RESOURCES"/>
	</h4>
	<input type="text" name="acquisitionCreationWizardJsp" size="50"
		<%
			if (expenditureTrackingSystem.getAcquisitionCreationWizardJsp() != null && !expenditureTrackingSystem.getAcquisitionCreationWizardJsp().isEmpty()) {
		%>
				value="<%= expenditureTrackingSystem.getAcquisitionCreationWizardJsp() %>"
		<%
			}
		%>
	/>

	<h4>
		<bean:message key="label.configuration.process.search.types" bundle="EXPENDITURE_RESOURCES"/>
	</h4>
	<table>
	<%
		for (final SearchProcessValues value : SearchProcessValues.values()) {
	%>
			<tr>
				<td>
					<%= value.getLocalizedName() %>
				</td>
				<td>
					<input type="checkbox" name="<%= value.name() %>"
						<% if (expenditureTrackingSystem.contains(value)) {%>
							checked="checked"
						<% } %>
					/>
				</td>
			</tr>
	<%
		}
	%>
	</table>

	<h4>
		<bean:message key="label.configuration.process.flow" bundle="EXPENDITURE_RESOURCES"/>
	</h4>
	<table>
        <tr>
            <td>
                <bean:message key="label.configuration.process.prior.consultation.available" bundle="EXPENDITURE_RESOURCES"/>
            </td>
            <td>
                <input type="checkbox" name="isPriorConsultationAvailable"
                    <% if (ExpenditureTrackingSystem.isPriorConsultationAvailable()) {%>
                            checked="checked"
                    <% } %>
                />
            </td>
        </tr>

		<tr>
			<td>
				<bean:message key="label.configuration.process.flow.start.with.invoice" bundle="EXPENDITURE_RESOURCES"/>
			</td>
			<td>
				<input type="checkbox" name="invoiceAllowedToStartAcquisitionProcess"
					<% if (expenditureTrackingSystem.getInvoiceAllowedToStartAcquisitionProcess() != null
								&& expenditureTrackingSystem.getInvoiceAllowedToStartAcquisitionProcess().booleanValue()) {%>
							checked="checked"
					<% } %>
				/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.configuration.process.flow.start.with.invoice.limit" bundle="EXPENDITURE_RESOURCES"/>
			</td>
			<td>
				<input type="text" name="maxValueStartedWithInvoive"
					<% if (expenditureTrackingSystem.getMaxValueStartedWithInvoive() != null) {%>
							value="<%= expenditureTrackingSystem.getMaxValueStartedWithInvoive().getValue() %>"
					<% } %>
						/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.configuration.process.flow.require.fund.allocation.prior.to.acquisition.request"
						bundle="EXPENDITURE_RESOURCES"/>
			</td>
			<td>
				<input type="checkbox" name="requireFundAllocationPriorToAcquisitionRequest"
					<% if (expenditureTrackingSystem.getRequireFundAllocationPriorToAcquisitionRequest() != null
								&& expenditureTrackingSystem.getRequireFundAllocationPriorToAcquisitionRequest().booleanValue()) {%>
							checked="checked"
					<% } %>
				/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.configuration.process.flow.register.diary.numbers.and.transaction.numbers"
						bundle="EXPENDITURE_RESOURCES"/>
			</td>
			<td>
				<input type="checkbox" name="registerDiaryNumbersAndTransactionNumbers"
					<% if (expenditureTrackingSystem.getRegisterDiaryNumbersAndTransactionNumbers() != null
								&& expenditureTrackingSystem.getRegisterDiaryNumbersAndTransactionNumbers().booleanValue()) {%>
							checked="checked"
					<% } %>
				/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.configuration.process.value.requireing.top.level.authorization" bundle="EXPENDITURE_RESOURCES"/>
			</td>
			<td>
				<input type="text" name="valueRequireingTopLevelAuthorization"
					<% if (expenditureTrackingSystem.getValueRequireingTopLevelAuthorization() != null) {%>
							value="<%= expenditureTrackingSystem.getValueRequireingTopLevelAuthorization().getValue() %>"
					<% } %>
						/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.configuration.process.flow.requireCommitmentNumber"
						bundle="EXPENDITURE_RESOURCES"/>
			</td>
			<td>
				<input type="checkbox" name="requireCommitmentNumber"
					<% if (expenditureTrackingSystem.getRequireCommitmentNumber() != null
								&& expenditureTrackingSystem.getRequireCommitmentNumber().booleanValue()) {%>
							checked="checked"
					<% } %>
				/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.configuration.process.flow.processesNeedToBeReverified"
						bundle="EXPENDITURE_RESOURCES"/>
			</td>
			<td>
				<input type="checkbox" name="processesNeedToBeReverified"
					<% if (expenditureTrackingSystem.getProcessesNeedToBeReverified() != null
								&& expenditureTrackingSystem.getProcessesNeedToBeReverified().booleanValue()) {%>
							checked="checked"
					<% } %>
				/>
			</td>
		</tr>
	</table>

	<h4>
		<bean:message key="label.configuration.process.documentation" bundle="EXPENDITURE_RESOURCES"/>
	</h4>
	<table>
		<tr>
			<td>
				<bean:message key="label.configuration.process.documentation.documentationUrl" bundle="EXPENDITURE_RESOURCES"/>
			</td>
			<td>
				<input type="text" name="documentationUrl" size="50"
					<% if (expenditureTrackingSystem.getDocumentationUrl() != null) {%>
							value="<%= expenditureTrackingSystem.getDocumentationUrl() %>"
					<% } %>
						/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.configuration.process.documentation.documentationLabel" bundle="EXPENDITURE_RESOURCES"/>
			</td>
			<td>
				<input type="text" name="documentationLabel" size="50"
					<% if (expenditureTrackingSystem.getDocumentationLabel() != null) {%>
							value="<%= expenditureTrackingSystem.getDocumentationLabel() %>"
					<% } %>
						/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.configuration.process.documentation.createSupplierUrl" bundle="EXPENDITURE_RESOURCES"/>
			</td>
			<td>
				<input type="text" name="createSupplierUrl" size="50"
					<% if (expenditureTrackingSystem.getCreateSupplierUrl() != null) {%>
							value="<%= expenditureTrackingSystem.getCreateSupplierUrl() %>"
					<% } %>
						/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.configuration.process.documentation.createSupplierLabel" bundle="EXPENDITURE_RESOURCES"/>
			</td>
			<td>
				<input type="text" name="createSupplierLabel" size="50"
					<% if (expenditureTrackingSystem.getCreateSupplierLabel() != null) {%>
							value="<%= expenditureTrackingSystem.getCreateSupplierLabel() %>"
					<% } %>
						/>
			</td>
		</tr>
	</table>

	<html:submit styleClass="inputbutton">
		<bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/>
	</html:submit>
</form>

<br/>
<br/>

<h3>
	<bean:message key="label.configuration.organizational.estructure" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
</h3>

<fr:form action="/expenditureConfiguration.do?method=viewConfiguration">

	<fr:edit id="expenditureTrackingSystem" name="expenditureTrackingSystem">
		<fr:schema type="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem" bundle="EXPENDITURE_ORGANIZATION_RESOURCES">
			<fr:slot name="organizationalAccountabilityType" layout="menu-select" key="label.organizationalAccountabilityType" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"
					required="true">
				<fr:property name="providerClass" value="module.organization.presentationTier.renderers.providers.AccountabilityTypesProvider" />
				<fr:property name="format" value="\${name.content}" />
				<fr:property name="sortBy" value="name" />
				<fr:property name="saveOptions" value="true"/>
			</fr:slot>
			<fr:slot name="organizationalMissionAccountabilityType" layout="menu-select" key="label.organizationalMissionAccountabilityType" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"
					required="true">
				<fr:property name="providerClass" value="module.organization.presentationTier.renderers.providers.AccountabilityTypesProvider" />
				<fr:property name="format" value="\${name.content}" />
				<fr:property name="sortBy" value="name" />
				<fr:property name="saveOptions" value="true"/>
			</fr:slot>
			<fr:slot name="unitPartyType" layout="menu-select" key="label.unitPartyType" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"
					required="true">
				<fr:property name="providerClass" value="module.organization.presentationTier.renderers.providers.PartyTypesProvider" />
				<fr:property name="format" value="\${name.content}" />
				<fr:property name="sortBy" value="name" />
			</fr:slot>
			<fr:slot name="costCenterPartyType" layout="menu-select" key="label.costCenterPartyType" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"
					required="true">
				<fr:property name="providerClass" value="module.organization.presentationTier.renderers.providers.PartyTypesProvider" />
				<fr:property name="format" value="\${name.content}" />
				<fr:property name="sortBy" value="name" />
			</fr:slot>
			<fr:slot name="projectPartyType" layout="menu-select" key="label.projectPartyType" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"
					required="true">
				<fr:property name="providerClass" value="module.organization.presentationTier.renderers.providers.PartyTypesProvider" />
				<fr:property name="format" value="\${name.content}" />
				<fr:property name="sortBy" value="name" />
			</fr:slot>
			<fr:slot name="subProjectPartyType" layout="menu-select" key="label.subProjectPartyType" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"
					required="true">
				<fr:property name="providerClass" value="module.organization.presentationTier.renderers.providers.PartyTypesProvider" />
				<fr:property name="format" value="\${name.content}" />
				<fr:property name="sortBy" value="name" />
			</fr:slot>
			<fr:slot name="institutionManagementEmail" key="label.institutionManagementEmail" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"
					required="true"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form mbottom0 mtop15"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:edit>

	<p class="mtop05">
		<html:submit styleClass="inputbutton">
			<bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/>
		</html:submit>
	</p>

</fr:form>


<% } %>
