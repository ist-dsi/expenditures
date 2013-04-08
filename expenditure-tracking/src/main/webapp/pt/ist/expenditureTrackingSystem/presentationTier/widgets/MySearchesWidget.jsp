<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<logic:notEmpty name="person" property="saveSearches">
	<table style="width: 100%;">
		<logic:iterate id="search" name="person" property="saveSearches">
			<bean:define id="OID" name="search" property="externalId" type="java.lang.String"/>
			<tr>
				<td>
				<html:link page="<%= "/search.do?method=viewSearch&searchOID=" + OID %>">
					<fr:view name="search" property="searchName" />
				</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
<logic:empty name="person" property="saveSearches">
	<p><em><bean:message key="label.no.savedSearches" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
</logic:empty>
