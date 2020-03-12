${portal.toolkit()}

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ir" tagdir="/WEB-INF/tags" %>

<spring:url var="listUrl" value="/internalrequest/search/"></spring:url>
<spring:url var="viewUrl" value="/internalrequest/search/view/"></spring:url>

<div class="page-header">
	<div class="row">
		<div class="col-xs-12">
			<h2><spring:message code="title.internalRequests.search" /></h2>
		</div>
	</div>
</div>

<div class="page-body">
	<div class="row">
		<div class="col-sm-12">
			<form id="searchForm" action="${listUrl}" method="GET" class="form-horizontal">
				<input type="hidden" name="page" id="page" value="${searchParams.page}">
				<input type="hidden" name="sort" id="sort" value="${searchParams.sort}">
				<input type="hidden" name="order" id="order" value="${searchParams.order.toString()}">
				<div class="form-group">
					<label for="year" class="col-sm-2 control-label"><spring:message code="label.internalRequest.year" /></label>
					<div class="col-sm-10">
						<input type="number" name="year" id="year" class="form-control" value='${searchParams.year}' required>
					</div>
				</div>
				<div class="form-group">
					<label for="processNumber" class="col-sm-2 control-label"><spring:message code="label.internalRequest.processNumber" /></label>
					<div class="col-sm-10">
						<input type="text" name="processNumber" id="processNumber" class="form-control" value='${searchParams.processNumber}'>
					</div>
				</div>
				<div class="form-group">
					<label for="requestingPerson" class="col-sm-2 control-label"><spring:message code="label.internalRequest.requestingPerson" /></label>
					<div class="col-sm-10">
						<input type="text" name="requestingPerson" id="requestingPerson" class="form-control" value='${searchParams.requestingPerson}'>
					</div>
				</div>
				<div class="form-group">
					<label for="requestingUnit" class="col-sm-2 control-label"><spring:message code="label.internalRequest.requestingUnit" /></label>
					<div class="col-sm-10">
						<input type="text" name="requestingUnit" id="requestingUnit" class="form-control" value='${searchParams.requestingUnit}'>
					</div>
				</div>
				<div class="form-group">
					<label for="requestedUnit" class="col-sm-2 control-label"><spring:message code="label.internalRequest.requestedUnit" /></label>
					<div class="col-sm-10">
						<input type="text" name="requestedUnit" id="requestedUnit" class="form-control" value='${searchParams.requestedUnit}'>
					</div>
				</div>
				<div class="form-group">
					<label for="item" class="col-sm-2 control-label"><spring:message code="label.internalRequest.item" /></label>
					<div class="col-sm-10">
						<input type="text" name="item" id="item" class="form-control" value='${searchParams.item}'>
					</div>
				</div>
				<div class="form-group">
					<label for="pendingState" class="col-sm-2 control-label"><spring:message code="label.internalRequest.pendingState" /></label>
					<div class="col-sm-10">
						<select name="pendingState" id="pendingState" class="form-control">
							<option value=""><spring:message code="info.search.select" /></option>
							<c:forEach items="${internalRequestStates}" var="state">
								<option value="${state.toString()}" ${state.equals(searchParams.pendingState) ? "selected" : ""}>
									<spring:message code="label.InternalRequestState.${state.toString()}" />
								</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="includeTaken" class="col-xs-10 col-sm-2 control-label"><spring:message code="label.search.includeTaken" /></label>
					<div class="col-xs-1">
						<input type="checkbox" name="includeTaken" id="includeTaken" class="form-control" value='true' ${searchParams.includeTaken ? "checked" : ""}>
					</div>
				</div>
				<div class="form-group">
					<label for="includeCancelled" class="col-xs-10 col-sm-2 control-label"><spring:message code="label.search.includeCancelled" /></label>
					<div class="col-xs-1">
						<input type="checkbox" name="includeCancelled" id="includeCancelled" class="form-control" value='true' ${searchParams.includeCancelled ? "checked" : ""}>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button type="submit" class="btn btn-default"><spring:message code="link.search" /></button>
					</div>
				</div>
			</form>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-12">

			<c:choose>
				<c:when test="${processes.nrOfElements == 0}">
					<div class="alert alert-danger">
						<spring:message code="info.search.noResults" />
					</div>
				</c:when>
				<c:otherwise>
					<div class="table-responsive">
						<table class="table table-hover" id="searchResults">
							<thead>
								<tr>
									<th class="col-xs-1">
										<a href="#" class="sortable" data-sort="processNumber"><spring:message code="label.internalRequest.processNumber" /></a>
									</th>
									<th class="col-xs-3">
										<a href="#" class="sortable" data-sort="requestingPerson"><spring:message code="label.internalRequest.requestingPerson" /></a>
									</th>
									<th class="col-xs-3">
										<a href="#" class="sortable" data-sort="requestingUnit"><spring:message code="label.internalRequest.requestingUnit" /></a>
									</th>
									<th class="col-xs-3">
										<a href="#" class="sortable" data-sort="requestedUnit"><spring:message code="label.internalRequest.requestedUnit" /></a>
									</th>
									<th class="col-xs-2"><spring:message code="title.internalRequest.items" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${processes.pageList}" var="process">
									<tr>
										<td>
											<a href='${viewUrl}${process.externalId}'><c:out value="${process.processNumber}"/></a>
										</td>
										<td><c:out value="${process.internalRequest.requestingPerson.presentationName}"/></td>
										<td><c:out value="${process.internalRequest.requestingUnit.name}"/></td>
										<td><c:out value="${process.internalRequest.requestedUnit.name}"/></td>
										<td>
											<c:forEach items="${process.internalRequest.itemsSet}" var="item">
												<c:out value="${item.quantity}"/>x <c:out value="${item.description}"/>
												<br />
											</c:forEach>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<ir:paging pagedListHolder="${processes}"></ir:paging>

					<script>
						$(document).ready(function() {
							$('.sortable').click(function() {
								var sortField = $("#sort");
								var orderField = $("#order");
								var selectedSort = $(this).data("sort");

								if (sortField.val() == selectedSort) {
									if (orderField.val() == "ASC") {
										orderField.val("DESC");
									} else {
										orderField.val("ASC");
									}
								} else {
									sortField.val(selectedSort);
									orderField.val("ASC");
								}
								$("#searchForm").submit();
							});
						});
					</script>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
