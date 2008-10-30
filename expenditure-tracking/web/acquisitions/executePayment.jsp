<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<bean:define id="processOID" name="process" property="OID"/>
<bean:define id="processClass" name="process" property="class.simpleName"/>

<h2><bean:message key="label.pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.PayAcquisition" bundle="ACQUISITION_RESOURCES"/></h2>

<fr:form action='<%= "/acquisition" + processClass +".do?method=executePayAcquisitionAction&amp;acquisitionProcessOid=" + processOID %>'>

	<table class="form mbottom05">
		<tr>
			<td>
				<bean:message key="acquisitionProcess.label.paymentInfo" bundle="ACQUISITION_RESOURCES"/>: 
			</td>
			<td>	
				<fr:edit id="reference" name="bean" slot="string" />
			</td>
		</tr>
	</table>
	
	<p>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
	</p>

</fr:form>