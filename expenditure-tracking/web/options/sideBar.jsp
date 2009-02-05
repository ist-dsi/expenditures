<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<ul>
	<ul>
		<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
			<li>
				<html:link action="/customize.do?method=showInterfaceOptions">
					<span><bean:message key="link.options.interface" bundle="EXPENDITURE_RESOURCES"/></span>
				</html:link>
				<span class="bar">|</span>
			</li>
		</logic:present>
		<li>
			<html:link action="/customize.do?method=showNotificationOptions">
				<span><bean:message key="link.options.notifications" bundle="EXPENDITURE_RESOURCES"/></span>
			</html:link>
		</li>
	</ul>
</ul>
