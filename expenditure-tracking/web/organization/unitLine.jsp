<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="unitToDisplay">
	<html:link action="/organization.do?method=viewOrganization" paramId="unitOid" paramName="unitToDisplay" paramProperty="OID">
		<bean:write name="unitToDisplay" property="name"/>
	</html:link>
	<logic:equal name="unitToDisplay" property="class.name" value="pt.ist.expenditureTrackingSystem.domain.organization.CostCenter">
		<logic:notEmpty name="unitToDisplay" property="costCenter">
			(cc <bean:write name="unitToDisplay" property="costCenter"/> )
		</logic:notEmpty>
	</logic:equal>
	<logic:equal name="unitToDisplay" property="class.name" value="pt.ist.expenditureTrackingSystem.domain.organization.Project">
		<logic:notEmpty name="unitToDisplay" property="projectCode">
			(p <bean:write name="unitToDisplay" property="projectCode"/> )
		</logic:notEmpty>
	</logic:equal>
	<logic:equal name="unitToDisplay" property="class.name" value="pt.ist.expenditureTrackingSystem.domain.organization.SubProject">
		<logic:notEmpty name="unitToDisplay" property="projectCode">
			(sp)
		</logic:notEmpty>
	</logic:equal>
</logic:present>
