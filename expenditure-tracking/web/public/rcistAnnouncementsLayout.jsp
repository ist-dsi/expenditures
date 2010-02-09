<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="myorg.presentationTier.LayoutContext"%>
<%@page import="myorg.presentationTier.actions.ContextBaseAction"%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">

<head>

<title></title> 

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" media="screen" href="http://fenix.ist.utl.pt/CSS/iststyle.css" />
<link rel="stylesheet" type="text/css" media="print" href="http://fenix.ist.utl.pt/CSS/iststyle_print.css" />
<link rel="stylesheet" type="text/css" media="screen" href="http://fenix.ist.utl.pt/CSS/webservice.css" />

</head>
<body>

<%
	final LayoutContext layoutContext = (LayoutContext) ContextBaseAction.getContext(request);
%>


<!-- START HEADER -->
	<div id="logoist">
		<h1><!-- HAS_CONTEXT --><a href="http://www.ist.utl.pt">Instituto Superior Técnico</a></h1>
		<!-- <img alt="[Logo] Instituto Superior Técnico" height="51" src="http://www.ist.utl.pt/img/wwwist.gif?contentContextPath_PATH=/homepage/ist12628/publicacoes&amp;_request_checksum_=066ce7940c117595bbc905fa691e9ed77e0814ab" width="234" /> -->
	</div>
	<div id="header_links"><a href="https://fenix.ist.utl.pt/loginPage.jsp">Login .IST</a> | <a href="#?contentContextPath_PATH=/homepage/ist12628/publicacoes&amp;_request_checksum_=97a96fb754c5f02785fc6499b2520ee217ded01b">Contactos</a></div>
<!-- END HEADER -->
<!--START MAIN CONTENT -->

<div id="container">
<div id="wrapper">
	<jsp:include page="<%= layoutContext.getBody() %>"/>
</div> <!-- #wrapper -->
</div> <!-- #container -->

</body>
</html>
