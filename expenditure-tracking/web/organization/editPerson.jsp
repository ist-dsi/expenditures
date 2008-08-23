<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.edit.person" bundle="ORGANIZATION_RESOURCES"/></h2>
<br />
<fr:edit id="person"
		name="person"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
		schema="editPerson"
		action="/organization.do?method=searchUsers">
	<fr:layout name="tabular">
	</fr:layout>
</fr:edit>
