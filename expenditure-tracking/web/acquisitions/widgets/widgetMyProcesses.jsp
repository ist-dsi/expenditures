<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.SavedSearch"%><div id="widgetMyProcesses" class="portlet">
		<div class="portlet-header"><bean:message key="process.title.myProcesses" bundle="EXPENDITURE_RESOURCES"/></div>
		<div class="portlet-content">
		
		<logic:notEmpty name="ownProcesses">
			<table>
				<logic:iterate id="process" name="ownProcesses">
					<bean:define id="className" name="process" property="class.simpleName"/>
					<bean:define id="oid" name="process" property="externalId" type="java.lang.String"/>
					<tr>
						<td>
							<html:link page="<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + oid %>">
								<fr:view name="process" property="acquisitionProcessId" />
							</html:link>
						</td>
						<td><fr:view name="process" property="processStateName" /></td>
					</tr>
				</logic:iterate>
				<tr>
					<td colspan="2" class="aright" style="padding-bottom: 8px !important;">
						<bean:define id="personOID" name="person" property="externalId" type="java.lang.String"/>
						<html:link page="<%= "/search.do?method=searchJump&requestingPerson=" + personOID %>"><bean:message key="label.viewAll" bundle="EXPENDITURE_RESOURCES"/></html:link>
					</td>
				</tr>
			</table>	
		</logic:notEmpty>
		<logic:empty name="ownProcesses">
			<p><em><bean:message key="label.no.ownProcesses" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
		</logic:empty>
		
		</div>
	</div>