<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<ul>
	<li>
		<html:link action="/organization.do?method=viewOrganization">
			<bean:message key="link.view.organization" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
	</li>
	<li>
		<html:link action="/organization.do?method=searchUsers">
			<bean:message key="search.link.users" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
	</li>
	<li>
		<html:link action="/organization.do?method=manageSuppliers">
			<bean:message key="supplier.link.manage" bundle="ORGANIZATION_RESOURCES"/>
		</html:link>
	</li>	
</ul>
