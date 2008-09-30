<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- public/viewAnnouncements.jsp -->


<h2><bean:message key="process.announcement.title" bundle="EXPENDITURE_RESOURCES"/></h2>


<logic:empty name="announcements">
	<p><em><bean:message key="process.messages.info.noProcessesToOperate" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:iterate id="announcement" name="announcements" indexId="Id">
	<div class="infoop2">
		<table class="tstyle1">
			<tr>
				<th><!-- <bean:message key="process.announcement.label.executionDays" bundle="EXPENDITURE_RESOURCES"/> --></th>
				<td><fr:view name="announcement" property="creationDate"/></td>
			</tr>
			<tr>
				<th><!-- <bean:message key="process.announcement.label.supplier" bundle="EXPENDITURE_RESOURCES"/> --></th>
				<td><fr:view name="announcement" property="supplier.name"/></td>
			</tr>
			<tr>
				<th><!-- <bean:message key="process.announcement.label.description" bundle="EXPENDITURE_RESOURCES"/> --></th>
				<td><fr:view name="announcement" property="description"/></td>
			</tr>
			<tr>
				<td colspan="2">
					<html:link page="/home.do?method=viewAnnouncement" paramId="announcementOid" paramName="announcement" paramProperty="OID">
					<bean:message key="link.viewMore" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</td>
			</tr>
		</table>
	</div>
</logic:iterate>
