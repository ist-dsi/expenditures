<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/collection-pager" prefix="cp"%>

<h2><bean:message bundle="MISSION_RESOURCES" key="label.mission.missionConfiguration.country.for.national.missions"/></h2>

<fr:edit id="missionSystem" name="missionSystem" action="/configureMissions.do?method=prepare">
	<fr:schema type="module.mission.domain.MissionSystem" bundle="MISSION_RESOURCES">
		<fr:slot name="country" key="label.mission.country" layout="autoComplete" required="true">
    	    <fr:property name="labelField" value="name.content"/>
			<fr:property name="format" value="\${name.content}"/>
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
