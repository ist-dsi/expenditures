<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoice"%>
<%@page import="java.util.Set"%>
<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present name="process" property="currentOwner">
	<bean:define id="ownerName" name="process" property="currentOwner.expenditurePerson.firstAndLastName"/>
	<div class="infobox_warning">
		<bean:message key="acquisitionProcess.message.info.currentOwnerIs" bundle="ACQUISITION_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>

<bean:define id="process" name="process" type="pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess"/>
<bean:define id="processId" name="process" property="externalId"/>
<bean:define id="name" name="information" property="activityName"/>

<bean:define id="urlActivity" value='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'/>
<bean:define id="urlView" value='<%= "/workflowProcessManagement.do?method=viewProcess&amp;processId=" + processId%>'/>

<div class="infobox_warning">
	<table>
		<%
			final User currentUser = Authenticate.getUser();
			final Set<AcquisitionInvoice> unconfirmedInvoices = process.getUnconfirmedInvoices(currentUser.getExpenditurePerson());
        	final int invoiceCount = unconfirmedInvoices.size();
        	int column = 1;
        	for (AcquisitionInvoice unconfirmedInvoice : unconfirmedInvoices) {
		%>
				<tr>
					<td>
						<bean:message key="activity.confirmation.pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ConfirmInvoice" bundle="ACQUISITION_RESOURCES"
								arg0="<%= unconfirmedInvoice.getInvoiceNumber() %>" arg1="<%= unconfirmedInvoice.getConfirmationReport() %>"/>
					</td>
				</tr>
		<%
        	}
		%>
	</table>
</div>

<br/>

<div class="forminline mbottom2">
	<fr:form id="allocationForm" action="<%= urlActivity %>">
		<fr:edit id="activityBean" name="information" visible="false"/>
		<html:submit styleClass="inputbutton"><bean:message key="button.confirm" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
	</fr:form>

	<fr:form id="allocationForm" action="<%= urlView %>">
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>
