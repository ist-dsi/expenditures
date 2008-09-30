<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<ul>
	<logic:present name="USER_SESSION_ATTRIBUTE" property="person">

		<li class="header">
			<strong><bean:message key="link.sideBar.announcements" bundle="EXPENDITURE_RESOURCES"/></strong>
			<div class="lic1"></div><div class="lic2"></div>
		</li>
		<logic:present role="ACQUISITION_CENTRAL,ACQUISITION_CENTRAL_MANAGER">
			<li>
				<html:link action="/announcementProcess.do?method=prepareCreateAnnouncement">
					<bean:message key="link.sideBar.announcementProcess.createAnnouncement" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
			<li>
				<html:link action="/announcementProcess.do?method=searchAnnouncementProcess">
					<bean:message key="link.sideBar.announcementProcess.searchProcesses" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
			<li>
				<html:link action="/announcementProcess.do?method=showMyProcesses">
					<bean:message key="link.sideBar.announcementProcess.myProcesses" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
		</logic:present>
		<logic:present role="ACQUISITION_CENTRAL_MANAGER">
			<li>
				<html:link action="/announcementProcess.do?method=showPendingProcesses">
					<bean:message key="link.sideBar.announcementProcess.pendingProcesses" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
		</logic:present>
	</logic:present>
</ul>
