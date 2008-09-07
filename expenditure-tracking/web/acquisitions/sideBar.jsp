<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter"%>

<ul>
	<li class="header">
		<strong><bean:message key="link.sideBar.acquisitionProcess" bundle="EXPENDITURE_RESOURCES"/></strong>
		<div class="lic1"></div><div class="lic2"></div>
	</li>
	<logic:present name="<%= SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person">
		<li>
			<html:link action="/afterTheFactAcquisitionProcess.do?method=prepareCreateAfterTheFactAcquisitionProcess">
				<bean:message key="link.sideBar.afterTheFactAcquisitionProcess.create" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="/acquisitionProcess.do?method=prepareCreateAcquisitionProcess">
				<bean:message key="link.sideBar.acquisitionProcess.create" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="/acquisitionProcess.do?method=searchAcquisitionProcess">
				<bean:message key="link.sideBar.acquisitionProcess.search" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="/acquisitionProcess.do?method=showPendingProcesses">
				<bean:message key="link.sideBar.acquisitionProcess.pendingProcesses" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>	
		<li>
			<html:link action="/acquisitionProcess.do?method=showMyProcesses">
				<bean:message key="link.sideBar.acquisitionProcess.myProcesses" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>	
	</logic:present>
</ul>
