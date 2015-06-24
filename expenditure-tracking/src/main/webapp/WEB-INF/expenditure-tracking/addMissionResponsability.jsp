<%@page import="java.util.Collection"%>
<%@page import="module.organization.domain.AccountabilityType"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@page import="module.organization.domain.Accountability"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="module.organization.domain.Unit"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page import="org.joda.time.LocalDate"%>
<%@page import="com.google.common.base.Strings"%>
<%@page import="java.lang.Boolean"%>



<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<style>
.ui-autocomplete-loading{background: url(/dot/images/autocomplete/spinner.gif) no-repeat right center}
</style>

<%
     final User user = (User) request.getAttribute("user");  
     final Unit unit = (Unit) request.getAttribute("unit");  
     final Collection<AccountabilityType> accountabilityTypes = (Collection<AccountabilityType>) request.getAttribute("accountabilityTypes");   
     final String contextPath = request.getContextPath();
     final String  iniDate = (String) request.getAttribute("iniDate");  
     final Boolean  me = (Boolean) request.getAttribute("invalidData");  
 %>
<script src='<%= contextPath + "/bennu-portal/js/angular.min.js" %>'></script>
<script src='<%= contextPath + "/bennu-scheduler-ui/js/libs/moment/moment.min.js" %>'></script>
<script src='<%= contextPath + "/webjars/highcharts/4.0.4/highcharts.js" %>'></script>
<script src='<%= contextPath + "/webjars/highcharts/4.0.4/highcharts-more.js" %>'></script>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>




<div class="page-header">
	<h2><%if(unit!=null){%>
		<span><%=unit.getPresentationName()%></span>
		<%}else if(user!=null){ %>
		<span><%=user.getProfile().getFullName() + " " + user.getUsername()%></span>
		<%}else{%>
		<span><spring:message code="title.mission.responsible.manage.missions" text="title.mission.responsible.manage.missions"></spring:message></span>
		<%}%>
	</h2>
</div>
<br>
<div class="form-group">
	<h3>
<%if(unit!=null){%>
		<spring:message code="activity.module.mission.person.mission.addPeople"
			text="activity.module.mission.person.mission.addPeople" />
<%}else if(user!=null){ %>
			<spring:message code="activity.module.mission.person.mission.addUnit"
			text="activity.module.mission.person.mission.addUnit" />
	</h3>
	<%}%>
</div>
<% if(me){%>
<div class="alert alert-danger ng-binding ng-hide" ng-show="error">
<spring:message code="message.mission.add.error" text="Invalid data"></spring:message>
</div>
<%}%>

<spring:url var="addAuthorizationURL"
	value="/expenditure-tracking/manageMissions/addMissionResponsability/" />


	<form id="addForm" class="form-horizontal" role="form"
		action="${addAuthorizationURL}" method="GET">
		
		

			<div class="form-group">
				<label class="control-label col-sm-2" for="unit"> <spring:message
						code="label.unit" text="Unit" />
				</label>
				<div class="col-sm-10">
					<%if(unit!=null){ 
					%>
					<input type="hidden" name="unitId" id="unitId" value="<%=unit.getExternalId()%>" /> 
					<input type="hidden" name="id" id="id" value="<%=unit.getExternalId()%>" /> 
					<input  type="text" class="form-control" value="<%=unit.getPresentationName()%>" disabled="disabled" />

					<%}else{ %>
					
					<input type="text" class="form-control" id="unit" value="" required="required"/>
					<input type="hidden" name="unitId" id="unitId" value="" /> 
				
					
						<%} %>
				</div>
			</div>
			
			<div class="form-group">

				<label class="control-label col-sm-2" for="user"> <spring:message code="label.person" text="Person" />
				</label>
				<div class="col-sm-10">
					<% if(user!=null){%>
					<input type="hidden" name="userId" id="userId" value="<%=user.getExternalId()%>" />
					<input type="hidden" name="id" id="id" value="<%=user.getExternalId()%>" /> 
					<input type="text" class="form-control" id="user" value="<%=user.getProfile().getFullName()%>" disabled="disabled" />

					<%}else{ %>	
					
					<input class="form-control" id="user" value="" required="required"/>
					<input type="hidden" name="userId" id="userId" value="" /> 
				
					<% } %>
				</div>
			</div>

			<div class="form-group">
				<label class="control-label col-sm-2" for="relationType"> <spring:message
						code="label.mission.Relationship.type" text="label.mission.Relationship.type" />
				</label>
				<div class="col-sm-10">
					<select id="authorityType" class="form-control" name="authorityType"
						required="required" onselect="" value="" >
						<option value="" ><spring:message
								code="label.mission.select.authority.type"
								text="label.mission.select.authority.type" /></option>
						<c:forEach var="acType" items="<%=accountabilityTypes%>">
							<option value="${acType.getExternalId()}" >${acType.getName().getContent()}
							</option>
						</c:forEach>
					</select>

				</div>
		
			</div>

			<div class="form-group">
				<label class="control-label col-sm-2" for="beginDate"> <spring:message
						code="label.mission.beginDate" text="label.mission.beginDate" />
				</label>
				<div class="col-sm-10">
					<input name="beginDate" type="text" class="form-control"
						id="beginDate" required="required" value="<%=iniDate%>"/>
				</div>
			</div>
	
		<div class="form-group">
		<div class="col-sm-10 col-sm-offset-2">
			<button id="submitRequest" class="btn btn-default">
				<spring:message code="label.mission.add.responsible"
					text="Add Responsability" />
			</button>
			</div>
		</div>
	</form>


<script type="text/javascript" >
 
 var pageContext ='<%= contextPath%>';
 $(function() {
	 $("#beginDate").datepicker();
	 
	 if(<%= unit!=null %>){
     	$("#user").autocomplete({
     		 focus: function(event, ui) {
     		
     			return false;
     			},
     		minLength: 2,		
     		contentType: "application/json; charset=UTF-8",
     		search  : function(){$(this).addClass('ui-autocomplete-loading');},
    		open    : function(){$(this).removeClass('ui-autocomplete-loading');},
     		source : function(request,response){
     				
     				$.post(pageContext + "/expenditure-tracking/manageMissions/user/json",request,function(result){
     				
     					response($.map(result,function(item){
     						
     						return{
     							label: item.name,
     							value: item.id
     							
     						}
     					}));
     		
     				});
     		},
     		
     		select: function( event, ui ) {
     			 $( "#user" ).val( ui.item.label);
     			 $( "#userId" ).val( ui.item.value );
     			 return false;
     		}
     });
     }else  if (<%=user!=null%>){
            	$("#unit").autocomplete({
            		focus: function(event, ui) {
             		
             			return false;
             			},
            		minLength: 2,		
            		contentType: "application/json; charset=UTF-8",
            		search  : function(){$(this).addClass('ui-autocomplete-loading');},
            		open    : function(){$(this).removeClass('ui-autocomplete-loading');},
            		source : function(request,response){
            				
            				$.post(pageContext + "/expenditure-tracking/manageMissions/unit/json", request,function(result){
            						
            					response($.map(result,function(item){
            						
            						return{
            							label: item.name,
            							value: item.id
            							
            						}
            					}));
            		
            				});
            		},
            		
            		select: function( event, ui ) {
            			 $( "#unit" ).val( ui.item.label );
            			 $( "#unitId" ).val( ui.item.value );
            			 return false;
            		}
            });
         
    }
          }); 
 </script>
