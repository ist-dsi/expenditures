<%@page import="java.util.Collection"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:empty name="processList">
	<p>
		<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.process.list.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="processList">
	<%
		final Collection directProcessList = (Collection) request.getAttribute("directProcessList");
	%>
	<ul class="operations mtop0">
		<logic:iterate id="process" name="processList">
			<li>
				<% if (directProcessList.contains(process)) { %>
					<html:link action="/workflowProcessManagement.do?method=viewProcess"
							paramId="processId" paramName="process" paramProperty="externalId">
						<bean:write name="process" property="presentationName"/>
					</html:link>
				<% } else { %>
					<html:link action="/workflowProcessManagement.do?method=viewProcess" styleClass="secondaryLink"
							paramId="processId" paramName="process" paramProperty="externalId">
						<bean:write name="process" property="presentationName"/>
					</html:link>
				<% } %>
			</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>
