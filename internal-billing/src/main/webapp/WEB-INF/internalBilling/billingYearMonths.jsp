<%@page import="pt.ist.internalBilling.domain.BillableStatus"%>
<jsp:include page="common.jsp"/>

<%@page import="pt.ist.internalBilling.domain.YearMonthStatus"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% final String contextPath = request.getContextPath(); %>

<div class="page-body">
    <br/>
    <div class="col-lg-12">    
     <spring:message  code="label.billingYearMonth.message" text="Insert year and month you want to close"></spring:message>
   	<br/>
    <br/> 
     </div>
    <form class="form-horizontal col-lg-12" action="<%= contextPath + "/internalBilling/billingYearMonth/closingYearMonth" %>" method="POST">
    ${csrf.field()}
        <div class="form-group">          
          
            <div class="col-lg-1">
                <spring:message var="YearPlaceholder" scope="request" code="label.internalBilling.billingYearMonth.billingYear"/>
                <input type="text" id="year" name="year" required="required" class="form-control"
                    placeholder="<%= request.getAttribute("YearPlaceholder") %>"/>               
            </div>
            <div class="col-lg-1">
                <spring:message var="MonthPlaceholder" scope="request" code="label.internalBilling.billingYearMonth.billingMonth"/>
                <input type="text" id="month" name="month" required="required" class="form-control"
                    placeholder="<%= request.getAttribute("MonthPlaceholder") %>"/>               
            </div>
            <div class="col-sm-3">
                <button id="submitRequest" class="btn btn-primary">
                    <spring:message code="label.internalBilling.close" text="Close" />
                </button>
            </div>
        </div>        
    </form>

    

    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <strong><spring:message code="label.internalBilling.billingYearMonth.billingStatus" text="Billing Status"/></strong>
            </div>
            <div class="panel-body">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>
                                    <spring:message code="label.internalBilling.billingYearMonth.billingYear" text="Year"/>
                                </th>
                                <th>
                                    <spring:message code="label.internalBilling.billingYearMonth.billingMonth" text="Month"/>
                                </th>
                                <th>
                                    <spring:message code="label.internalBilling.billingYearMonth.processing" text="Processing State"/>
                                </th>
                                <th>
                                    <spring:message code="label.internalBilling.billingYearMonth.document" text=""/>
                                </th>                             
                            </tr>
                        </thead>
                        <tbody id="billingYearMonth"/>
                    </table>
            </div>
        </div>
    </div>

 
</div>

<script type="text/javascript">
    var contextPath = '<%= contextPath %>';   
    var billingYearMonth = ${billingYearMonth};
   
    $(document).ready(function() {
      
        $(billingYearMonth).each(function(i, b) {        
        	var row = $('<tr/>').appendTo($('#billingYearMonth')); 
        	var hrefDownload = contextPath + b.reportLink;
            row.append($('<td/>').html($('<span>').text(b.year)));
            row.append($('<td/>').html($('<span>').text(b.month)));            
            row.append($('<td/>').html($('<span>').text(b.billingYearMonthStatusDescription)));
            row.append($('<td/>').html($('<a class="btn btn-default" href="'+ hrefDownload +'">').text('Download')));
            
        });
                
    });


</script>
