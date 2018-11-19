<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="module.mission.domain.MissionProcess"%>
<%@page import="java.util.stream.Stream"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<% final Object o = request.getAttribute("processList"); %>
<% final Stream<MissionProcess> processList = o instanceof Stream ? (Stream<MissionProcess>) o : ((Collection) o).stream(); %>
<% boolean empty = true; %>
<ul class="operations mtop0">
<% for (final Iterator<MissionProcess> iterator = processList.sorted(MissionProcess.COMPARATOR_BY_PROCESS_NUMBER.reversed()).iterator(); iterator.hasNext(); ) { %>
        <% empty = false; %>
        <% final MissionProcess missionProcess = iterator.next(); %>
            <li>
                <a href="<%= request.getContextPath() %>/workflowProcessManagement.do?method=viewProcess&processId=<%= missionProcess.getExternalId() %>">
                    <%= missionProcess.getPresentationName() %>
                </a>
            </li>        
<% } %>
</ul>
<% if (empty) { %>
    <p>
        <bean:message bundle="MISSION_RESOURCES" key="label.module.mission.process.list.none"/>
    </p>    
<% } %>
