<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<bean:define id="processOID" name="process" property="OID"/>

<h2><bean:message key="label.pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.PayAcquisition" bundle="ACQUISITION_RESOURCES"/></h2>

<fr:form action="<%= "/acquisitionProcess.do?method=executePayAcquisitionAction&amp;acquisitionProcessOid=" + processOID %>">
	<table>
		<tr>
			<td>
				<bean:message key="label.payment.identification" bundle="ACQUISITION_RESOURCES"/>: 
			</td>
			<td>	
				<fr:edit id="reference" name="bean" slot="string" />
			</td>
		</tr>
		<tr>
			<td>
					<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
			</td>
			<td>
			</td>
		</tr>
	</table> 
</fr:form>