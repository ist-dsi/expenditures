<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.edit.supplier" bundle="ORGANIZATION_RESOURCES"/></h2>
<br/>
<fr:edit action="/organization.do?method=editSupplier" name="supplier" id="edit"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"
		schema="viewSupplier">
	<fr:layout name="tabular">
	</fr:layout>
</fr:edit>
<br/>
