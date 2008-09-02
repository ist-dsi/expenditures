<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="person.label.create" bundle="ORGANIZATION_RESOURCES"/></h2>
<br />
<fr:edit id="bean"
		name="bean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.CreatePersonBean"
		schema="createPerson"
		action="/organization.do?method=createPerson">
	<fr:layout name="tabular">
	</fr:layout>
	<fr:destination name="cancel" path="/organization.do?method=searchUsers" />
</fr:edit>
