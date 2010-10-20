<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="pt.ist.fenixWebFramework.renderers.utils.RenderUtils"%>

<bean:define id="process" name="information" property="process"/>
<bean:define id="mission" name="process" property="mission"/>
<bean:define id="fundAllocations" name="mission" property="allFundAllocations"/>

<logic:notEmpty name="fundAllocations">
	<div class="infobox mtop15">
 		<p class="mvert025">
 			<bean:message key="label.mission.process.fundAllocations.previous" bundle="MISSION_RESOURCES"/>:
    	</p>
		<ul>
			<logic:iterate id="fundAllocation" name="fundAllocations">
				<li>
					<bean:write name="fundAllocation"/>
				</li>
			</logic:iterate>
		</ul>
	</div>
</logic:notEmpty>
