<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoiceItem"%>
<%@page import="module.workflow.domain.ProcessFile"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess"%>
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
        <table class="table">
            <tr>
                <th rowspan="2">
                    <bean:message key="label.invoice" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <th rowspan="2">
                    <bean:message key="label.invoice.date" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <logic:notEmpty name="process" property="advancePaymentDocument">
	                <th rowspan="2">
	                    <bean:message key="label.advancePayment" bundle="ACQUISITION_RESOURCES"/>
	                </th>
                </logic:notEmpty>
                <th colspan="5">
                    <bean:message key="label.items" bundle="EXPENDITURE_RESOURCES"/>
                </th>
            </tr>
            <tr>
                <th>
                    <bean:message key="label.item" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <th>
                    <bean:message key="label.quantity" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <th>
                    <bean:message key="label.unitValue" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <th>
                    <bean:message key="label.vatValue" bundle="EXPENDITURE_RESOURCES"/>
                </th>
                <th>
                    <bean:message key="label.additionalCostValue" bundle="EXPENDITURE_RESOURCES"/>
                </th>
            </tr>
        <% for (final ProcessFile processFile : process.getFilesSet()) {
               if (processFile instanceof AcquisitionInvoice) {
                   final AcquisitionInvoice acquisitionInvoice = (AcquisitionInvoice) processFile;
                   final int rowCount = acquisitionInvoice.getItemSet().size();
                   final int span = rowCount == 0 ? 1 : rowCount;
                   int i = 0;
                   %>
                    <tr>
                        <td rowspan="<%= span %>">
                            <html:link action='<%= "/workflowProcessManagement.do?method=downloadFile&processId=" + processId + "&fileId=" + acquisitionInvoice.getExternalId() %>'>
                                <%= acquisitionInvoice.getInvoiceNumber() %>
                            </html:link>
                        </td>
                        <td rowspan="<%= span %>">
                            <%= acquisitionInvoice.getInvoiceDate().toString("yyyy-MM-dd") %>
                        </td>
                       <logic:notEmpty name="process" property="signedAdvancePaymentDocument">
			                <td rowspan="<%= span %>">
			                	  	<bean:message key="<%=acquisitionInvoice.getAdvancePaymentInvoice().toString()%>" bundle="ENUMERATION_RESOURCES"/>
			                </td>
		                </logic:notEmpty>
                        <% for (final AcquisitionInvoiceItem acquisitionInvoiceItem : acquisitionInvoice.getItemSet()) {
                              if (i++ == 0) {
                        %>
                                        <td>
                                            <%= acquisitionInvoiceItem.getItem().getDescription() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getQuantity() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getUnitValue().toFormatString() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getVatValue() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getAdditionalCostValue() == null ? "" : acquisitionInvoiceItem.getAdditionalCostValue().toFormatString() %>
                                        </td>
                        <%     }
                           } %>
                    </tr>
                    <%  i = 0;
                        for (final AcquisitionInvoiceItem acquisitionInvoiceItem : acquisitionInvoice.getItemSet()) {
                          if (i++ > 0) {
                    %>
                            <tr>
                                        <td>
                                            <%= acquisitionInvoiceItem.getItem().getDescription() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getQuantity() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getUnitValue().toFormatString() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getVatValue() %>
                                        </td>
                                        <td>
                                            <%= acquisitionInvoiceItem.getAdditionalCostValue() == null ? "" : acquisitionInvoiceItem.getAdditionalCostValue().toFormatString() %>
                                        </td>
                              </tr>
                    <%     }
                       } %>

                   <%
               }
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
