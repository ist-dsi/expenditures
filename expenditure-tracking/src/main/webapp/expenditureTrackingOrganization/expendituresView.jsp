<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present name="party" property="expenditureUnit">
	<bean:define id="expenditureUnit" name="party" property="expenditureUnit"/>

	<logic:equal name="expenditureUnit" property="defaultRegeimIsCCP" value="true">
		<bean:message key="label.unit.default.regeim.is.ccp" bundle="EXPENDITURE_RESOURCES"/>
	</logic:equal>
	<logic:equal name="expenditureUnit" property="defaultRegeimIsCCP" value="false">
		<bean:message key="label.unit.default.regeim.is.not.ccp" bundle="EXPENDITURE_RESOURCES"/>
	</logic:equal>
	<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.AQUISITIONS_UNIT_MANAGER">
		<html:link action="/expenditureTrackingOrganization.do?method=changeDefaultRegeimIsCCP" paramId="unitOid" paramName="expenditureUnit" paramProperty="externalId">
			<bean:message key="label.unit.default.regeim.toggle" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
	</logic:present>


	<logic:notEmpty name="expenditureUnit" property="authorizations">
		<h3 class="mtop15 mbottom05"><bean:message key="authorizations.label.responsibles" bundle="EXPENDITURE_RESOURCES"/></h3>
		<html:link action="/expenditureTrackingOrganization.do?method=viewAuthorizationLogs" paramId="unitOid" paramName="expenditureUnit" paramProperty="externalId">
			<bean:message key="authorizations.link.logs" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
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
