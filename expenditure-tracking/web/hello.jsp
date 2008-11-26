<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="title.site" bundle="EXPENDITURE_RESOURCES"/></h2>

<bean:message key="label.siteIntroduction" bundle="EXPENDITURE_RESOURCES"/>

<table class="tstyle5 width33">
	<tr>
		<th>
			<h3 class="mvert05"><bean:message key="label.startAcquisition" bundle="EXPENDITURE_RESOURCES"/></h3>
		</th>
		<th>
			<h3 class="mvert05"><bean:message key="label.help" bundle="EXPENDITURE_RESOURCES"/></h3>
		</th>
		<th>
			<h3 class="mvert05"><bean:message key="label.userSuport" bundle="EXPENDITURE_RESOURCES"/></h3>
		</th>
	</tr>
	<tr>
		<td>
			<p class="mvert05">
				<bean:message key="label.startAcquisition.text" bundle="EXPENDITURE_RESOURCES"/>
			</p>
		</td>
		<td>
			<p class="mvert05">
				<bean:message key="label.help.text" bundle="EXPENDITURE_RESOURCES"/>
			</p>
		</td>
		<td>
			<p class="mvert05">
			<bean:message key="label.userSuport.processes" bundle="EXPENDITURE_RESOURCES"/>:
			
				<a href='mailto:<bean:message key="label.userSuport.processes.email" bundle="EXPENDITURE_RESOURCES"/>'>
				<bean:message key="label.userSuport.processes.email" bundle="EXPENDITURE_RESOURCES"/>:
				</a>
			</p>
			<p class="mvert05">
			<bean:message key="label.userSuport.technical" bundle="EXPENDITURE_RESOURCES"/>:
			
				<a href='mailto:<bean:message key="label.userSuport.technical.email" bundle="EXPENDITURE_RESOURCES"/>'>
				<bean:message key="label.userSuport.technical.email" bundle="EXPENDITURE_RESOURCES"/>:
				</a>
			</p>
		</td>
	</tr>
</table>

<logic:present name="user">
	<bean:define id="person" name="user" property="person"></bean:define>
	<logic:equal name="person" property="options.displayAuthorizationPending" value="true">
		<logic:notEmpty name="pendingAuthorizationAcquisitionProcesses">
		<h2><bean:message key="title.pendingAuthorizations" bundle="EXPENDITURE_RESOURCES"/></h2>
			<fr:view name="pendingAuthorizationAcquisitionProcesses"
					schema="viewAcquisitionProcessInList">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2"/>

					<fr:property name="linkFormat(view)" value="/acquisition${class.simpleName}.do?method=viewAcquisitionProcess&acquisitionProcessOid=${OID}"/>
					<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
					<fr:property name="key(view)" value="link.view"/>
					<fr:property name="order(view)" value="1"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</logic:equal>
</logic:present>
