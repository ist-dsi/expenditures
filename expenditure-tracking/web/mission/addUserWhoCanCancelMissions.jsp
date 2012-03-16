<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<h2><bean:message key="label.mission.missionConfiguration.users.who.can.cancelMissions.addUser" bundle="MISSION_RESOURCES"/></h2>

<form action="<%= request.getContextPath() + "/configureMissions.do" %>" method="post">
	<html:hidden property="method" value="addUserWhoCanCancelMissions"/>
	<input name="username" type="text"/>
</form>