<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

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
