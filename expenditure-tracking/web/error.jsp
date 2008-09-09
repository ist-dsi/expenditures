<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">
<head>
	<tiles:insert page="head.jsp"/>
</head>

<body>

<div id="container">

	<div id="header">
		<tiles:insert page="pageHeader.jsp"/>
	</div>

	<div id="tabs10">
		<tiles:insert page="topBar.jsp"/>
	</div>

	<div id="container2">

		<div id="sidebar">
		</div>

		<div id="content">
			<h2>
		  		Ocorreu um erro !
		  	</h2>
		</div> <!-- content -->

	</div> <!-- container2 -->

	<div id="footer">
		<tiles:insert page="footer.jsp"/>
	</div>

	<div class="cont_c1"></div>
	<div class="cont_c2"></div>

</div> <!-- container -->

</body>
</html:html>
