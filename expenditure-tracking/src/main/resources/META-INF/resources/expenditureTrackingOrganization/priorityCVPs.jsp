<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2>
	<bean:message key="title.managePriorityCPVS" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
</h2>

<table>
<logic:iterate id="cpv" name="cpvs">
	<tr>
		<td><fr:view name="cpv" property="code"/> - <fr:view name="cpv" property="description"/></td>
		<td>
		<html:link page="/expenditureTrackingOrganization.do?method=removePriorityCPV" paramId="cpvId" paramName="cpv" paramProperty="externalId">
			<bean:message key="link.remove" bundle="MYORG_RESOURCES"/>	
		</html:link>
		</td>
	</tr>
</logic:iterate>
</table>


<fr:edit id="cpvToAdd" name="bean" slot="domainObject" action="/expenditureTrackingOrganization.do?method=addPriorityCPV">
	<fr:layout name="autoComplete">
		<fr:property name="labelField" value="fullDescription"/>
		<fr:property name="format" value="${code} - ${description}"/>
		<fr:property name="minChars" value="1"/>		
		<fr:property name="args" value="provider=pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider.CPVAutoCompleteProvider"/>
		<fr:property name="size" value="40"/>
	</fr:layout>
</fr:edit>
