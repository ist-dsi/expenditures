<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

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
