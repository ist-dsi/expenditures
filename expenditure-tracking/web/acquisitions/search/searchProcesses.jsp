<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>


<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess"%>

<h2><bean:message key="process.label.searchProcesses" bundle="EXPENDITURE_RESOURCES"/></h2>

<bean:define id="schema" value="search.default" toScope="request" />

<logic:present name="searchBean" property="searchClass">
	<bean:define id="simpleName" name="searchBean"
		property="searchClass.simpleName" />
	<bean:define id="schema" value='<%= "search." + simpleName%>'/>
</logic:present>

<div class="forminline mbottom1 mtop075">
	<table>
		<tr>
			<td>
				<fr:form action="/search.do?method=mySearches">
					<fr:edit id="mySearches" name="mySearches" schema='viewMySavedSearches'>
						<fr:layout name="tabular">
							<fr:property name="classes" value="structural thmiddle thlight mvert05"/>
						</fr:layout>
						<fr:destination name="mySearch" path="/search.do?method=mySearches"/>
					</fr:edit>
				</fr:form>
			</td>
			<td>
				| <html:link page="/search.do?method=configurateMySearches"><bean:message key="link.configureSearches" bundle="EXPENDITURE_RESOURCES"/></html:link> | <a href="#" onClick="javascript:document.getElementById('advancedSearch').style.display='block'"><bean:message key="link.advancedSearch" bundle="EXPENDITURE_RESOURCES"/> »</a>
			</td>
		</tr>
	</table>
</div>

<div id="advancedSearch" style="display: none;">
<div class="mbottom15" style="border: 3px solid #eaeaea; background: #fafafa; padding: 0.5em 1em 1em 1em;">
	
	<span class="color888 smalltxt">Para mais opções de pesquisa, seleccione o tipo de aquisição.</span>
	
	<fr:form action="/search.do?method=search">
		<fr:edit id="searchBean" name="searchBean" schema='<%= schema %>' >
			<fr:layout name="tabular">
				<fr:property name="classes" value="form"/>
			</fr:layout>
			<fr:destination name="typeSelector" path="/search.do?method=changeSelectedClass"/>
		</fr:edit>
		<html:submit styleClass="inputbutton"><bean:message key="button.search" bundle="EXPENDITURE_RESOURCES"/> </html:submit> | <a href="#" onClick="javascript:document.getElementById('advancedSearch').style.display='none'"><bean:message key="link.closeAdvancedSearch" bundle="EXPENDITURE_RESOURCES"/></a>
	</fr:form>
</div>
</div>

<logic:equal name="advanced" value="true">
	<script type="text/javascript">
		 document.getElementById('advancedSearch').style.display='block';
	</script>
</logic:equal>


<logic:notEmpty name="results">
	<bean:size id="listSize" name="collectionPager" property="collection"/>
		<bean:define id="pagerString" name="pagerString"/>

	<p class="mvert05"><span style="background: #fff5dd; font-style: italic; color: #542;">
		<span><bean:message key="label.search.suggestion" bundle="ACQUISITION_RESOURCES"/></span>
	</p>

	<table class="width100pc">
	<tr>
	<td>
		<em><bean:message key="label.numberOfFoundProcesses" bundle="ACQUISITION_RESOURCES" arg0="<%= listSize.toString() %>"/>.</em> 
		<logic:equal name="searchBean" property="searchObjectAvailable" value="false">
			<a href="#" onClick="javascript:document.getElementById('saveSearch').style.display='block'"><bean:message key="label.saveSearch" bundle="EXPENDITURE_RESOURCES"/></a>
		</logic:equal>
	
	</td>
	<td class="aright">
	<cp:collectionPages url="<%= "/search.do?method=searchJump" + pagerString %>" 
			pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>
	</td>
	</tr>
	</table>
	
	<div id="saveSearch" style="display: none;">
		<logic:present name="invalidName">
		<div class="error1">
			<span><bean:message key="message.info.mustHaveAName" bundle="EXPENDITURE_RESOURCES"/></span>
		</div>
			<script type="text/javascript">
				 document.getElementById('saveSearch').style.display='block';
			</script>
		</logic:present>
		<div class="warning2">
			<div style="padding: 0.5em 0;">
				<fr:form id="saveForm" action="/search.do?method=saveSearch">
					<fr:edit id="beanToSave" name="searchBean" visible="false"/>
			 		<bean:message key="label.name" bundle="EXPENDITURE_RESOURCES"/>:
			 		<fr:edit id="searchName" name="savingName" slot="string">
				 		<fr:layout>
							<fr:property name="size" value="40"/>
						</fr:layout>
			 		</fr:edit>
					<html:submit styleClass="inputbutton"><bean:message key="label.save" bundle="EXPENDITURE_RESOURCES"/></html:submit>
					<bean:define id="cancelLabel">
						<bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/>
					</bean:define>
					<input type="button" class="inputbutton" onclick="javascript:document.getElementById('saveSearch').style.display='none';" value="<%= cancelLabel %>"/> 
				</fr:form>
			</div>
		</div>
	</div>


	<fr:view name="results" schema="viewProcessesInList">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 mtop05 width100pc asd"/>
				<fr:property name="columnClasses" value="width100px,,,,,,,,nowrap,,,,,,,,,,,,,,,"/>
				<fr:property name="linkFormat(view)" value="/acquisition${class.simpleName}.do?method=viewProcess&processOid=${OID}"/>
				<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="order(view)" value="1"/>
			</fr:layout>
	</fr:view>	
	<p class="aright mtop05">
		<cp:collectionPages url="<%= "/search.do?method=searchJump" + pagerString %>" 
			pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>
	</p>
	

	<p class="mvert025"><em><bean:message key="label.allocationState.title" bundle="EXPENDITURE_RESOURCES"/>:</em></p>
	<p class="mvert025"><em><bean:message key="label.project.allocation" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
	<p class="mvert025"><em><bean:message key="label.allocation" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
	<p class="mvert025"><em><bean:message key="label.project.finalAllocation" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
	<p class="mvert025"><em><bean:message key="label.finalAllocation" bundle="EXPENDITURE_RESOURCES"/>.</em></p>	
</logic:notEmpty>

<logic:empty name="results">
	<p><em><bean:message key="process.label.searchResultEmpty" bundle="EXPENDITURE_RESOURCES"/></em></p>
</logic:empty>