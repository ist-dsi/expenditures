<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/collection-pager" prefix="cp"%>

<h2><bean:message key="label.mission.missionConfiguration.daily.personel.expense.table.edit" bundle="MISSION_RESOURCES"/></h2>

<fr:edit id="dailyPersonelExpenseTable" name="dailyPersonelExpenseTable" action="/configureMissions.do?method=prepare">
	<fr:schema type="module.mission.domain.util.DailyPersonelExpenseTableBean" bundle="MISSION_RESOURCES">
    	<fr:slot name="aplicableSince" key="label.daily.personel.expense.table.since" bundle="MISSION_RESOURCES" required="true"/>
    	<fr:slot name="aplicableToMissionClass" layout="menu-select" key="label.mission.type" bundle="MISSION_RESOURCES" required="true">
        	<fr:property name="providerClass" value="module.mission.presentationTier.provider.MissionClassProvider" />
        	<fr:property name="eachLayout" value="name-resolver"/>
        	<fr:property name="bundle" value="MISSION_RESOURCES"/>
    	</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>
