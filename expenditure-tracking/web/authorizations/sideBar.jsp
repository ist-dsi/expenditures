<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<%@page import="pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter"%>


	<logic:present name="<%= SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person">
		<ul>
			<li>
				<html:link page="/authorizations.do?method=viewAuthorizations">
					<bean:message key="link.view.authorizations" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
		</ul>
	</logic:present>
