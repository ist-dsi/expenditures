<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<!-- public/viewAnnouncement.jsp -->

<div class="wrapper">

<h2><bean:message key="process.announcement.title.detail" bundle="EXPENDITURE_RESOURCES"/></h2>

<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<div class="infoop2">
<fr:view name="announcement" schema="viewAnnouncement">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
</fr:view>
<fr:view name="announcement" schema="viewAnnouncement.publish">
	<fr:layout name="tabular" />
</fr:view>
</div>

</div>