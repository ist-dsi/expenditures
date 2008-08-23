<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="link.edit.unit" bundle="ORGANIZATION_RESOURCES"/></h2>
<br />
<bean:define id="unit"
		name="unit"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Unit"
		/>
<bean:define id="urlView">/organization.do?method=viewOrganization&amp;unitOid=<%= unit.getOID() %></bean:define>
<fr:edit id="unit"
		name="unit"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Unit"
		schema="editUnit"
		action="<%= urlView %>">
	<fr:layout name="tabular">
	</fr:layout>
</fr:edit>
