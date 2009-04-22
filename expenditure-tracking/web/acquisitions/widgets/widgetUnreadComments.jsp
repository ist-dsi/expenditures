<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<div id="widgetUnreadComments" class="portlet">
		<div class="portlet-header"><bean:message key="process.title.processesWithUnreadComments" bundle="EXPENDITURE_RESOURCES"/></div>
		<div class="portlet-content">
			<logic:notEmpty name="processesWithUnreadComments">
				<table>
					<logic:iterate id="process" name="processesWithUnreadComments">
						<bean:define id="className" name="process" property="class.simpleName"/>
						<bean:define id="oid" name="process" property="OID"/>
						<tr>
							<td>
								<html:link page="<%= "/acquisition" + className + ".do?method=viewProcess&processOid=" + oid %>">
									<fr:view name="process" property="acquisitionProcessId" />
								</html:link>	
							</td>
							<td><fr:view name="process" property="processStateName" /></td>
						</tr>
					</logic:iterate>
					<tr>
						<td colspan="2" class="aright" style="padding-bottom: 8px !important;">
							<html:link styleId="markAllAsRead" page="/dashBoard.do?method=markAllCommentsAsRead">
								<bean:message key="label.markAllAsRead" bundle="EXPENDITURE_RESOURCES"/>
							</html:link>
							
							<bean:define id="message">
								<bean:message key="label.markAllAsRead.confirmation" bundle="EXPENDITURE_RESOURCES"/>
							</bean:define>
							
							<bean:define id="title">
								<bean:message key="title.confirmation" bundle="EXPENDITURE_RESOURCES"/>
							</bean:define>
							<script src="<%= request.getContextPath() + "/javaScript/jquery.alerts.js"%>" type="text/javascript"></script> 
 							<script src="<%= request.getContextPath() + "/javaScript/alertHandlers.js"%>" type="text/javascript"></script> 
							<script type="text/javascript"> 
							   linkConfirmationHook('markAllAsRead', '<%= message %>','<%= title %>'); 
							</script> 
						</td>
					</tr>
				</table>
			</logic:notEmpty>
			<logic:empty name="processesWithUnreadComments">
				<p><em><bean:message key="label.no.processesWithUnreadComments" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
			</logic:empty>
			</div>
	</div>