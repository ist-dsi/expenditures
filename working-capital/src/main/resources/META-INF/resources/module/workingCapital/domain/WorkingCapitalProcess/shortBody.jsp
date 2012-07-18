<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="workingCapital" name="process" property="workingCapital"/>

<div class="infobox mtop1 mbottom1">
	<fr:view name="workingCapital">
		<fr:schema bundle="WORKING_CAPITAL_RESOURCES" type="module.workingCapital.domain.WorkingCapital">
			<fr:slot name="unit.presentationName" key="label.module.workingCapital"/>
			<fr:slot name="workingCapitalInitialization.requestCreation" key="label.module.workingCapital.requestingDate"/>
			<fr:slot name="workingCapitalInitialization.requestor.name"  key="label.module.workingCapital.requester"/>
			<logic:present name="workingCapital" property="movementResponsible">
				<fr:slot name="movementResponsible.name" key="label.module.workingCapital.movementResponsible"/>
			</logic:present>
		</fr:schema>
		<fr:layout name="tabular">
				<fr:property name="columnClasses" value="aleft,,"/>
		</fr:layout>
	</fr:view>

</div>
