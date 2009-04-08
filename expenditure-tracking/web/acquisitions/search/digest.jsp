<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<h2> <bean:message key="link.sideBar.acquisitionProcess.digest" bundle="EXPENDITURE_RESOURCES"/> </h2>

<%@page
	import="pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter"%>
	
<bean:define id="person"
						name="<%= SetUserViewFilter.USER_SESSION_ATTRIBUTE%>"
						property="user.expenditurePerson" toScope="request"/>
		
<script type="text/javascript">

$(function() {

	$(".column").sortable({
			connectWith: '.column',
			stop: function(event, ui) {
				var portletsContainer1 = $("#column1").find(".portlet");
				var portletsContainer2 = $("#column2").find(".portlet");
				var portletsContainer3 = $("#column3").find(".portlet");


				var portlets1 = "";
				var portlets2 = "";
				var portlets3 = "";
				jQuery.each(portletsContainer1, function() {
					portlets1 = portlets1 + $(this).attr('id') + ',';
				});
				jQuery.each(portletsContainer2, function() {
					portlets2 = portlets2 + $(this).attr('id') + ',';
				});
				jQuery.each(portletsContainer3, function() {
					portlets3 = portlets3 + $(this).attr('id') + ',';
				});
								
				$.get("/workflow/dashBoard.do?method=order", { column1: portlets1, column2: portlets2, column3: portlets3 });
			}
		});


		$(".portlet-header .ui-icon").click(function() {
			$(this).toggleClass("ui-icon-minusthick");
			$(this).parents(".portlet:first").find(".portlet-content").toggle();
		});

		$(".column").disableSelection();

		
	});
	</script>
	
<div class="dashboard">

<bean:define id="dashBoard" name="person" property="dashBoard"/>

<div id="column1" class="column">
	<logic:iterate id="widget" name="dashBoard" property="column1">
		<jsp:include page="<%= "../widgets/" + widget +".jsp" %>" flush="false"/>
	</logic:iterate>
</div>

<div id="column2" class="column">
		<logic:iterate id="widget" name="dashBoard" property="column2">
		<jsp:include page="<%= "../widgets/" + widget +".jsp" %>" flush="false"/>
	</logic:iterate>
</div>

<div id="column3" class="column">
	<logic:iterate id="widget" name="dashBoard" property="column3">
		<jsp:include page="<%= "../widgets/" + widget +".jsp" %>" flush="false"/>
	</logic:iterate>
</div>

</div><!-- End demo -->
