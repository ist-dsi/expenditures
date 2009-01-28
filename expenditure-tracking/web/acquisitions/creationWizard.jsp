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


<ul>
	<li>
		<strong>
			<html:link action="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcess">
				<bean:message key="link.create.simplifiedAcquisitionProcedure" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</strong>
		<p class="mtop025">
			<bean:message key="message.info.acquisitionSimplifiedProcessExplanation" bundle="EXPENDITURE_RESOURCES"/>
		</p>
	</li>

	<li>
		<strong>
			<html:link action="/acquisitionRefundProcess.do?method=prepareCreateRefundProcess">
				<bean:message key="link.sideBar.refundProcess.create" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</strong>
		<p class="mtop025">
			<bean:message key="message.info.refundProcessExplanation" bundle="EXPENDITURE_RESOURCES"/>
		</p>
	</li>
</ul>


		
			