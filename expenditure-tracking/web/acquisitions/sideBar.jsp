<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="pt.ist.expenditureTrackingSystem.domain.RoleType"%>


<ul>
	<logic:present name="USER_SESSION_ATTRIBUTE" property="person">

		<li class="header">
			<strong><bean:message key="link.sideBar.simplifiedProcedure" bundle="EXPENDITURE_RESOURCES"/></strong>
			<div class="lic1"></div><div class="lic2"></div>
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

	
		<li class="header">
			<strong><bean:message key="link.sideBar.directContract" bundle="EXPENDITURE_RESOURCES"/></strong>
			<div class="lic1"></div><div class="lic2"></div>
		</li>
		<bean:define id="person" name="USER_SESSION_ATTRIBUTE" type="pt.ist.expenditureTrackingSystem.domain.organization.Person" property="person"/>
		<% if (person.hasRoleType(RoleType.ACQUISITION_CENTRAL)) { %>
		<li>
			<html:link action="/afterTheFactAcquisitionProcess.do?method=prepareCreateAfterTheFactAcquisitionProcess">
				<bean:message key="link.sideBar.afterTheFactAcquisitionProcess.create" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
		<% } %>
		<%--
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
		--%>
		<% if (person.hasRoleType(RoleType.ACQUISITION_CENTRAL_MANAGER)) { %>
			<li>
				<html:link action="/acquisitionProcess.do?method=prepareCreateAnnouncement">
					<bean:message key="link.sideBar.acquisitionProcess.createAnnouncement" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
		<% } %>

	</logic:present>
</ul>
