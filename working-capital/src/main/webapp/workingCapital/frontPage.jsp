<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page"/>
</h2>

<div class="yearSearch">
<fr:form action="/workingCapital.do?method=frontPage">
<fr:edit id="workingCapitalContext" name="workingCapitalContext" >
	<fr:schema type="module.workingCapital.presentationTier.action.util.WorkingCapitalContext" bundle="WORKING_CAPITAL_RESOURCES">
		<fr:slot name="workingCapitalYear" key="label.module.workingCapital.seeYear" required="true" layout="menu-select-postback">
			<fr:property name="providerClass" value="module.workingCapital.presentationTier.provider.WorkingCapitalYearProvider"/>
			<fr:property name="saveOptions" value="true"/>
			<fr:property name="format" value="\${year}"/>
			<fr:property name="nullOptionHidden" value="true"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form" />
		<fr:property name="columnClasses" value=",,tderror" />
	</fr:layout>
</fr:edit>
</fr:form>
</div>

<!-- 
<a href="<%= request.getContextPath() + "/workingCapital.do?method=prepareCreateWorkingCapitalInitialization" %>">
	<span>Constituir Novo Fundo</span>
</a>
 -->


<div style="float: left; width: 100%">
	<table style="width: 100%; margin: 1em 0;">
		<tr>
			<td style="border: 1px dotted #aaa; padding: 10px 15px; width: 48%; vertical-align: top;">
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.pending.my.aproval"/>
					</b>
				</p>
				<logic:present name="workingCapitalContext" property="workingCapitalYear">
					<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.pendingAproval"/>
					<jsp:include page="processList.jsp"/>
				</logic:present>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.pending.my.verification"/>
					</b>
				</p>
				<logic:present name="workingCapitalContext" property="workingCapitalYear">
					<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.pendingVerification"/>
					<bean:define id="processListDirect" toScope="request" name="workingCapitalContext" property="workingCapitalYear.pendingDirectVerification"/>
					<jsp:include page="processListDirect.jsp"/>
				</logic:present>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.pending.my.processing"/>
					</b>
				</p>
				<logic:present name="workingCapitalContext" property="workingCapitalYear">
					<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.pendingProcessing"/>
					<bean:define id="processListDirect" toScope="request" name="workingCapitalContext" property="workingCapitalYear.pendingDirectVerification"/>
					<jsp:include page="processListDirect.jsp"/>
				</logic:present>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.pending.my.authorization"/>
					</b>
				</p>
				<logic:present name="workingCapitalContext" property="workingCapitalYear">
					<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.pendingAuthorization"/>
					<jsp:include page="processList.jsp"/>
				</logic:present>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.payment.pending"/>
					</b>
				</p>
				<logic:present name="workingCapitalContext" property="workingCapitalYear">
					<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.pendingPayment"/>
					<jsp:include page="processList.jsp"/>
				</logic:present>
			</td>
			<td style="border: none; width: 2%; padding: 0;"></td>
			<td style="border: 1px dotted #aaa; padding: 10px 15px; width: 48%; vertical-align: top;">
<!-- Take and give is not an available operation for working capital processes.
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.taken"/>
					</b>
				</p>
				<logic:present name="workingCapitalContext" property="workingCapitalYear">
					<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.taken"/>
					<jsp:include page="processList.jsp"/>
				</logic:present>

				<br/>
 -->
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.mine"/>
					</b>
				</p>
				<logic:present name="workingCapitalContext" property="workingCapitalYear">
					<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.myWorkingCapital"/>
					<jsp:include page="processList.jsp"/>
				</logic:present>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.requested"/>
					</b>
				</p>
				<logic:present name="workingCapitalContext" property="workingCapitalYear">
					<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.requestedWorkingCapital"/>
					<jsp:include page="processList.jsp"/>
				</logic:present>

				<br/>
				<p class="mtop0 mbottom05">
					<b>
						<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.front.page.aprovalResponsible"/>
					</b>
				</p>
				<logic:present name="workingCapitalContext" property="workingCapitalYear">
					<bean:define id="processList" toScope="request" name="workingCapitalContext" property="workingCapitalYear.aprovalResponsibleWorkingCapital"/>
					<jsp:include page="processList.jsp"/>
				</logic:present>

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
        
	<fr:form id="workCapSearchForm" action="/workingCapital.do?method=search">
		
		<fr:edit id="workingCapitalInitializationBean" name="workingCapitalContext" >
			<fr:schema type="module.workingCapital.presentationTier.action.util.WorkingCapitalContext" bundle="WORKING_CAPITAL_RESOURCES">
				<fr:slot name="workingCapitalYear" key="label.module.workingCapital.year" required="true" layout="menu-select">
					<fr:property name="providerClass" value="module.workingCapital.presentationTier.provider.WorkingCapitalYearProvider"/>
					<fr:property name="saveOptions" value="true"/>
					<fr:property name="format" value="\${year}"/>
					<fr:property name="nullOptionHidden" value="true"/>
				</fr:slot>
				<fr:slot name="party" layout="autoComplete" key="label.party" bundle="WORKING_CAPITAL_RESOURCES"
						help="label.module.workingCapital.selectParty.help">
		        	<fr:property name="labelField" value="partyName.content"/>
					<fr:property name="format" value="\${presentationName}"/>
					<fr:property name="minChars" value="3"/>
					<fr:property name="args" value="provider=module.workingCapital.presentationTier.provider.PartiesWithWorkingCapitalFundsAutoCompleteProvider"/>
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
	</fr:form>

	<p>
		<logic:present name="unitProcesses">
			<bean:define id="processList" toScope="request" name="unitProcesses"/>
			<logic:empty name="processList">
				<p>
					<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.process.list.none"/>
				</p>
			</logic:empty>
			<logic:notEmpty name="processList">
			<p>
			<bean:define id="yearOid" name="workingCapitalContext" property="workingCapitalYear.externalId" type="java.lang.String"/>
			<bean:define id="partyOid" type="java.lang.String" > 
				<logic:empty name="workingCapitalContext" property="party">
					blank
				</logic:empty>
				<logic:notEmpty name="workingCapitalContext" property="party">
					<bean:write name="workingCapitalContext" property="party.externalId"/>
				</logic:notEmpty>
			</bean:define>
			<html:link action='<%="/workingCapital.do?method=exportSearchToExcel&yearOid="+yearOid+"&party0id="+partyOid%>'>
			<img border="0" src="<%= request.getContextPath() + "/images/excel.gif" %>">
				<bean:message key="link.xlsFileToDownload" bundle="WORKING_CAPITAL_RESOURCES" />
			</html:link>
		
	</p>
				<fr:view name="processList">
					<fr:schema bundle="WORKING_CAPITAL_RESOURCES" type="module.workingCapital.domain.WorkingCapitalProcess">
					 	<fr:slot name="workingCapital.unit.presentationName" layout="link" key="label.module.workingCapital">
						 	<fr:property name="useParent" value="true"/>
						 	<fr:property name="linkFormat" value="<%= "/workflowProcessManagement.do?method=viewProcess&processId=${externalId}" %>"/>
					 	</fr:slot>
					 	<fr:slot name="presentableAcquisitionProcessState.localizedName" key="WorkingCapitalProcessState"/>
					 	<fr:slot name="workingCapital.accountingUnit.name" key="label.module.workingCapital.initialization.accountingUnit"/>
					</fr:schema>
					<fr:layout name="tabular-sortable">
						<fr:property name="classes" value="tview2 tdleft"/>

						<fr:property name="sortParameter" value="sortBy"/>
						<bean:define id="workingCapitalYearOid" name="workingCapitalContext" property="workingCapitalYear.externalId" type="java.lang.String"/>
						<logic:present name="workingCapitalContext" property="party">
							<bean:define id="partyId" name="workingCapitalContext" property="party.externalId" type="java.lang.String"/>
							<fr:property name="sortUrl" value='<%= "/workingCapital.do?method=sort&partyId=" + partyId + "&workingCapitalYearOid=" + workingCapitalYearOid %>' />
						</logic:present>
						<logic:notPresent name="workingCapitalContext" property="party">
							<fr:property name="sortUrl" value='<%= "/workingCapital.do?method=sort&workingCapitalYearOid=" + workingCapitalYearOid %>' />
						</logic:notPresent>
						<fr:property name="sortBy" value="workingCapital.unit.presentationName=asc"/>
						<fr:property name="sortIgnored" value="true"/>					
						<fr:property name="sortableSlots" value="workingCapital.unit.presentationName, presentableAcquisitionProcessState.localizedName, workingCapital.accountingUnit.name" />
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
		</logic:present>
   </p>
</div>	
		

