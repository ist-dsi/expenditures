<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/collection-pager" prefix="cp"%>

<h2><bean:message key="label.mission.missionConfiguration.users.who.can.cancelMissions.addUser" bundle="MISSION_RESOURCES"/></h2>

<form action="<%= request.getContextPath() + "/configureMissions.do" %>" method="post">
	<html:hidden property="method" value="addUserWhoCanCancelMissions"/>
	<input name="username" type="text"/>
</form>
