<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<ul>
	<li>
		<html:link action="/home.do?method=firstPage">
			<span>
				<bean:message key="link.home" bundle="EXPENDITURE_RESOURCES"/>
			</span>
		</html:link>
	</li>
	<li>
		<html:link action="/acquisitionProcess.do?method=searchAcquisitionProcess">
			<span>
				<bean:message key="link.aquisition.processes" bundle="EXPENDITURE_RESOURCES"/>
			</span>
		</html:link>
	</li>
	<li>
		<html:link action="/organization.do?method=viewOrganization">
			<span>
				<bean:message key="link.view.organization" bundle="EXPENDITURE_RESOURCES"/>
			</span>
		</html:link>
	</li>
</ul>
