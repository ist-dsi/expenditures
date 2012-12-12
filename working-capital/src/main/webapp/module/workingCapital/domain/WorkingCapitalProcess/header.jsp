<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="workingCapital" name="process" property="workingCapital"/>

<h2>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital"/>
	-
	<bean:write name="workingCapital" property="unit.presentationName"/>
	-
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.year"/>
	<bean:write name="workingCapital" property="workingCapitalYear.year"/> 
</h2>

