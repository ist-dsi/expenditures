<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.create.unit" bundle="ORGANIZATION_RESOURCES"/></h2>
<br />
<fr:edit id="bean"
		name="bean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.CreateUnitBean"
		schema="createUnit"
		action="/organization.do?method=createNewUnit">
	<fr:layout name="tabular">
	</fr:layout>
	<fr:destination name="cancel" path="/organization.do?method=viewOrganization" />
</fr:edit>
