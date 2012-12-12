<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<!-- announcement/viewAnnouncement.jsp -->

<div class="wrapper">

<div class="infobox">
	<fr:view name="announcementProcess" schema="viewAnnouncementDetails" property="announcement">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

</div>
