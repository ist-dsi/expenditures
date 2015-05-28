<%@page import="com.google.gson.JsonArray"%>
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
<%@page import="module.organization.domain.Unit"%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
 

<%
    final String contextPath = request.getContextPath();
%>
<script src='<%= contextPath + "/bennu-portal/js/angular.min.js" %>'></script>
<script src='<%= contextPath + "/bennu-scheduler-ui/js/libs/moment/moment.min.js" %>'></script>
<script src='<%= contextPath + "/webjars/highcharts/4.0.4/highcharts.js" %>'></script>
<script src='<%= contextPath + "/webjars/highcharts/4.0.4/highcharts-more.js" %>'></script>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>


<div class="page-header">
	<h1>
		<spring:message code="title.mission.responsible.manage.missions"
			text="Responsible management by missions" />
	</h1>
</div>


<form id="searchForm" class="form-horizontal" role="form"
		action="${searchUrl}" method="GET">
			
	
	<div class="form-group">
			<label class="control-label col-sm-2" for="searchId"> <spring:message
					code="label.search" text="Search" />
			</label> 
		
			<div class="col-sm-10">
			
			<input  id="searchString" class="form-control" placeholder='<spring:message code="label.mission.search" text="Unidade/Pessoa"/>' autofocus />
			
			<input type="hidden" id="partyId" name="partyId" value="">
			</div>
		
	</div>
	
	<div class="form-group">
		<div class="col-sm-10 col-sm-offset-2">
			<button id="searchRequest" class="btn btn-default">
				<spring:message code="label.submit" text="Procurar" />
			</button>
		</div>
	</div>
	
	</form>


<script type="text/javascript" >
var pageContext= '<%=contextPath%>';
$(function(){
	

	$('#searchString').autocomplete({
		focus: function(event, ui) {
 		//	$( "#searchString" ).val( ui.item.label);
 			return false;
 			},
		
		minLength: 2,		
		contentType: "application/json; charset=UTF-8",
		source : function(request,response){
				
				$.post(pageContext + "/expenditure-tracking/manageMissions/populate/json", request,function(result){
									
					response($.map(result,function(item){
						
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



