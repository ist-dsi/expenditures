<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<div id="widgetQuickView" class="portlet">
		<div class="portlet-header"><bean:message key="title.quickAccess" bundle="EXPENDITURE_RESOURCES" /></div>

		<div class="portlet-content">
				<logic:present name="widgetQuickView.messages">
					<bean:define id="label" name="widgetQuickView.messages" type="java.lang.String"/>
					<p><em><bean:message key="<%= label %>" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
				</logic:present>
				<fr:form action="/dashBoard.do?method=quickAccess">
				<table>
					<tr>
						<td>
							<fr:edit id="quickAccess" name="searchBean" schema="search.quick">
								<fr:layout>
									<fr:property name="columnClasses" value=",,dnone"/>
								</fr:layout>
							</fr:edit>
						</td>
						<td>
							<html:submit><bean:message key="link.view" bundle="EXPENDITURE_RESOURCES"/></html:submit>
						</td>
					</tr>
				</table>
				</fr:form>
		</div>
</div>