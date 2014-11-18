<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Project" %>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<script type="text/javascript">
function inactiveEntities(func) {
	$('.filterableInactiveProjectsTable').each(function(index) {
		$(this).children().children().each(function(tableRowIndex) {
			if(tableRowIndex > 0) {
				var active = $(this).find('td:eq(3)').html().trim(); 
				if(active == "Não" || active == "No") {
					func($(this));
				}
			}
		});
	});
}

function toggleInactive(elem){
	if( $('#toggleInactiveChbox').attr('checked')) {
		elem.show();
	} else {
		elem.hide();
	}
}
</script>
<script type="text/javascript">
$(function() {
	inactiveEntities(toggleInactive);
});
</script>


<h2><bean:message key="title.accounting.unit" bundle="EXPENDITURE_RESOURCES"/> <bean:write name="accountingUnit" property="name"/></h2>

<h3 class="mbottom05"><bean:message key="title.accounting.unit.responsibles" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<logic:empty name="accountingUnit" property="responsiblePeople">
	<p class="mtop05">
		<em><bean:message key="accountingUnit.message.responsibles.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="accountingUnit" property="responsiblePeople">
	<fr:view name="accountingUnit" property="responsiblePeople"
			schema="viewPeopleInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value=",,aleft,,,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewPerson"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/personOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h3 class="mbottom05"><bean:message key="title.accounting.unit.members" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<logic:empty name="accountingUnit" property="people">
	<p class="mtop05">
		<em><bean:message key="accountingUnit.message.members.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="accountingUnit" property="people">
	<fr:view name="accountingUnit" property="people"
			schema="viewPeopleInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value=",,aleft,,,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewPerson"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/personOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h3 class="mbottom05"><bean:message key="title.project.accounting.unit.responsibles" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<logic:empty name="accountingUnit" property="responsibleProjectAccountants">
	<p class="mtop05">
		<em><bean:message key="accountingUnit.message.responsibles.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="accountingUnit" property="responsibleProjectAccountants">
	<fr:view name="accountingUnit" property="responsibleProjectAccountants"
			schema="viewPeopleInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value=",,aleft,,,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewPerson"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/personOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h3 class="mbottom05"><bean:message key="title.project.accounting.unit.members" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<logic:empty name="accountingUnit" property="projectAccountants">
	<p class="mtop05">
		<em><bean:message key="accountingUnit.message.members.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="accountingUnit" property="projectAccountants">
	<fr:view name="accountingUnit" property="projectAccountants"
			schema="viewPeopleInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value=",,aleft,,,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewPerson"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/personOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h3 class="mbottom05"><bean:message key="title.treasury.accounting.unit.members" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<logic:empty name="accountingUnit" property="treasuryMembers">
	<p class="mtop05">
		<em><bean:message key="accountingUnit.message.members.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="accountingUnit" property="treasuryMembers">
	<fr:view name="accountingUnit" property="treasuryMembers"
			schema="viewPeopleInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value=",,aleft,,,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewPerson"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/personOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>


<h2 class="mtop15 mbottom05"><bean:message key="title.accounting.unit.units" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h2>
<% if (ExpenditureTrackingSystem.isManager()) { %>
	<p class="mtop05">
		<html:link action="/expenditureTrackingOrganization.do?method=prepareAddUnitToAccountingUnit" paramId="accountingUnitOid" paramName="accountingUnit" paramProperty="externalId">
			<bean:message key="unit.link.add.accounting.unit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
		</html:link>
	</p>
<% } %>

<h3 class="mtop15 mbottom05"><bean:message key="title.accounting.unit.costCenters" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>

<logic:empty name="accountingUnit" property="costCenters">
	<p>
		<em><bean:message key="accountingUnit.message.costCenters.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>
<logic:notEmpty name="accountingUnit" property="costCenters">
	<fr:view name="accountingUnit" property="costCenters"
			schema="unitList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop05"/>
			<fr:property name="columnClasses" value=",,aleft,,,"/>
			<fr:property name="sortBy" value="name=asc"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewOrganization"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/unitOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h3 class="mtop15 mbottom05"><bean:message key="title.accounting.unit.projects" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>

<logic:empty name="accountingUnit" property="projects">
	<p>
		<em><bean:message key="accountingUnit.message.projects.none" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></em>
	</p>
</logic:empty>
<logic:notEmpty name="accountingUnit" property="projects">
<label for="toggleInactiveChbox">
<bean:message key="accountingUnit.message.projects.showInactive" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/> </label>
<input style="vertical-align: bottom;" type="checkbox" name="toggleInactive" id="toggleInactiveChbox" onclick="inactiveEntities(toggleInactive)">
	<fr:view name="accountingUnit" property="projects"
			schema="projectList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop05 filterableInactiveProjectsTable"/>
			<fr:property name="columnClasses" value=",number,aleft,,,"/>
			<fr:property name="sortBy" value="shortIdentifier=asc"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewOrganization"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/unitOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
