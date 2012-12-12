<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/chart" prefix="chart" %>

<bean:define id="accountability" name="functionDelegationBean" property="accountability" toScope="request" type="module.organization.domain.Accountability"/>

<jsp:include page="delegationForAuthorizationHeader.jsp"/>

<html:messages id="message" message="true">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<h3>
	<bean:message key="label.delegations.edit.title" bundle="MISSION_RESOURCES"/>
</h3>


<fr:form action="/missionOrganization.do?method=editDelegation">		
	<fr:view name="functionDelegationBean">
		<fr:schema type="module.mission.domain.util.FunctionDelegationBean" bundle="ORGANIZATION_RESOURCES">
			<fr:slot name="unit.presentationName" key="label.unit" bundle="ORGANIZATION_RESOURCES">
				<fr:property name="size" value="60"/>
			</fr:slot>
			<fr:slot name="person.presentationName" key="label.person" bundle="ORGANIZATION_RESOURCES">
				<fr:property name="size" value="60"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="mbottom0" />
			<fr:property name="columnClasses" value="width100px, width300px" />
		</fr:layout>
	</fr:view>
	
	<fr:edit id="functionDelegationBean" name="functionDelegationBean">
		<fr:schema type="module.mission.domain.util.FunctionDelegationBean" bundle="ORGANIZATION_RESOURCES">
			<fr:slot name="beginDate" key="label.begin" required="true"/>
			<fr:slot name="endDate" key="label.end"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tgluetop mtop0" />
			<fr:property name="columnClasses" value="width100px,width300px,tderror" />
		</fr:layout>
		<fr:destination name="cancel" path='<%="/missionOrganization.do?method=showDelegationsForAuthorization&authorizationId=" + accountability.getExternalId()%>' />
	</fr:edit>
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
	<html:cancel styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/></html:cancel>
</fr:form>
