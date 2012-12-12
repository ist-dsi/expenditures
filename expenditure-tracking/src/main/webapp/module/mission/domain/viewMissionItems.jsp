<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>
<%@page import="pt.ist.bennu.core.presentationTier.servlets.filters.contentRewrite.ContentContextInjectionRewriter"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>

<bean:define id="totalItems" name="totalItems"/>
<bean:define id="add" value="<%=  Integer.valueOf("0").toString() %>" type="java.lang.String"/>

<logic:present name="shift" scope="request">
	<bean:define id="add" name="shift" type="java.lang.String"/>
</logic:present>

<h3 class="mtop0 mbottom1"><bean:message bundle="MISSION_RESOURCES" name="missionItemTypeHeaderKey"/></h3>
<logic:empty name="missionItems">
	<p class="mtop05 mbottom2">
		<em>
			<bean:message bundle="MISSION_RESOURCES" key="label.mission.items.none"/>
		</em>
	</p>
</logic:empty>

<logic:notEmpty name="missionItems">

<bean:define id="removeUrl">/missionProcess.do?method=removeMissionItem&amp;processId=<bean:write name="process" property="externalId"/></bean:define>
	
<logic:iterate id="missionItem" name="missionItems" indexId="itemIndex">
	<bean:define id="missionItem" name="missionItem" toScope="request"/>
	<bean:define id="itemOID" name="missionItem" property="externalId" type="java.lang.String"/>
		<div id="<%= "item" + itemOID %>">
			<bean:define id="currentPosition" value="<%= Integer.valueOf(Integer.valueOf(add) + itemIndex + 1).toString()%>"/>
			<strong><bean:message key="label.mission.item" bundle="MISSION_RESOURCES"/></strong> (<fr:view name="currentPosition"/>/<fr:view name="totalItems"/>)

			<logic:equal name="missionItem" property="availableForEdit" value="true">
				<wf:activityLink processName="process" activityName="EditItemActivity" scope="request" paramName0="missionItem" paramValue0="<%= itemOID %>">
					<bean:message bundle="MISSION_RESOURCES" key="link.edit"/>
				</wf:activityLink>

				<wf:activityLink processName="process" activityName="DistributeItemCostsActivity" scope="request" paramName0="missionItem" paramValue0="<%= itemOID %>">
					<bean:message bundle="MISSION_RESOURCES" key="activity.DistributeItemCostsActivity"/>
				</wf:activityLink>

				<wf:activityLink id="<%= "removeLink" + itemOID %>" processName="process" activityName="RemoveItemActivity" scope="request" paramName0="missionItem" paramValue0="<%= itemOID %>">
					<bean:message bundle="MISSION_RESOURCES" key="link.remove"/>
				</wf:activityLink>
			</logic:equal>

				<bean:define id="simpleClassName" name="missionItem" property="class.simpleName"/>

				<logic:equal name="missionItem" property="consistent" value="false">
					<div class="highlightBox">
						<bean:message bundle="MISSION_RESOURCES" key="link.mission.item.not.consistent"/>
					</div>
				</logic:equal>

				<jsp:include page="<%= "missionItemsDisplay/" + simpleClassName + ".jsp" %>" />
				<p class="aright mvert0">
					<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
					<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#"><bean:message key="link.top" bundle="MYORG_RESOURCES"/></a>
					<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>
				</p>
			</div>
	</logic:iterate>
</logic:notEmpty>
