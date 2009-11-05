<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="acquisitionProcess.title.createAcquisitionRequest" bundle="ACQUISITION_RESOURCES"/></h2>

<div class="infobox">
	<bean:message key="acquisitionProcess.message.note" bundle="ACQUISITION_RESOURCES" />
</div>

<p class="mtop15 mbottom05"><strong><fr:view name="acquisitionProcessBean" property="classification"/></strong></p>

<logic:notEmpty name="acquisitionProcessBean" property="suppliers">

	<table>
		<logic:iterate id="supplier" name="acquisitionProcessBean" property="suppliers" indexId="index">
		<tr>
			<td>
				<fr:view name="supplier" property="name"/>
			</td>
			<td>
				<fr:form action="<%= "/acquisitionSimplifiedProcedureProcess.do?method=removeSupplierInCreationPostBack&index=" + index.toString() %>">
					<fr:edit id="<%= "bean-" + index.toString() %>" name="acquisitionProcessBean" visible="false"/>
					<html:submit><bean:message key="link.remove" bundle="MYORG_RESOURCES"/></html:submit>
				</fr:form>
			</td>
		</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>

<fr:form
	action="/acquisitionSimplifiedProcedureProcess.do?method=addSupplierInCreationPostBack">
	<fr:edit id="bean" name="acquisitionProcessBean"
		schema="createStandardAcquistion.selectSuppliers" />
	<html:submit><bean:message key="link.add" bundle="MYORG_RESOURCES"/></html:submit>
</fr:form>

<fr:form
	action="/acquisitionSimplifiedProcedureProcess.do?method=createNewAcquisitionProcess">
	<fr:edit id="acquisitionProcessBean" name="acquisitionProcessBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean"
		schema="createStandardAcquistion.selectRequester">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form" />
			<fr:property name="columnClasses" value=",,tderror" />
		</fr:layout>
	</fr:edit>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.create" bundle="EXPENDITURE_RESOURCES" />
	</html:submit>
</fr:form>