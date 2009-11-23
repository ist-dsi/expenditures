<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
	<bean:message key="title.comments" bundle="EXPENDITURE_RESOURCES"/>
</h2>

<bean:define id="processClass" name="process" property="class.simpleName" type="java.lang.String"/>
<bean:define id="actionMapping" value='<%="/acquisition" + processClass %>'/>

<p>
	<html:link page='<%= actionMapping + ".do?method=viewProcess"%>' paramId="processOid" paramName="process" paramProperty="externalId">
		« <bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
</p>

<bean:define id="processRequest" name="process" property="request" toScope="request"/>
<bean:define id="requestClass" name="processRequest" property="class.simpleName"/>
<jsp:include page='<%=  "commons/view" + requestClass + ".jsp" %>' flush="true"/>

<logic:empty name="comments">
	<p class="mtop15"><em>Não existem comentários.</em></p>
</logic:empty>


<logic:notEmpty name="comments">
	<div class="mvert2">
	<logic:iterate id="comment" name="comments">
		<div class="comment">
			<p>
				<span><fr:view name="comment" property="commenter.expenditurePerson.name"/></span> <fr:view name="comment" property="date"/>
			</p>
			<div class="body"><fr:view name="comment" property="comment" layout="null-as-label" type="java.lang.String"/></div>
		</div>
	</logic:iterate>
	</div>
</logic:notEmpty>

<bean:define id="processOid" name="process" property="externalId" type="java.lang.String"/>


<fr:form action='<%= actionMapping + ".do?method=addComment&processOid=" + processOid%>'>
	 
	<fr:edit id="comment" name="bean" visible="false"/>

	<table class="form">
		<tr>
			<td>
				<bean:message key="label.addComment" bundle="EXPENDITURE_RESOURCES"/>:
			</td>
			<td>
				<fr:edit id="comment-text" name="bean" slot="comment" type="java.lang.String" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:layout name="longText">
						<fr:property name="rows" value="6"/>
						<fr:property name="columns" value="60"/>
						<fr:property name="classes" value="form"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
			<tr>
			<td>
				<bean:message key="label.notifyPeopleByEmail" bundle="EXPENDITURE_RESOURCES"/>:
			</td>
			<td>
				<fr:edit id="peopleToNotify" name="bean" slot="peopleToNotify">
					<fr:layout name="option-select">
						<fr:property name="providerClass" value="module.workflow.presentationTier.renderers.providers.CommentersForProcess"/>
						<fr:property name="eachLayout" value="values"/>
						<fr:property name="eachSchema" value="viewUsersInList"/> 
						<fr:property name="saveOptions" value="true"/>
						<fr:property name="selectAllShown" value="true"/>
						<fr:property name="classes" value="nobullet"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
	</table>
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.add.name" bundle="RENDERER_RESOURCES"/> </html:submit>
</fr:form>
