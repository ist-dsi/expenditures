<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@page import="java.util.ResourceBundle"%>
<%@page import="org.fenixedu.bennu.core.i18n.BundleUtil"%>
<%@page import="module.mission.domain.MissionSystem"%>


<div class="page-header">
	<h2><spring:message code="acquisitionCreationWizard.title.newAcquisitionOrRefund"></spring:message></h2>
</div>

<spring:url var="backUrl" value="/expenditure/acquisitons/create" />


<div class="col-sm-12">
	<spring:message code="acquisitionCreationWizard.text.information.intro"></spring:message>
	<ul>
		<li><spring:message code="acquisitionCreationWizard.text.information.simplified"></spring:message></li>
		<li><spring:message code="acquisitionCreationWizard.text.information.standard"></spring:message></li>
		<li><spring:message code="acquisitionCreationWizard.text.information.refund"></spring:message></li>
		<li><spring:message code="acquisitionCreationWizard.text.information.consultation"></spring:message></li>
	</ul>
</div>
<div class="col-sm-12">
	<a class="btn btn-default" href="${backUrl}">
		<spring:message code="link.back" text="Back" />
	</a>
</div>