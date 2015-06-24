<%@page import="module.organization.domain.Person"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.stream.Stream"%>
<%@page import="java.util.Collection"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page import="pt.ist.expenditureTrackingSystem.ui.MissionResponsibilityController"%>
<%@page import="org.springframework.web.bind.annotation.RequestParam"%>
<%@page import="module.organization.domain.AccountabilityType"%>
<%@page import="java.util.Set"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.*"%>
<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script src='<%=request.getContextPath() + "/bennu-portal/js/angular.min.js"%>'></script>

<jsp:include page="manageMissions.jsp" />

<%
    final String contextPath = request.getContextPath();
    final User user = (User) request.getAttribute("selectedUser");
    final Boolean closerr = (Boolean) request.getAttribute("er");
    final String linha = (String) request.getAttribute("linha");
%>

<div class="page-header">
	<h2 class="ng-scope">
		<img class="img-circle" width="50" height="50" alt="" src="<%=user.getProfile().getAvatarUrl() + "?s=50"%>" />
		<%=user.getProfile().getDisplayName() %>
		<small>
			<%=user.getUsername()%>
		</small>
	</h2>
</div>

<%
	if (ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup().isMember(Authenticate.getUser())) {
%>
		<div>
			<a href="<%=contextPath%>/expenditure-tracking/manageMissions/prepareRelationshipType/<%=user.getExternalId()%>"
					class="" title="">
				<spring:message code="activity.module.mission.person.mission.addUnit"
						text="activity.module.mission.person.mission.addTypeOfRelation" />
			</a>
		</div>
<%
	}
%>

<jsp:include page="showPersonWorkingPlaceInformation.jsp" />

<jsp:include page="showPersonMissionResponsibilities.jsp" />

