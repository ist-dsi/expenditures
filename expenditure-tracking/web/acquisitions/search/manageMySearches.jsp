<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<logic:iterate id="systemSearch" name="systemSearches" >
	<logic:equal name="systemSearch" property="searchDefaultForCurrentUser" value="false"> 
		<bean:define id="searchOID" name="systemSearch" property="OID"/>
		<html:link page='<%= "/search.do?method=setSearchAsDefault&savedSearchOID=" + searchOID%>'>
			Set Default
		</html:link>
	</logic:equal>
	<logic:equal name="systemSearch" property="searchDefaultForCurrentUser" value="true">
		Default
	</logic:equal>
	<div class="infoop2">
		<fr:view name="systemSearch" layout="tabular-nonNullValues"/>
	</div>
</logic:iterate>


<logic:iterate id="userSearch" name="userSearches" >
	<bean:define id="searchOID" name="userSearch" property="OID"/>
	<html:link page='<%= "/search.do?method=deleteMySearch&savedSearchOID=" + searchOID%>'>
		Apagar
	</html:link>
	
	<logic:equal name="userSearch" property="searchDefaultForCurrentUser" value="false"> 
		<bean:define id="searchOID" name="userSearch" property="OID"/>
		<html:link page='<%= "/search.do?method=setSearchAsDefault&savedSearchOID=" + searchOID%>'>
			Set Default
		</html:link>
	</logic:equal>
	<logic:equal name="userSearch" property="searchDefaultForCurrentUser" value="true">
		Default
	</logic:equal>
	<div class="infoop2">
		<fr:view name="userSearch" layout="tabular-nonNullValues"/>
	</div>
</logic:iterate>