<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page"/>
</h2>

<html:link page="/workingCapital.do?method=prepareCreateWorkingCapitalInitialization">
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.create"/>
</html:link>

<logic:iterate id="workingCapital" name="workingCapitals">
	<br/>
	<html:link page="/workingCapital.do?method=viewWorkingCapital" paramId="workingCapitalOid" paramName="workingCapital" paramProperty="externalId">
		<bean:write name="workingCapital" property="externalId"/>
	</html:link>
	<logic:present name="workingCapital" property="workingCapitalProcess">
		:
		<html:link action="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="workingCapital" paramProperty="workingCapitalProcess.externalId">
			<bean:write name="workingCapital" property="workingCapitalProcess" />
		</html:link>
	</logic:present>
</logic:iterate>
