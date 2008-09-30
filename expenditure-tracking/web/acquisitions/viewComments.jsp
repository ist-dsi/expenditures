<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
	<bean:message key="title.comments" bundle="EXPENDITURE_RESOURCES"/>
</h2>

<style>
div.comment {
margin-bottom: 1.5em;
}
div.comment p {
margin-bottom: 0 !important;
}
div.comment p span {
margin-bottom: 0 !important;
font-weight: bold;
}
div.comment div {
margin-top: 0;
}
</style>

<p>
	<html:link page="/acquisitionProcess.do?method=viewAcquisitionProcess" paramId="acquisitionProcessOid" paramName="acquisitionProcess" paramProperty="OID">
		« <bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
</p>

<logic:iterate id="comment" name="comments">
	<div class="comment">
	<p>
		<span><fr:view name="comment" property="commenter.name"/></span> <fr:view name="comment" property="date"/>
	</p>
	<div><fr:view name="comment" property="comment"/></div>
	</div>
</logic:iterate>

<bean:define id="acquisitionProcessOid" name="acquisitionProcess" property="OID"/>

<p>

<fr:form action="<%= "/acquisitionProcess.do?method=addComment&acquisitionProcessOid=" + acquisitionProcessOid%>">

	<table class="form">
	<tr>
	<td>
		<bean:message key="label.addComment" bundle="EXPENDITURE_RESOURCES"/>:
	</td>
	<td>
	<fr:edit id="comment" name="bean" slot="string" type="java.lang.String" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
		<fr:layout name="longText">
			<fr:property name="rows" value="5"/>
				<fr:property name="columns" value="40"/>
			<fr:property name="classes" value="form"/>
		</fr:layout>
	</fr:edit>
	</td>
	</tr>
	</table>
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
</fr:form>
</p>