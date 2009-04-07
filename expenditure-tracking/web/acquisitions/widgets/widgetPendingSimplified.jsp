<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<div id="widgetPendingSimplified" class="portlet">
		<div class="portlet-header"><bean:message key="link.sideBar.simplifiedProcedure" bundle="EXPENDITURE_RESOURCES" /></div>
		<div class="portlet-content">
			<logic:notEmpty name="simplifiedCounters">
				<table>
					<logic:iterate id="counter" name="simplifiedCounters">
						<tr>
							<bean:define id="state" name="counter" property="countableObject" />
							<td><fr:view name="counter" property="countableObject" /></td>
							<td>
								<bean:define id="showResponsabilities" value="false"
									toScope="request" />
								<logic:present name="person">
									<logic:notEmpty name="person" property="authorizations">
										<bean:define id="showResponsabilities" value="true"
											toScope="request" />
									</logic:notEmpty>
								</logic:present>
								
								<html:link
									page="<%="/search.do?method=searchJump&searchClass=pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess&hasAvailableAndAccessibleActivityForUser=true&responsibleUnitSetOnly="
									    + showResponsabilities.toString() + "&acquisitionProcessStateType=" + state%>">
										<fr:view name="counter" property="value" />	    
								</html:link>
										
							</td>
						</tr>
					</logic:iterate>
				</table>	
			</logic:notEmpty>
			<logic:empty name="simplifiedCounters">
				<em><bean:message key="label.no.pendingProcesses" bundle="EXPENDITURE_RESOURCES"/></em>.
			 </logic:empty>
		</div>
	</div>