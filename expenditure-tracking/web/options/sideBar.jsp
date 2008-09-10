<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<ul>
	<ul>
		<logic:present role="MANAGER">
			<li>
				<html:link action="/customize.do?method=showInterfaceOptions">
					<bean:message key="link.options.interface" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
		</logic:present>
		<li>
			<html:link action="/customize.do?method=showNotificationOptions">
				<bean:message key="link.options.notifications" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
</ul>
