<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<span class="processNumber">
		<bean:message bundle="MISSION_RESOURCES" key="title.mission.process"/>:
	</span>
	<bean:write name="process" property="processIdentification"/>
	<span class="processType">
		(
		<logic:equal name="process" property="mission.grantOwnerEquivalence" value="true">
			<bean:message bundle="MISSION_RESOURCES" key="title.mission.process.foreign.grantOwnerEquivalence"/>
		</logic:equal>
		<logic:notEqual name="process" property="mission.grantOwnerEquivalence" value="true">
			<bean:message bundle="MISSION_RESOURCES" key="title.mission.process.foreign"/>
		</logic:notEqual>
		)
	</span>
</h2>
