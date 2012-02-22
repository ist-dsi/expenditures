<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="name" name="information" property="activityName"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>

<bean:define id="urlActivity" value='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'/>
<bean:define id="urlView" value='<%= "/workflowProcessManagement.do?method=viewProcess&amp;processId=" + processId%>'/>

<div class="dinline forminline">
	<fr:form action="<%= urlActivity %>">
		<fr:edit id="activityBean" name="information" visible="false"/>

		<table class="tstyle2 mtop05 mbottom15">
			<tr>
				<th>
					<bean:message key="label.unit" bundle="EXPENDITURE_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.commitmentNumber" bundle="EXPENDITURE_RESOURCES"/>
				</th>
			</tr>
			<logic:iterate id="commitmentNumberBean" name="information" property="commitmentNumberBeans" scope="request">
				<tr>
					<td>
						<bean:write name="commitmentNumberBean" property="financer.unit.presentationName"/>
					</td>
					<td>
						<bean:define id="commitmentNumberBeanId">commitmentNumberBean<bean:write name="commitmentNumberBean" property="financer.externalId"/></bean:define>
						<fr:edit name="commitmentNumberBean" slot="commitmentNumber">
					 		<fr:layout>
								<fr:property name="size" value="25"/>
							</fr:layout>
				 		</fr:edit>
					</td>
				</tr>
			</logic:iterate>
		</table>

		<html:submit styleClass="inputbutton"><bean:message key="button.submit" bundle="EXPENDITURE_RESOURCES"/></html:submit>
	</fr:form>

	<fr:form action='<%= urlView %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>
