<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>


<logic:empty name="announcements">
	<p><em><bean:message key="process.messages.info.noAvailableAnnouncements" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
</logic:empty>

<%
	final String css = request.getParameter("css");
%>
<bean:define id="url" type="java.lang.String">/viewAcquisitionAnnouncements.do?method=list<% if (request.getParameter("processClassification") != null) {%>&amp;processClassification=<%= request.getParameter("processClassification") %><% } if (css != null && !css.isEmpty()) { %>&amp;css=<%= css %><% } %></bean:define>
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

