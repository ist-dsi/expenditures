<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<ul>
	<li>
		<html:link action="/statistics.do?method=showSimplifiedProcessStatistics">
			<bean:message key="label.statistics.process.simplified" bundle="STATISTICS_RESOURCES"/>
		</html:link>
	</li>
</ul>
