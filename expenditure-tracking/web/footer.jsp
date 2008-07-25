<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p>
	&copy;<dt:format pattern="yyyy"><dt:currentTime/></dt:format> <bean:message key="label.copyright.institution.name" bundle="EXPENDITURE_RESOURCES"/>
</p>
