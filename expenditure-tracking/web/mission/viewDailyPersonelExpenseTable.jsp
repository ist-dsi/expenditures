<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<h2><bean:message key="label.mission.missionConfiguration.daily.personel.expense.table.view" bundle="MISSION_RESOURCES"/></h2>

<fr:view name="dailyPersonelExpenseTable">
	<fr:schema type="module.mission.domain.util.DailyPersonelExpenseTableBean" bundle="MISSION_RESOURCES">
    	<fr:slot name="aplicableToMissionClass" layout="name-resolver" key="label.mission.type" bundle="MISSION_RESOURCES"/>
    	<fr:slot name="aplicableSince" key="label.daily.personel.expense.table.since" bundle="MISSION_RESOURCES"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tview2" />
	</fr:layout>
</fr:view>

<html:link action="/configureMissions.do?method=editDailyPersonelExpenseTable" paramId="dailyPersonelExpenseTableOid" paramName="dailyPersonelExpenseTable" paramProperty="externalId">
	<bean:message key="link.edit" bundle="MISSION_RESOURCES"/>
</html:link>

<html:link action="/configureMissions.do?method=deleteDailyPersonelExpenseTable" paramId="dailyPersonelExpenseTableOid" paramName="dailyPersonelExpenseTable" paramProperty="externalId">
	<bean:message key="link.delete" bundle="MISSION_RESOURCES"/>
</html:link>

<p>
	<logic:iterate id="otherDailyPersonelExpenseTable" name="dailyPersonelExpenseTable" property="dailyPersonelExpenseTablesForSameType">
		&nbsp;&nbsp;
		<html:link action="/configureMissions.do?method=viewDailyPersonelExpenseTable" paramId="dailyPersonelExpenseTableOid" paramName="otherDailyPersonelExpenseTable" paramProperty="externalId">
			<fr:view name="otherDailyPersonelExpenseTable" property="aplicableSince"/>
		</html:link>
	</logic:iterate>
</p>

<p>
	<html:link action="/configureMissions.do?method=prepareCreateDailyPersonelExpenseCategory" paramId="dailyPersonelExpenseTableOid" paramName="dailyPersonelExpenseTable" paramProperty="externalId">
		<bean:message key="label.mission.missionConfiguration.daily.personel.expense.category.create" bundle="MISSION_RESOURCES"/>
	</html:link>
</p>
<logic:empty name="dailyPersonelExpenseTable" property="dailyPersonelExpenseCategories">
	<p>
		<bean:message key="label.mission.missionConfiguration.daily.personel.expense.category.none" bundle="MISSION_RESOURCES"/>
	</p>
</logic:empty>
<logic:notEmpty name="dailyPersonelExpenseTable" property="dailyPersonelExpenseCategories">
	<fr:view name="dailyPersonelExpenseTable" property="sortedDailyPersonelExpenseCategories">
		<fr:schema type="module.mission.domain.DailyPersonelExpenseCategory" bundle="MISSION_RESOURCES">
   			<fr:slot name="description" key="label.mission.missionConfiguration.daily.personel.expense.category.description" bundle="MISSION_RESOURCES"/>
   			<fr:slot name="value" key="label.mission.missionConfiguration.daily.personel.expense.category.value" bundle="MISSION_RESOURCES"/>
   			<fr:slot name="minSalaryValue" key="label.mission.missionConfiguration.daily.personel.expense.category.minSalaryValue" bundle="MISSION_RESOURCES"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tview1"/>
			<fr:property name="linkFormat(edit)" value="/configureMissions.do?method=editDailyPersonelExpenseCategory&dailyPersonelExpenseCategoryOid=${externalId}"/>
			<fr:property name="bundle(edit)" value="MISSION_RESOURCES"/>
			<fr:property name="key(edit)" value="link.edit"/>
			<fr:property name="order(edit)" value="1"/>

			<fr:property name="linkFormat(delete)" value="/configureMissions.do?method=deleteDailyPersonelExpenseCategory&dailyPersonelExpenseCategoryOid=${externalId}"/>
			<fr:property name="bundle(delete)" value="MISSION_RESOURCES"/>
			<fr:property name="key(delete)" value="link.delete"/>
			<fr:property name="order(delete)" value="2"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
