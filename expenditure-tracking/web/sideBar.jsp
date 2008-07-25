<%@page import="pt.ist.expenditureTrackingSystem.presentationTier.Context"%>

<%
	final Context context = Context.getContext();
	final String module = context == null ? null : context.getModule();
	if (module == null) {
%>
		No module selected.
<%
	} else {
	    final String sideBar = "/" + module + "/sideBar.jsp";
%>
		<jsp:include page="<%= sideBar %>"></jsp:include>
<%
	}
%>
