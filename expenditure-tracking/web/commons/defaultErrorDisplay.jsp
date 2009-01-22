<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<messages:hasMessages type="WARN">
	<div class="warning1">
		<span><messages:showMessages type="WARN"/></span>
	</div>
</messages:hasMessages>

<messages:hasMessages type="ERROR">
	<div class="error1">
		<span><messages:showMessages type="ERROR"/></span>
	</div>
</messages:hasMessages>

<fr:messages type="conversion">
	<div class="error1">
		<span><fr:message type="conversion"/></span>
	</div>
</fr:messages>