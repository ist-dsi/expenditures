<jsp:include page="unit_common.jsp"/>

<% final String contextPath = request.getContextPath(); %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>

<div class="col-lg-6">
    <div class="panel panel-default">
        <div class="panel-body">
            <div>
                <table class="table">
                    <thead><tr><th><spring:message code="label.unit.parent" text="Parent Unit"/></th></tr></thead>
                    <tbody id="parentUnit"/>
                </table>
            </div>
            <div>
                <table class="table">
                    <thead><tr><th><spring:message code="label.unit.children" text="Subunits"/></th></tr></thead>
                    <tbody id="subunits"/>
                </table>
            </div>
        </div>
    </div>
</div>

<div class="col-lg-6">
    <div class="panel panel-default">
        <div class="panel-body">
            <div>
                <table class="table">
                    <thead><tr><th><spring:message code="label.unit.authorities" text="Authorities"/></th></tr></thead>
                    <tbody id="authorizations"/>
                </table>
                <table class="table">
                    <thead><tr><th><spring:message code="label.unit.observers" text="Observers"/></th></tr></thead>
                    <tbody id="observers"/>
                </table>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var unit = ${unit};
    var contextPath = '<%= contextPath %>';
    var authorizations = <%= request.getAttribute("authorizations")%>;
    var observers = <%= request.getAttribute("observers")%>;
    $(document).ready(function() {
        document.getElementById("structureTab").parentNode.classList.add("active");
        	
        if (unit.parent != null) {
            var hrefUnit = contextPath + unit.parent.relativePath;
            var row = $('<tr/>').appendTo($('#parentUnit'));
            row.append($('<td/>').html($('<a href="' + hrefUnit + '">').text(unit.parent.name)));
        }

        $(unit.subunits).each(function(i, u) {
            var hrefUnit = contextPath + u.relativePath;
            var row = $('<tr/>').appendTo($('#subunits'));
            row.append($('<td/>').html($('<a href="' + hrefUnit + '">').text(u.name)));
        });

        $(authorizations).each(function(i, a) {
            var hrefUser = contextPath + a.user.relativePath;
            var row = $('<tr/>').appendTo($('#authorizations'));
            var userColumn = $('<td/>').appendTo(row);
            userColumn.append($('<img class="img-circle" alt="" src="' + a.user.avatarUrl + '"/>'));
            userColumn.append($('<a href="' + hrefUser + '">').text(a.user.name));
        });

        $(observers).each(function(i, o) {
            var hrefUser = contextPath + o.relativePath;
            var row = $('<tr/>').appendTo($('#observers'));
            var userColumn = $('<td/>').appendTo(row);
            userColumn.append($('<img class="img-circle" alt="" src="' + o.avatarUrl + '"/>'));
            userColumn.append($('<a href="' + hrefUser + '">').text(o.name));
        });
    });
</script>
