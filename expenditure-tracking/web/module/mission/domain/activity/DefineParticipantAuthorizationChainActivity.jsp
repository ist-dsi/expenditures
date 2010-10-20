<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<bean:define id="process" name="information" property="process"/>

<logic:iterate id="entry" name="information" property="possibleParticipantAuthorizationChains">
	<bean:define id="person" name="entry" property="key"/>
	<bean:define id="participantAuthorizationChain" name="entry" property="value"/>

	<div class="mbottom2">
	
		<p>
			<html:link styleClass="secondaryLink" page="/expenditureTrackingOrganization.do?method=viewPerson" paramId="personOid" paramName="person" paramProperty="externalId">
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
