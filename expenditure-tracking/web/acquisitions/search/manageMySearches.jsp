<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<div class="helpicon" title="Ajuda">
	<a href="https://fenix-ashes.ist.utl.pt/fenixWiki/Qualidade/Aquisicoes/PesquisaAquisicoes#head-519394d479afe350af823e8512e152289a161ec5" target="_blank"><img src="images/icon_help.gif"></a>
</div>

<h2><bean:message key="title.configureSearches" bundle="EXPENDITURE_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="/search.do?method=search">
			<bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
	</li>
</ul>



<ul style="padding-left: 0em; list-style: none;">

<logic:iterate id="systemSearch" name="systemSearches" >
	<bean:define id="searchOID" name="systemSearch" property="externalId" type="java.lang.String"/>
	<li class="mvert1">
		<div class="infoop6">
			<span>
				<strong><html:link page='<%= "/search.do?method=viewSearch&searchOID=" + searchOID%>'><fr:view name="systemSearch" property="searchName"/></html:link></strong>
				<logic:equal name="systemSearch" property="searchDefaultForCurrentUser" value="false"> 
					(<html:link page='<%= "/search.do?method=setSearchAsDefault&savedSearchOID=" + searchOID%>'><bean:message key="link.setAsDefault" bundle="EXPENDITURE_RESOURCES"/></html:link>)
				</logic:equal>
				<logic:equal name="systemSearch" property="searchDefaultForCurrentUser" value="true">
					(<strong style="color: #777;"><bean:message key="label.default" bundle="EXPENDITURE_RESOURCES"/></strong>)
				</logic:equal>
			</span>
			<div class="mtop05">
				<fr:view name="systemSearch"  schema="viewSavedSearch">
					<fr:layout name="tabular-nonNullValues">
						<fr:property name="classes" value="thlight thleft mtop0"/>
					</fr:layout>
				</fr:view>
			</div>
		</div>
	</li>
</logic:iterate>

<logic:iterate id="userSearch" name="userSearches" >
	<bean:define id="searchOID" name="userSearch" property="externalId" type="java.lang.String"/>
	<li class="mvert1">
		<div class="infoop6">
			<span>
				<strong><html:link page='<%= "/search.do?method=viewSearch&searchOID=" + searchOID%>'><fr:view name="userSearch" property="searchName"/></html:link></strong>
				( <html:link page='<%= "/search.do?method=deleteMySearch&savedSearchOID=" + searchOID%>'><bean:message key="link.delete" bundle="EXPENDITURE_RESOURCES"/></html:link> |
				<logic:equal name="userSearch" property="searchDefaultForCurrentUser" value="false"> 
					<html:link page='<%= "/search.do?method=setSearchAsDefault&savedSearchOID=" + searchOID%>'>
						<bean:message key="link.setAsDefault" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</logic:equal>
				<logic:equal name="userSearch" property="searchDefaultForCurrentUser" value="true">
					<strong style="color: #777;"><bean:message key="label.default" bundle="EXPENDITURE_RESOURCES"/></strong>
				</logic:equal>)
			</span>
			<div>
				<fr:view name="userSearch" schema="viewSavedSearch">
					<fr:layout name="tabular-nonNullValues">
						<fr:property name="classes" value="thlight thleft mtop0"/>
					</fr:layout>
				</fr:view>
			</div>
		</div>
	</li>
</logic:iterate>

</ul>