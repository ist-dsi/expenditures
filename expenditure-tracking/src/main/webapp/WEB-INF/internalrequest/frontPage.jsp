${portal.toolkit()}

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="ir" tagdir="/WEB-INF/tags" %>

<spring:url var="viewUrl" value="/internalrequest/search/view/"></spring:url>

<div class="page-header">
	<div class="row">
		<div class="col-xs-12">
			<h2><spring:message code="title.internalRequests.frontPage" /></h2>
		</div>
	</div>
</div>

<div class="page-body">
	<div class="row">
		<div class="col-sm-6">
			<form method="GET" class="form-horizontal">
				<div class="form-group">
					<label for="year" class="col-sm-2 control-label"><spring:message code="label.internalRequest.year" /></label>
					<div class="col-sm-10 input-group">
						<input type="number" name="year" id="year" class="form-control" value='${searchParams.year}' required>
						<span class="input-group-btn">
							<button class="btn btn-default" type="submit"><spring:message code="link.view" /></button>
						</span>
					</div>
				</div>
			</form>
		</div>
	</div>

	<div style="float: left; width: 100%">
		<table style="width: 100%; margin: 1em 0;">
			<tr>
				<td style="border: 1px dotted #aaa; padding: 10px 15px; width: 48%; vertical-align: top;">
					<p class="mtop0 mbottom05">
						<b><spring:message code="label.internalRequest.pendingState.approval" /></b>
					</p>
					<ir:internalRequestProcessList processList="${pendingApproval}" viewUrl="${viewUrl}" />
					<br />

					<p class="mtop0 mbottom05">
						<b><spring:message code="label.internalRequest.pendingState.budgeting" /></b>
					</p>
					<ir:internalRequestProcessList processList="${pendingBudget}" viewUrl="${viewUrl}" />
					<br />

					<p class="mtop0 mbottom05">
						<b><spring:message code="label.internalRequest.pendingState.authorization" /></b>
					</p>
					<ir:internalRequestProcessList processList="${pendingAuthorization}" viewUrl="${viewUrl}" />
					<br />

					<p class="mtop0 mbottom05">
						<b><spring:message code="label.internalRequest.pendingState.processing" /></b>
					</p>
					<ir:internalRequestProcessList processList="${pendingProcessing}" viewUrl="${viewUrl}" />
					<br />

					<p class="mtop0 mbottom05">
						<b><spring:message code="label.internalRequest.pendingState.delivery" /></b>
					</p>
					<ir:internalRequestProcessList processList="${pendingDelivery}" viewUrl="${viewUrl}" />
					<br />

					<p class="mtop0 mbottom05">
						<b><spring:message code="label.internalRequest.pendingState.costImputation" /></b>
					</p>
					<ir:internalRequestProcessList processList="${pendingCostImputation}" viewUrl="${viewUrl}" />
				</td>
				<td style="border: none; width: 2%; padding: 0;"></td>
				<td style="border: 1px dotted #aaa; padding: 10px 15px; width: 48%; vertical-align: top;">
					<p class="mtop0 mbottom05">
						<b><spring:message code="label.internalRequest.requestingPerson.me" /></b>
					</p>
					<ir:internalRequestProcessList processList="${requestedByMe}" viewUrl="${viewUrl}" />
					<br />

					<p class="mtop0 mbottom05">
						<b><spring:message code="label.internalRequest.taken" /></b>
					</p>
					<ir:internalRequestProcessList processList="${takenByMe}" viewUrl="${viewUrl}" />
				</td>
			</tr>
		</table>
	</div>
</div>
