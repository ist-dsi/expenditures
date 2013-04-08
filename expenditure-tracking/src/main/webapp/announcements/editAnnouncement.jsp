<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<!-- announcements/createAnnouncement.jsp -->

<h2><bean:message key="process.announcement.title.announcements.edit" bundle="EXPENDITURE_RESOURCES"/></h2>

<bean:define id="announcementProcess" name="announcementBean" property="announcement.announcementProcess" type="pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess" />

<bean:define id="urlEdit">/announcementProcess.do?method=editAnnouncementForApproval&amp;announcementProcessOid=<%= announcementProcess.getOID() %></bean:define>
<bean:define id="urlView">/announcementProcess.do?method=viewAnnouncementProcess&amp;announcementProcessOid=<%= announcementProcess.getOID() %></bean:define>
<fr:edit id="announcementBean"
		name="announcementBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.AnnouncementBean"
		schema="createAnnouncement"
		action="<%= urlEdit %>" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
		<fr:property name="optionalMarkShown" value="true"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>
