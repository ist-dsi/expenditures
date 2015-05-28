
<%@page import="module.organization.domain.Person"%>
<%@page import="java.util.Collections"%>

<%@page import="java.util.stream.Stream"%>
<%@page import="java.util.Collection"%>
<%@page
	import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page
	import="pt.ist.expenditureTrackingSystem.ui.MissionResponsibilityController"%>
<%@page import="org.springframework.web.bind.annotation.RequestParam"%>

<%@page import="module.organization.domain.AccountabilityType"%>
<%@page import="java.util.Set"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>


<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.*"%>
<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<script
	src='<%=request.getContextPath() + "/bennu-portal/js/angular.min.js"%>'></script>

<%
 
    final String contextPath = request.getContextPath();
    final User user = (User) request.getAttribute("selectedUser");
    final Boolean closerr =(Boolean) request.getAttribute("er");
    final String linha = (String) request.getAttribute("linha");
 
%>
  

<div class="page-header">
	<h1>
		<spring:message code="title.mission.responsible.manage.missions"
			text="Responsible management by missions" />
	</h1>
</div>

<div class="infobox">
	<table id="addResultTable" class="table" >
			<thead>
				<tr>
					<th><spring:message code="label.expenditure.assign.photo" text=""/></th>
					<th><spring:message code="label.expenditure.assign.userName" text="user"/></th>
					<th><spring:message code="label.expenditure.assign.name" text="name"/></th>	
					<th><spring:message code="label.expenditure.assign.email" text="email"/></th>							
				</tr>
			</thead>
			<tbody id="searchResultsToAdd">
				
					
					<tr class="ng-scope" >
						<td ><code class="ng-binding">
							<img class="img-circle" width="50" height="50" alt="" src="<%= user.getProfile().getAvatarUrl() + "?s=50" %>"/>
							</code>
						</td>
						<td><code class="ng-binding">
							<%= user.getUsername() %>
							</code>
						</td>
						<td class="ng-binding">
							<%= user.getProfile().getFullName() %>
						</td>
						<td class="ng-binding">
							<%= user.getProfile().getEmail()==null ? "" : user.getProfile().getEmail() %>
						</td>

					</tr>
				
			</tbody>
		</table>

</div>

<jsp:include page="showPersonWorkingPlaceInformation.jsp"/>
<br>
<jsp:include page="showPersonMissionResponsibilities.jsp"/>

