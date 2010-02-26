<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message key="module.workingCapital.title.showAllInitializations" bundle="WORKING_CAPITAL_RESOURCES"/>
</h2>

<bean:define id="process" name="workingCapital" property="workingCapitalProcess" toScope="request"/>
<bean:define id="processId" name="process" property="externalId"/>

<jsp:include page="../module/workingCapital/domain/WorkingCapitalProcess/header.jsp"/>

  
<html:link page='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'> 
	<bean:message key="link.back" bundle="MYORG_RESOURCES"/>
</html:link>

<logic:iterate id="workingCapitalInitialization" name="workingCapital" property="workingCapitalInitializations">

	<div class="infobox mtop1 mbottom1">
				<fr:view name="workingCapitalInitialization" schema="workingCapitalInitialization.view">
					<fr:layout name="tabular">
						<fr:property name="columnClasses" value="aleft width215px,,"/>
					</fr:layout>
				</fr:view>		
	</div>

</logic:iterate>