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
<%@page import="pt.ist.internalBilling.domain.UnitBeneficiary"%>
<%@page import="pt.ist.internalBilling.domain.UserBeneficiary"%>
<%@page import="pt.ist.internalBilling.domain.Beneficiary"%>
<%@page import="pt.ist.internalBilling.domain.Billable"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% final String contextPath = request.getContextPath(); %>
<% final Billable billable = (Billable) request.getAttribute("billable"); %>

<form method="POST" action="<%= contextPath + "/internalBilling/billable/" + billable.getExternalId() + "/authorizePhone" %>">
    <input name="internalCalls" type="hidden" value="true"/>

    <div class="form-group">
        <label class="control-label col-sm-2" for="screenName">
            <spring:message code="label.service.phone.screen.name" text="Screen Name"/>
        </label>
        <div class="col-sm-10">
            <% final Beneficiary beneficiary = billable.getBeneficiary(); %>
            <% final String screenName = beneficiary instanceof UserBeneficiary
                    ? ((UserBeneficiary) beneficiary).getUser().getProfile().getDisplayName()
                    : ((UnitBeneficiary) beneficiary).getUnit().getUnit().getAcronym(); %>
            <input name="screenName" type="text" class="form-control" value="<%= screenName %>"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2" for="">
            <spring:message code="label.service.phone.internal.calls" text="Internal Calls" />
        </label>
        <div class="col-sm-10">
            <input name="internalCallsHack" type="checkbox" checked="checked" disabled="disabled" class="form-control"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2" for="">
            <spring:message code="label.service.phone.national.hardline" text="National Calls" />
        </label>
        <div class="col-sm-10">
            <input name="nationalHardline" type="checkbox" checked="checked" class="form-control"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2" for="">
            <spring:message code="label.service.phone.mobile.network" text="Mobile Networks" />
        </label>
        <div class="col-sm-10">
            <input name="mobileNetwork" type="checkbox" checked="checked" class="form-control"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2" for="">
            <spring:message code="label.service.phone.international.calss" text="International Calls" />
        </label>
        <div class="col-sm-10">
            <input name="internationalCalls" type="checkbox" class="form-control" class="form-control" style="border: none;"/>
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-10 col-sm-offset-2">
            <button id="submitRequest" class="btn btn-success">
                <spring:message code="label.authorize" text="Authorize"/>
            </button>
        </div>
    </div>

</form>
