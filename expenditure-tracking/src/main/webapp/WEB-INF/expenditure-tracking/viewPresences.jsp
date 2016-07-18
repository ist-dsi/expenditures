<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://fenix-ashes.ist.utl.pt/chart" prefix="chart"%>

<%@page import="java.util.List"%>
<%@page import="module.organization.domain.Accountability"%>
<%@page import="module.organization.domain.AccountabilityType"%>
<%@page import="module.mission.domain.util.FindUnitMemberPresence"%>
<%@page import="java.util.Set"%>

<%@page import="java.util.Collection"%>
<%@page import="module.organization.domain.Person"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.List"%>
<%@page import="module.organization.domain.Unit"%>


<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    final String contextPath = request.getContextPath();
    final Unit unit = (Unit) request.getAttribute("selectedUnit");
    final FindUnitMemberPresence searchUnitMember =
            (FindUnitMemberPresence) request.getAttribute("searchUnitMemberPresence");
    final Boolean me = (Boolean) request.getAttribute("notAutorize");
    final Set<Person> peoples = (Set<Person>) request.getAttribute("people");
   
%>
<div class="page-header">
	<h1>
		<spring:message code="title.mission.responsible.manage.missions"
			text="Responsible management by missions" />
	</h1>
</div>

<div>
	<h3>
		<span><%=unit.getPresentationName()%></span>
	</h3>
</div>

<div class="infobox">

	<spring:url var="searchUrl"
		value="/expenditure-tracking/manageMissions/Presences/" />

	<div>
		<form:form id="searchForm" class="form-group" role="form"
			action="${searchUrl}" method="POST"
			modelAttribute="searchUnitMemberPresence">
			${csrf.field()}
			<input type="hidden" name="unitId" value="${unitId}" />

			<div class="form-group">
				<div>
					<form:label path="day">
						<spring:message code="label.date" text="Date" />
					</form:label>
					<form:input path="day" type="date" value="${day}" />
				</div>
				<div>
					<form:label path="includeSubUnits">
						<spring:message code="label.includeSubUnits"
							text="Include SubUnits?" />
					</form:label>
					<form:checkbox path="includeSubUnits" value="${includeSubUnits}" />

				</div>
				<div>
					<form:label path="onMission">
						<spring:message code="label.onMission" text="On Mission?" />
					</form:label>
					<form:checkbox path="onMission" value="${onMission}" />

				</div>
				<div>
					<label for="accountabilityType"> <spring:message
							code="label.mission.participants.accountabilityTypes" text="" />
					</label>
					<div id="accountabilityType">
						<form:checkboxes path="accountabilityTypes"
							items="${accountabilityTypesList}" itemValue="externalId"
							itemLabel="name" />
						<br>
					</div>

				</div>
			</div>

			<form:button>Submeter</form:button>
		</form:form>
	</div>
</div>
<br>

<div>
	<%
	    if (peoples != null && !peoples.isEmpty()) {
	%>


	<table class="tstyle3" style="text-align: left;">
		<tr>
			<th style="color: graytext;"></th>
			<th><spring:message code="label.person" text="Person" /></th>
			<th></th>
		</tr>
		<%for(Person person:peoples){ 
			    final User user =
                        User.findByUsername(person.getUser().getUsername());
			%>
		<tr class="ng-scope">
			<td><code class="ng-binding">
					<%
						    if (user.getProfile() != null) {
						%>
					<img class="img-circle" width="40" height="40" alt=""
						src='<%=user.getProfile().getAvatarUrl() + "?s=40"%>' />
					<%
						    }
						%>
				</code></td>
			<td><code class="ng-binding">
					<a
						href="<%=contextPath%>/expenditure-tracking/manageMissions/?partyId=<%=user.getExternalId()%>"
						class="" title=""> <%=user.getDisplayName()%>
					</a>
				</code></td>
			<td><code class="ng-binding">
					<a
						href="<%=contextPath%>/expenditure-tracking/manageMissions/searchMission/<%=person.getExternalId() %>"
						class="" title=""> <spring:message
							code="label.search.missions" text="Search Missions" /></a>
				</code></td>
		</tr>
<% }%>
	</table>


	<%
	    } else if(peoples!=null && peoples.isEmpty()) {
	%>
	<div class="alert alert-danger ng-binding ng-hide" ng-show="error">
		<spring:message code="label.day.on.mission.none" text="" />
	</div>
	<%
	    }
	%>
</div>