<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<ul>
	<logic:present name="USER_SESSION_ATTRIBUTE" property="person">

		<li class="header">
			<strong><bean:message key="link.sideBar.announcements" bundle="EXPENDITURE_RESOURCES"/></strong>
			<div class="lic1"></div><div class="lic2"></div>
		</li>
		<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL_MANAGER">
			<li>
				<html:link action="/announcementProcess.do?method=prepareCreateAnnouncement">
					<span><bean:message key="link.sideBar.announcementProcess.createAnnouncement" bundle="EXPENDITURE_RESOURCES"/></span>
				</html:link>
				<span class="bar">|</span>
			</li>
			<li>
				<html:link action="/announcementProcess.do?method=searchAnnouncementProcess">
					<span><bean:message key="link.sideBar.announcementProcess.searchProcesses" bundle="EXPENDITURE_RESOURCES"/></span>
				</html:link>
				<span class="bar">|</span>
			</li>
			<li>
				<html:link action="/announcementProcess.do?method=showMyProcesses">
					<span><bean:message key="link.sideBar.announcementProcess.myProcesses" bundle="EXPENDITURE_RESOURCES"/></span>
				</html:link>
			</li>
		</logic:present>
		<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL_MANAGER">
			<li>
				<span class="bar">|</span>
				<html:link action="/announcementProcess.do?method=showPendingProcesses">
					<span><bean:message key="link.sideBar.announcementProcess.pendingProcesses" bundle="EXPENDITURE_RESOURCES"/></span>
				</html:link>
			</li>
		</logic:present>
	</logic:present>
</ul>
