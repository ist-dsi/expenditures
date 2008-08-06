<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="processOID" name="acquisitionProcess" property="OID"/>
<fr:edit id="unitToAdd" name="domainObjectBean" schema="select.unit.from.domain.object.bean" 
action="<%="/acquisitionProcess.do?method=addPayingUnit&acquisitionProcessOid=" + processOID%>"
/>