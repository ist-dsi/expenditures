<%@page import="java.util.Collection"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:empty name="processList">
	<p>
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.process.list.none"/>
	</p>
</logic:empty>
<logic:notEmpty name="processList">
	<ul class="operations mtop0">
		<%
		final Collection directProcessList = (Collection) request.getAttribute("processListDirect");
		%>
		<logic:iterate id="process" name="processList">
			<li>
				<% if (directProcessList.contains(process)) { %>
				<html:link action="/workflowProcessManagement.do?method=viewProcess" paramId="processId"
						paramName="process" paramProperty="externalId">
					<bean:write name="process" property="workingCapital.unit.presentationName"/>
				</html:link>
				<% } else { %>
					<html:link action="/workflowProcessManagement.do?method=viewProcess" paramId="processId" styleClass="secondaryLink"
							paramName="process" paramProperty="externalId">
						<bean:write name="process" property="workingCapital.unit.presentationName"/>
					</html:link>
				<% } %>
			</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>
