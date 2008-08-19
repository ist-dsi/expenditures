<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<bean:define id="processOID" name="process" property="OID"/>

Payment Info: <fr:edit id="reference" name="bean" slot="string" action="<%= "/acquisitionProcess.do?method=executePayAcquisitionAction&amp;acquisitionProcessOid=" + processOID %>"/> 