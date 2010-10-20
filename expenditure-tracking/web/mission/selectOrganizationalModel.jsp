<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>
<%@ taglib uri="/WEB-INF/chart.tld" prefix="chart" %>

<h2><bean:message key="label.mission.missionConfiguration.select.organizationalModel" bundle="MISSION_RESOURCES"/></h2>

<logic:empty name="organizationalModels">
	<bean:message key="label.models.none" bundle="ORGANIZATION_RESOURCES"/>
</logic:empty>

<logic:notEmpty name="organizationalModels">
	<chart:orgChart id="organizationalModel" name="organizationalModelChart" type="java.lang.Object">
		<div class="orgTBox orgTBoxLight">
			<html:link action="/configureMissions.do?method=selectOrganizationalModel" paramId="organizationalModelOid" paramName="organizationalModel" paramProperty="externalId">
				<bean:write name="organizationalModel" property="name.content"/>
			</html:link>
		</div>
	</chart:orgChart>
</logic:notEmpty>
