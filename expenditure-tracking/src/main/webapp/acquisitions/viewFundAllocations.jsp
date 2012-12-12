<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<h2><bean:message key="title.checkFundAllocations" bundle="EXPENDITURE_RESOURCES"/></h2>

<fr:edit id="dateSelection"  name="bean" schema="dateIntervalBean.dateSelection" action="/acquisitionProcess.do?method=checkFundAllocations">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
	</fr:layout>
</fr:edit>

<logic:notEmpty name="processes">
	
	<bean:size id="listSize" name="processes"/>
	
	<p class="mtop15 mbottom05"><em><bean:message key="label.numberOfFoundProcesses" bundle="ACQUISITION_RESOURCES" arg0="<%= listSize.toString() %>"/>.</em></p>
	
	<fr:view name="processes" schema="list.processes">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop05"/>		
		</fr:layout>
	</fr:view>
</logic:notEmpty>
