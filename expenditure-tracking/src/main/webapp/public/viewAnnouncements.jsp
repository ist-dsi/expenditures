<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/collection-pager" prefix="cp"%>

<!-- public/viewAnnouncements.jsp -->



<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>

<h2><bean:message key="process.announcement.title" bundle="EXPENDITURE_RESOURCES"/></h2>

<logic:empty name="announcements">
	<p><em><bean:message key="process.messages.info.noAvailableAnnouncements" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
</logic:empty>

<p class="aright mtop05">
		<cp:collectionPages url="/expendituresHome.do?method=showAcquisitionAnnouncements" 
			pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>
</p>

<logic:iterate id="announcement" name="announcements" indexId="Id">
	<fr:view name="announcement">
		<fr:layout name="expandable">
		
			<fr:property name="classes" value="infobox"/>
			<fr:property name="smallLayout" value="tabular"/>
			<fr:property name="smallSchema" value="viewAnnouncementShort.public"/>
			<fr:property name="smallSubProperty(classes)" value="tstyle1"/>
			<fr:property name="smallSubProperty(columnClasses)" value="width190px,,"/>
			
			<fr:property name="expandedLayout" value="tabular"/>
			<fr:property name="expandedSchema" value="viewAnnouncementDetails.public"/>
			<fr:property name="expandedSubProperty(classes)" value="tstyle1"/>
			<fr:property name="expandedSubProperty(columnClasses)" value="width190px,,"/>
			
		</fr:layout>
	</fr:view>
</logic:iterate>

<p class="aright mtop05">
		<cp:collectionPages url="/expendituresHome.do?method=showAcquisitionAnnouncements"
			pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>
	</p>
