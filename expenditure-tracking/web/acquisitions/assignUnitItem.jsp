<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<bean:define id="processOID" name="acquisitionProcess" property="OID"/>

<bean:define id="acquisitionRequestItemOid" name="acquisitionRequestItem" property="OID"/>

<div class="infoop2" style="width: 360px">
	<fr:view name="acquisitionRequestItem"
			schema="viewAcquisitionRequestItem">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<fr:edit id="unitItemBeans" name="unitItemBeans" schema="unit.item.bean.edition" action="<%="/acquisitionProcess.do?method=executeAssignPayingUnitToItemCreation&acquisitionProcessOid=" + processOID + "&acquisitionRequestItemOid=" + acquisitionRequestItemOid%>">
	<fr:layout name="tabular"/>
	<fr:destination name="cancel" path="<%= "/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid="  + processOID %>"/>
</fr:edit>