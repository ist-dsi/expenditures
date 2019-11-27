<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<logic:empty name="process" property="acquisitionRequest.contractSimpleDescription">
	<div class="infobox_warning">
	 	<p class="mvert025">
	 		<bean:message key="message.info.mustFillContractDescription" bundle="ACQUISITION_RESOURCES" />
	 	</p>
	 </div>
	 <html:link page='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>« <bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/></html:link>
</logic:empty>
<logic:notEmpty name="process" property="acquisitionRequest.contractSimpleDescription">
	<div class="forminline mbottom2">
		<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>
				<fr:edit id="activityBean" name="information">
					<fr:schema type="pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RequestAdvancePaymentActivityInformation" bundle="ACQUISITION_RESOURCES">
						<fr:slot name="template" key="label.advancePayment.subject" layout="menu-select-postback" bundle="ACQUISITION_RESOURCES" required="true">
							<fr:property name="from" value="advancePaymentDocumentTemplateSet" />
							<fr:property name="format" value="\${type.content}" />
						</fr:slot>
						<logic:notEmpty name="information" property="template">
							<bean:define id="partialValue" name="information" property="template.partialValue" type="java.lang.Boolean"/>
						<bean:define id="needAcquisitionJustification" name="information" property="template.needAcquisitionJustification" type="java.lang.Boolean"/>
						<bean:define id="needEntityJustification" name="information" property="template.needEntityJustification" type="java.lang.Boolean"/>
						<fr:slot name="paymentMethod" key="label.advancePayment.paymentMethod" bundle="ACQUISITION_RESOURCES" required="true"/>
						<fr:slot name="percentage" key="label.advancePayment.percentage" bundle="ACQUISITION_RESOURCES">
							<logic:equal name="partialValue" value="false">
								<fr:property name="readOnly" value="true" />
							</logic:equal>
							<logic:equal name="partialValue" value="true">
								<fr:property name="required" value="true" />
								<fr:validator name="pt.ist.expenditureTrackingSystem.presentationTier.renderers.DoubleRangeValidator">
			            			<fr:property name="upperBound" value="100"/>
			            			<fr:property name="lowerBound" value="0.01"/>
			        			</fr:validator>
							</logic:equal>
							<fr:property name="size" value="5" />
							<fr:property name="maxLength" value="5" />
						</fr:slot>
						<fr:slot name="acquisitionJustification" key="label.advancePayment.acquisitionJustification" bundle="ACQUISITION_RESOURCES">
							<logic:equal name="needAcquisitionJustification" value="false">
								<fr:property name="readOnly" value="true" />
							</logic:equal>
							<logic:equal name="needAcquisitionJustification" value="true">
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
							</logic:equal>
						</fr:slot>
						<fr:slot name="entityJustification" key="label.advancePayment.entityJustification" bundle="ACQUISITION_RESOURCES">
							<logic:equal name="needEntityJustification" value="false">
								<fr:property name="readOnly" value="true" />
							</logic:equal>
							<logic:equal name="needEntityJustification" value="true">
								<fr:property name="required" value="true" />
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
							</logic:equal>
						</fr:slot>
						</logic:notEmpty>
					</fr:schema>
					<fr:destination name="postBack" path="/expenditureProcesses.do?method=advancePaymentTemplatePostBack"/>
					<fr:destination name="invalid" path="/expenditureProcesses.do?method=advancePaymentTemplatePostBack"/>
				</fr:edit>
				<html:submit styleClass="btn btn-primary"><bean:message key="button.submit" bundle="EXPENDITURE_RESOURCES"/></html:submit>
		</fr:form>
	
		
		<fr:form action='<%="/workflowProcessManagement.do?method=viewProcess&processId=" + processId%>'>
			<html:submit styleClass="btn btn-default">
				<bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES" />
			</html:submit>
		</fr:form>
	</div>
</logic:notEmpty>