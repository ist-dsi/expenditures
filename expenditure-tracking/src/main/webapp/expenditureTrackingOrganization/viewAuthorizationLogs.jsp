<%@page import="pt.ist.expenditureTrackingSystem.util.PhotoTool"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="authorizations.link.logs" bundle="EXPENDITURE_RESOURCES"/></h2>

<logic:present name="unit">
	<div class="infobox">
		<fr:view name="unit" schema="unit">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="rowClasses" value=",tdbold"/>
			</fr:layout>
		</fr:view>
	</div>
</logic:present>

<logic:present name="person">
	<div class="infobox">
		<table style="width: 100%;">
			<tr>
				<td style="vertical-align: top;">
					<fr:view name="person" type="pt.ist.expenditureTrackingSystem.domain.organization.Person" schema="viewPerson">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1"/>
							<fr:property name="columnClasses" value=",,tderror"/>
						</fr:layout>
					</fr:view>
				</td>
				<td style="text-align: right;">
					<html:img src="<%= PhotoTool.getPhotoUrl(((pt.ist.expenditureTrackingSystem.domain.organization.Person) request.getAttribute("person")).getUser().getUsername(), request.getContextPath()) %>"
						align="middle" styleClass="float: right; border: 1px solid #aaa; padding: 3px;" />
				</td>
			</tr>
		</table>
	</div>
</logic:present>

<logic:notPresent name="authorizationLogs">
	<bean:message key="authorizations.logs.none" bundle="EXPENDITURE_RESOURCES"/>
</logic:notPresent>

<logic:present name="authorizationLogs">
	<logic:empty name="authorizationLogs">
		<bean:message key="authorizations.logs.empty" bundle="EXPENDITURE_RESOURCES"/>
	</logic:empty>
	<logic:notEmpty name="authorizationLogs">
		<fr:view name="authorizationLogs" schema="pt.ist.expenditureTrackingSystem.domain.authorizations.AuthorizationLog">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
