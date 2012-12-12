<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>


<h2><bean:message key="title.missions" bundle="MISSION_RESOURCES"/></h2>

<p>
	<html:link styleClass="big" action="/missionProcess.do?method=prepareNewMissionCreation">
		+ <bean:message key="link.mission.process.new" bundle="MISSION_RESOURCES"/>
	</html:link>
</p>


<h3 class="mtop15">Lista de Processos</h3>


<ul>
	<logic:iterate id="missionProcess" name="missionProcesses">
		<li>
			<html:link action="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="missionProcess" paramProperty="externalId">
				<bean:write name="missionProcess" property="processIdentification" />
			</html:link>
		</li>
	</logic:iterate>
</ul>
