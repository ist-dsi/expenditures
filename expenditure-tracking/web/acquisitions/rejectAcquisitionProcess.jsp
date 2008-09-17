<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- acquisitions/rejectAcquisitionProcess.jsp -->

<h2><bean:message key="acquisitionProcess.title.rejectAcquisitionRequest" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="acquisitionProcess" name="acquisitionProcess" type="pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess" />

<div class="infoop2">
	<fr:view name="acquisitionProcess" property="acquisitionRequest"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest"
			schema="viewAcquisitionRequest">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<bean:define id="urlView">/acquisitionProcess.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>
<bean:define id="urlAdd">/acquisitionProcess.do?method=rejectAcquisitionProcess&amp;acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>
<fr:edit id="stateBean"
		name="stateBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.ProcessStateBean"
		schema="stateJustification"
		action="<%= urlAdd %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>





