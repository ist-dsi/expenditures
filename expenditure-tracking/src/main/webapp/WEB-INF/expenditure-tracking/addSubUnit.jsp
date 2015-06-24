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
    
     final Unit unit = (Unit) request.getAttribute("unit");  
     
     final String contextPath = request.getContextPath();
     final String  iniDate = (String) request.getAttribute("iniDate");  
     final Boolean  me = (Boolean) request.getAttribute("invalidData");  
     final Collection<AccountabilityType> types = (Collection<AccountabilityType>) request.getAttribute("types");
     final String  messageError = (String) request.getAttribute("addError");  
 %>
<script src='<%= contextPath + "/bennu-portal/js/angular.min.js" %>'></script>
<script src='<%= contextPath + "/bennu-scheduler-ui/js/libs/moment/moment.min.js" %>'></script>
<script src='<%= contextPath + "/webjars/highcharts/4.0.4/highcharts.js" %>'></script>
<script src='<%= contextPath + "/webjars/highcharts/4.0.4/highcharts-more.js" %>'></script>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>




<div class="page-header">
	<h2>
		<span><%=unit.getPresentationName()%></span>
	</h2>
</div>
<br>



<div class="form-group">
	<h3>
		<spring:message code="activity.module.mission.person.mission.addSubunit"
			text="activity.module.mission.person.mission.addSubunit" />
	</h3>
</div>
<%if(messageError!=null){ %>
<span class="error0"><%= messageError%></span>
<%} %>
<spring:url var="addSubunitURL"
	value="/expenditure-tracking/manageMissions/addSubUnit/" />

	<form id="addForm" class="form-horizontal" role="form"
		action="${addSubunitURL}" method="GET">
		<%if(unit!=null){%>
			<input type="hidden" name="id" id="id" value="<%=unit.getExternalId()%>" /> 
		<%}%>
							
			<div class="form-group">
				<label class="control-label col-sm-2" for="unit"> <spring:message
						code="label.unit" text="Unit" />
				</label>
				<div class="col-sm-10">				
					<input type="text" class="form-control" id="unit" value="" required="required"/>
					<input type="hidden" name="unitId" id="unitId" value="" /> 							
				</div>
			</div>
			
			<div class="form-group">
				<label class="control-label col-sm-2" for="relationType"> <spring:message
						code="label.mission.Relationship.type" text="label.mission.Relationship.type" />
				</label>
				<div class="col-sm-10">
					<select id="type" class="form-control" name="type"
						required="required" onselect="" value="" >
						<%if(types.size()>1) {%>
						<option value=""><spring:message
								code="label.mission.select.authority.type"
								text="label.mission.select.authority.type" /></option>
						<c:forEach var="acType" items="<%=types%>">
							<option value="${acType.getExternalId()}" >${acType.getName().getContent()}
							</option>
						</c:forEach>
						<%}else{ %>
						<option value="<%=types.iterator().next().getExternalId()%>"><%=types.iterator().next().getName().getContent()%></option>
						<%} %>
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
			<spring:message code="label.mission.add.responsible" text="Add SubUnit" />
		</button>
		</div>
		</div>
	</form>


<script type="text/javascript" > 
 var pageContext ='<%= contextPath%>';

 $(function() {
	
	 $("#beginDate").datepicker();
   	 $("#unit").autocomplete({
   		focus: function(event, ui) {
    		
    			return false;
    			},
   		minLength: 2,		
   		contentType: "application/json; charset=UTF-8",
   		search  : function(){$(this).addClass('ui-autocomplete-loading');},
		open    : function(){$(this).removeClass('ui-autocomplete-loading');},
   		source : function(request,response){	
   				$.post(pageContext + "/expenditure-tracking/manageMissions/allUnit/json", request,function(result){   						
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


 }); 
 </script>
