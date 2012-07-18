<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<!-- announcements/rejectAnnouncementProcess.jsp -->

<h2><bean:message key="title.rejectAnnouncementRequest" bundle="ANNOUNCEMENT_RESOURCES"/></h2>

<bean:define id="announcementProcess" name="announcementProcess" type="pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess" />
<jsp:include page="viewAnnouncement.jsp" flush="true"/>

<bean:define id="urlView">/announcementProcess.do?method=viewAnnouncementProcess&amp;announcementProcessOid=<%= announcementProcess.getOID() %></bean:define>
<bean:define id="urlAdd">/announcementProcess.do?method=rejectAnnouncementProcess&amp;announcementProcessOid=<%= announcementProcess.getOID() %></bean:define>
<fr:edit id="stateBean"
		name="stateBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.ProcessStateBean"
		schema="stateJustification"
		action="<%= urlAdd %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>





