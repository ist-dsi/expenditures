<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital"/>
</h2>

<bean:define id="workingCapital" name="workingCapitalProcess" property="workingCapital"/>

<div class="infobox mtop1 mbottom1">
	<h3 class="mbottom05">
		<bean:write name="workingCapital" property="unit.presentationName"/>
		-
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.year"/>
		<bean:write name="workingCapital" property="workingCapitalYear.year"/>
	</h3>

	<p>
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.movementResponsible"/>:
		:
		<strong>
			<bean:write name="workingCapital" property="movementResponsible.name"/>
		</strong>
	</p>
</div>

