<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:empty name="processList">
	<p>
		<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.process.list.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="processList">
	<ul class="operations mtop0">
		<logic:iterate id="process" name="processList">
			<li>
				<html:link action="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:write name="process" property="presentationName"/>
				</html:link>
			</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>
