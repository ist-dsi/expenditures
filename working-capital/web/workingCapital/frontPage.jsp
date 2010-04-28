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
		<fr:slot name="workingCapitalYear" key="label.module.workingCapital.seeYear" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" layout="menu-select-postback">
			<fr:property name="providerClass" value="module.workingCapital.presentationTier.provider.WorkingCapitalYearProvider"/>
			<fr:property name="saveOptions" value="true"/>
			<fr:property name="format" value="${year}"/>
			<fr:property name="nullOptionHidden" value="true"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form" />
		<fr:property name="columnClasses" value=",,tderror" />
	</fr:layout>
</fr:edit>
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

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.pending.payment"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.pendingPayment"/>
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
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.aprovalResponsible"/>
					</b>
				</p>
				<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.aprovalResponsibleWorkingCapital"/>
				<jsp:include page="processList.jsp"/>

			</td>
		</tr>
	</table>
</div>

<div style="clear: left;"></div>

<div style="border: 1px dotted #aaa; background: #f5f5f5; padding: 10px;  margin-top: 10px;">
		
	<h3 class="mtop0">
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.search"/>
	</h3>
        
	<p class="mvert05">
		<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.search.text"/>
	</p>
        
	<fr:form action="/workingCapital.do?method=search">
		<fr:edit id="workingCapitalInitializationBean" name="workingCapitalContext" >
			<fr:schema type="module.workingCapital.presentationTier.action.util.WorkingCapitalContext" bundle="WORKING_CAPITAL_RESOURCES">
				<fr:slot name="workingCapitalYear" key="label.module.workingCapital.year" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" layout="menu-select">
					<fr:property name="providerClass" value="module.workingCapital.presentationTier.provider.WorkingCapitalYearProvider"/>
					<fr:property name="saveOptions" value="true"/>
					<fr:property name="format" value="${year}"/>
					<fr:property name="nullOptionHidden" value="true"/>
				</fr:slot>
				<fr:slot name="party" layout="autoComplete" key="label.party" bundle="WORKING_CAPITAL_RESOURCES"
						validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"
						help="label.module.workingCapital.selectParty.help">
		        	<fr:property name="labelField" value="partyName.content"/>
					<fr:property name="format" value="${presentationName}"/>
					<fr:property name="minChars" value="3"/>
					<fr:property name="args" value="provider=module.organization.presentationTier.renderers.providers.PartiesAutoCompleteProvider"/>
					<fr:property name="size" value="60"/>
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form" />
				<fr:property name="columnClasses" value=",,tderror" />
			</fr:layout>
		</fr:edit>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
		&nbsp;&nbsp; 
		<html:link action="/workingCapital.do?method=listProcesses" paramId="workingCapitalYearOid" paramName="workingCapitalContext" paramProperty="workingCapitalYear.externalId">
			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.list.all"/>
		</html:link>
	</fr:form>

	<p>
		<logic:present name="unitProcesses">
			<bean:define id="processList" toScope="request" name="unitProcesses"/>
			<jsp:include page="processList.jsp"/>
		</logic:present>			
   </p>     
</div>	
		

