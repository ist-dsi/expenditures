<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/collection-pager" prefix="cp"%>

<h2><bean:message key="label.mission.searchMissions" bundle="MISSION_RESOURCES"/></h2>

<fr:form action="/searchMissions.do?method=search">
	<fr:edit id="searchBean" name="searchBean" schema="module.mission.search.default" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
		</fr:layout>
	</fr:edit>
	<html:submit styleClass="inputbutton"><bean:message key="button.search" bundle="MISSION_RESOURCES"/> </html:submit>
</fr:form>

<c:if test="${not empty searchResults}">
	<bean:define id="searchParametersWithSort"><c:out value="${searchBean.requestParametersWithSort}" /></bean:define>
	<bean:define id="searchParameters"><c:out value="${searchBean.requestParameters}" /></bean:define>
	<br/>
	<html:link action="<%= "/searchMissions.do?method=downloadSearchResult&" + searchParametersWithSort %>">
		<img border="0" src="images/excel.gif">
		<bean:message key="link.xlsFileToDownload" bundle="ACQUISITION_RESOURCES"/>
	</html:link>
	<p class="aright mtop05">
		<cp:collectionPages url="<%= "/searchMissions.do?method=search&" + searchParametersWithSort %>" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>
	</p>
	<fr:view name="searchResults" schema="module.mission.domain.Mission.search">
		<fr:layout name="tabular-sortable">		
			<fr:property name="classes" value="tview1 width100pc"/>
			<fr:property name="columnClasses" value="width30px,,,,,,aleft,"/>
			
			<fr:property name="sortParameter" value="sortBy"/>
       		<fr:property name="sortUrl" value="<%= "/searchMissions.do?method=search&" + searchParameters%>" />
		    <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "missionProcess.processIdentification=desc" : request.getParameter("sortBy") %>"/>
			<fr:property name="sortIgnored" value="true"/>					
			<fr:property name="sortableSlots" value="missionProcess.processIdentification, country, daparture, arrival, missionItemsCount, missionProcess.dateFromLastActivity, requestingPerson" />
		</fr:layout>
	</fr:view>
	<p class="aright mtop05">
		<cp:collectionPages url="<%= "/searchMissions.do?method=search&" + searchParametersWithSort %>" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>
	</p>
</c:if>
