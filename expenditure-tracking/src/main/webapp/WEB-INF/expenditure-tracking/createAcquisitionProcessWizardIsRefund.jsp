<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@page import="java.util.ResourceBundle"%>
<%@page import="org.fenixedu.bennu.core.i18n.BundleUtil"%>
<%@page import="module.mission.domain.MissionSystem"%>

<spring:url var="refundUrl"
	value="/acquisitionRefundProcess.do?method=prepareCreateRefundProcessUnderCCP" />
<spring:url var="infoUrl"
	value="/expenditure/acquisitons/create/info" />


<div class="page-header">
    <h2><spring:message code="acquisitionCreationWizard.title.newAcquisitionOrRefund"></spring:message></h2>
</div>

<div class="page-body">
    <div class="infobox_dotted" id="selectSupplierQuestionDiv">
        <spring:message code="acquisitionCreationWizard.label.isRefund"/>
        <br/>
        <br/>

        <form class="form-horizontal" method="POST">
            <input type="radio" name="refund" value="true" hidden="true" checked="checked"/>
            <button type="submit" class="btn btn-primary btn-xlarge">
                <spring:message code="label.yes"/>
            </button>

            <a href="<%= request.getContextPath() %>/expenditure/acquisitons/create/info" class="btn btn-default btn-xlarge">
                <spring:message code="label.no"/>
            </a>
        </form>
    </div>
</div>

<spring:url var="backUrl" value="/expenditure/acquisitons/create" />
<form class="form-horizontal" method="POST">
	<div class="form-group">
    	<label class="control-label col-sm-2" for="description">
        	<spring:message code="acquisitionCreationWizard.label.isRefund"></spring:message>
        </label>
        <div class="col-sm-10">
		    <label class="radio-inline"><input type="radio" name="refund" value="true" required="required"><spring:message code="label.yes" /></label>
			<label class="radio-inline"><input type="radio" name="refund" value="false" required="required"><spring:message code="label.no" /></label>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-2">
         	<a class="btn btn-default" href="${backUrl}">
         		<spring:message code="link.back" text="Back"></spring:message>
         	</a>
        </div>
        <div class="col-sm-10">
           	<button type="submit" class="btn btn-primary">
           		<spring:message code="link.next" text="Next"></spring:message>
           	</button>
        </div>
    </div>
</form>