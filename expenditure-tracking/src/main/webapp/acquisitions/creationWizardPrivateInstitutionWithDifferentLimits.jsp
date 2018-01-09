<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<h2><bean:message key="title.newAcquisitionOrRefund" bundle="EXPENDITURE_RESOURCES"/></h2>

<p class="mvert05">
	<bean:message key="label.selectProcessType" bundle="EXPENDITURE_RESOURCES"/>:
</p>

<table style="width: 100%;">
	<tr>
		<td style="width: 100%; vertical-align: top; padding-left: 10px;">
		
			<div class="infobox5" style="float: left; width: 98%">
				<div>
<%--
					<ul class="list-reset" class="mtop5">
						<li style="padding-bottom: 10px;">
 --%>
							<strong>
								<html:link styleClass="big" action="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcessCT10000">
									<bean:message key="link.create.process.CT10000" bundle="EXPENDITURE_RESOURCES"/>
								</html:link>
							</strong>
							<br/>
							<bean:message key="message.info.processExplanation.CT10000.private.variation" bundle="EXPENDITURE_RESOURCES"/>
							<br/>
							<br/>

<%--
						</li>
						<li style="padding-bottom: 10px;">
--%>
							<strong>
								<html:link styleClass="big" action="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcessCT75000">
									<bean:message key="link.create.process.CT75000" bundle="EXPENDITURE_RESOURCES"/>
								</html:link>
							</strong>
							<br/>
							<bean:message key="message.info.processExplanation.CT75000.private.variation" bundle="EXPENDITURE_RESOURCES" arg0="<%= ExpenditureTrackingSystem.getInstance().getTopLevelUnitsSet().iterator().next().getUnit().getAcronym() %>"/>
							<br/>
							<br/>
<%--
						</li>
						<li style="padding-bottom: 10px;">
--%>
							<strong>
<!-- 
								<html:link styleClass="big" action="/acquisitionRefundProcess.do?method=prepareCreateRefundProcessUnderRCIST">
								</html:link>
-->
									<bean:message key="link.create.refundProcess" bundle="EXPENDITURE_RESOURCES"/>
							</strong>
							<br/>
							<bean:message key="message.info.refundProcessExplanation" bundle="EXPENDITURE_RESOURCES"/>
<%--
						</li>
					</ul>
--%>
				</div>
			</div>
		
		
		</td>
	</tr>
</table>





<!--
<div class="clear"></div>
-->






			
