<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<h2><bean:message key="label.pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.AddPayingUnit" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="processOID" name="acquisitionProcess" property="OID"/>
<fr:edit id="unitToAdd" name="domainObjectBean" schema="select.unit.from.domain.object.bean" 
action="<%="/acquisitionProcess.do?method=addPayingUnit&acquisitionProcessOid=" + processOID%>">
	<fr:destination name="cancel" path="<%= "/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid=" + processOID %>"/>
</fr:edit>