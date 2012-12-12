<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<h2><bean:message key="title.newAcquisition" bundle="EXPENDITURE_RESOURCES"/></h2>

<p class="mvert05">
	<bean:message key="label.selectProcessType" bundle="EXPENDITURE_RESOURCES"/>:
</p>

<table>
	<tr>
		<td style="width: 50%; vertical-align: top; padding-right: 10px;">
		
			<div class="infobox5" style="float: left;">
				<h3><bean:message key="label.ccp" bundle="EXPENDITURE_RESOURCES"/></h3>
				<div>
					<p>
						<bean:message key="label.ccp.explanation" bundle="EXPENDITURE_RESOURCES"/>
					</p>
					<ul class="list-reset">
						<li style="padding-bottom: 10px;">
							<strong>
								<html:link styleClass="big" action="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcess">
									<bean:message key="link.create.simplifiedAcquisitionProcedure" bundle="EXPENDITURE_RESOURCES"/>
								</html:link>
							</strong>
							<br/>
							<bean:message key="message.info.acquisitionSimplifiedProcessExplanation" bundle="EXPENDITURE_RESOURCES"/>
						</li>
						<li style="padding-bottom: 10px;">
							<strong>
							<%-- 
								<html:link styleClass="big" action="/acquisitionStandardProcedureProcess.do?method=prepareCreateAcquisitionStandardProcess">
							 --%>
									<bean:message key="link.create.standardAcquisitionProcess" bundle="EXPENDITURE_RESOURCES"/>
							<%-- 
								</html:link>
							 --%>
							</strong>
							<br/>
							<bean:message key="message.info.acquisitionStandardProcessExplanation" bundle="EXPENDITURE_RESOURCES"/>
						</li>
						<li style="padding-bottom: 10px;">
							<strong>
								<html:link styleClass="big" action="/acquisitionRefundProcess.do?method=prepareCreateRefundProcessUnderCCP">
									<bean:message key="link.create.refundProcess" bundle="EXPENDITURE_RESOURCES"/>
								</html:link>
							</strong>
							<br/>
							<bean:message key="message.info.refundProcessExplanation" bundle="EXPENDITURE_RESOURCES"/>
						</li>
					</ul>
				</div>
			</div>





		</td>
		
		<td style="width: 50%; vertical-align: top; padding-left: 10px;">
		
			<div class="infobox5" style="float: right">
				<h3><bean:message key="label.ScienticeAndTechnologyProcesses" bundle="EXPENDITURE_RESOURCES"/></h3>
				<div>
					<p class="mbottom5">
						<bean:message key="label.ScienticeAndTechnologyProcesses.explanation" bundle="EXPENDITURE_RESOURCES"/>
					</p>
					<ul class="list-reset" class="mtop5">
						<li style="padding-bottom: 10px;">
							<strong>
								<html:link styleClass="big" action="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcessCT10000">
									<bean:message key="link.create.process.CT10000" bundle="EXPENDITURE_RESOURCES"/>
								</html:link>
							</strong>
							<br/>
							<bean:message key="message.info.processExplanation.CT10000" bundle="EXPENDITURE_RESOURCES"/>
						</li>
						<li style="padding-bottom: 10px;">
							<strong>
								<html:link styleClass="big" action="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcessCT75000">
									<bean:message key="link.create.process.CT75000" bundle="EXPENDITURE_RESOURCES"/>
								</html:link>
							</strong>
							<br/>
							<bean:message key="message.info.processExplanation.CT75000" bundle="EXPENDITURE_RESOURCES"/>
						</li>
						<li style="padding-bottom: 10px;">
							<strong>
								<html:link styleClass="big" action="/acquisitionRefundProcess.do?method=prepareCreateRefundProcessUnderRCIST">
									<bean:message key="link.create.refundProcess" bundle="EXPENDITURE_RESOURCES"/>
								</html:link>
							</strong>
							<br/>
							<bean:message key="message.info.refundProcessExplanation" bundle="EXPENDITURE_RESOURCES"/>
						</li>
					</ul>
				</div>
			</div>
		
		
		</td>
	</tr>
</table>





<!--
<div class="clear"></div>
-->






			
