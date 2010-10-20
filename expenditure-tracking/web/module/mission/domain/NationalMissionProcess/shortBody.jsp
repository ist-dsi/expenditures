<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<jsp:include page="summary.jsp"/>

<html:link page="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
	« <bean:message key="link.back" bundle="MISSION_RESOURCES"/>
</html:link>
