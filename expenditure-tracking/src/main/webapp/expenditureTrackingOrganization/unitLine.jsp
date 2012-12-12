<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present name="unitToDisplay">
	<html:link action="/expenditureTrackingOrganization.do?method=viewOrganization" paramId="unitOid" paramName="unitToDisplay" paramProperty="externalId">
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
