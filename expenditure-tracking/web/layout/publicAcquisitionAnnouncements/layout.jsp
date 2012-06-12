<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="myorg.presentationTier.LayoutContext"%>
<%@page import="myorg.presentationTier.actions.ContextBaseAction"%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">
	<head>
		<title></title>
		<%
			final String css = request.getParameter("css");
			if (css != null && !css.isEmpty()) {
		%>
				<link rel="stylesheet" type="text/css" media="screen" href="<%= css %>" />
		<%
			}
		%>
	</head>
	<body>
		<%
			final LayoutContext layoutContext = (LayoutContext) ContextBaseAction.getContext(request);
		%>
		<jsp:include page="<%= layoutContext.getBody() %>"/>
	</body>
</html>
