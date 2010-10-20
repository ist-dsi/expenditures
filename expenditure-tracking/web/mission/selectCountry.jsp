<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<h2><bean:message bundle="MISSION_RESOURCES" key="label.mission.missionConfiguration.country.for.national.missions"/></h2>

<fr:edit id="missionSystem" name="missionSystem" action="/configureMissions.do?method=prepare">
	<fr:schema type="module.mission.domain.MissionSystem" bundle="MISSION_RESOURCES">
		<fr:slot name="country" key="label.mission.country" layout="autoComplete" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
    	    <fr:property name="labelField" value="name.content"/>
			<fr:property name="format" value="${name.content}"/>
			<fr:property name="minChars" value="3"/>		
			<fr:property name="args" value="provider=module.geography.presentationTier.provider.CountryAutoCompleteProvider"/>
			<fr:property name="size" value="60"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>