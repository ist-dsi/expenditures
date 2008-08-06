<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


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
			<fr:property name="link(delete)" value="/acquisitionProcess.do?method=deleteAcquisitionRequestItem"/>
			<fr:property name="bundle(delete)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(delete)" value="link.delete"/>
			<fr:property name="param(delete)" value="OID/acquisitionRequestItemOid"/>
			<fr:property name="order(delete)" value="1"/>
		</fr:layout>
</fr:view>

