<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@page
	import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page import="module.organization.domain.Unit"%>
<%@page import="java.util.List"%>
<%@page import="module.organization.domain.Accountability"%>
<%@page import="java.util.Set"%>
<%@page import="module.organization.domain.AccountabilityType"%>
<%@page import="java.util.Collection"%>
<%@page import="module.organization.domain.Person"%>
<%@page import="org.joda.time.LocalDate"%>


<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/chart" prefix="chart"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<script
	src='<%=request.getContextPath() + "/bennu-portal/js/angular.min.js"%>'></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/moment.min.js"%>">
	
</script>
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
    final Unit unit = (Unit) request.getAttribute("selectedUnit");
    

    final Collection<Accountability> workerAccountabilities =
            (Collection<Accountability>) request.getAttribute("workerAccountabilities");
    final Collection<Accountability> authorityAccountabilities =
            (Collection<Accountability>) request.getAttribute("authorityAccountabilities");
    final Boolean me = (Boolean)request.getAttribute("notAutorize");

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

<%
    if (ExpenditureTrackingSystem.isManager()) {
        if (unit.getMissionSystemFromUnitWithResumedAuthorizations()==null) {
%>

<p>
	<spring:message
		code="label.module.mission.unitWithResumedAuthorizations.not"
		text="This unit does not have the list of pending authorizations sumarized for the units responsibles." />
	<a
		href="<%=contextPath%>/expenditure-tracking/manageMissions/addUnitWithResumedAuthorizations/<%=unit.getExternalId()%>"
		class="" title=""> <spring:message
			code="label.module.mission.unitWithResumedAuthorizations.add.summary"
			text="Add Summary" />
	</a>
</p>
<%
    } else {
%>

<p style="color: green;">
	<spring:message
		code="label.module.mission.unitWithResumedAuthorizations"
		text="This unit has the list of pending authorizations sumarized for the units responsibles," />
	<a
		href="<%=contextPath%>/expenditure-tracking/manageMissions/removeUnitWithResumedAuthorizations/
		<%=unit.getExternalId()%>"
		class="" title=""> <spring:message
			code="label.module.mission.unitWithResumedAuthorizations.remove.summary"
			text="Remove Summary" />
	</a>
</p>

<%
    }
    }
%>

<br>
<html:messages id="message" message="true">
	<span class="error0"> <bean:write name="message" /> </span>
</html:messages>
<br/>
<div
	style="background-color: rgb(245, 245, 245); border-bottom-color: rgb(221, 221, 221); border-bottom-style: solid; border-bottom-width: 1px; border-collapse: collapse; border-left-color: rgb(68, 68, 68); border-left-style: none; border-left-width: 0px; border-right-color: rgb(68, 68, 68); border-right-style: none; border-right-width: 0px; border-top-color: rgb(221, 221, 221); border-top-style: solid; border-top-width: 1px; color: rgb(68, 68, 68); padding: 4px 0px 4px 4px; margin-top: 15px; font-weight: bold; width: 719px;">
	<label for="toggleInactiveChbox"><spring:message code="text.mission.view.inactive" text="Mostrar pessoas inactivas"/></label> <input
		style="vertical-align: bottom;" type="checkbox" name="toggleInactive"
		id="toggleInactiveChbox" onclick="inactiveEntities(toggleInactive)">
</div>
<br>
<div>
	<a
		href="<%=contextPath%>/expenditure-tracking/manageMissions/viewPresences?unitId=<%=unit.getExternalId()%>"
		class="" title=""><spring:message
			code="label.module.mission.view.member.presence"
			text="View Member presence" /></a><% if(me){%><span class="error0"> <spring:message code="label.not.authorized" text="true"/></span><%} %>
</div>
<%
	if (ExpenditureTrackingSystem.getInstance().getAcquisitionsUnitManagerGroup().isMember(Authenticate.getUser())) {
%>
<div>
	<a
		href="<%=contextPath%>/expenditure-tracking/manageMissions/prepareDelegateForAuthorization/<%=unit.getExternalId()%>"
		class="" title=""><spring:message
			code="activity.module.mission.person.mission.addResponsability"
			text="activity.module.mission.person.mission.addResponsability" /></a>
</div>
<%} %>
<br>
<div>
	<%
	    if (authorityAccountabilities == null || (authorityAccountabilities != null && authorityAccountabilities.isEmpty())) {
	%>

	<h3>
		<spring:message
			code="label.module.mission.person.mission.responsibilities" text="label.module.mission.person.mission.responsibilities" />
	</h3>

	<div class="alert alert-danger ng-binding ng-hide" ng-show="error">
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
	<table class="filterableInactiveAccountabilityTable">
		<thead>
			<tr>
				<th></th>
				<th><spring:message code="label.people" text="People" /></th>
				<th><spring:message code="label.mission.authority.type"
						text="label.mission.authority.type" /></th>
				<th><spring:message code="label.mission.beginDate"
						text="Data Inicio" /></th>
				<th><spring:message code="label.mission.endDate"
						text="Data Fim" /></th>
				<th ><spring:message code="label.mission.delegation"
						text="Delegações" /> </th>
				
			</tr>
		<thead>
		<tbody>
			<%
			    for (final Accountability authorityAccountability : authorityAccountabilities) {
			            if (authorityAccountability.getChild().isPerson()) {
			                final User user =
			                        User.findByUsername(((Person) authorityAccountability.getChild()).getUser().getUsername());
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
							class="" title=""> <%=user.getPresentationName()%>
						</a>
					</code></td>
				<td valign="middle"><code class="ng-binding">
						<span><%=authorityAccountability.getAccountabilityType().getName().getContent()%></span>
					</code>
						<%
						    if (authorityAccountability.getFunctionDelegationDelegator() != null) {
						%>
						
						<br>
						<spring:message code="label.delegation.by" text="Delegation By" />: 	
						<a
							href="<%=contextPath%>/expenditure-tracking/manageMissions/showDelegationsForAuthorization/<%=authorityAccountability.getFunctionDelegationDelegator().getAccountabilityDelegator()
                                    .getExternalId()%>"
							class="" title=""> <%=authorityAccountability.getFunctionDelegationDelegator().getAccountabilityDelegator()
                                    .getChild().getPresentationName()%>
						</a> 
						
						<%
						    }
						%>
					</td>

				<td><code class="ng-binding">
						<%=authorityAccountability.getBeginDate()%>
					</code></td>
				<td class="endDateColumn"><span><%=authorityAccountability.getEndDate() == null ? " " : authorityAccountability.getEndDate()%></span>
				</td>
				
				<td><code class="ng-binding">
						<a
							href="<%=contextPath%>/expenditure-tracking/manageMissions/showDelegationsForAuthorization/<%=authorityAccountability.getExternalId()%>"
							class="" title=""> <spring:message code="label.delegations"
								text="Delegations"
								arguments="<%=authorityAccountability.getFunctionDelegationDelegated().size()%>" />
						</a>
					</code></td>
					
				<%
				    }
				%>

			</tr>
			<%
			    }
			%>
		</tbody>

	</table>
	<%
	    }
	%>
</div>

<br />

<div>
	<%
	    if (workerAccountabilities == null || (workerAccountabilities != null && workerAccountabilities.isEmpty())) {
	%>
	<h3>
		<spring:message code="label.module.mission.unit.members"
			text="label.module.mission.unit.members" />
	</h3>

	<div class="alert alert-danger ng-binding ng-hide" ng-show="error">
		<spring:message
			code="label.module.mission.person.working.place.information.none"
			text="Information for unit members not exist" />
	</div>


	<%
	    } else {
	%>
	<div>
		<h3>
			<spring:message code="label.module.mission.unit.members"
				text="label.module.mission.unit.members" />
		</h3>
	</div>
	<table class="tstyle3 filterableInactiveAccountabilityTable">
		<thead>
			<tr>
				<th></th>
				<th><spring:message code="label.people" text="People" /></th>
				<th><spring:message code="label.mission.member.type"
						text="Member Type" /></th>
				<th><spring:message code="label.mission.beginDate"
						text="Data Inicio" /></th>
				<th><spring:message code="label.mission.endDate"
						text="Data Fim" /></th>

			</tr>
		</thead>
		<tbody>
			<%
			    for (final Accountability authorityAccountability : workerAccountabilities) {
			            if (authorityAccountability.getChild().isPerson()) {
			                final User user =
			                        User.findByUsername(((Person) authorityAccountability.getChild()).getUser().getUsername());
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
							class="" title=""> <%=user.getPresentationName()%>
						</a>
					</code></td>
				<td><code class="ng-binding">
						<%=authorityAccountability.getAccountabilityType().getName().getContent()%>
					</code></td>
				<td><code class="ng-binding">
						<%=authorityAccountability.getBeginDate()%>
					</code></td>
				<td class="endDateColumn"><span><%=authorityAccountability.getEndDate() == null ? "" : authorityAccountability.getEndDate()%></span>
				</td>
			</tr>
			<%
			    }
			        }
			%>
		</tbody>
	</table>
	<%
	    }
	%>
</div>