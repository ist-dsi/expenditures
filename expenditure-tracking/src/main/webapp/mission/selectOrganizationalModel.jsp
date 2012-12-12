<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/collection-pager" prefix="cp"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/chart" prefix="chart" %>

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
