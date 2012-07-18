<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<!-- announcements/viewActiveProcesses.jsp -->

<h2>
	<bean:message key="process.announcement.title" bundle="EXPENDITURE_RESOURCES"/>
</h2>

<logic:empty name="activeProcesses">
	<p><em><bean:message key="process.messages.info.noProcessesToOperate" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
</logic:empty>


<fr:view name="activeProcesses" schema="viewAnnouncementProcessInList">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="columnClasses" value="aleft,,,,aright,action"/>
		<fr:property name="link(view)" value="/announcementProcess.do?method=viewAnnouncementProcess"/>
		<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
		<fr:property name="key(view)" value="link.view"/>
		<fr:property name="param(view)" value="externalId/announcementProcessOid"/>
		<fr:property name="order(view)" value="1"/>
		<%--
		<fr:property name="sortBy" value="dateFromLastActivity=asc"/>
		--%>
	</fr:layout>
</fr:view>
