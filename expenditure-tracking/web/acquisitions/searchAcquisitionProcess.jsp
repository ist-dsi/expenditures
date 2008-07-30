<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.search.aquisition.process" bundle="EXPENDITURE_RESOURCES"/></h2>
<br/>
<fr:edit action="/acquisitionProcess.do?method=searchAcquisitionProcess"  id="searchAcquisitionProcess"
		name="searchAcquisitionProcess"
		type="pt.ist.expenditureTrackingSystem.domain.acquisitions.SearchAcquisitionProcess"
		schema="searchAcquisitionProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
	</fr:layout>
</fr:edit>


<bean:define id="acquisitionProcesses" name="searchAcquisitionProcess" property="result"/>
<fr:view name="acquisitionProcesses"
		schema="viewAcquisitionProcessInList">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 mtop2"/>

		<fr:property name="link(view)" value="/acquisitionProcess.do?method=viewAcquisitionProcess"/>
		<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
		<fr:property name="key(view)" value="link.view"/>
		<fr:property name="param(view)" value="OID/acquisitionProcessOid"/>
		<fr:property name="order(view)" value="1"/>
	</fr:layout>
</fr:view>
