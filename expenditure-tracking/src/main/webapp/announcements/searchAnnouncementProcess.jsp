<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="process.label.searchProcesses" bundle="EXPENDITURE_RESOURCES"/></h2>

<fr:form action="/announcementProcess.do?method=searchAnnouncementProcess" >
	<fr:edit id="searchAnnouncementProcess"
			name="searchAnnouncementProcess"
			type="pt.ist.expenditureTrackingSystem.domain.announcements.SearchAnnouncementProcess"
			schema="searchAnnouncementProcess">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
		</fr:layout>
	</fr:edit>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
</fr:form>


<bean:define id="announcementProcesses" name="searchAnnouncementProcess" property="result"/>

<logic:notEmpty name="announcementProcesses">
	<fr:view name="announcementProcesses"
			schema="viewAnnouncementProcessInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop2"/>
	
			<fr:property name="link(view)" value="/announcementProcess.do?method=viewAnnouncementProcess"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/announcementProcessOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="announcementProcesses">
	<p><em><bean:message key="process.label.searchResultEmpty" bundle="EXPENDITURE_RESOURCES"/></em></p>
</logic:empty>
