<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>

<style>
	table.rcistTable {
		margin: 0;
		margin-bottom: 20px;
	}
	table.rcistTable th {
		width: 115px;
	}
	table.rcistTable th b {
		font-weight: normal;
	}
	table.rcistTable th, table.rcistTable td {
		background: #f5f5f5 !important;
	padding: 7px 10px;
	}
	table.rcistTable tr.firstrow th, table.rcistTable tr.firstrow td {
		border-top: 2px solid #ccc;
	}
</style>


<logic:empty name="announcements">
	<p><em><bean:message key="process.messages.info.noAvailableAnnouncements" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
</logic:empty>

<bean:define id="url" type="java.lang.String">/viewAcquisitionAnnouncements.do?method=list<% if (request.getParameter("processClassification") != null) {%>&amp;processClassification=<%= request.getParameter("processClassification") %><% } %></bean:define>
<p class="aright mbottom1">
		<cp:collectionPages url="<%= url %>" 
			pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>
</p>

<logic:iterate id="announcement" name="announcements" indexId="Id">
	<div>
	<fr:view name="announcement" schema="viewRCISTAnnouncement">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thnowrap thlight thleft thtop width100pc rcistTable"/>
			<fr:property name="rowClasses" value="firstrow,,,,"/>
		</fr:layout>
	</fr:view>
	</div>
</logic:iterate>

