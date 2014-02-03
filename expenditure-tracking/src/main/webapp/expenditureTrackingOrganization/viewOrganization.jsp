<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem.InfoProvider"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="pt.ist.bennu.core.domain.User"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="module.projects.presentationTier.servlet.ProjectsInitializer.ProjectReportsInfoProvider"%>




<h2><bean:message key="title.viewOrganization" bundle="EXPENDITURE_RESOURCES"/></h2>

<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.AQUISITIONS_UNIT_MANAGER">
	<div class="infobox_dotted">
		<ul>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
<%--
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=downloadMGPProjects">
						<bean:message key="label.projects.mgp.download" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
					</html:link>
				</li>
 --%>
			</logic:present>
			<li>
				<html:link action="/expenditureTrackingOrganization.do?method=downloadUnitResponsibles">
					<bean:message key="label.unit.responsibilities.download" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</html:link>
			</li>
		</ul>
	</div>
</logic:present>

<div class="mbottom15">
	<fr:form action="/expenditureTrackingOrganization.do?method=viewOrganization">
	<fr:edit id="unitBean"
			name="unitBean"
			schema="unitBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:edit>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
	</fr:form>
</div>


<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
	<logic:notPresent name="unit">
		<p>
			<html:link action="/expenditureTrackingOrganization.do?method=prepareCreateUnit">
				<bean:message key="unit.link.create" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</p>
	</logic:notPresent>
	<logic:present name="unit">
		<p>
			<html:link action="/expenditureTrackingOrganization.do?method=prepareCreateUnit" paramId="unitOid" paramName="unit" paramProperty="externalId">
				<bean:message key="unit.link.create" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</p>
	</logic:present>
</logic:present>

<style>
<!--
	div.unitbox {
		margin: 10px 0 10px 0;
		background: #fff;
		background: #f5f5f5;
	}
	div.unitbox div {
		margin: 0 10px 0 10px;
		padding: 5px 0px 5px 0px;
	}
-->
</style>

<logic:present name="unit">
	<div class="unitbox col2-1"><div>
		<table>
			<tr style="text-align: left;">
				<th>
					<bean:message bundle="EXPENDITURE_ORGANIZATION_RESOURCES" key="unit.label.type"/>
				</th>
				<td>
					<bean:write name="unit" property="type"/>
				</td>
			</tr>
			<tr style="text-align: left;">
				<th>
					<bean:message bundle="EXPENDITURE_ORGANIZATION_RESOURCES" key="unit.label.name"/>
				</th>
				<td>
					<strong>
						<bean:write name="unit" property="presentationName"/>
					</strong>
				</td>
			</tr>
			<logic:present name="unit" property="parentUnit">
				<tr style="text-align: left;">
					<th>
						<bean:message key="unit.title.superior.unit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
					</th>
					<td>
						<html:link styleClass="secondaryLink" action="/expenditureTrackingOrganization.do?method=viewOrganization" paramId="unitOid" paramName="unit" paramProperty="parentUnit.externalId">
							<bean:write name="unit" property="parentUnit.presentationName"/>
						</html:link>
					</td>
				</tr>
			</logic:present>
			<logic:present name="unit" property="accountManager">
				<tr style="text-align: left;">
					<th>
						<bean:message bundle="EXPENDITURE_ORGANIZATION_RESOURCES" key="unit.label.account.manager.name"/>
						&nbsp;&nbsp;&nbsp;
					</th>
					<td>
						<html:link styleClass="secondaryLink" action="/expenditureTrackingOrganization.do?method=viewPerson" paramId="personOid" paramName="unit" paramProperty="accountManager.externalId">
							<bean:write name="unit" property="accountManager.name"/>
						</html:link>
					</td>
				</tr>
			</logic:present>
		</table>
	</div></div>
				<div class="unitbox col2-2"><div>
					 <%
 						ExpenditureTrackingSystem.InfoProvider infoProvider = ExpenditureTrackingSystem.getInfoProvider();
 						System.out.println(infoProvider);
 						if(infoProvider != null){
 	   						final Unit unit = (Unit) request.getAttribute("unit");
 	   						Map<String, String> links = infoProvider.getLinks("viewOrganization.jsp", unit);
 	   						if(links != null){      
		  						%>
 	      						<h4><%=infoProvider.getTitle() %></h4>
 	      						<ul>
 	      						<%
 	      						for(Map.Entry<String, String> entry : links.entrySet()){
 	         						String linkTitle = entry.getKey();
 	         						String link = entry.getValue();
 	        						%>
 	        						<li>
 	        							<html:link page="<%=link%>">
											<%=linkTitle%>
										</html:link>
									</li>
 	        						<%     
 	      						}
 	      						%>
 	      						</ul>
 	      						<%
 	   						}
 						}
					%>
				</div></div>

	<p class="mtop05">
		<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
			<html:link action="/expenditureTrackingOrganization.do?method=editUnit" paramId="unitOid" paramName="unit" paramProperty="externalId">
				<bean:message key="link.edit" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
			| 
			<html:link action="/expenditureTrackingOrganization.do?method=deleteUnit" paramId="unitOid" paramName="unit" paramProperty="externalId">
				<bean:message key="link.delete" bundle="EXPENDITURE_RESOURCES"/>
			</html:link>
		</logic:present>
		<logic:equal name="unit" property="currentUserResponsibleForUnit" value="true">
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
			| 
			</logic:present>
			<html:link action="/expenditureTrackingOrganization.do?method=manageObservers" paramId="unitOid" paramName="unit" paramProperty="externalId">
				<bean:message key="label.observers" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/> ( <fr:view name="unit" property="observersCount"/>)
			</html:link>
		</logic:equal>
	</p>

	<logic:equal name="unit" property="defaultRegeimIsCCP" value="true">
		<bean:message key="label.unit.default.regeim.is.ccp" bundle="EXPENDITURE_RESOURCES"/>
	</logic:equal>
	<logic:equal name="unit" property="defaultRegeimIsCCP" value="false">
		<bean:message key="label.unit.default.regeim.is.not.ccp" bundle="EXPENDITURE_RESOURCES"/>
	</logic:equal>
	<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.AQUISITIONS_UNIT_MANAGER">
		<html:link action="/expenditureTrackingOrganization.do?method=changeDefaultRegeimIsCCP" paramId="unitOid" paramName="unit" paramProperty="externalId">
			<bean:message key="label.unit.default.regeim.toggle" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
	</logic:present>

	<logic:notEmpty name="unit" property="authorizations">
		<%
 						ExpenditureTrackingSystem.InfoProvider genericInfoProvider = ExpenditureTrackingSystem.getInfoProvider();
 						if(genericInfoProvider != null && genericInfoProvider instanceof ProjectReportsInfoProvider){
 	   						final Unit unit = (Unit) request.getAttribute("unit");
 	   						if(unit.isProject()) {
	 	   						if(!unit.isActive()){ %>
	 	   							<h3 class="mtop15 mbottom05"><bean:message key="label.summaryNotAvailable" bundle="EXPENDITURE_RESOURCES"/></h3>
	 	   						<% } else {
	 	   						ProjectReportsInfoProvider projectInfoProvider = (ProjectReportsInfoProvider) genericInfoProvider;
	 	   						List<List<String>> summary;
	 	   						summary = projectInfoProvider.getSummary("viewOrganization.jsp", unit);
	 	   						int i;
	 	   						if(summary != null || summary.get(0).size()>0){%>
									<h3 class="mtop15 mbottom05"><bean:message key="label.summary" bundle="EXPENDITURE_RESOURCES"/></h3>
	 	   						    <table class="tstyle2 mtop05">
										<tr>
											<%for(i = 0; i<summary.get(0).size(); i++){
		 	         							String title = summary.get(0).get(i);
		 	        							%>
	 	        								<th>
	 	        									<%=title%>
	 	        								</th>
	 	        							<% } %>
										</tr>
										<tr>
		 	      						<% for(i = 0; i<summary.get(0).size(); i++){
		 	         							String value = summary.get(1).get(i);
		 	        							%>
	 	        								<td>
	 	        									<%=value%>
	 	        								</td>
	 	        						<%     
	 	      						} %>
	 	      						</tr>
								</table>
	 	      				<%	}
	 							}
	 						}
	 					}
					%>
	</logic:notEmpty>

	<h3 class="mtop15 mbottom05"><bean:message key="authorizations.label.responsibles" bundle="EXPENDITURE_RESOURCES"/></h3>
	<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.AQUISITIONS_UNIT_MANAGER">
		<html:link action="/expenditureTrackingOrganization.do?method=prepareCreateAuthorizationUnitWithoutPerson" paramId="unitOid" paramName="unit" paramProperty="externalId">
			<bean:message key="label.add.authorization" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
	</logic:present>
	<html:link action="/expenditureTrackingOrganization.do?method=viewAuthorizationLogs" paramId="unitOid" paramName="unit" paramProperty="externalId">
		<bean:message key="authorizations.link.logs" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
	<logic:notEmpty name="unit" property="authorizations">
		<table class="tstyle2 mtop05">
			<tr>
				<th>
					<bean:message bundle="EXPENDITURE_ORGANIZATION_RESOURCES" key="person.label.name"/>
				</th>
				<th>
					<bean:message bundle="EXPENDITURE_RESOURCES" key="label.unit"/>
				</th>
				<th>
					<bean:message bundle="EXPENDITURE_RESOURCES" key="authorizations.label.startDate"/>
				</th>
				<th>
					<bean:message bundle="EXPENDITURE_RESOURCES" key="authorizations.label.endDate"/>
				</th>
				<th>
					<bean:message bundle="EXPENDITURE_RESOURCES" key="authorizations.label.canDelegate"/>
				</th>
				<th>
					<bean:message bundle="EXPENDITURE_RESOURCES" key="authorizations.label.maxAmount"/>
				</th>
				<th>
				</th>
			</tr>
			<logic:iterate id="authorization" name="unit" property="authorizations">
				<tr>
					<td>
						<html:link styleClass="secondaryLink" action="/expenditureTrackingOrganization.do?method=viewPerson" paramId="personOid" paramName="authorization" paramProperty="person.externalId">
							<fr:view name="authorization" property="person.name"/>
						</html:link>
					</td>
					<td>	
						<fr:view name="authorization" property="unit.name"/>
					</td>
					<td>
						<logic:present name="authorization" property="startDate">
							<fr:view name="authorization" property="startDate"/>
						</logic:present>
						<logic:notPresent name="authorization" property="startDate">
							-
						</logic:notPresent>
					</td>
					<td>
						<logic:present name="authorization" property="endDate">
							<fr:view name="authorization" property="endDate"/>
						</logic:present>
						<logic:notPresent name="authorization" property="endDate">
							-
						</logic:notPresent>
					</td>
					<td>
						<fr:view name="authorization" property="canDelegate"/>
					</td>
					<td>
						<fr:view name="authorization" property="maxAmount"/>
					</td>
					<td>
						<html:link action="/expenditureTrackingOrganization.do?method=viewAuthorization" paramId="authorizationOid" paramName="authorization" paramProperty="externalId">
							<bean:message key="label.view" bundle="EXPENDITURE_RESOURCES"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
	<logic:empty name="unit" property="authorizations">
		<p><em><bean:message key="authorizations.label.noResponsiblesDefinedForUnit" bundle="EXPENDITURE_RESOURCES"/>.</em></p>
	</logic:empty>

	<h3 class="mtop15 mbottom05"><bean:message key="label.observers" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
	<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.AQUISITIONS_UNIT_MANAGER">
		<html:link action="/expenditureTrackingOrganization.do?method=manageObservers" paramId="unitOid" paramName="unit" paramProperty="externalId">
			<bean:message key="label.observers.add" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/> ( <fr:view name="unit" property="observersCount"/>)
		</html:link>
	</logic:present>
	<logic:notEmpty name="unit" property="observers">
		<ul>
			<logic:iterate name="unit" property="observers" id="person">
				<li>
					<html:link page="/expenditureTrackingOrganization.do?&method=viewPerson" paramName="person" paramId="personOid" paramProperty="externalId">
						<fr:view name="person" property="name"/>
					</html:link>
				</li>
			</logic:iterate>
		</ul>
	</logic:notEmpty>
	<logic:empty name="unit" property="observers">
		<p><em>
			<bean:message key="label.noAssociatedPeople" bundle="EXPENDITURE_RESOURCES"/>
		</em></p>
	</logic:empty>
</logic:present>

<logic:present name="units">
	<logic:notEmpty name="units">
		<logic:present name="unit">
			<h3 class="mtop15 mbottom05"><bean:message key="unit.title.subunit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
		</logic:present>
		<fr:view name="units" schema="unitList">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 mtop05"/>
				<fr:property name="columnClasses" value=",,aleft,"/>
				<fr:property name="sortBy" value="name=asc"/>
				<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewOrganization"/>
				<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="param(view)" value="externalId/unitOid"/>
				<fr:property name="order(view)" value="1"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>

<h3 class="mtop15 mbottom05"><bean:message key="accountingUnit.title" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/></h3>
<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
	<p class="mtop05">
		<html:link action="/expenditureTrackingOrganization.do?method=prepareCreateAccountingUnit">
			<bean:message key="unit.link.create.accounting.unit" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
		</html:link>
	</p>
</logic:present>

<logic:present name="accountingUnits">
	<logic:empty name="accountingUnits">
		<bean:message key="accountingUnit.message.none.defined" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
	</logic:empty>
	<logic:notEmpty name="accountingUnits">
		<fr:view name="accountingUnits" schema="accountingUnits">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 mtop05"/>
				<fr:property name="columnClasses" value="aleft,,,,,"/>
				<fr:property name="sortBy" value="name=asc"/>
				<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewAccountingUnit"/>
				<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="param(view)" value="externalId/accountingUnitOid"/>
				<fr:property name="order(view)" value="1"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
