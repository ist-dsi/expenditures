<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
	<bean:message key="title.manageObservers" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
</h2>
<h3><fr:view name="unit" property="presentationName"/></h3>

<ul>
	<li>
		<html:link page="/expenditureTrackingOrganization.do?method=viewLoggedPerson">
			<bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
	</li>
</ul>
<bean:define id="unitOid" name="unit" property="externalId" type="java.lang.String"/>

<bean:define id="observers" name="unit" property="observers"/>

<p>
	<strong><bean:message key="label.currentObservers" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></strong>:
	<logic:notEmpty name="observers">
		<fr:view name="observers" schema="viewPeopleInList">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2"/>
		
				<fr:property name="link(delete)" value="<%= "/expenditureTrackingOrganization.do?method=removeObserver&unitOid=" + unitOid%>"/>
				<fr:property name="bundle(delete)" value="EXPENDITURE_RESOURCES"/>
				<fr:property name="key(delete)" value="link.delete"/>
				<fr:property name="param(delete)" value="externalId/observerOid"/>
				<fr:property name="order(delete)" value="1"/>
			
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="observers">
		<bean:message key="label.noAssociatedPeople" bundle="EXPENDITURE_RESOURCES"/>
	</logic:empty>
</p>

<p>
	<strong><bean:message key="label.addObserver" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></strong>:
	
	<fr:form action="<%= "/expenditureTrackingOrganization.do?method=addObserver&unitOid=" + unitOid %>">
	<fr:edit id="bean"
				name="bean"
				type="pt.ist.expenditureTrackingSystem.domain.organization.SearchUsers"
				schema="selectUser">
			<fr:layout name="tabular">
				<fr:property name="classes" value="form"/>
				<fr:property name="columnClasses" value=",,tderror"/>
			</fr:layout>
	</fr:edit>
	<html:submit styleClass="inputbutton"><bean:message key="label.add" bundle="EXPENDITURE_RESOURCES"/></html:submit>
	</fr:form>
</p>