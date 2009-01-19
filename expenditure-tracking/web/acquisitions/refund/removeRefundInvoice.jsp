<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="process" name="item" property="request.process"/>
<bean:define id="itemOid" name="item" property="OID"/>
<bean:define id="processOid" name="process" property="OID"/>
<bean:define id="processClass" name="process" property="class.simpleName"/>
<bean:define id="actionMapping" value='<%= "/acquisition" + processClass%>'/>

<ul>
	<li>
		 <html:link action='<%= actionMapping + ".do?method=viewProcess" %>' paramId="processOid" paramName="process" paramProperty="OID">
				<bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/>
		  </html:link>
	</li>
</ul>

<logic:empty name="item" property="invoices">
	Sem facturas
</logic:empty>

<logic:notEmpty name="item" property="invoices">
				<table class="tstyle5">
					<tr>
					<th><bean:message key="acquisitionProcess.label.invoice.number" bundle="ACQUISITION_RESOURCES"/></th>		
					<th><bean:message key="acquisitionProcess.label.invoice.date" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="label.supplier" bundle="EXPENDITURE_RESOURCES"/></th>
					<th><bean:message key="acquisitionProcess.label.invoice.file" bundle="ACQUISITION_RESOURCES"/></th>
					</tr>	
					<logic:iterate id="invoice" name="item" property="invoices">
						<td><fr:view name="invoice" property="invoiceNumber"/></td>
						<td><fr:view name="invoice" property="invoiceDate"/></td>
					    <td> 
					    	<logic:present name="invoice" property="supplier">
					    	  <fr:view name="invoice" property="supplier.name"/>
					    	</logic:present>
					 		<logic:notPresent name="invoice" property="supplier">
					    	-
					    	</logic:notPresent>
					    
					    </td>
						<td>
					   	 <html:link action='<%= actionMapping + ".do?method=downloadInvoice" %>' paramId="invoiceOID" paramName="invoice" paramProperty="OID">
							<bean:write name="invoice" property="file.filename"/>
						  </html:link>
						</td>
						<td>
						  <html:link action='<%= actionMapping + ".do?method=removeRefundInvoice&refundProcessOid=" + processOid + "&refundItemOid=" + itemOid %>' paramId="invoiceOid" paramName="invoice" paramProperty="OID">
								<bean:message key="link.delete" bundle="EXPENDITURE_RESOURCES"/>
						  </html:link>
						</td>
						</tr>
				</logic:iterate>
				</table>
				
		</logic:notEmpty>