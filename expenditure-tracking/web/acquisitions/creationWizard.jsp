<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<h2><bean:message key="title.newAcquisition" bundle="EXPENDITURE_RESOURCES"/></h2>


<p>
	<strong>
		<bean:message key="label.selectProcessType" bundle="EXPENDITURE_RESOURCES"/>:
	</strong>
</p>


<div class="infoop6 mvert1">

	<ul class="mvert05" style="padding-left: 0em; list-style: none;">
		<li class="mbottom1">
			<p class="mvert025">
				<strong>
					<html:link styleClass="big" action="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcess">
						<bean:message key="link.create.simplifiedAcquisitionProcedure" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</strong>
			</p>
			<p class="mvert025">
				<bean:message key="message.info.acquisitionSimplifiedProcessExplanation" bundle="EXPENDITURE_RESOURCES"/>
			</p>
		</li>

		<li>
			<p class="mvert025">
				<strong>
					<html:link styleClass="big" action="/acquisitionRefundProcess.do?method=prepareCreateRefundProcess">
						<bean:message key="link.create.refundProcess" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</strong>
			</p>
			<p class="mvert025">
				<bean:message key="message.info.refundProcessExplanation" bundle="EXPENDITURE_RESOURCES"/>
			</p>
		</li>
	
		<li class="mbottom1">
			<p class="mvert025">
				<strong>
				<%-- 
					<html:link styleClass="big" action="/acquisitionStandardProcedureProcess.do?method=prepareCreateAcquisitionStandardProcess">
				 --%>
						<bean:message key="link.create.standardAcquisitionProcess" bundle="EXPENDITURE_RESOURCES"/>
				<%-- 
					</html:link>
				 --%>
				</strong>
			</p>
			<p class="mvert025">
				<bean:message key="message.info.acquisitionStandardProcessExplanation" bundle="EXPENDITURE_RESOURCES"/>
			</p>
		</li>
	
	</ul>

</div>

		
			