<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2><bean:message key="title.newAcquisition" bundle="EXPENDITURE_RESOURCES"/></h2>

<p class="mvert05">
	<bean:message key="label.selectProcessType" bundle="EXPENDITURE_RESOURCES"/>:
</p>

<table style="width: 100%;">
	<tr>
		<td style="width: 100%; vertical-align: top; padding-right: 10px;">
		
			<div class="infobox5" style="float: left; width: 100%">
				<div>
					<ul class="list-reset" class="mtop5">
						<li style="padding-bottom: 10px;">
							<strong>
								<html:link styleClass="big" action="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcessNormal">
									<bean:message key="link.create.process.Normal" bundle="EXPENDITURE_RESOURCES"/>
								</html:link>
							</strong>
							<br/>
							<bean:message key="message.info.processExplanation.Normal" bundle="EXPENDITURE_RESOURCES"/>
						</li>
						<li style="padding-bottom: 10px;">
							<strong>
								<html:link styleClass="big" action="/acquisitionRefundProcess.do?method=prepareCreateRefundProcessUnderNormal">
									<bean:message key="link.create.refundProcess.Normal" bundle="EXPENDITURE_RESOURCES"/>
								</html:link>
							</strong>
							<br/>
							<bean:message key="message.info.refundProcessExplanation.Normal" bundle="EXPENDITURE_RESOURCES"/>
						</li>
					</ul>
				</div>
			</div>
		</td>		
	</tr>
</table>
