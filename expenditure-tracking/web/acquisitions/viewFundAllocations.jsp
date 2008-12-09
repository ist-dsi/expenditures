<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>


<h2><bean:message key="title.checkFundAllocations" bundle="EXPENDITURE_RESOURCES"/></h2>
<fr:edit id="dateSelection"  name="bean" schema="dateIntervalBean.dateSelection" action="/acquisitionProcess.do?method=checkFundAllocations">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form tstyle5"/>
	</fr:layout>
</fr:edit>

<logic:notEmpty name="processes">
	<fr:view name="processes" schema="list.processes">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>		
		</fr:layout>
	</fr:view>
</logic:notEmpty>