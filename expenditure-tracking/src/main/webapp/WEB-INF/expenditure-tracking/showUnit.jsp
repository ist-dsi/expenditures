<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="java.text.Collator"%>
<%@page import="java.util.Comparator"%>
<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="module.organization.domain.Party"%>
<%@page import="module.mission.domain.MissionSystem"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page import="java.util.List"%>
<%@page import="java.util.SortedSet"%>
<%@page import="java.util.TreeSet"%>
<%@page import="module.organization.domain.Accountability"%>
<%@page import="java.util.Set"%>
<%@page import="module.organization.domain.AccountabilityType"%>
<%@page import="java.util.Collection"%>
<%@page import="module.organization.domain.Person"%>
<%@page import="org.joda.time.LocalDate"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/chart" prefix="chart"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<script src='<%=request.getContextPath() + "/bennu-portal/js/angular.min.js"%>'></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/moment.min.js"%>"></script>
<script type="text/javascript">
    var now = <%= new LocalDate()%>;
	var currentDate = moment(now, 'YYYY-MM-DD');

	function inactiveEntities(func) {

		$('.filterableInactiveAccountabilityTable')
				.each(
						function(index) {
							$(this)
									.children()
									.children()
									.each(
											function(tableRowIndex) {
												if (tableRowIndex > 0) {
													var endDateStr = $(
															$(this)
																	.find(
																			'.endDateColumn'))
															.children().html()
															.trim();
													if (endDateStr.length > 0
															&& !moment(
																	endDateStr,
																	'YYYY-MM-DD')
																	.isAfter(
																			currentDate)) {

														func($(this));
													}
												}
											});
						});
	}

	function toggleInactive(elem) {

		if ($('#toggleInactiveChbox').prop('checked')) {
			elem.show();
		} else {
			elem.hide();
		}
	}

	function highlightInactive(elem) {
		elem.find('.endDateColumn').children().css({
			'background-color' : '#fcfbad',
			'padding' : '3px'
		});
	}
</script>
<script type="text/javascript">
	$(function() {
		inactiveEntities(toggleInactive);
		inactiveEntities(highlightInactive);

	});
</script>

<%
    final String contextPath = request.getContextPath();
    final module.organization.domain.Unit unit = (module.organization.domain.Unit) request.getAttribute("selectedUnit");
    final Collection<Accountability> workerAccountabilities = (Collection<Accountability>) request.getAttribute("workerAccountabilities");
    final Collection<Accountability> authorityAccountabilities = (Collection<Accountability>) request.getAttribute("authorityAccountabilities");
    final Boolean me = (Boolean)request.getAttribute("notAutorize");
	final Collection<AccountabilityType> types = MissionSystem.getInstance().getAccountabilityTypesForUnits();
	final Boolean notActive = (Boolean)request.getAttribute("notActive");
%>

<jsp:include page="manageMissions.jsp" />

<div class="page-header">
	<h1>
		<span><%=unit.getPresentationName()%></span>
	</h1>
</div>

<p>
	<spring:message code="label.unit.parent" text="Parent unit" />:
	<%
		for (final Accountability pa : unit.getParentAccountabilitiesSet()) {
		    if (types.contains(pa.getAccountabilityType()) && pa.isActiveNow() && pa.isValid()) {
		        final Party parent = pa.getParent();
	%>
				<a href="<%=contextPath + "/expenditure-tracking/manageMissions/?partyId=" + parent.getExternalId() %>" class="" title="">
					<%= parent.getPresentationName() %>
				</a>
	<%
		    }
		}
	%>
</p>

<%
    if (ExpenditureTrackingSystem.isManager()) {
        if (unit.getMissionSystemFromUnitWithResumedAuthorizations()==null) {
%>
			<p>
				<spring:message code="label.module.mission.unitWithResumedAuthorizations.not"
						text="This unit does not have the list of pending authorizations sumarized for the units responsibles." />
				<a href="<%=contextPath%>/expenditure-tracking/manageMissions/addUnitWithResumedAuthorizations/<%=unit.getExternalId()%>"
						class="" title="">
					<spring:message code="label.module.mission.unitWithResumedAuthorizations.add.summary"
							text="Add Summary" />
				</a>
			</p>
<%
    	} else {
%>
			<p style="color: green;">
				<spring:message code="label.module.mission.unitWithResumedAuthorizations"
						text="This unit has the list of pending authorizations sumarized for the units responsibles," />
				<a href="<%=contextPath%>/expenditure-tracking/manageMissions/removeUnitWithResumedAuthorizations/<%=unit.getExternalId()%>"
						class="" title="">
					<spring:message code="label.module.mission.unitWithResumedAuthorizations.remove.summary"
							text="Remove Summary" />
				</a>
			</p>
<%
		}
    }
%>

<div>
	<html:messages id="message" message="true">
		<span class="error0"> <bean:write name="message" /> </span>
	</html:messages>
</div>
<div>
	<span style="margin-right: 30px;">
		<a href="<%=contextPath%>/expenditure-tracking/manageMissions/viewPresences?unitId=<%=unit.getExternalId()%>"
				class="" title="">
			<spring:message code="label.module.mission.view.member.presence" text="View Member presence"/>
		</a>
		<% if (me) { %>
			<span class="error0"><spring:message code="label.not.authorized" text="true"/></span>
		<% } %>
	</span>
		<% if (ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup().isMember(Authenticate.getUser())) { %>
	<span style="margin-right: 30px;">
				<a href="<%=contextPath%>/expenditure-tracking/manageMissions/prepareRelationshipType/<%=unit.getExternalId()%>"
						class="" title="">
					<spring:message code="activity.module.mission.person.mission.addPeople"
							text="activity.module.mission.person.mission.addTypeOfRelation" /></a>
	</span>
	<span style="margin-right: 30px;">
				<a href="<%=contextPath%>/expenditure-tracking/manageMissions/prepareAddSubUnit/<%=unit.getExternalId()%>"
						class="" title="">
					<spring:message code="activity.module.mission.person.mission.addSubunit"
							text="Add SubUnit" /></a>
							<% if(notActive){%>
								<span class="error0">
								<spring:message code="message.mission.add.unit.notActiv" text="Unidade não ativa"></spring:message>
								</span>
							<%}%>
	</span>
		<% } %>
	<span>
		<label for="toggleInactiveChbox">
			<spring:message code="text.mission.view.inactive" text="Mostrar tipos de Relações inactivas"/>
		</label>
		<input style="vertical-align: text-bottom;" type="checkbox" name="toggleInactive" id="toggleInactiveChbox" onclick="inactiveEntities(toggleInactive)">
	</span>
</div>
<br>
<div>
	<%
	    if (authorityAccountabilities == null || (authorityAccountabilities != null && authorityAccountabilities.isEmpty())) {
	%>
			<h3>
				<spring:message code="label.module.mission.person.mission.responsibilities" text="label.module.mission.person.mission.responsibilities" />
			</h3>
			<div class="alert alert-danger ng-binding ng-hide" ng-show="error">
				<spring:message code="label.module.mission.person.mission.responsibilities.none" text="aa" />
			</div>
	<%
	    } else {
	%>
			<div>
				<h3>
					<spring:message code="label.module.mission.person.mission.responsibilities" text="label.module.mission.person.mission.responsibilities" />
				</h3>
			</div>
			<table class="table filterableInactiveAccountabilityTable">
				<thead>
					<tr>
						<th></th>
						<th><spring:message code="label.people" text="People" /></th>
						<th><spring:message code="label.mission.authority.type" text="label.mission.authority.type" /></th>
						<th><spring:message code="label.mission.beginDate" text="Data Inicio" /></th>
						<th><spring:message code="label.mission.endDate" text="Data Fim" /></th>
						<th><spring:message code="label.mission.delegation" text="Delegações" /> </th>
					</tr>
				</thead>
				<tbody>
				    <% for (final Accountability authorityAccountability : authorityAccountabilities) {
			            if (authorityAccountability.getChild().isPerson()) {
			                final User user = User.findByUsername(((Person) authorityAccountability.getChild()).getUser().getUsername());
					%>
							<tr class="ng-scope">
								<td>
									<% if (user.getProfile() != null) { %>
										<img class="img-circle" width="40" height="40" alt="" src='<%=user.getProfile().getAvatarUrl() + "?s=40"%>' />
									<% } %>
								</td>
								<td>
									<a href="<%=contextPath%>/expenditure-tracking/manageMissions/?partyId=<%=user.getExternalId()%>" class="" title="">
										<%=user.getPresentationName()%>
									</a>
								</td>
								<td valign="middle">
									<span><%=authorityAccountability.getAccountabilityType().getName().getContent()%></span>
									<% if (authorityAccountability.getFunctionDelegationDelegator() != null) { %>						
										<br>
										<spring:message code="label.delegation.by" text="Delegation By" />: 	
										<a href="<%=contextPath%>/expenditure-tracking/manageMissions/showDelegationsForAuthorization/<%=authorityAccountability.getFunctionDelegationDelegator().getAccountabilityDelegator().getExternalId()%>"
												class="" title="">
											<%=authorityAccountability.getFunctionDelegationDelegator().getAccountabilityDelegator().getChild().getPresentationName()%>
										</a> 
									<% } %>
								</td>
								<td><%=authorityAccountability.getBeginDate()%></td>
								<td class="endDateColumn">
									<span><%=authorityAccountability.getEndDate() == null ? " " : authorityAccountability.getEndDate()%></span>
								</td>
								<td>
									<a href="<%=contextPath%>/expenditure-tracking/manageMissions/showDelegationsForAuthorization/<%=authorityAccountability.getExternalId()%>"
											class="" title="">
										<spring:message code="label.delegations" text="Delegations" arguments="<%=authorityAccountability.getFunctionDelegationDelegated().size()%>" />
									</a>
								</td>
						    </tr>
					    <% } %>
				    <% } %>
				</tbody>
		</table>
	<% } %>
</div>

<div>
	<%
	    if (workerAccountabilities == null || (workerAccountabilities != null && workerAccountabilities.isEmpty())) {
	%>
			<h3>
				<spring:message code="label.module.mission.unit.members" text="label.module.mission.unit.members" />
			</h3>
			<div class="alert alert-danger ng-binding ng-hide" ng-show="error">
				<spring:message code="label.module.mission.person.working.place.information.none" text="Information for unit members not exist" />
			</div>
	<%
	    } else {
	%>
			<div>
				<h3>
					<spring:message code="label.module.mission.unit.members" text="label.module.mission.unit.members" />
				</h3>
			</div>
			<table class="table filterableInactiveAccountabilityTable">
				<thead>
					<tr>
						<th></th>
						<th><spring:message code="label.people" text="People" /></th>
						<th><spring:message code="label.mission.member.type" text="Member Type" /></th>
						<th><spring:message code="label.mission.beginDate" text="Data Inicio" /></th>
						<th><spring:message code="label.mission.endDate" text="Data Fim" /></th>
					</tr>
				</thead>
				<tbody>
					<% for (final Accountability authorityAccountability : workerAccountabilities) {
						if (authorityAccountability.getChild().isPerson()) {
							final User user = User.findByUsername(((Person) authorityAccountability.getChild()).getUser().getUsername());
					%>
							<tr class="ng-scope">
								<td>
									<% if (user.getProfile() != null) { %>
										<img class="img-circle" width="40" height="40" alt="" src='<%=user.getProfile().getAvatarUrl() + "?s=40"%>' />
									<% } %>
								</td>
								<td>
									<a href="<%=contextPath%>/expenditure-tracking/manageMissions/?partyId=<%=user.getExternalId()%>" class="" title="">
										<%=user.getPresentationName()%>
									</a>
								</td>
								<td>
									<%=authorityAccountability.getAccountabilityType().getName().getContent()%>
								</td>
								<td>
									<%=authorityAccountability.getBeginDate()%>
								</td>
								<td class="endDateColumn">
									<span><%=authorityAccountability.getEndDate() == null ? "" : authorityAccountability.getEndDate()%></span>
								</td>
							</tr>
					    <% } %>
                    <% } %>
				</tbody>
			</table>
	<% } %>
</div>

<div>
<div>
	<h3>
		<spring:message code="label.unit.children" text="Subunits" />
	</h3>
</div>
<spring:url var="removeURL" value="/expenditure-tracking/manageMissions/removeSubUnit/" />
<table class="table filterableInactiveAccountabilityTable">
	<thead>
		<tr>
			<th></th>
			<th><spring:message code="label.unit" text="Unit" /></th>
			<th><spring:message code="label.mission.member.type" text="Member Type" /></th>
			<th><spring:message code="label.mission.beginDate" text="Data Inicio" /></th>
			<th><spring:message code="label.mission.endDate" text="Data Fim" /></th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<%
		//final SortedSet<Accountability> result = new TreeSet<Accountability>(Accountability.COMPARATOR_BY_CHILD_PARTY_NAMES);
		final SortedSet<Accountability> result = new TreeSet<Accountability>(Unit.ACCOUNTABILITY_COMPARATOR_BY_NAME);
		result.addAll(unit.getChildAccountabilitiesSet());
			for (final Accountability a : result) {
			    if (types.contains(a.getAccountabilityType()) && a.isValid()) {
			        final Party child = a.getChild();
		%>
			<tr>
				<td></td>
				<td>
					<a href="<%=contextPath + "/expenditure-tracking/manageMissions/?partyId=" + child.getExternalId() %>" class="" title="">
						<%= child.getPresentationName() %>
					</a>			
				</td>
				<td>
					<%=a.getAccountabilityType().getName().getContent()%>
				</td>
				<td>
					<%=a.getBeginDate()%>
				</td>
				<td class="endDateColumn">
					<span><%=a.getEndDate() == null ? "" : a.getEndDate()%></span>
				</td>
				<td>
				<%
				   if (ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup().isMember(Authenticate.getUser()) && a.isActiveNow()) {
				%>
					<form id="searchForm" class="form-horizontal" role="form"
						action="${removeURL}" method="GET">
						<input id="partyId" name="partyId" type="hidden"
							   value="<%=unit.getExternalId()%>" />
					    <input id="accountId" name="accountId" type="hidden" value="<%=a.getExternalId() %>"/>
					    <input type="submit" class="btn" title="Close"
							   value='<spring:message code="label.close" text="Fechar"/>' />
					</form>
				<%
				    }
				%>
				</td>
			</tr>
		<%
			    }
			}
		%>
	</tbody>
</table>
</div>