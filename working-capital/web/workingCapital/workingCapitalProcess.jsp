<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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

