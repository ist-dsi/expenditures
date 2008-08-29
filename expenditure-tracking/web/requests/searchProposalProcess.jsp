<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.search.aquisition.process" bundle="EXPENDITURE_RESOURCES"/></h2>

<fr:edit action="/requestForProposalProcess.do?method=searchRequestProposalProcess"  id="searchProposalRequestProcess"
		name="searchRequestProposalProcess"
		schema="searchProposalRequestProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:edit>


<bean:define id="acquisitionProcesses" name="searchRequestProposalProcess" property="result"/>
<fr:view name="acquisitionProcesses"
		schema="viewRequestProcessInList">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 mtop2"/>

		<fr:property name="link(view)" value="/requestForProposalProcess.do?method=viewRequestForProposalProcess"/>
		<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
		<fr:property name="key(view)" value="link.view"/>
		<fr:property name="param(view)" value="OID/requestForProposalProcessOid"/>
		<fr:property name="order(view)" value="1"/>
	</fr:layout>
</fr:view>
