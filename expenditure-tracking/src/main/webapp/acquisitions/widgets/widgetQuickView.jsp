<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<div id="widgetQuickView" class="portlet">
	<div class="portlet-header"><bean:message key="title.quickAccess" bundle="EXPENDITURE_RESOURCES" /></div>

	<div class="portlet-content">
		<fr:form action="/dashBoard.do?method=quickAccess">
			<table class="quicksearch">
				<tr>
					<td>
						<fr:edit id="quickAccess" name="searchBean" schema="search.quick">
							<fr:layout>
								<fr:property name="classes" value="thnowrap"/>
								<fr:property name="columnClasses" value=",,dnone"/>
							</fr:layout>
						</fr:edit>
					</td>
					<td>
						<html:submit><bean:message key="link.view" bundle="EXPENDITURE_RESOURCES"/></html:submit>
					</td>
				</tr>
			</table>
		</fr:form>
		<logic:present name="widgetQuickView.messages">
			<bean:define id="label" name="widgetQuickView.messages" type="java.lang.String"/>
			<p class="mtop0"><em><bean:message key="<%= label %>" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
		</logic:present>
	</div>
</div>
