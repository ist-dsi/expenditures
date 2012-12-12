<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="title.site" bundle="EXPENDITURE_RESOURCES"/></h2>

<div class="infobox_warning">
	Notícia: Foram efectuadas alterações no sistema de navegação e layout. Mais informação <a href="https://fenix-ashes.ist.utl.pt/fenixWiki/Qualidade/Aquisicoes/Interface" target="_blank">aqui</a>.
</div>

<bean:message key="label.siteIntroduction" bundle="EXPENDITURE_RESOURCES"/>

<table class="tstyle3 width33 tdleft">
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
				<html:link action="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcess">
					<bean:message key="link.sideBar.acquisitionProcess.create" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</p>
		</td>
		<td>
			<p class="mvert05">
				<bean:message key="label.help.text" bundle="EXPENDITURE_RESOURCES"/>
			</p>
		</td>
		<td>
			<p class="mvert05">
				<bean:message key="label.userSuport.processes" bundle="EXPENDITURE_RESOURCES"/>
				<a href='mailto:<bean:message key="label.userSuport.processes.email" bundle="EXPENDITURE_RESOURCES"/>'>
					<bean:message key="label.userSuport.processes.email" bundle="EXPENDITURE_RESOURCES"/>
				</a>
			</p>
			<p class="mvert05">
				<bean:message key="label.userSuport.technical" bundle="EXPENDITURE_RESOURCES"/>
				<a href='mailto:<bean:message key="label.userSuport.technical.email" bundle="EXPENDITURE_RESOURCES"/>'>
				<bean:message key="label.userSuport.technical.email" bundle="EXPENDITURE_RESOURCES"/>
				</a>
			</p>
			<p class="mvert05">
				<bean:message key="label.userSuport.newSuppliers" bundle="EXPENDITURE_RESOURCES"/>
				<a href='mailto:<bean:message key="label.userSuport.newSuppliers.email" bundle="EXPENDITURE_RESOURCES"/>'>
					<bean:message key="label.userSuport.newSuppliers.email" bundle="EXPENDITURE_RESOURCES"/>
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
					<fr:property name="linkFormat(view)" value="/acquisition${class.simpleName}.do?method=viewAcquisitionProcess&acquisitionProcessOid=${externalId}"/>
					<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
					<fr:property name="key(view)" value="link.view"/>
					<fr:property name="order(view)" value="1"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</logic:equal>
</logic:present>
