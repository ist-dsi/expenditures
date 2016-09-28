
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.google.gson.JsonArray"%>
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
<%@page import="module.organization.domain.Unit"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

<%
    final String contextPath = request.getContextPath();
%>


<script src='<%= contextPath + "/bennu-portal/js/angular.min.js" %>'></script>
<script src='<%= contextPath + "/webjars/highcharts/4.0.4/highcharts.js" %>'></script>
<script src='<%= contextPath + "/webjars/highcharts/4.0.4/highcharts-more.js" %>'></script>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>


<style>
.ui-autocomplete-loading{background: url(<%= contextPath %>/images/autocomplete/spinner.gif) no-repeat right center}
</style>

<spring:url var="searchUrl"
	value="/expenditure-tracking/manageMissions/" />
<form id="searchForm" class="form-horizontal" role="form" action="${searchUrl}" method="GET">
${csrf.field()} 

		<div class="form-group" style="margin-top: 25px;">
			<label class="control-label col-sm-1" for="searchId">
				<spring:message code="label.search" text="Search" />
			</label> 
			<input id="searchString" class="form-control" placeholder='<spring:message code="label.mission.search" text="Unidade/Pessoa"/>' autofocus style="display: inline; width: 70%;"/>
			<input type="hidden" id="partyId" name="partyId" value="">
			<button id="searchRequest" class="btn btn-default">
				<spring:message code="label.submit" text="Procurar" />
			</button>
		</div>
</form>


<script type="text/javascript">
	var pageContext= '<%=contextPath%>';

	$(function() {
				
		$('#searchString').autocomplete({
			 
			focus: function(event, ui) {
 				return false;
 			},
 			
 			minLength: 2,	
			contentType: "text/plain; charset=UTF-8",
			search  : function(){$(this).addClass('ui-autocomplete-loading');},
			open    : function(){$(this).removeClass('ui-autocomplete-loading');},
			source : function(request,response){
				request.term=encodeURIComponent(request.term);
				$.get(pageContext + "/expenditure-tracking/manageMissions/populate/json",request,function(result) {
					response($.map(result,function(item) {
						return{
							label: item.name,
							value: item.id
						}
					}));
				});
			},
			
			select: function( event, ui ) {
				$( "#searchString" ).val( ui.item.label );
				$( "#partyId" ).val( ui.item.value );				
				return false;
			}
		});
		 
	});
	
</script>
