<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

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