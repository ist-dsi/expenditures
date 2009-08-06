<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<div id="widgetMySearches" class="portlet">
		<div class="portlet-header"><bean:message key="process.title.mySearchs" bundle="EXPENDITURE_RESOURCES"/></div>
		<div class="portlet-content">
		
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
		</div>
	</div>
	