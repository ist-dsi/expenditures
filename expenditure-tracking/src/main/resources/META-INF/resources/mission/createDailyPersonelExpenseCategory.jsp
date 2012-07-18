<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/collection-pager" prefix="cp"%>

<h2><bean:message key="label.mission.missionConfiguration.daily.personel.expense.category.create" bundle="MISSION_RESOURCES"/></h2>

<bean:define id="url" type="java.lang.String">/configureMissions.do?method=createDailyPersonelExpenseCategory&amp;dailyPersonelExpenseTableOid=<bean:write name="dailyPersonelExpenseCategoryBean" property="dailyPersonelExpenseTable.externalId"/></bean:define>
<fr:edit id="dailyPersonelExpenseCategoryBean" name="dailyPersonelExpenseCategoryBean" action="<%= url %>">
	<fr:schema type="module.mission.domain.util.DailyPersonelExpenseCategoryBean" bundle="MISSION_RESOURCES">
    	<fr:slot name="description" key="label.mission.missionConfiguration.daily.personel.expense.category.description" bundle="MISSION_RESOURCES" required="true"/>
    	<fr:slot name="value" key="label.mission.missionConfiguration.daily.personel.expense.category.value" bundle="MISSION_RESOURCES" required="true"/>
    	<fr:slot name="minSalaryValue" key="label.mission.missionConfiguration.daily.personel.expense.category.minSalaryValue" bundle="MISSION_RESOURCES" required="true"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>
