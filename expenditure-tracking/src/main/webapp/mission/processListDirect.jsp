<%@page import="java.util.stream.Collectors"%>
<%@page import="module.mission.domain.MissionProcess"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.stream.Stream"%>
<%@page import="java.util.Collection"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<% final Set<Object> directProcessList = ((Stream<Object>) request.getAttribute("directProcessList")).collect(Collectors.toSet()); %>
<% final Set<Object> processList = ((Stream<Object>) request.getAttribute("processList")).collect(Collectors.toSet()); %>
<% if (processList.isEmpty()) { %>
	<p>
		<bean:message bundle="MISSION_RESOURCES" key="label.module.mission.process.list.none"/>
	</p>
<% } else { %>
	<ul class="operations mtop0">
	   <% for (final Object o : processList) { %>
	       <% final MissionProcess process = (MissionProcess) o; %>
            <% final String style = directProcessList.contains(process) ? "" : "secondaryLink"; %>
			<li>
                <a href="<%= request.getContextPath() %>/workflowProcessManagement.do?method=viewProcess&processId=<%= process.getExternalId() %>"
                    class="<%= style %>">
                    <%= process.getPresentationName() %>
                </a>
			</li>
	   <% } %>
	</ul>
<% } %>
