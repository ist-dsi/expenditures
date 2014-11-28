<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/collection-pager" prefix="cp"%>

<h2><bean:message key="label.mission.searchMissions" bundle="MISSION_RESOURCES"/></h2>

<fr:form action="/searchMissions.do?method=search">
	<fr:edit id="searchBean" name="searchBean">
		<fr:schema type="module.mission.domain.util.SearchMissions" bundle="MISSION_RESOURCES">
			<fr:slot name="processNumber" key="label.mission.processNumber"/>
			<fr:slot name="missionResponsible" layout="autoComplete" key="label.mission.requester.unit">
		        <fr:property name="labelField" value="user.name"/>
				<fr:property name="format" value="\${user.name} (\${user.username})"/>
				<fr:property name="minChars" value="3"/>		
				<fr:property name="args" value="provider=module.mission.presentationTier.provider.autoComplete.PersonAutoComplete"/>
				<fr:property name="size" value="50"/>
			</fr:slot>
			<fr:slot name="payingUnit" layout="autoComplete" key="label.mission.financer">
		        <fr:property name="labelField" value="presentationName"/>
				<fr:property name="format" value="\${presentationName}"/>
				<fr:property name="minChars" value="3"/>		
				<fr:property name="args" value="provider=pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider.UnitAutoCompleteProvider"/>
				<fr:property name="classes" value="inputsize300px"/>
			</fr:slot>
			<fr:slot name="accountingUnit" layout="menu-select" key="label.mission.financer.accounting.unit">
				<fr:property name="providerClass" value="pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider.AccountingUnitProvider"/>
				<fr:property name="format" value="\${name}" />
			</fr:slot>	
			<fr:slot name="national" key="label.mission.national"/>
			<fr:slot name="foreign" key="label.mission.foreign"/>
			<fr:slot name="date" key="label.mission.date" layout="picker" />
			<fr:slot name="interval" key="label.mission.interval" layout="picker" />
			<fr:slot name="requestingPerson" layout="autoComplete" key="label.mission.requester.person">
		        <fr:property name="labelField" value="user.name"/>
				<fr:property name="format" value="\${user.name} (\${user.username})"/>
				<fr:property name="minChars" value="3"/>		
				<fr:property name="args" value="provider=module.mission.presentationTier.provider.autoComplete.PersonAutoComplete"/>
				<fr:property name="size" value="50"/>
			</fr:slot>
			<fr:slot name="participant" layout="autoComplete" key="label.mission.participant">
		        <fr:property name="labelField" value="user.name"/>
				<fr:property name="format" value="\${user.name} (\${user.username})"/>
				<fr:property name="minChars" value="3"/>		
				<fr:property name="args" value="provider=module.mission.presentationTier.provider.autoComplete.PersonAutoComplete"/>
				<fr:property name="size" value="50"/>
			</fr:slot>
			<fr:slot name="accountManager" layout="autoComplete" key="label.mission.accountManager">
		        <fr:property name="labelField" value="user.name"/>
				<fr:property name="format" value="\${user.name} (\${user.username})"/>
				<fr:property name="minChars" value="3"/>		
				<fr:property name="args" value="provider=module.mission.presentationTier.provider.autoComplete.PersonAutoComplete"/>
				<fr:property name="size" value="50"/>
			</fr:slot>
			<fr:slot name="filterCanceledProcesses" key="label.mission.filterCanceledProcesses"/>
			<fr:slot name="filterTakenProcesses" key="label.mission.filterTakenProcesses"/>
			<fr:slot name="pendingState" key="label.mission.pending.state">
				<fr:property name="format" value="\${localizedName}" />
			</fr:slot>
			<fr:slot name="participantAuthorizationAuthority" layout="autoComplete" key="label.mission.participantAuthorizationAuthority">
		        <fr:property name="labelField" value="user.name"/>
				<fr:property name="format" value="\${user.name} (\${user.username})"/>
				<fr:property name="minChars" value="3"/>		
				<fr:property name="args" value="provider=module.mission.presentationTier.provider.autoComplete.PersonAutoComplete"/>
				<fr:property name="size" value="50"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
		</fr:layout>
	</fr:edit>
	<html:submit styleClass="inputbutton"><bean:message key="button.search" bundle="MISSION_RESOURCES"/> </html:submit>
</fr:form>

<logic:notEmpty name="searchResults">
	<bean:define id="searchParametersWithSort"><bean:write name="searchBean" property="requestParametersWithSort"/></bean:define>
	<bean:define id="searchParameters"><bean:write name="searchBean" property="requestParameters" /></bean:define>
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
       		<fr:property name="sortUrl" value='<%= "/searchMissions.do?method=search&" + searchParameters%>' />
		    <fr:property name="sortBy" value='<%= request.getParameter("sortBy") == null ? "missionProcess.processIdentification=desc" : request.getParameter("sortBy") %>'/>
			<fr:property name="sortIgnored" value="true"/>					
			<fr:property name="sortableSlots" value="missionProcess.processIdentification, country, daparture, arrival, missionItemsCount, missionProcess.dateFromLastActivity, requestingPerson" />
		</fr:layout>
	</fr:view>
	<p class="aright mtop05">
		<cp:collectionPages url='<%= "/searchMissions.do?method=search&" + searchParametersWithSort %>' pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>
	</p>
</logic:notEmpty>
