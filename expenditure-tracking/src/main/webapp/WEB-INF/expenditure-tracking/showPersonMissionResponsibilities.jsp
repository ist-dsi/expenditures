
<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>
<%@page import="module.organization.domain.Person"%>
<%@page import="module.mission.domain.MissionSystem"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="module.organization.domain.Accountability"%>
<%@page import="java.util.Set"%>
<%@page import="module.organization.domain.AccountabilityType"%>
<%@page import="java.util.Collection"%>
<%@page import="module.mission.domain.util.AuthorizationChain"%>
<%@page
	import="module.mission.domain.util.ParticipantAuthorizationChain"%>
<%@page import="module.organization.domain.Unit"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="org.joda.time.LocalDate"%>
<%@page import="org.springframework.web.bind.annotation.RequestParam"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
    final String contextPath = request.getContextPath();
    final Collection<Accountability> workingPlaceAccountabilities =
            (Collection<Accountability>) request.getAttribute("workingPlaceAccountabilities");
    final Collection<Accountability> authorityAccountabilities =
            (Collection<Accountability>) request.getAttribute("authorityAccountabilities");
    final MissionSystem missionSystem = MissionSystem.getInstance();
    final User user = (User) request.getAttribute("selectedUser");
    final Boolean closerr =(Boolean) request.getAttribute("er");
    final String linha = (String) request.getAttribute("linha");

%>

<%
	if (ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup().isMember(Authenticate.getUser())) {
%>
<div>
	<a
		href="<%=contextPath%>/expenditure-tracking/manageMissions/prepareDelegateForAuthorization/<%=user.getExternalId()%>"
		class="" title=""><spring:message
			code="activity.module.mission.person.mission.addResponsability"
			text="activity.module.mission.person.mission.addResponsability" /></a>
</div>
<%
}
%>

<div>
	<%
	    if (authorityAccountabilities == null || (authorityAccountabilities != null && authorityAccountabilities.isEmpty())) {
	%>

	<h3>
		<spring:message
			code="label.module.mission.person.mission.responsibilities" text="label.module.mission.person.mission.responsibilities" />
	</h3>

	<div class="error0">
		<spring:message
			code="label.module.mission.person.mission.responsibilities.none"
			text="aa" />
	</div>


	<%
	    } else {
	%>

	

	<div>
		<h3>
			<spring:message
				code="label.module.mission.person.mission.responsibilities"
				text="label.module.mission.person.mission.responsibilities" />
		</h3>
	</div>


<div class="ng-scope">
<spring:url var="removeURL"
	value="/expenditure-tracking/manageMissions/removeMissionResponsability/" />
	<table class="table" id="personMissionResponsability">
		<thead>
			<tr>
				<th><spring:message code="label.unit" text="label.unit" /></th>
				<th><spring:message code="label.mission.authority.type"
						text="label.mission.authority.type" /></th>
				<th><spring:message code="label.mission.beginDate"
						text="Data Inicio" /></th>
				<th><spring:message code="label.mission.endDate"
						text="Data Fim" /></th>
			    <th></th>
			    
			</tr>
		</thead>
		<tbody id="searchResultsRespPerson">
			<%
			    for (final Accountability authorityAccountability : authorityAccountabilities) {
			%>

			<tr class="ng-scope">
				<td><code class="ng-binding">
				<a
				href="<%=contextPath%>/expenditure-tracking/manageMissions/?partyId=<%=authorityAccountability.getParent().getExternalId()%>"
				class="" title=""> <%=authorityAccountability.getParent().getPresentationName()%>
				</a>
					</code>
				</td>

				<td><code class="ng-binding">
						<%=authorityAccountability.getAccountabilityType().getName().getContent()%>
					</code></td>
				<td><code class="ng-binding">
						<%=authorityAccountability.getBeginDate()%>
					</code></td>
				<td class="ng-binding"><%=authorityAccountability.getEndDate() == null ? "" : authorityAccountability.getEndDate()%></td>
				<%
	if (ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup().isMember(Authenticate.getUser())) {
%>
				<td>
				<% if(authorityAccountability.getEndDate()== null) { 
				%>				
					<form id="searchForm" class="form-horizontal" role="form" action="${removeURL}" method="GET"> 
					     
							<input id ="partyId" name="partyId" type="hidden" value="<%=user.getExternalId()%>"/>
							<input id ="accountId" name="accountId" type="hidden" value="<%=authorityAccountability.getExternalId()%>"/>
							<input type="submit" class="btn" title="Close" value='<spring:message code="label.close" text="Fechar"/>'/>
							
					</form>
					<% } %>
				</td><% if(closerr && linha.equals(authorityAccountability.getExternalId())){%>
				<span class="error0"> <spring:message code="error.mission.begin.is.after.end" text="true"/>
				</span>
					<%}
				} %>
			
				
			</tr>
			<%
			    }
			%>
		</tbody>
	</table>
</div>
	<%
	    }
	%>

</div>

