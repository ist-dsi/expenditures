<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<div class="infobox">
	<fr:view name="missionItem" schema="module.mission.domain.PersonalVehiclItem.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="structural thmiddle thlight mvert05"/>
		</fr:layout>
	</fr:view>
</div>

