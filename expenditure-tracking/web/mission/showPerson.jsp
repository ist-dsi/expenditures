<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<div class="infobox">
	<table style="width: 100%;">
		<tr>
			<td style="vertical-align: top;">
				<fr:view name="person">
					<fr:schema type="module.organization.domain.Person" bundle="MISSION_RESOURCES">
				    	<fr:slot name="name" key="label.organization.name"/>
				    	<fr:slot name="user.username" key="label.organization.username"/>
						<fr:slot name="user.expenditurePerson.email" key="label.organization.email"/>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
						<fr:property name="columnClasses" value=",,tderror"/>
					</fr:layout>
				</fr:view>
			</td>
			<td style="text-align: right;">
				<html:img src="https://fenix.ist.utl.pt/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage"
					paramId="uuid" paramName="person" paramProperty="user.username"
					align="middle" styleClass="float: right; border: 1px solid #aaa; padding: 3px;" />
			</td>
		</tr>
	</table>
</div>

<jsp:include page="showPersonWorkingPlaceInformation.jsp"/>

<jsp:include page="showPersonMissionResponsibilities.jsp"/>
