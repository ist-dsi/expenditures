
${portal.toolkit()}

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ir" tagdir="/WEB-INF/tags" %>

<spring:url var="confirmUrl" value="/internalrequest/deliver/confirm"></spring:url>

<div class="page-header">
	<div class="row">
		<div class="col-xs-12">
			<h2><spring:message code="title.internalRequests.confirmDelivery" /></h2>
		</div>
	</div>
</div>

<div class="page-body">
	<div class="row">
		<div class="col-sm-12">
			<div class="alert alert-info" role="alert">
				<spring:message code="info.delivery.confirm" />
			</div>
			<form id="confirmDeliveryForm" action="${confirmUrl}" method="GET" class="form-horizontal">
				<div class="form-group">
					<label for="processNumber" class="col-sm-2 control-label"><spring:message code="label.internalRequest.processNumber" /></label>
					<div class="col-sm-10">
						<input type="text" name="processNumber" id="processNumber" class="form-control" required>
					</div>
				</div>
				<div class="form-group">
					<label for="deliveryCode" class="col-sm-2 control-label"><spring:message code="label.internalRequest.deliveryCode" /></label>
					<div class="col-sm-10">
						<input type="text" name="deliveryCode" id="deliveryCode" class="form-control" required>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button type="submit" class="btn btn-default"><spring:message code="activity.DeliveryConfirmationActivity" /></button>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
