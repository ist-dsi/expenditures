<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="party" property="expenditureUnit">
	<bean:define id="expenditureUnit" name="party" property="expenditureUnit"/>

	<logic:notEmpty name="expenditureUnit" property="authorizations">
		<html:link action="/expenditureTrackingOrganization.do?method=viewAuthorizationLogs" paramId="unitOid" paramName="expenditureUnit" paramProperty="externalId">
			<bean:message key="authorizations.link.logs" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>

		<h3 class="mtop15 mbottom05"><bean:message key="authorizations.label.responsibles" bundle="EXPENDITURE_RESOURCES"/></h3>
		<fr:view name="expenditureUnit" property="sortedAuthorizationsSet" schema="viewAuthorization">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 mtop05"/>
			</fr:layout>
		</fr:view>	
	</logic:notEmpty>	
	<logic:empty name="expenditureUnit" property="authorizations">
		<p><em><bean:message key="authorizations.label.noResponsiblesDefinedForUnit" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
	</logic:empty>
</logic:present>
