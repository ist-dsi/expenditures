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
					<bean:define id="oid" name="process" property="OID"/>
					
					<tr>
						<td>
							<html:link page="<%= "/acquisition" + className + ".do?method=viewProcess&processOid=" + oid %>">
								<fr:view name="process" property="acquisitionRequest.acquisitionProcessId" />
							</html:link>
						</td>
						<td><fr:view name="process" property="acquisitionProcessState.acquisitionProcessStateType" /></td>
					</tr>
				</logic:iterate>
				<tr><td colspan="2" class="aright">
					<bean:define id="personOID" name="person" property="OID"/>
					<html:link page="<%= "/search.do?method=searchJump&requestingPerson=" + personOID %>"><bean:message key="label.viewAll" bundle="EXPENDITURE_RESOURCES"/></html:link>
				</td></tr>
				
			</table>	
		</logic:notEmpty>
		
		<logic:empty name="ownProcesses">
			<em><bean:message key="label.no.ownProcesses" bundle="EXPENDITURE_RESOURCES"/></em>.
		</logic:empty>
		
		</div>
	</div>