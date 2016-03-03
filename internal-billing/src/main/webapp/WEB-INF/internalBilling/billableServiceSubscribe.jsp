<%--
    Copyright Â© 2014 Instituto Superior Técnico

    This file is part of the Internal Billing Module.

    The Internal Billing Module is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    The Internal Billing Module is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with MGP Viewer.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="pt.ist.fenixframework.FenixFramework"%>
<%@page import="com.google.gson.JsonElement"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page import="com.google.gson.JsonObject"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% final String contextPath = request.getContextPath(); %>
<script src='<%= contextPath + "/bennu-portal/js/angular.min.js" %>'></script>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>
<script src='<%= contextPath + "/webjars/angular-ui-bootstrap/0.9.0/ui-bootstrap-tpls.min.js" %>'></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

<div class="page-header">
    <h2>
        <spring:message code="label.internalBilling.billableService.subscribe" text="Subscribe"/>
    </h2>
</div>

<% final JsonArray billableServices = (JsonArray) request.getAttribute("billableServices"); %>
<div class="page-body">
    <form class="form-horizontal" action="<%= contextPath + "/internalBilling/billableService/subscribe" %>" method="POST">
        <div class="form-group">
            <label class="control-label col-sm-2" for="type">
                <spring:message code="label.internalBilling.billableService" text="Service"/>
            </label>
            <div class="col-sm-10">
                <select name="billableService" class="form-control" id="billableService" required="required">
                    <option selected="selected" style="display:none;" disabled="disabled">Select</option>
                    <% for (final JsonElement e : billableServices) {
                        final JsonObject billableService = e.getAsJsonObject();
                    %>
                        <option value="<%= billableService.get("id").getAsString() %>">
                            <%= billableService.get("type").getAsString() %>
                            -
                            <%= billableService.get("title").getAsString() %>
                        </option>
                    <% } %>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="title">
                <spring:message code="label.internalBilling.billableService.financer" text="Financer" />
            </label>
            <div class="col-sm-10">
                <% final String unitParam = request.getParameter("unit"); %>
                <% final Unit unit = unitParam == null ? null : (Unit) FenixFramework.getDomainObject(unitParam); %>
                <% if (unit == null) { %>
                    <input id="financerTerm" class="form-control" autofocus style="display: inline;"/>
                    <input type="hidden" id="financer" name="financer" value="">
                <% } else { %>
                    <input id="financerTerm" class="form-control" autofocus style="display: inline;"
                        value="<%= unit.getPresentationName() %>"/>
                    <input type="hidden" id="financer" name="financer" value="<%= unitParam %>">
                <% } %>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="title">
                <spring:message code="label.internalBilling.billableService.beneficiary" text="Financer" />
            </label>
            <div class="col-sm-10">
                <% final String userParam = request.getParameter("user"); %>
                <% final User user = userParam == null ? null : (User) FenixFramework.getDomainObject(userParam); %>
                <% if (user == null) { %>
                    <input id="beneficiaryTerm" class="form-control" autofocus style="display: inline;"/>
                    <input type="hidden" id="beneficiary" name="beneficiary" value="">
                <% } else { %>
                    <input id="beneficiaryTerm" class="form-control" autofocus style="display: inline;"
                        value="<%= user.getProfile().getDisplayName() + " (" + user.getUsername() + ")" %>"/>
                    <input type="hidden" id="beneficiary" name="beneficiary" value="<%= userParam %>">
                <% } %>
            </div>
        </div>
        <jsp:include page="billableServiceSubscribe.jsp"/>
        <div class="form-group">
            <div class="col-sm-10 col-sm-offset-2">
                <button id="submitRequest" class="btn btn-primary">
                    <spring:message code="label.subscribe" text="Subscribe" />
                </button>
            </div>
        </div>
    </form>
</div>

<style>
    .ui-autocomplete-loading{background: url(/dot/images/autocomplete/spinner.gif) no-repeat right center}
</style>

<script type="text/javascript" >
    var pageContext= '<%=contextPath%>';
    $(function() {
        $('#financerTerm').autocomplete({
            focus: function(event, ui) {
                //  $( "#searchString" ).val( ui.item.label);
                return false;
            },
            minLength: 2,   
            contentType: "application/json; charset=UTF-8",
            search  : function(){$(this).addClass('ui-autocomplete-loading');},
            open    : function(){$(this).removeClass('ui-autocomplete-loading');},
            source : function(request,response){
                $.post(pageContext + "/internalBilling/billableService/availableUnits", request,function(result) {
                    response($.map(result,function(item) {
                        return{
                            label: item.name,
                            value: item.id
                        }
                    }));
                });
            },
            
            select: function( event, ui ) {
                $( "#financerTerm" ).val( ui.item.label );
                $( "#financer" ).val( ui.item.value );               
                return false;
            }
        });
    });
    $(function() {
        $('#beneficiaryTerm').autocomplete({
            focus: function(event, ui) {
                //  $( "#searchString" ).val( ui.item.label);
                return false;
            },
            minLength: 2,   
            contentType: "application/json; charset=UTF-8",
            search  : function(){$(this).addClass('ui-autocomplete-loading');},
            open    : function(){$(this).removeClass('ui-autocomplete-loading');},
            source : function(request,response){
                $.post(pageContext + "/internalBilling/billableService/availableParties", request,function(result) {
                    response($.map(result,function(item) {
                        return{
                            label: item.name,
                            value: item.id
                        }
                    }));
                });
            },
            
            select: function( event, ui ) {
                $( "#beneficiaryTerm" ).val( ui.item.label );
                $( "#beneficiary" ).val( ui.item.value );               
                return false;
            }
        });
    });
</script>
