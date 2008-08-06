<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<bean:define id="processOID" name="acquisitionProcess" property="OID"/>

<ul>
	<li>
		<html:link page="<%="/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid=" + processOID %>">
			<bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
	</li>
</ul>

<fr:view name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItems" schema="itemDescription">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="link(edit)" value="/acquisitionProcess.do?method=editAcquisitionRequestItem"/>
			<fr:property name="bundle(edit)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(edit)" value="link.edit"/>
			<fr:property name="param(edit)" value="OID/acquisitionRequestItemOid"/>
			<fr:property name="order(edit)" value="1"/>
		</fr:layout>
</fr:view>

