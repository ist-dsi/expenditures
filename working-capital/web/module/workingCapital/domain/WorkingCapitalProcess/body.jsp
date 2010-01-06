<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h3>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization"/>
</h3>

<bean:define id="workingCapital" name="process" property="workingCapital"/>

<div class="infobox mtop1 mbottom1">
	<p>
		<logic:iterate id="workingCapitalInitialization" name="workingCapital" property="sortedWorkingCapitalInitializations">

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.movementResponsible"/>:
			<bean:write name="workingCapital" property="movementResponsible.name"/>

			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.fiscalId"/>:
			<bean:write name="workingCapitalInitialization" property="fiscalId"/>

			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.bankAccountId"/>:
			<bean:write name="workingCapitalInitialization" property="bankAccountId"/>

			<br/>
			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.requestedAnualValue.requested"/>:
			<fr:view name="workingCapitalInitialization" property="requestedAnualValue"/>

			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.authorizedAnualValue"/>:
			<logic:present name="workingCapitalInitialization" property="authorizedAnualValue">
				<strong>
					<fr:view name="workingCapitalInitialization" property="authorizedAnualValue"/>
				</strong>
			</logic:present>

			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.maxAuthorizedAnualValue"/>:
			<logic:present name="workingCapitalInitialization" property="maxAuthorizedAnualValue">
				<fr:view name="workingCapitalInitialization" property="maxAuthorizedAnualValue"/>
			</logic:present>

			<br/>
			<br/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.creation"/>:
			<fr:view name="workingCapitalInitialization" property="requestCreation"/>

			<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.initialization.requester"/>:
			<bean:write name="workingCapitalInitialization" property="requestor.name"/>

			<br/>
			<br/>

			Verificação pelo responsável pela contabilidade em:
			<fr:view name="workingCapitalInitialization" property="requestCreation"/>

			por:
			<bean:write name="workingCapitalInitialization" property="requestor.name"/>

			<br/>
			<br/>

			Parecer do Responsável da Unidade em:
			<fr:view name="workingCapitalInitialization" property="requestCreation"/>

			por:
			<bean:write name="workingCapitalInitialization" property="requestor.name"/>

			<br/>
			<br/>

			Despacho do Conselho de Gestão em:
			<fr:view name="workingCapitalInitialization" property="requestCreation"/>

			por:
			<bean:write name="workingCapitalInitialization" property="requestor.name"/>

		</logic:iterate>
	</p>
</div>

