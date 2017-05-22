<jsp:include page="user_common.jsp"/>

<% final String contextPath = request.getContextPath(); %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="col-lg-12">
    <div class="panel panel-default">
        <div class="panel-body">
            <div>
                <table class="table">
                    <thead>
                        <tr>
                            <th><spring:message code="label.unit.responsibilities" text="Responsibilites"/></th>
                            <th><spring:message code="label.authorization.start" text="Start"/></th>
                            <th><spring:message code="label.authorization.end" text="End"/></th>
                            <th><spring:message code="label.authorization.maxValue" text="Max. Value"/></th>
                        </tr>
                    </thead>
                    <tbody id="authorizations"/>
                </table>
            </div>
        </div>
    </div>
</div>

<div class="col-lg-12">
    <div class="panel panel-default">
        <div class="panel-body">
            <div>
                <table class="table">
                    <thead><tr><th><spring:message code="label.unit.observe" text="Observations"/></th></tr></thead>
                    <tbody id="observers"/>
                </table>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var user = ${user};
    var contextPath = '<%= contextPath %>';
    var authorizations = <%= request.getAttribute("authorizations")%>;
    var observers = <%= request.getAttribute("observers")%>;
    $(document).ready(function() {
        document.getElementById("structureTab").parentNode.classList.add("active");

        $(authorizations).each(function(i, a) {
            var hrefUnit = contextPath + a.unit.relativePath;
            var row = $('<tr/>').appendTo($('#authorizations'));
            var unitColumn = $('<td/>').appendTo(row);
            unitColumn.append($('<a href="' + hrefUnit + '">').text(a.unit.name));
            row.append($('<td/>').text(!a.startDate ? '' : a.startDate));
            row.append($('<td/>').text(!a.endDate ? '' : a.endDate));
            row.append($('<td/>').text(a.maxAmount));
        });

        $(observers).each(function(i, o) {
            var hrefUnit = contextPath + o.relativePath;
            var row = $('<tr/>').appendTo($('#observers'));
            var unitColumn = $('<td/>').appendTo(row);
            unitColumn.append($('<a href="' + hrefUnit + '">').text(o.name));
        });
    });
</script>
