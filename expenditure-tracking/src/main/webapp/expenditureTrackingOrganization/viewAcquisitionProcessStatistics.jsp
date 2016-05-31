<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="user" name="userAcquisitionProcessStatistics" property="user"/>
<bean:define id="userOid" name="user" property="externalId"/>
<bean:define id="person" name="user" property="expenditurePerson"/>

<h2><bean:message key="user.label.view" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/> <bean:write name="person" property="username"/></h2>

<div class="infobox">
	<table style="width: 100%;">
		<tr>
			<td style="vertical-align: top;">
				<fr:view name="person"
						type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
						schema="viewPerson">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
						<fr:property name="columnClasses" value=",,tderror"/>
					</fr:layout>
				</fr:view>
			</td>
			<td style="text-align: right;">
				<% if (((User) user).getProfile() != null) { %>
					<html:img src="<%= ((User) user).getProfile().getAvatarUrl() %>"
						align="middle" styleClass="float: right; border: 1px solid #aaa; padding: 3px;" />
				<% } %>
			</td>
		</tr>
	</table>
</div>

<div class="dinline forminline">
	<fr:form action='<%= "/expenditureTrackingOrganization.do?method=viewAcquisitionProcessStatistics&userOid=" + userOid %>'>
		<fr:edit id="userAcquisitionProcessStatistics" name="userAcquisitionProcessStatistics">
			<fr:schema type="pt.ist.expenditureTrackingSystem.domain.organization.UserAcquisitionProcessStatistics" bundle="EXPENDITURE_ORGANIZATION_RESOURCES">
    			<fr:slot name="paymentProcessYear" key="label.userAcquisitionProcessStatistics.paymentProcessYear" layout="menu-select-postback">
					<fr:property name="providerClass" value="pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider.PaymentProcessYearProvider" />
					<fr:property name="format" value="\${year}" />
					<fr:property name="sortBy" value="year" />
    			</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form"/>
				<fr:property name="columnClasses" value=",,tderror"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
</div>

<table class="tstyle2">
	<tr>
		<th>
			<bean:message bundle="ACQUISITION_RESOURCES" key="label.userAcquisitionProcessStatistics.processType"/>
		</th>
		<th>
			<bean:message bundle="EXPENDITURE_ORGANIZATION_RESOURCES" key="label.userAcquisitionProcessStatistics.numberOfParticipatedProcesses"/>
		</th>
		<th>
			<bean:message bundle="EXPENDITURE_ORGANIZATION_RESOURCES" key="label.userAcquisitionProcessStatistics.numberOfActivities"/>
		</th>
		<th>
			<bean:message bundle="EXPENDITURE_ORGANIZATION_RESOURCES" key="label.userAcquisitionProcessStatistics.averateaActivityDuration"/>
		</th>
	</tr>
	<logic:iterate id="entry" name="userAcquisitionProcessStatistics" property="processTypeMap">
		<tr>
			<td>
				<fr:view name="entry" property="value.processType"/>
			</td>
			<td>
				<fr:view name="entry" property="value.numberOfParticipatedProcesses"/>
			</td>
			<td>
				<fr:view name="entry" property="value.numberOfActivities"/>
			</td>
			<td>
				<fr:view name="entry" property="value.averateaActivityDuration"/>
			</td>
		</tr>
	</logic:iterate>
	<tr>
		<th>
			<bean:message bundle="ACQUISITION_RESOURCES" key="label.userAcquisitionProcessStatistics.total"/>
		</th>
		<td>
			<fr:view name="userAcquisitionProcessStatistics" property="numberOfParticipatedProcesses"/>
		</td>
		<td>
			<fr:view name="userAcquisitionProcessStatistics" property="numberOfActivities"/>
		</td>
		<td>
			<fr:view name="userAcquisitionProcessStatistics" property="averateaActivityDuration"/>
		</td>
	</tr>
</table>
