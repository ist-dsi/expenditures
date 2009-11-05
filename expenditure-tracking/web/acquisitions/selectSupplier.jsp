<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.selectSupplier" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="acquisitionProcess" name="acquisitionProcess" toScope="request"/>
<bean:define id="acquisitionProcessOID" name="acquisitionProcess" property="externalId"/>
<bean:define id="processRequest" name="acquisitionProcess" property="request" toScope="request"/>

<ul>
	<li>
		<html:link page="<%= "/acquisitionSimplifiedProcedureProcess.do?method=viewProcess&acquisitionProcessOid=" + acquisitionProcessOID %>">
			<bean:message key="link.back" bundle="MYORG_RESOURCES"/>
		</html:link>
	</li>
</ul>

<jsp:include page="commons/viewAcquisitionRequest.jsp" flush="true"/>
<logic:present name="processRequest" property="selectedSupplier">
	<div class="infobox_warning">
		<strong><bean:message key="label.warning.alreadyExistsOneSelectedSupplier" bundle="ACQUISITION_RESOURCES"/>:</strong>
			<fr:view name="processRequest" property="selectedSupplier.fiscalIdentificationCode"/> - <fr:view name="processRequest" property="selectedSupplier.name"/>
	</div>
</logic:present>

<div class="infobox">
<table class="structural" style="width: 100%">
<logic:iterate name="processRequest" property="suppliers" id="supplier">
	<tr>
		<td>
		<fr:view name="supplier" property="fiscalIdentificationCode"/> - <fr:view name="supplier" property="name"/>
		</td>
		<td class="aright">
		<html:link page="<%= "/acquisitionSimplifiedProcedureProcess.do?method=selectSupplier&acquisitionProcessOid=" + acquisitionProcessOID %>" paramId="supplierOID" paramName="supplier" paramProperty="externalId">
			<bean:message key="link.choose" bundle="MYORG_RESOURCES"/>
		</html:link>
		</td>
	</tr>
</logic:iterate>
</table>
</div>