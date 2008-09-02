<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="title.preferences" bundle="EXPENDITURE_RESOURCES"/></h2>

<h4 class="mvert05"><bean:message key="title.homeConfiguration" bundle="EXPENDITURE_RESOURCES"/></h4>

<fr:edit id="options"
		name="options"
		type="pt.ist.expenditureTrackingSystem.domain.Options"
		schema="editOptions"
		action="/home.do?method=firstPage">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
	</fr:layout>
</fr:edit>
