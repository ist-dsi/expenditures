<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>

<ul>
	<li>
		<html:link action="/acquisitionProcess.do?method=createNewAcquisitionProcess">
			<bean:message key="link.create.aquisition.process" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
	</li>
	<li>
		<html:link action="/acquisitionProcess.do?method=searchAcquisitionProcess">
			<bean:message key="link.search.aquisition.process" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
	</li>
</ul>
