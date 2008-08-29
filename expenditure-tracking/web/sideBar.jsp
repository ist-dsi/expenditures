<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page import="pt.ist.expenditureTrackingSystem.presentationTier.Context"%>

<%
	final Context context = Context.getContext();
	final String module = context == null ? null : context.getModule();
	if (module == null) {
%>
	<ul>
		<li>
			<html:link action="/home.do?method=showActiveRequestsForProposal">
				Anúncios
			</html:link>
		</li>
		<li>
			<html:link action="/home.do?method=firstPage">
				Estatísticas
			</html:link>
		</li>
		<li>
			<html:link action="/home.do?method=firstPage">
				Documentação
			</html:link>
		</li>
	</ul>
<%
	} else {
	    final String sideBar = "/" + module + "/sideBar.jsp";
%>
		<jsp:include page="<%= sideBar %>"></jsp:include>
<%
	}
%>
