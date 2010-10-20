<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<div class="infobox">
	<fr:view name="missionItem" schema="module.mission.domain.PersonalVehiclItem.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="structural thmiddle thlight mvert05"/>
		</fr:layout>
	</fr:view>
</div>

