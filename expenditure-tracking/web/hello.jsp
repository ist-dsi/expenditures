<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:notPresent name="user">
	<h2>Anúncios</h2>
	<p>
		...
	</p>

	<h2>Estatísticas</h2>
	<p>
		...
	</p>

	<h2>Documentação</h2>
	<p>
		...
	</p>
</logic:notPresent>
<logic:present name="user">
	<bean:define id="person" name="user" property="person"></bean:define>
	<logic:equal name="person" property="options.displayAuthorizationPending" value="true">
		<h2><bean:message key="label.acquisition.processes.pending.authorization" bundle="EXPENDITURE_RESOURCES"/></h2>
		<br/>
		<logic:present name="pendingAuthorizationAcquisitionProcesses">
			<fr:view name="pendingAuthorizationAcquisitionProcesses"
					schema="viewAcquisitionProcessInList">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1"/>

					<fr:property name="link(view)" value="/acquisitionProcess.do?method=viewAcquisitionProcess"/>
					<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
					<fr:property name="key(view)" value="link.view"/>
					<fr:property name="param(view)" value="OID/acquisitionProcessOid"/>
					<fr:property name="order(view)" value="1"/>
				</fr:layout>
			</fr:view>
		</logic:present>
	</logic:equal>
</logic:present>
