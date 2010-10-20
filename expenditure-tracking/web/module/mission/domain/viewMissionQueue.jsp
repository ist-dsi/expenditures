<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<%--
<logic:equal name="process" property="areAllParticipantsAuthorized" value="true">
	<tr>
		<td colspan="4">
			<blockquote>
				<logic:present name="process" property="currentQueue">
					<logic:equal name="process" property="authorized" value="true">
						<bean:message bundle="MISSION_RESOURCES" key="label.mission.queue.in.queue"/>
					</logic:equal>
				</logic:present>
				<logic:notPresent name="process" property="currentQueue">
					<logic:notEmpty name="process" property="queueHistory">
						<bean:message bundle="MISSION_RESOURCES" key="label.mission.queue.in.queue.history"/>
					</logic:notEmpty>
				</logic:notPresent>
			</blockquote>
		</td>
	</tr>
</logic:equal>
 --%>