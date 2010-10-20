<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="module.organization.domain.OrganizationalModel"%>
<%@page import="myorg.domain.MyOrg"%>

<logic:equal name="process" property="mission.readyToHaveAssociatedPaymentProcesses" value="true">
	<div class="infobox">
		<bean:message bundle="MISSION_RESOURCES" key="label.mission.associated.payment.processes"/>
	</div>
</logic:equal>

