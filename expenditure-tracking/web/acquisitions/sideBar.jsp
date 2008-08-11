<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter"%>

<ul>
	<logic:present name="<%= SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person">
		<li>
			<html:link action="/acquisitionProcess.do?method=prepareCreateAcquisitionProcess">
				<bean:message key="link.create.aquisition.process" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
	</logic:present>
	<li>
		<html:link action="/acquisitionProcess.do?method=searchAcquisitionProcess">
			<bean:message key="link.search.aquisition.process" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
	</li>
	<li>
		<html:link action="/acquisitionProcess.do?method=showPendingProcesses">
			<bean:message key="link.show.aquisition.pending.processes" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
	</li>	
</ul>
