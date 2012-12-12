<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/collection-pager" prefix="cp"%>

<h2><bean:message key="label.mission.missionConfiguration" bundle="MISSION_RESOURCES"/></h2>

<fr:edit id="accountabilityTypeQueueBean" name="accountabilityTypeQueueBean"
		action="/configureMissions.do?method=addQueueForAccountabilityType">
	<fr:schema type="module.mission.domain.util.AccountabilityTypeQueueBean" bundle="MISSION_RESOURCES">
    	<fr:slot name="accountabilityType" layout="menu-select" key="label.accountability.type" bundle="ORGANIZATION_RESOURCES">
        	<fr:property name="providerClass" value="module.mission.presentationTier.provider.AccountabilityTypesProvider" />
        	<fr:property name="eachSchema" value="accountabilityType-name"/>
        	<fr:property name="eachLayout" value="values"/>
        	<fr:property name="classes" value="nobullet noindent"/>
        	<fr:property name="sortBy" value="name"/>
			<fr:property name="saveOptions" value="true"/>
    	</fr:slot>
    	<fr:slot name="workflowQueue" layout="menu-select" key="label.queue" bundle="MISSION_RESOURCES">
        	<fr:property name="providerClass" value="module.workflow.presentationTier.provider.WorkflowQueueProvider" />
        	<fr:property name="eachSchema" value="workflowQueue-name"/>
        	<fr:property name="eachLayout" value="values"/>
        	<fr:property name="classes" value="nobullet noindent"/>
        	<fr:property name="sortBy" value="name"/>
			<fr:property name="saveOptions" value="true"/>
    	</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>
