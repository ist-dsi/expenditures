<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital"/>
</h2>

<bean:define id="workingCapital" name="process" property="workingCapital"/>

<div class="infobox mtop1 mbottom1">
	<h3 class="mbottom05">
		<bean:write name="workingCapital" property="unit.presentationName"/>
		-
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.year"/>
		<bean:write name="workingCapital" property="workingCapitalYear.year"/>
	</h3>

	<fr:view name="workingCapital">
		<fr:schema bundle="WORKING_CAPITAL_RESOURCES" type="module.workingCapital.domain.WorkingCapital">
			<fr:slot name="movementResponsible.name" key="label.module.workingCapital.movementResponsible"/>
			<fr:slot name="workingCapitalInitialization.requestCreation" key="label.module.workingCapital.requestingDate"/>
			<fr:slot name="workingCapitalInitialization.requestor.name"  key="label.module.workingCapital.requester"/>
		</fr:schema>
		<fr:layout name="tabular">
				<fr:property name="columnClasses" value="aleft,,"/>
		</fr:layout>
	</fr:view>

</div>