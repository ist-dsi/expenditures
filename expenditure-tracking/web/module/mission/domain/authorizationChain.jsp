<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:write name="authorizationChain" property="unit.partyName"/>
<logic:present name="authorizationChain" property="next">
	>
	<bean:define id="authorizationChain" name="authorizationChain" property="next" toScope="request"/>
	<jsp:include page="authorizationChain.jsp"/>
</logic:present>
