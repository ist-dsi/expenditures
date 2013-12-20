<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

<bean:define id="process" name="information" property="process"/>

<logic:iterate id="entry" name="information" property="possibleParticipantAuthorizationChains">
	<bean:define id="person" name="entry" property="key"/>
	<bean:define id="participantAuthorizationChain" name="entry" property="value"/>

	<div class="mbottom2">
	
		<p>
			<html:link styleClass="secondaryLink" page="/expenditureTrackingOrganization.do?method=viewPerson" paramId="personOid" paramName="person" paramProperty="user.expenditurePerson.externalId">
				<b><fr:view name="person" property="name"/></b>
			</html:link>
		</p>
	
		<ul>
		<logic:iterate id="participantAuthorizationChain" name="entry" property="value">
			<li>
				<bean:define id="authorizationChain" name="participantAuthorizationChain" property="authorizationChain" toScope="request"/>
				<jsp:include page="../authorizationChain.jsp"/>
				<p style="margin: 0.25em 0 1.5em 0;">
					<bean:define id="url" type="java.lang.String">/missionProcess.do?method=defineParticipantAuthorizationChain&amp;processId=<bean:write name="process" property="externalId"/>&amp;authorizationChainExternalId=<bean:write name="participantAuthorizationChain" property="authorizationChain.exportString"/></bean:define>
					<html:link page="<%= url %>" paramId="personId" paramName="person" paramProperty="externalId">
						<bean:message bundle="MISSION_RESOURCES" key="link.participant.authorization.chain.select"/>
					</html:link>
				</p>
			</li>
		</logic:iterate>
		</ul>
	
	</div>

</logic:iterate>
