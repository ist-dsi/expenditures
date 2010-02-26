<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="workingCapital" name="process" property="workingCapital"/>

<h2>
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital"/> - <bean:write name="workingCapital" property="unit.presentationName"/> - <bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.year"/>
	<bean:write name="workingCapital" property="workingCapitalYear.year"/> 
</h2>

