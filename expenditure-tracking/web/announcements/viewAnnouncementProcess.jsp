<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- announcement/viewAnnouncementProcess.jsp -->
<div class="wrapper">

<h2><bean:message key="title.viewAnnouncementProcess" bundle="ANNOUNCEMENT_RESOURCES"/></h2>

<div class="infobox_dotted">
	<ul>
		<logic:iterate id="activity" name="announcementProcess" property="activeActivities">
			<bean:define id="activityName" name="activity" property="class.simpleName"/> 
			<li>
				<html:link page='<%= "/announcementProcess.do?method=execute" + activityName %>' paramId="announcementProcessOid" paramName="announcementProcess" paramProperty="externalId">
					<fr:view name="activity" property="class">
						<fr:layout name="label">
							<fr:property name="bundle" value="ANNOUNCEMENT_RESOURCES"/>
						</fr:layout>
					</fr:view>
				</html:link>
			</li>
		</logic:iterate>
	</ul>
	<logic:empty name="announcementProcess" property="activeActivities">
		<em>
			<bean:message key="messages.info.noOperatesAvailabeATM" bundle="EXPENDITURE_RESOURCES"/>.
		</em>
	</logic:empty>
</div>

<div class="infobox">
	<fr:view name="announcementProcess" property="announcement"
			type="pt.ist.expenditureTrackingSystem.domain.announcements.Announcement"
			schema="viewAnnouncementDetails">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL_MANAGER">
	<fr:view name="announcementProcess" property="announcement"
			type="pt.ist.expenditureTrackingSystem.domain.announcements.Announcement"
			schema="viewAnnouncementDetails.admin">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</logic:present>

<logic:present name="rejectionMotive">
	<div class="infobox_warning">
		<b><bean:message key="label.rejectionJustification" bundle="ANNOUNCEMENT_RESOURCES" />:</b>
		<logic:notEmpty name="rejectionMotive">
			<bean:write name="rejectionMotive" />
		</logic:notEmpty>
		<logic:empty name="rejectionMotive">
			<bean:message key="label.rejectionJustification.notSuppliedDescription" bundle="ANNOUNCEMENT_RESOURCES" />
		</logic:empty>
	</div>
</logic:present>