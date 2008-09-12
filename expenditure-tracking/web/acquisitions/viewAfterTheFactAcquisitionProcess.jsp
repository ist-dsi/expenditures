<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<h2><bean:message key="afterTheFactAcquisitionProcess.title.viewAfterTheFactAcquisitionProcess" bundle="ACQUISITION_RESOURCES"/></h2>

<div class="infoop1">
	<ul>
		<logic:iterate id="activity" name="afterTheFactAcquisitionProcess" property="activeActivities">
			<bean:define id="activityName" name="activity" property="class.simpleName"/> 
			<li>
				<html:link page='<%= "/afterTheFactAcquisitionProcess.do?method=execute" + activityName %>' paramId="afterTheFactAcquisitionProcessOid" paramName="afterTheFactAcquisitionProcess" paramProperty="OID">
					<fr:view name="activity" property="class">
						<fr:layout name="label">
							<fr:property name="bundle" value="ACQUISITION_RESOURCES"/>
						</fr:layout>
					</fr:view>
				</html:link>
			</li>
		</logic:iterate>
	</ul>
	<logic:empty name="afterTheFactAcquisitionProcess" property="activeActivities">
		<em>
			<bean:message key="messages.info.noOperatesAvailabeATM" bundle="EXPENDITURE_RESOURCES"/>.
		</em>
	</logic:empty>
</div>

<div class="infoop2">
	<fr:view name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact.supplier"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact"
			schema="viewSupplierShort">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<div class="infoop2">
	<fr:view name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact"
			schema="viewAcquisitionAfterTheFact">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<div class="documents">
	<p>
		<bean:message key="acquisitionProcess.label.invoice" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact.invoice">
			<logic:present name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact.invoice.content">
				<html:link action="/acquisitionProcess.do?method=downloadInvoice" paramId="invoiceOid" paramName="afterTheFactAcquisitionProcess" paramProperty="acquisitionAfterTheFact.invoice.OID">
					<bean:write name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact.invoice.filename"/>
				</html:link>
			</logic:present>	
			<logic:notPresent name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact.invoice">
				<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
			</logic:notPresent>
		</logic:present>
		<logic:notPresent name="afterTheFactAcquisitionProcess" property="acquisitionAfterTheFact.invoice">
			<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
		</logic:notPresent>
	</p>
</div>
