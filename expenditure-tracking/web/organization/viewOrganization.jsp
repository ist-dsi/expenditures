<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="title.viewOrganization" bundle="EXPENDITURE_RESOURCES"/></h2>

<br/>
<fr:edit id="unitBean"
		name="unitBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.UnitBean"
		schema="unitBean">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:edit>

<logic:present role="MANAGER">
	<br />
	<logic:notPresent name="unit">
		<html:link action="/organization.do?method=prepareCreateUnit">
			<bean:message key="unit.link.create" bundle="ORGANIZATION_RESOURCES"/>
		</html:link>
	</logic:notPresent>
	<logic:present name="unit">
		<html:link action="/organization.do?method=prepareCreateUnit" paramId="unitOid" paramName="unit" paramProperty="OID">
			<bean:message key="unit.link.create" bundle="ORGANIZATION_RESOURCES"/>
		</html:link>
	</logic:present>
</logic:present>

<br/>
<br/>
<logic:present name="unit">
	<fr:view name="unit" schema="unit">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="aleft,,,,aright,"/>
		</fr:layout>
	</fr:view>
	<logic:present role="MANAGER">
		<html:link action="/organization.do?method=editUnit" paramId="unitOid" paramName="unit" paramProperty="OID">
			<bean:message key="link.edit" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<html:link action="/organization.do?method=deleteUnit" paramId="unitOid" paramName="unit" paramProperty="OID">
			<bean:message key="link.delete" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<br/>
		<br/>
	</logic:present>

	<logic:notEmpty name="unit" property="authorizations">
		<h3><bean:message key="authorizations.label.responsibles" bundle="EXPENDITURE_RESOURCES"/>:</h3>
		<fr:view name="unit" property="authorizations" schema="viewAuthorization">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2"/>
			</fr:layout>
		</fr:view>	
	</logic:notEmpty>	
	<logic:empty name="unit" property="authorizations">
		<p><em><bean:message key="authorizations.label.noResponsiblesDefinedForUnit" bundle="EXPENDITURE_RESOURCES"/></em></p>
	</logic:empty>

	<logic:present name="unit" property="parentUnit">
		<br/>
		<h3>Unidade Superior: </h3>
		<bean:define id="unitToDisplay" toScope="request" name="unit" property="parentUnit"/>
		<jsp:include page="unitLine.jsp" flush="false"/>
	</logic:present>

</logic:present>

<logic:present name="units">
	<logic:notEmpty name="units">
		<logic:present name="unit">
			<br/>
			<h3>SubUnidades</h3>
		</logic:present>
		<fr:view name="units"
				schema="unitList">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2"/>
				<fr:property name="columnClasses" value="aleft,,,,aright,"/>
				<fr:property name="sortBy" value="name=asc"/>

				<fr:property name="link(view)" value="/organization.do?method=viewOrganization"/>
				<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="param(view)" value="OID/unitOid"/>
				<fr:property name="order(view)" value="1"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>