<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="title.viewOrganization" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="mbottom15">
	<fr:form action="/expenditureTrackingOrganization.do?method=viewOrganization">
	<fr:edit id="unitBean"
			name="unitBean"
			schema="unitBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:edit>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
	</fr:form>
</div>


<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
	<logic:notPresent name="unit">
		<p>
			<html:link action="/expenditureTrackingOrganization.do?method=prepareCreateUnit">
				<bean:message key="unit.link.create" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</p>
	</logic:notPresent>
	<logic:present name="unit">
		<p>
			<html:link action="/expenditureTrackingOrganization.do?method=prepareCreateUnit" paramId="unitOid" paramName="unit" paramProperty="OID">
				<bean:message key="unit.link.create" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</p>
	</logic:present>
</logic:present>



<logic:present name="unit">
	<div class="infoop2">
		<fr:view name="unit" schema="unit">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="rowClasses" value=",tdbold"/>
			</fr:layout>
		</fr:view>
	</div>
	
	<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
		<p class="mtop05">
			<html:link action="/expenditureTrackingOrganization.do?method=editUnit" paramId="unitOid" paramName="unit" paramProperty="OID">
				<bean:message key="link.edit" bundle="EXPENDITURE_RESOURCES"/>
			</html:link> | 
			<html:link action="/expenditureTrackingOrganization.do?method=deleteUnit" paramId="unitOid" paramName="unit" paramProperty="OID">
				<bean:message key="link.delete" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</p>
	</logic:present>

	<logic:notEmpty name="unit" property="authorizations">
		<h3 class="mtop15 mbottom05"><bean:message key="authorizations.label.responsibles" bundle="EXPENDITURE_RESOURCES"/></h3>
		<fr:view name="unit" property="authorizations" schema="viewAuthorization">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 mtop05"/>
			</fr:layout>
		</fr:view>	
	</logic:notEmpty>	
	<logic:empty name="unit" property="authorizations">
		<p><em><bean:message key="authorizations.label.noResponsiblesDefinedForUnit" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
	</logic:empty>

	<logic:present name="unit" property="parentUnit">
		<h3 class="mtop15 mbottom05"><bean:message key="unit.title.superior.unit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
		<bean:define id="unitToDisplay" toScope="request" name="unit" property="parentUnit"/>
		<jsp:include page="unitLine.jsp" flush="false"/>
	</logic:present>
</logic:present>

<logic:present name="units">
	<logic:notEmpty name="units">
		<logic:present name="unit">
			<h3 class="mtop15 mbottom05"><bean:message key="unit.title.subunit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
		</logic:present>
		<fr:view name="units" schema="unitList">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 mtop05"/>
				<fr:property name="columnClasses" value=",,aleft,"/>
				<fr:property name="sortBy" value="name=asc"/>
				<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewOrganization"/>
				<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="param(view)" value="OID/unitOid"/>
				<fr:property name="order(view)" value="1"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>

<h3 class="mtop15 mbottom05"><bean:message key="accountingUnit.title" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
	<p class="mtop05">
		<html:link action="/expenditureTrackingOrganization.do?method=prepareCreateAccountingUnit">
			<bean:message key="unit.link.create.accounting.unit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
		</html:link>
	</p>
</logic:present>

<logic:present name="accountingUnits">
	<logic:empty name="accountingUnits">
		<bean:message key="accountingUnit.message.none.defined" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
	</logic:empty>
	<logic:notEmpty name="accountingUnits">
		<fr:view name="accountingUnits" schema="accountingUnits">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 mtop05"/>
				<fr:property name="columnClasses" value="aleft,,,,,"/>
				<fr:property name="sortBy" value="name=asc"/>
				<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewAccountingUnit"/>
				<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="param(view)" value="OID/accountingUnitOid"/>
				<fr:property name="order(view)" value="1"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>

