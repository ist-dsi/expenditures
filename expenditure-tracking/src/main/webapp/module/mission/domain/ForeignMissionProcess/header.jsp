<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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
