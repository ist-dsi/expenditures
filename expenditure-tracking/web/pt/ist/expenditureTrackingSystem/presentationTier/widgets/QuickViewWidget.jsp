<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:define id="widget" name="widget" toScope="request" type="module.dashBoard.domain.DashBoardWidget"/>
<bean:define id="widgetId" name="widget" property="externalId" type="java.lang.String" />
<fr:form action="<%="/dashBoardManagement.do?method=widgetSubmition&dashBoardWidgetId=" + widgetId %>">
	<table class="quicksearch">
		<tr>
			<td>
			<fr:edit id="quickAccess" name="searchBean" schema="search.quick">
				<fr:layout>
					<fr:property name="classes" value="thnowrap" />
					<fr:property name="columnClasses" value=",,dnone" />
				</fr:layout>
			</fr:edit>
			</td>
			<td>
				<html:submit><bean:message key="link.view" bundle="EXPENDITURE_RESOURCES"/></html:submit>
			</td>
		</tr>
	</table>
</fr:form>
<logic:present name="widgetQuickView.messages">
	<bean:define id="label" name="widgetQuickView.messages" type="java.lang.String"/>
	<p class="mtop0"><em><bean:message key="<%= label %>" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
</logic:present>
