<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:define id="dashBoardId" name="widget" property="dashBoardPanel.externalId" type="java.lang.String"/>

<table>
	<tr>
		<td>
		<bean:message key="label.emailNotification.request" bundle="EXPENDITURE_RESOURCES"/>
		</td>
	</tr>
	<tr>
		<td>
		<fr:form action="/dashBoardManagement.do">
			<html:hidden property="method" value="viewDashBoardPanel"/>
			<html:hidden property="dashBoardId" value="<%= dashBoardId %>"/>
			<bean:define id="options" name="person" property="options"/>
			<fr:edit name="options" slot="receiveNotificationsByEmail">
				<fr:layout name="radio-postback">
					<fr:property name="classes" value="nobullet liinline"/>
				</fr:layout>
			</fr:edit>
		</fr:form>
		</td>
	</tr>
</table>
