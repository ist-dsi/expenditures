<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="process" name="information" property="process"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<div class="dinline forminline">

	<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>

		<fr:edit id="activityBean" name="information">
			<fr:schema type="module.workingCapital.domain.activity.RequestCapitalActivityInformation" bundle="WORKING_CAPITAL_RESOURCES">
				<fr:slot name="requestedValue" key="label.module.workingCapital.requestedValue" required="true"/>
				<fr:slot name="paymentMethod" key="label.module.workingCapital.paymentMethod" required="true"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form listInsideClear" />
				<fr:property name="columnClasses" value="width100px,,tderror" />
			</fr:layout>
		</fr:edit>

		<html:submit styleClass="inputbutton"><bean:message key="button.atribute" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
	
	</fr:form>

	<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>

</div>