<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present name="processRequest" property="acquisitionProcess.missionProcess">
	<div class="infobox mtop15">
 		<p class="mvert025">
 			<bean:message key="label.acquisition.process.consult.mission.process.fundAllocations" bundle="ACQUISITION_RESOURCES"/>:
    	</p>
		<ul>
			<logic:iterate id="financer" name="processRequest" property="acquisitionProcess.missionProcess.mission.financer">
				<li>
					<fr:view name="financer" property="unit.presentationName"/>
					<table class="tstyle2 mtop05 mbottom15">
						<tr>
							<th>
								<bean:message key="label.acquisition.process.consult.mission.process.fundAllocations.item" bundle="ACQUISITION_RESOURCES"/>:
							</th>
							<th>
								<bean:message key="label.acquisition.process.consult.mission.process.fundAllocations.value" bundle="ACQUISITION_RESOURCES"/>:
							</th>
							<logic:equal name="financer" property="projectFinancer" value="true">
								<th>
									<bean:message key="label.acquisition.process.consult.mission.process.fundAllocations.projectId" bundle="ACQUISITION_RESOURCES"/>:
								</th>
							</logic:equal>
							<th>
								<bean:message key="label.acquisition.process.consult.mission.process.fundAllocations.id" bundle="ACQUISITION_RESOURCES"/>:
							</th>
						</tr>
						<logic:iterate id="missionItemFinancer" name="financer" property="missionItemFinancers">
							<tr>
								<td>
									<bean:write name="missionItemFinancer" property="missionItem.localizedName"/>: 
									<bean:write name="missionItemFinancer" property="missionItem.itemDescription"/>
								</td>
								<td>
									<fr:view name="missionItemFinancer" property="missionItem.previsionaryCosts"/>
								</td>
								<logic:equal name="financer" property="projectFinancer" value="true">
									<td>
										<bean:write name="missionItemFinancer" property="projectFundAllocationId"/>
									</td>
								</logic:equal>
								<td>
									<bean:write name="missionItemFinancer" property="fundAllocationId"/>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</li>
			</logic:iterate>
		</ul>
	</div>
</logic:present>

<logic:present name="process" property="currentOwner">
	<bean:define id="ownerName" name="process" property="currentOwner.expenditurePerson.firstAndLastName"/>
	<div class="infobox_warning">
		<bean:message key="acquisitionProcess.message.info.currentOwnerIs" bundle="ACQUISITION_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>

<bean:define id="name" name="information" property="activityName"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>

<p class="mtop15 mbottom025"><bean:message key="acquisitionProcess.label.fundAllocation.insert" bundle="ACQUISITION_RESOURCES"/>:</p>

<bean:define id="urlActivity" value='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'/>
<bean:define id="urlView" value='<%= "/workflowProcessManagement.do?method=viewProcess&amp;processId=" + processId%>'/>

<div class="dinline forminline">
	<fr:form action="<%= urlActivity %>">
		<fr:edit  id="activityBean" name="information" visible="false"/>
	
		<fr:edit id="funds"	schema="editFinancerFundAllocationId" name="information" property="beans" >
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle2 mtop05 mbottom15"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%= urlView %>" />
		</fr:edit>
		
		<html:submit styleClass="inputbutton"><bean:message key="button.atribute" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
	</fr:form>

	<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>


