<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.joda.time.LocalDate"%>
<%@page import="module.organization.domain.Person"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="module.organization.domain.Accountability"%>
<%@page import="java.util.Set"%>
<%@page import="module.organization.domain.AccountabilityType"%>
<%@page import="java.util.Collection"%>
<%@page import="module.organization.domain.Unit"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="org.joda.time.LocalDate"%>
<%@page import="org.springframework.web.bind.annotation.RequestParam"%>
<%@page import="module.mission.domain.util.AuthorizationChain"%>
<%@page
	import="module.mission.domain.util.ParticipantAuthorizationChain"%>
<%@page
	import="pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization"%>
<%@page
	import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@page import="module.mission.domain.MissionSystem"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
    final String contextPath = request.getContextPath();
    final Collection<Accountability> workingPlaceAccountabilities = (Collection<Accountability>) request.getAttribute("workingPlaceAccountabilities");
    final User user = (User) request.getAttribute("selectedUser");
    request.setAttribute("userId",user.getExternalId());
    final Boolean closerr =(Boolean) request.getAttribute("er");
    final String linha = (String) request.getAttribute("linha");
%>

<script src='<%=contextPath + "/bennu-portal/js/angular.min.js"%>'></script>
<script
	src='<%=contextPath + "/bennu-scheduler-ui/js/libs/moment/moment.min.js"%>'></script>
<script
	src='<%=contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js"%>'></script>


<div>
	<%
	    if (workingPlaceAccountabilities == null || (workingPlaceAccountabilities != null && workingPlaceAccountabilities.isEmpty())) {
	%>
	<h3>
		<spring:message
			code="label.module.mission.person.working.place.information"
			text="label.module.mission.person.working.place.information" />
	</h3>

	<div class="alert alert-danger ng-binding ng-hide" ng-show="error">
		<spring:message
			code="label.module.mission.person.working.place.information.none"
			text="aa" />
	</div>
	<%
	    } else {
	%>
	<div>
		<h3>
			<spring:message
				code="label.module.mission.person.working.place.information"
				text="label.module.mission.person.working.place.information" />
		</h3>
	</div>
	<div ng-app="" ng-controller="loadMovementsController">
	<spring:url var="removeURL" value="/expenditure-tracking/manageMissions/removeMissionResponsability/" />
		<table class="table">
			<thead>
				<tr>
					<th><spring:message code="label.unit" text="Unit" /></th>
					<th><spring:message code="label.mission.member.type"
							text="Relation" /></th>
					<th><spring:message code="label.mission.beginDate"
							text="Begin Date" /></th>
					<th><spring:message code="label.mission.endDate"
							text="End Date" /></th>
					<th><spring:message
							code="label.module.mission.persons.authorizationChain"
							text="Cadeia de Autorizações" /></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat-start="m in movs">
					<td><a id="workingId"
						ng-href="<%=contextPath%>/expenditure-tracking/manageMissions/?partyId={{m.externalId}}"
						class="" title=""> {{m.presentationName}}</a></td>
					<td>{{m.content}}</td>
					<td>{{m.beginDate}}</td>
					<td>{{m.endDate == null ? "" : m.endDate}}</td>
					<td ng-click="toggleDetailedMovs(m);"><span
						class="btn btn-default" title="View"><spring:message
								code="label.details" text="Details" /></span></td>
					<%
					    if (ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup().isMember(Authenticate.getUser())) {
					%>
					<td ng-if="!m.endDate">
						<form id="searchForm" class="form-horizontal" role="form"
							action="${removeURL}" method="GET">
							<input id="partyId" name="partyId" type="hidden"
								value="<%=user.getExternalId()%>" />
								 <input id="accountId" name="accountId" type="hidden" value="{{m.id}}"/>
								  <input type="submit" class="btn" title="Close"
										 value='<spring:message code="label.close" text="Fechar"/>' />
						</form>
					</td>
					<%
					    }
					%>
				</tr>
				<tr ng-repeat-end ng-show="m.visibility"
					style="background-color: #F0F0F0;">
					<td colspan=5>
						<table class="table" style="background-color: #F0F0F0;">
							<thead>
								<tr>
									<th><spring:message
											code="link.mission.dislocation.authority.order" text="Order" /></th>
									<th><spring:message code="label.unit" text="Unit" /></th>
									<th><spring:message
											code="link.mission.dislocation.authority" text="Authority" /></th>
									<th><spring:message code="label.mission.member.type"
											text="Relation" /></th>
								</tr>
							</thead>
							<tbody ng-repeat="dm in m.details">
								<tr ng-repeat="p in dm.persons">
									<td rowspan="{{dm.size}}" ng-if="$index==0">{{dm.order}}</td>
									<td rowspan="{{dm.size}}" ng-if="$index==0"><a
										id="workingId"
										ng-href="<%=contextPath%>/expenditure-tracking/manageMissions/?partyId={{dm.unitId}}"
										class="" title=""> {{dm.unitName }}</a></td>
									<td><a
										ng-href="<%=contextPath%>/expenditure-tracking/manageMissions/?partyId={{p.personId}}"
										class="" title=""> {{ p.personName}}</a></td>
									<td>{{ p.type}}</td>

								</tr>
							</tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<%
	    }
	%>
</div>

<script type="text/javascript">
	var contextPath = '<%=contextPath%>';
	var user = '<%=user.getExternalId()%>';

	function loadMovementsController($scope, $http) {

		//Show/Hide child table from movement 'mov' and fetches the data for this table only if the table is being shown
		$scope.toggleDetailedMovs = function(mov) {
			mov.visibility = !mov.visibility;

			if (mov.visibility) {
				$http(
						{
							url : contextPath
									+ "/expenditure-tracking/manageMissions/ "
									+ user + "/authorizationChain/json",
							method : "GET",
							params : {
								param : mov.id
							}
						}).success(function(response) {
					mov.details = response;

				});
			}
			


		}
			
		$scope.isUndefinedOrNull = function(n) {
			
				if(!n.endDate){
				$("#accountId").val(escape(n.id));
			    	return true;
				}else{
					return false;
				}
		}

		//Fetches data for the main table
		$http.get(
				contextPath + "/expenditure-tracking/manageMissions/" + user
						+ "/authorizationChain/json").success(
				function(response) {
					$scope.movs = response;

					for (i = 0; i < $scope.movs.length; i++) {
						$scope.movs[i].visibility = false;
					}
				});

	}
</script>