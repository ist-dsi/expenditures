<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<bean:write name="authorizationChain" property="unit.partyName"/>
<logic:present name="authorizationChain" property="next">
	>
	<bean:define id="authorizationChain" name="authorizationChain" property="next" toScope="request"/>
	<jsp:include page="authorizationChain.jsp"/>
</logic:present>
