<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<messages:hasMessages type="WARN">
	<div class="warning1">
		<span><messages:showMessages type="WARN"/></span>
	</div>
</messages:hasMessages>

<messages:hasMessages type="ERROR">
	<div class="error1">
		<messages:showMessages type="ERROR"/>
	</div>
</messages:hasMessages>