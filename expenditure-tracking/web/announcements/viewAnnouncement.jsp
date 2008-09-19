<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<!-- announcement/viewAnnouncement.jsp -->

<div class="wrapper">

<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<div class="infoop2">
	<fr:view name="announcementProcess" schema="viewAnnouncementDetails" property="announcement">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

</div>