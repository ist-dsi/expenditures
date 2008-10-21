<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<h2><bean:message key="label.pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.AddPayingUnit" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="processOID" name="acquisitionProcess" property="OID"/>

<div class="infoop2">
Indique o número ou nome do centro de custo/projecto responsável pelo financiamento da aquisição. Tem de escolher uma das opções da lista.
</div>

<fr:edit id="unitToAdd" name="domainObjectBean" schema="select.unit.from.domain.object.bean" 
action='<%="/acquisitionProcess.do?method=addPayingUnit&acquisitionProcessOid=" + processOID%>'>
	<fr:destination name="cancel" path='<%= "/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid=" + processOID %>'/>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:edit>