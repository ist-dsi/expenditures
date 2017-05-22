<jsp:include page="unit_common.jsp"/>

<% final String contextPath = request.getContextPath(); %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script src='<%= contextPath + "/js/internal-billing/sort.js" %>'></script>


<div class="page-body">
    <table id="logTable" class="table">
        <thead>
            <tr>
                <th width="12%" onclick="sortTable('logTable', 0);">
                    <spring:message code="label.operation.date"/>
                </th>
                <th onclick="sortTable('logTable', 1);">
                    <spring:message code="label.operation"/>
                </th>
                <th width="35%">
                    <spring:message code="label.operation.executor"/>
                </th>
            </tr>
        </thead>
        <tbody id="logsTableBody"/>
    </table>
</div>

<script type="text/javascript">
    var logs = ${logs};
    var contextPath = '<%= contextPath %>';
    $(document).ready(function() {
    	document.getElementById("logsTab").parentNode.classList.add("active");
        $(logs).each(function(i, l) {
            var row = $('<tr/>').appendTo($('#logsTableBody'));
        	row.append($('<td/>').text(l.when));
            row.append($('<td/>').text(l.description));
        	var userColumn = $('<td/>').appendTo(row);
            var hrefUser = contextPath + l.user.relativePath;
            userColumn.append($('<img class="img-circle" alt="" src="' + l.user.avatarUrl + '"/>'));
            userColumn.append($('<a href="' + hrefUser + '">').text(l.user.name));
        });
    });
</script>
