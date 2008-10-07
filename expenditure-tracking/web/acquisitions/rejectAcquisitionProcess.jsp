<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- acquisitions/rejectAcquisitionProcess.jsp -->

<h2><bean:message key="acquisitionProcess.title.rejectAcquisitionRequest" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="acquisitionProcess" name="acquisitionProcess" type="pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess" toScope="request"/>
 
<jsp:include page="viewAcquisitionRequest.jsp" flush="true"/>

<bean:define id="urlView">/acquisitionProcess.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>
<bean:define id="urlAdd">/acquisitionProcess.do?method=rejectAcquisitionProcess&amp;acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>

<div class="forminline">
	<fr:form action="<%= urlAdd %>">
		<fr:edit id="stateBean"
				name="stateBean"
				type="pt.ist.expenditureTrackingSystem.domain.dto.ProcessStateBean"
				schema="stateJustification"
				>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form"/>
				<fr:property name="columnClasses" value=",,tderror"/>
			</fr:layout>
		</fr:edit>
		<html:submit styleClass="inputbutton"><bean:message key="button.reject" bundle="EXPENDITURE_RESOURCES"/></html:submit>
	</fr:form>
	
	<fr:form action="<%= urlView %>">
		<html:cancel styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/></html:cancel>
	</fr:form>
</div>





