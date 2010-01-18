<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<div class="infobox">
	<fr:view name="processRequest" 
			schema="viewRefundRequest">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>

<logic:present name="processRequest" property="acquisitionProcess.missionProcess">
	<div class="infobox mtop15">
 		<p class="mvert025">
 			<bean:message key="label.acquisition.process.consult.mission.process" bundle="ACQUISITION_RESOURCES"/>:
			<html:link target="blank" action="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="processRequest" paramProperty="acquisitionProcess.missionProcess.externalId">
	 			<bean:write name="processRequest" property="acquisitionProcess.missionProcess.processIdentification"/>
			</html:link>
    	</p>
	</div>
</logic:present>
