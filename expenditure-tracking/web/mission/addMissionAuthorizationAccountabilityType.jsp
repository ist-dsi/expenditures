<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<h2><bean:message key="label.mission.missionConfiguration" bundle="MISSION_RESOURCES"/></h2>

<fr:edit id="missionAuthorizationAccountabilityTypeBean" name="missionAuthorizationAccountabilityTypeBean"
		action="/configureMissions.do?method=addMissionAuthorizationAccountabilityType">
	<fr:schema type="module.mission.domain.util.MissionAuthorizationAccountabilityTypeBean" bundle="ORGANIZATION_RESOURCES">
    	<fr:slot name="accountabilityType" layout="menu-select" key="label.accountability.type" bundle="ORGANIZATION_RESOURCES">
        	<fr:property name="providerClass" value="module.mission.presentationTier.provider.AccountabilityTypesProvider" />
        	<fr:property name="eachSchema" value="accountabilityType-name"/>
        	<fr:property name="eachLayout" value="values"/>
        	<fr:property name="classes" value="nobullet noindent"/>
        	<fr:property name="sortBy" value="name"/>
			<fr:property name="saveOptions" value="true"/>
    	</fr:slot>
    	<fr:slot name="accountabilityTypes" layout="option-select" key="label.accountability.types.for.mission.authorization" bundle="MISSION_RESOURCES">
        	<fr:property name="providerClass" value="module.mission.presentationTier.provider.AccountabilityTypesProvider" />
        	<fr:property name="eachSchema" value="accountabilityType-name"/>
        	<fr:property name="eachLayout" value="values"/>
        	<fr:property name="classes" value="nobullet"/>
			<fr:property name="saveOptions" value="true"/>
			<fr:property name="sortBy" value="name"/>
    	</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>