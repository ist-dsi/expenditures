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
	<h2>Criar novo Processo de Aquisição/Reembolso</h2>
</div>

<p class="mvert05">
	Trata-se de um reembolso no contexto de uma missão?
	<form method="POST">
        <button type="submit" name="refund" value="1">Sim</button>
		<button type="submit" name="refund" value="0">Não</button>
	</form>
</p>