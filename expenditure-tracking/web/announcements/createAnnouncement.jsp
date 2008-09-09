<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- announcements/createAnnouncement.jsp -->

<h2><bean:message key="process.announcement.title.announcements.create" bundle="EXPENDITURE_RESOURCES"/></h2>

<fr:edit id="announcementBean"
		name="announcementBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAnnouncementBean"
		schema="createAnnouncement"
		action="/acquisitionProcess.do?method=createAnnouncement">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="/acquisitionProcess.do?method=showPendingProcesses"/>
</fr:edit>
