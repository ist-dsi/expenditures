<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<jsp:include page="summary.jsp"/>

<jsp:include page="viewAssociatedMissionProcesses.jsp"/>

<jsp:include page="viewPaymentProcesses.jsp"/>

<jsp:include page="processMessages.jsp"/>

<jsp:include page="processStageView.jsp"/>

<jsp:include page="listFinancers.jsp"/>

<jsp:include page="listParticipants.jsp"/>

<jsp:include page="listMissionItems.jsp"/>
