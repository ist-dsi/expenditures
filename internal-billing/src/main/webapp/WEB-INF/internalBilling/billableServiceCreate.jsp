<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% final String contextPath = request.getContextPath(); %>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>

<div class="page-header">
    <h2>
        <spring:message code="label.internalBilling.billableService.create" text="Create Service"/>
    </h2>
</div>

<div class="page-body">
    <form class="form-horizontal" action="<%= contextPath + "/internalBilling/billableService/createService" %>" method="POST">
    ${csrf.field()}
        <div class="form-group">
            <label class="control-label col-sm-2" for="type">
                <spring:message code="label.internalBilling.billableService.type" text="Type" />
            </label>
            <div class="col-sm-10">
                <select name="type" class="form-control" id="type" required="required" onchange="displayServiceSpecificOptions();">
                    <option value="" disabled selected>
                        <spring:message code="label.choose" text="Select an option" />
                    </option>
                    <option value="pt.ist.internalBilling.domain.PhoneService">
                        <spring:message code="label.pt.ist.internalBilling.domain.PhoneService" text="Phone" />
                    </option>
                    <option value="pt.ist.internalBilling.domain.PrintService">
                        <spring:message code="label.pt.ist.internalBilling.domain.PrintService" text="Print" />
                    </option>
                    <option value="pt.ist.internalBilling.domain.VirtualHostingService">
                        <spring:message code="label.pt.ist.internalBilling.domain.VirtualHostingService" text="Virtual Hosting" />
                    </option>
                </select>
            </div>
        </div>
    </form>

    <form id="phoneForm" class="form-horizontal" action="<%= contextPath + "/internalBilling/billableService/createService" %>" method="POST"
            style="display: none;">
            ${csrf.field()}
        <input type="hidden" name="type" value="pt.ist.internalBilling.domain.PhoneService">
        <jsp:include page="billableServiceCreateFormCommon.jsp"/>
        <div id="phoneOptions" class="form-group" style="display: none;">
        </div>
        <div class="form-group">
            <div class="col-sm-10 col-sm-offset-2">
                <button id="submitRequest" class="btn btn-primary">
                    <spring:message code="label.create" text="Create" />
                </button>
            </div>
        </div>
    </form>

    <form id="printForm" class="form-horizontal" action="<%= contextPath + "/internalBilling/billableService/createService" %>" method="POST"
            style="display: none;">
            ${csrf.field()}
        <input type="hidden" name="type" value="pt.ist.internalBilling.domain.PrintService">
        <jsp:include page="billableServiceCreateFormCommon.jsp"/>
        <div class="form-group">
            <div class="col-sm-10 col-sm-offset-2">
                <button id="submitRequest" class="btn btn-primary">
                    <spring:message code="label.create" text="Create" />
                </button>
            </div>
        </div>
    </form>

    <form id="virtualHostingForm" class="form-horizontal" action="<%= contextPath + "/internalBilling/billableService/createService" %>" method="POST"
            style="display: none;">
            ${csrf.field()}
        <input type="hidden" name="type" value="pt.ist.internalBilling.domain.VirtualHostingService">
        <jsp:include page="billableServiceCreateFormCommon.jsp"/>
        <div id="virtualHostingOptions" class="form-group" style="display: none;">
        </div>
        <div class="form-group">
            <div class="col-sm-10 col-sm-offset-2">
                <button id="submitRequest" class="btn btn-primary">
                    <spring:message code="label.create" text="Create" />
                </button>
            </div>
        </div>
    </form>

</div>

<script type="text/javascript" >
    var pageContext= '<%=contextPath%>';

    function displayServiceSpecificOptions() {
    	var t = document.getElementById("type");
    	var v = t.options[t.selectedIndex].value;
    	if (v.localeCompare('pt.ist.internalBilling.domain.PhoneService') == 0) {
    		document.getElementById("phoneForm").style.display = 'block';
    		document.getElementById("printForm").style.display = 'none';
    		document.getElementById("virtualHostingForm").style.display = 'none';
    	} else if (v.localeCompare('pt.ist.internalBilling.domain.PrintService') == 0) {
    		document.getElementById("phoneForm").style.display = 'none';
    		document.getElementById("printForm").style.display = 'block';
    		document.getElementById("virtualHostingForm").style.display = 'none';
    	} else if (v.localeCompare('pt.ist.internalBilling.domain.VirtualHostingService') == 0) {
    		document.getElementById("phoneForm").style.display = 'none';
    		document.getElementById("printForm").style.display = 'none';
    		document.getElementById("virtualHostingForm").style.display = 'block';
    	}
    }
</script>
