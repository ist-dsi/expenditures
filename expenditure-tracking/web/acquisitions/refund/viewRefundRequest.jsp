<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="processClass" name="refundProcess" property="class.simpleName"/>
<bean:define id="actionMapping" value='<%= "/acquisition" + processClass%>'/>

<h2><bean:message key="refundProcess.title.viewRefundRequest" bundle="ACQUISITION_RESOURCES"/></h2>

<jsp:include page="../../commons/defaultErrorDisplay.jsp"/>

<logic:present name="refundProcess" property="currentOwner">
	<bean:define id="ownerName" name="refundProcess" property="currentOwner.firstAndLastName"/>
	<div class="infoop4">
		<bean:message key="acquisitionProcess.message.info.currentOwnerIs" bundle="ACQUISITION_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>


<div class="infoop1">
	<ul>
	<logic:iterate id="activity" name="refundProcess" property="activeActivitiesForRequest">
		<bean:define id="activityName" name="activity" property="class.simpleName"/> 
		<li>
			<html:link page='<%= actionMapping + ".do?method=execute" + activityName %>' paramId="refundProcessOid" paramName="refundProcess" paramProperty="OID">
				 <fr:view name="activity" property="class">
					<fr:layout name="label">
						<fr:property name="bundle" value="ACQUISITION_RESOURCES"/>
						<fr:property name="escape" value="false"/>
					</fr:layout>
				</fr:view>
			</html:link>
		</li>
	</logic:iterate>
	</ul>
	<logic:empty name="refundProcess" property="activeActivitiesForRequest">
		<p>
			<em>
				<bean:message key="messages.info.noOperatesAvailabeATM" bundle="EXPENDITURE_RESOURCES"/>.
			</em>
		</p>
	</logic:empty>
</div>

<bean:define id="urlConfirm"><%=actionMapping %>.do</bean:define>
<bean:define id="processOid" name="refundProcess" property="OID"/>

<logic:present name="confirmTake">
	<div class="warning2">
		<p><span><bean:message key="message.confirm.take.acquisition.process" bundle="ACQUISITION_RESOURCES"/></span></p>
		<div class="forminline">
			<form action="<%= request.getContextPath() + urlConfirm %>" method="post">
				<html:hidden property="method" value="takeProcess"/>
				<html:hidden property="confirmTake" value="yes"/>
				<html:hidden property="processOid" value="<%= processOid.toString() %>"/>
				<html:submit styleClass="inputbutton"><bean:message key="button.yes" bundle="EXPENDITURE_RESOURCES"/></html:submit>
			</form>
			<form action="<%= request.getContextPath() + urlConfirm %>" method="post">
				<html:hidden property="method" value="viewProcess"/>
				<html:hidden property="processOid" value="<%= processOid.toString() %>"/>
				<html:cancel styleClass="inputbutton"><bean:message key="button.no" bundle="EXPENDITURE_RESOURCES"/></html:cancel>
			</form>
		</div>
	</div>
</logic:present>

<ul class="operations">
	<li>
	<logic:present name="refundProcess" property="currentOwner">
		<logic:equal name="refundProcess" property="userCurrentOwner" value="true">
				<html:link page='<%= actionMapping + ".do?method=releaseProcess" %>' paramId="processOid" paramName="refundProcess" paramProperty="OID">
					<bean:message key="acquisitionProcess.link.releaseProcess" bundle="ACQUISITION_RESOURCES"/>
				</html:link>
		</logic:equal>
		<logic:equal name="refundProcess" property="userCurrentOwner" value="false">
				<html:link page='<%= actionMapping + ".do?method=stealProcess" %>' paramId="processOid" paramName="refundProcess" paramProperty="OID">
					<bean:message key="acquisitionProcess.link.stealProcess" bundle="ACQUISITION_RESOURCES"/>
				</html:link>
		</logic:equal>
	</logic:present>
	<logic:notPresent name="refundProcess" property="currentOwner">
		<html:link page='<%= actionMapping + ".do?method=takeProcess" %>' paramId="processOid" paramName="refundProcess" paramProperty="OID">
				<bean:message key="acquisitionProcess.link.takeProcess" bundle="ACQUISITION_RESOURCES"/>
		</html:link>
	</logic:notPresent>
	</li>
	<li>
		<html:link page='<%= actionMapping + ".do?method=prepareGenericUpload" %>' paramId="processOid" paramName="refundProcess" paramProperty="OID">
			<bean:message key="acquisitionProcess.link.uploadFile" bundle="ACQUISITION_RESOURCES"/>
		</html:link>
	</li>

	<bean:size id="comments"  name="refundProcess" property="comments"/>
	<li> 
		<html:link page='<%= actionMapping + ".do?method=viewComments"%>' paramId="processOid" paramName="refundProcess" paramProperty="OID">
			<bean:message key="link.comments" bundle="EXPENDITURE_RESOURCES"/> (<%= comments %>)
		</html:link>	
		<logic:greaterThan name="comments" value="0">
			|
			<span class="color888" style="font-size: 0.9em;">
				<bean:message key="label.lastBy" bundle="EXPENDITURE_RESOURCES"/> 
				<bean:define id="mostRecentComment" name="refundProcess" property="mostRecentComment"/>
				<strong><fr:view name="mostRecentComment" property="commenter.name"/></strong>, <fr:view name="mostRecentComment" property="date"/> 
			</span>
		</logic:greaterThan>
	</li>
</ul>

<div class="infoop2">
	<fr:view name="refundProcess" property="request"
			schema="viewRefundRequest">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<logic:notEmpty name="refundProcess" property="request.totalAmountsForEachPayingUnit">
	<fr:view name="refundProcess" property="request.totalAmountsForEachPayingUnit"
			schema="viewPayingUnitWithTotalAmount">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5"/>
			<fr:property name="columnClasses" value=",,nowrap,,"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>


<p>
		<bean:message key="acquisitionProcess.label.otherFiles" bundle="ACQUISITION_RESOURCES"/>:
		<logic:notEmpty name="refundProcess" property="files">
			<logic:iterate id="file" name="refundProcess" property="files">
				<html:link action='<%= actionMapping + ".do?method=downloadGenericFile" %>' paramId="fileOID" paramName="file" paramProperty="OID">
					<bean:write name="file" property="displayName"/>
				</html:link>, 
			</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="refundProcess" property="files"><em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em></logic:empty>
	</p>
	


<logic:iterate id="refundItem" name="refundProcess" property="request.requestItems"> 
	<bean:define id="itemOID" name="refundItem" property="OID"/>
	<logic:iterate id="activity" name="refundProcess" property="activeActivitiesForItem" indexId="index">
		<logic:greaterThan name="index" value="0"> | </logic:greaterThan>
			<bean:define id="activityName" name="activity" property="class.simpleName"/> 
			<html:link page='<%= actionMapping + ".do?method=execute" + activityName + "&refundItemOid=" + itemOID%>' paramId="refundProcessOid" paramName="refundProcess" paramProperty="OID">
			<fr:view name="activity" property="class">
				<fr:layout name="label">
						<fr:property name="bundle" value="ACQUISITION_RESOURCES"/>
				</fr:layout>
			</fr:view>
		</html:link>
	</logic:iterate>
	<div class="infoop2">
	<fr:view name="refundItem" 
			schema="viewRefundItem">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
	</div>
		<logic:notEmpty name="refundItem" property="unitItems">
					<table class="payingunits">
						<logic:iterate id="unitItem" name="refundItem" property="sortedUnitItems">
							<tr>
								<td>
									<fr:view name="unitItem" property="unit.presentationName"/>
								</td>
								<td class="nowrap vatop">
									<logic:present name="unitItem" property="realShareValue">
										<fr:view name="unitItem" property="realShareValue"/>
									</logic:present>
									<logic:notPresent name="unitItem" property="realShareValue">
										<fr:view name="unitItem" property="shareValue"/>
									</logic:notPresent>
								</td>
							</tr>
						</logic:iterate>
					</table>
			</logic:notEmpty>
		<logic:notEmpty name="refundItem" property="invoices">
				<table class="tstyle5">
					<tr>
					<th><bean:message key="acquisitionProcess.label.invoice.number" bundle="ACQUISITION_RESOURCES"/></th>		
					<th><bean:message key="acquisitionProcess.label.invoice.date" bundle="ACQUISITION_RESOURCES"/></th>
					<th><bean:message key="label.supplier" bundle="EXPENDITURE_RESOURCES"/></th>
					<th><bean:message key="acquisitionProcess.label.invoice.file" bundle="ACQUISITION_RESOURCES"/></th>
					</tr>	
					<logic:iterate id="invoice" name="refundItem" property="invoices">
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
				</logic:iterate>
				</table>
				
		</logic:notEmpty>
	
</logic:iterate>
