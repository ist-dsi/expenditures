<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

<style type="text/css">
	.delivery-code {
		margin-top: 5px;
	}
</style>

<logic:equal name="process" property="userAbleToSeeDeliveryConfirmationCode" value="true">
	<div class="alert alert-info alert-countdown" role="alert">
		<div class="media">
			<div class="media-left media-middle">
				<div id="qrcode"></div>
			</div>
			<div class="media-body">
				<bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="info.deliveryCode"/>
				<h2 class="media-heading delivery-code"><fr:view name="process" property="deliveryConfirmationCode"/></h2>
				<h3><bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="info.deliveryCode.timeRemaining"/>: <span id="countdown"></span></h3>
			</div>
		</div>
	</div>

	<div class="alert alert-warning alert-expired hidden" role="alert">
		<bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="info.deliveryCode.expired"/>
		<wf:activityLink processName="process" activityName="GenerateDeliveryConfirmationCodeActivity" scope="request">
			<bean:message bundle="INTERNAL_REQUEST_RESOURCES" key="activity.GenerateDeliveryConfirmationCodeActivity"/>
		</wf:activityLink>
	</div>

	<bean:define id="processNumber" name="process" property="processNumber" type="java.lang.String"/>
	<bean:define id="deliveryCode" name="process" property="deliveryConfirmationCode" type="java.lang.String"/>
	<script type="text/javascript" src="<%= request.getContextPath()%>/javaScript/qrCode/qrcode.min.js"></script>
	<script>
		$(document).ready(function () {
			var countdownTo = new Date("<fr:view name='process' property='deliveryConfirmationTimeLimitIsoString'/>").getTime();

			function updateCountDown() {
				var now = new Date().getTime();
				var distance = countdownTo - now;
				if(distance < 0) {
					$(".alert-countdown").addClass("hidden");
					$(".alert-expired").removeClass("hidden");
					return;
				}

				var hours = Math.floor(distance / (1000 * 60 * 60));
				var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
				var seconds = Math.floor((distance % (1000 * 60)) / 1000);

				var countdownText = minutes.toString().padStart(2, "0") + ":" + seconds.toString().padStart(2, "0");
				if(hours > 0) {
					countdownText = hours + ":" + countdownText;
				}

				$("#countdown").text(countdownText);
			}

			setInterval(updateCountDown, 1000);
			updateCountDown();

			function createConfirmDeliveryUrlQrCode() {
				const parsedUrl = new URL(window.location.href);
				const confirmDeliveryUrl = new URL("<%= request.getContextPath() %>/internalrequest/deliver/confirm?processNumber=<%= processNumber %>&deliveryCode=<%= deliveryCode %>", parsedUrl);
				new QRCode("qrcode", {
					text: confirmDeliveryUrl.toString(),
					width: 128,
					height: 128,
				});

			}
			createConfirmDeliveryUrlQrCode();
		});
	</script>
</logic:equal>
