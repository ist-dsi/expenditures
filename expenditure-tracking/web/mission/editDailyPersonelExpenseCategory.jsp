<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<h2><bean:message key="label.mission.missionConfiguration.daily.personel.expense.category.edit" bundle="MISSION_RESOURCES"/></h2>

<bean:define id="url" type="java.lang.String">/configureMissions.do?method=viewDailyPersonelExpenseTable&amp;dailyPersonelExpenseTableOid=<bean:write name="dailyPersonelExpenseCategory" property="dailyPersonelExpenseTable.externalId"/></bean:define>
<fr:edit id="dailyPersonelExpenseCategory" name="dailyPersonelExpenseCategory" action="<%= url %>">
	<fr:schema type="module.mission.domain.DailyPersonelExpenseCategory" bundle="MISSION_RESOURCES">
    	<fr:slot name="description" key="label.mission.missionConfiguration.daily.personel.expense.category.description" bundle="MISSION_RESOURCES" required="true"/>
    	<fr:slot name="value" key="label.mission.missionConfiguration.daily.personel.expense.category.value" bundle="MISSION_RESOURCES" required="true"/>
    	<fr:slot name="minSalaryValue" key="label.mission.missionConfiguration.daily.personel.expense.category.minSalaryValue" bundle="MISSION_RESOURCES" required="true"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>
