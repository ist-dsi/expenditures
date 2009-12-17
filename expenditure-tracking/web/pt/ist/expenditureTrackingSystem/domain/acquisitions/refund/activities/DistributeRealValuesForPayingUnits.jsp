<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<h2><bean:message key="acquisitionRequestItem.title.editRealValues" bundle="ACQUISITION_RESOURCES"/></h2>


<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="processClass" name="process" property="class.simpleName"/>
<bean:define id="outOfLabel">
	<bean:message key="acquisitionRequestItem.label.outOf" bundle="ACQUISITION_RESOURCES"/>
</bean:define>
<bean:define id="name" name="information" property="activityName"/>

<jsp:include page="/commons/defaultErrorDisplay.jsp"/>

<logic:equal name="information" property="item.filledWithRealValues" value="false">
	<div class="infobox_warning">
		<strong><bean:message key="messages.info.attention" bundle="EXPENDITURE_RESOURCES"/></strong>: <bean:message key="acquisitionRequestItem.message.warn.mustDefineRealValuesFirst" bundle="ACQUISITION_RESOURCES"/>
	</div>
	<html:link page='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>« <bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/></html:link>
</logic:equal>
			
<logic:equal name="information" property="item.filledWithRealValues" value="true">
	
<bean:define id="maxValue" name="information" property="item.realValue.roundedValue"/>
<jsp:include page="/commons/defaultErrorDisplay.jsp"/>

<div class="dinline forminline">	
	
	<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>
	
		<fr:edit id="activityBean" name="information" visible="false"/>
		
		<table class="tstyle3 inputaright" id="assign">	
			<tr>
				<th></th>
				<th><bean:message key="acquisitionProcess.label.payingUnit" bundle="ACQUISITION_RESOURCES"/></th>
				<th><bean:message key="acquisitionRequestItem.label.effectiveValue" bundle="ACQUISITION_RESOURCES"/></th>
				<th><bean:message key="acquisitionRequestItem.label.estimatedValue" bundle="ACQUISITION_RESOURCES"/></th>
			</tr>
			<logic:iterate id="bean" name="information" property="beans" indexId="id">
					<tr  id='<%= "tr" + id %>'">
					<td><input type="checkbox" checked="true" disabled="disabled"/></td>
					<td><fr:view name="bean" property="unit.presentationName"/></td>
					<td class="aright"><fr:view name="bean" property="shareValue"/></td>
					<td><fr:edit name="bean" slot="realShareValue"/></td>
				</tr>
			</logic:iterate>
				<tr>
					<td colspan="3" class="aright">
						<strong><bean:message key="label.total" bundle="EXPENDITURE_RESOURCES"/></strong>
					</td>
					<td class="aright">
						<span id="sum">
						</span> 
					</td>
					<td>
					</td>
				</tr>
		</table>
	
		<html:submit styleClass="inputbutton"><bean:message key="button.atribute" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
	
	</fr:form>

	<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>

	<script type="text/javascript">

			function getShares(maxValue,outOfLabel) {
				var maxValueFloat = parseFloat(maxValue);
				var activeRows = "";
				jQuery.each($("#assign input[type='checkbox']:checked"), function() {
					var value = $(this).parent("td").parent("tr").attr("id")
					if (activeRows.length > 0) {
						activeRows += ",";
					}
					activeRows += value;
				});
				
				var url = '<%= request.getContextPath() +  "/acquisition" + processClass + ".do?method=calculateShareValuesViaAjax" %>';
				if (activeRows.length > 0) {
				$.getJSON(url,
					 	    { money: maxValueFloat, requestors: activeRows },
					    	function(data) { 
						    	size = data.length;
						    	for (i = 0; i < size; i++) {
									var trId = data[i]['id'];
									var value = data[i]['share'].replace(".",",");
									$("#" + trId + " td:last").children("input").attr("value",value);
									writeSum(maxValue,outOfLabel);
							    }
					 	    });
				}else {
					writeSum(maxValue,outOfLabel);
				}
					 			
			}
			
			function writeSum(maxValue, outOfLabel) {
				var sum = parseFloat("0");	
				var maxValueFloat = parseFloat(maxValue);
				
				jQuery.each($("#assign input[type='checkbox']:checked"), function() {
					var value = $(this).parent("td").siblings("td:last").children("input").val()
					sum += parseFloat(value.replace('.','').replace(',','.'));
				});

				sum = Math.round(sum*100)/100;
				if (sum > maxValueFloat) {
					sumValue = "<span class=\"invalid\">" + sum + "</span>";
				}
				else if (sum == maxValueFloat) {
					sumValue = "<span class=\"valid\">" + sum + "</span>";
				}
				else {
					sumValue = sum + "";
				}

				$("#sum").empty();
				$("#sum").append(sumValue.replace('.',',') + ' (' + outOfLabel + ' ' + maxValue.replace('.',',') + ')');
			}

			$("#assign input[type='checkbox']").click(function() {
					if(!$(this).attr('checked')) {
						$(this).parent("td").siblings("td:last").children("input").attr('value','0');
					}
					<%= "getShares('" + maxValue + "', '" + outOfLabel + "');" %>
			});

			
			$("input[type=text]").keyup(function() {
				<%= "writeSum('" + maxValue + "', '" + outOfLabel + "');" %>
			});

			<%= "writeSum('" + maxValue + "', '" + outOfLabel + "');" %>
		</script>
</logic:equal>