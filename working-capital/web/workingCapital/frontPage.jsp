<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page"/>
</h2>

<fr:form action="/workingCapital.do?method=frontPage">
<fr:edit id="workingCapitalContext" name="workingCapitalContext" >
	<fr:schema type="module.workingCapital.presentationTier.action.util.WorkingCapitalContext" bundle="WORKING_CAPITAL_RESOURCES">
		<fr:slot name="year" key="label.module.workingCapital.year" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form" />
		<fr:property name="columnClasses" value=",,tderror" />
	</fr:layout>
</fr:edit>

<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
</fr:form>

<div style="float: left; width: 100%">
	<table style="width: 100%; margin: 1em 0;">
		<tr>
			<td style="border: 1px dotted #aaa; padding: 10px 15px; width: 48%; vertical-align: top;">
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.pending.my.aproval"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.pendingAproval"/>
				<jsp:include page="processList.jsp"/>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.pending.my.verification"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.pendingVerification"/>
				<jsp:include page="processList.jsp"/>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.pending.my.authorization"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.pendingAuthorization"/>
				<jsp:include page="processList.jsp"/>
			</td>
			<td style="border: none; width: 2%; padding: 0;"></td>
			<td style="border: 1px dotted #aaa; padding: 10px 15px; width: 48%; vertical-align: top;">
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.mine"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.myWorkingCapital"/>
				<jsp:include page="processList.jsp"/>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.requested"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.requestedWorkingCapital"/>
				<jsp:include page="processList.jsp"/>

				<br/>
				<html:link page="/workingCapital.do?method=prepareCreateWorkingCapitalInitialization">
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.create"/>
				</html:link>
			</td>
		</tr>
	</table>
</div>
<div style="clear: left;"></div>

<br/>

<h3>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.search"/>
</h3>
<fr:edit id="workingCapitalContextUnitSearch" name="workingCapitalContext" action="/workingCapital.do?method=search">
	<fr:schema type="module.workingCapital.presentationTier.action.util.WorkingCapitalContext" bundle="WORKING_CAPITAL_RESOURCES">
		<fr:slot name="unit" layout="autoComplete" key="label.party" bundle="ORGANIZATION_RESOURCES" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
        	<fr:property name="labelField" value="partyName.content"/>
			<fr:property name="format" value="${presentationName}"/>
			<fr:property name="minChars" value="3"/>
			<fr:property name="args" value="provider=module.organization.presentationTier.renderers.providers.UnitAutoCompleteProvider"/>
			<fr:property name="size" value="60"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form" />
		<fr:property name="columnClasses" value=",,tderror" />
	</fr:layout>
</fr:edit>

<br/>

<logic:present name="unitProcesses">
	<bean:define id="processList" toScope="request" name="unitProcesses"/>
	<jsp:include page="processList.jsp"/>
</logic:present>
