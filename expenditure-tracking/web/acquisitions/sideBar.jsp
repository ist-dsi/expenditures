<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<logic:present name="USER_SESSION_ATTRIBUTE" property="person">

	<ul>
		<li class="header">
			<strong><bean:message key="link.sideBar.simplifiedProcedure" bundle="EXPENDITURE_RESOURCES"/></strong>
			<div class="lic1"></div><div class="lic2"></div>
		</li>
		<li>
			<html:link action="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcess">
				<bean:message key="link.sideBar.acquisitionProcess.create" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="/acquisitionSimplifiedProcedureProcess.do?method=searchAcquisitionProcess">
				<bean:message key="link.sideBar.acquisitionProcess.search" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="/acquisitionSimplifiedProcedureProcess.do?method=showPendingProcesses">
				<bean:message key="link.sideBar.acquisitionProcess.pendingProcesses" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>	
		<li>
			<html:link action="/acquisitionSimplifiedProcedureProcess.do?method=showMyProcesses">
				<bean:message key="link.sideBar.acquisitionProcess.myProcesses" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
	</ul>

<%-- 
<ul>
		<li class="header">
			<strong><bean:message key="link.sideBar.standardProcedure" bundle="EXPENDITURE_RESOURCES"/></strong>
			<div class="lic1"></div><div class="lic2"></div>
		</li>
		<li>
			<html:link action="/acquisitionStandardProcedureProcess.do?method=prepareCreateAcquisitionStandardProcess">
				<bean:message key="link.sideBar.acquisitionProcess.create" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="/acquisitionStandardProcedureProcess.do?method=searchAcquisitionProcess">
				<bean:message key="link.sideBar.acquisitionProcess.search" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="/acquisitionStandardProcedureProcess.do?method=showPendingProcesses">
				<bean:message key="link.sideBar.acquisitionProcess.pendingProcesses" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>	
		<li>
			<html:link action="/acquisitionStandardProcedureProcess.do?method=showMyProcesses">
				<bean:message key="link.sideBar.acquisitionProcess.myProcesses" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
--%>

<%--
	<logic:present role="ACQUISITION_CENTRAL_MANAGER">
		<ul>
			<li class="header">
				<strong><bean:message key="link.sideBar.directContract" bundle="EXPENDITURE_RESOURCES"/></strong>
				<div class="lic1"></div><div class="lic2"></div>
			</li>
				<li>
					<html:link action="/acquisitionProcess.do?method=prepareCreateAcquisitionProcess">
						<bean:message key="link.sideBar.acquisitionProcess.create" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
				<li>
					<html:link action="/acquisitionProcess.do?method=searchAcquisitionProcess">
						<bean:message key="link.sideBar.acquisitionProcess.search" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
				<li>
					<html:link action="/acquisitionProcess.do?method=showPendingProcesses">
						<bean:message key="link.sideBar.acquisitionProcess.pendingProcesses" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>	
				<li>
					<html:link action="/acquisitionProcess.do?method=showMyProcesses">
						<bean:message key="link.sideBar.acquisitionProcess.myProcesses" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
		</ul>
	</logic:present>
--%>
<%--
	<ul>
		<li class="header">
			<strong><bean:message key="link.sideBar.refundProcedure" bundle="EXPENDITURE_RESOURCES"/></strong>
			<div class="lic1"></div><div class="lic2"></div>
		</li>
		<li>
			<html:link action="/acquisitionRefundProcess.do?method=prepareCreateRefundProcess">
				<bean:message key="link.sideBar.refundProcess.create" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="/acquisitionRefundProcess.do?method=searchRefundProcess">
				<bean:message key="link.sideBar.acquisitionProcess.search" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="/acquisitionRefundProcess.do?method=showPendingProcesses">
				<bean:message key="link.sideBar.acquisitionProcess.pendingProcesses" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>	
		<li>
			<html:link action="/acquisitionRefundProcess.do?method=showMyProcesses">
				<bean:message key="link.sideBar.acquisitionProcess.myProcesses" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
--%>
	<logic:present role="ACQUISITION_CENTRAL,ACQUISITION_CENTRAL_MANAGER">
		<ul>
			<li class="header">
				<strong><bean:message key="link.sideBar.other.operations" bundle="EXPENDITURE_RESOURCES"/></strong>
				<div class="lic1"></div><div class="lic2"></div>
			</li>
			<li>
				<html:link action="/acquisitionAfterTheFactAcquisitionProcess.do?method=prepareCreateAfterTheFactAcquisitionProcess">
					<bean:message key="link.sideBar.afterTheFactAcquisitionProcess.create" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
			<li>
				<html:link action="/acquisitionAfterTheFactAcquisitionProcess.do?method=prepareImport">
					<bean:message key="link.sideBar.importAfterTheFactAcquisitions" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
			<li>
				<html:link action="/acquisitionAfterTheFactAcquisitionProcess.do?method=listImports">
					<bean:message key="link.sideBar.listAfterTheFactAcquisitions" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
	
		</ul>
	</logic:present>

	<logic:present role="ACCOUNTING_MANAGER,PROJECT_ACCOUNTING_MANAGER">
		<ul>
				<li class="header">
					<strong><bean:message key="link.sideBar.other.operations" bundle="EXPENDITURE_RESOURCES"/></strong>
					<div class="lic1"></div><div class="lic2"></div>
				</li>
				<li>
					<html:link action="/acquisitionProcess.do?method=checkFundAllocations">
						<bean:message key="link.sideBar.checkFundAllocations" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
		</ul>
	</logic:present>
	
</logic:present>

