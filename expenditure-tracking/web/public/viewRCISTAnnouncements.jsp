<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>

<style>
table.rcistTable th {
	
}
table.rcistTable th, table.rcistTable td {
background: #f5f5f5 !important;
padding: 7px 10px;
}

table.rcistTable tr.firstrow th, table.rcistTable tr.firstrow td {
border-top: 2px solid #ccc;
}
</style>


<h2><bean:message key="title.rcist.announcements" bundle="ACQUISITION_RESOURCES"/></h2>

<p>
	<bean:message key="description.rcist.announcements" bundle="ACQUISITION_RESOURCES"/>
</p>

<logic:empty name="announcements">
	<p><em><bean:message key="process.messages.info.noAvailableAnnouncements" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
</logic:empty>

<p class="aright mtop05">
		<cp:collectionPages url="/viewRCISTAnnouncements.do?method=viewRCIST" 
			pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>
</p>

<logic:iterate id="announcement" name="announcements" indexId="Id">
	<div>
	<fr:view name="announcement" schema="viewRCISTAnnouncement">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin width100pc rcistTable"/>
			<fr:property name="rowClasses" value="firstrow,,"/>
			<fr:property name="columnClasses" value="width100px,,"/>
			
		</fr:layout>
	</fr:view>
	</div>
</logic:iterate>

<p class="aright mtop05">
		<cp:collectionPages url="/viewRCISTAnnouncements.do?method=viewRCIST"
			pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>
	</p>