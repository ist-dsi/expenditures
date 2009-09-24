<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/chart.tld" prefix="chart" %>

<h2>
	<bean:message key="link.topBar.connectUnits" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
</h2>

<bean:define id="partyChart" name="partyChart" type="module.organization.presentationTier.actions.OrganizationModelAction.PartyChart"/>
<bean:define id="unitChart" name="unitChart" type="pt.ist.expenditureTrackingSystem.presentationTier.actions.organization.ConnectUnitsAction.UnitChart"/>

<table width="100%" align="center" class="table">
	<tr>
		<th>
			Estrutura Organizacional
		</th>
		<th>
		</th>
		<th>
			Estrutura de Aquisições
		</th>
	</tr>
	<tr>
		<td align="center">
			<logic:present name="partyChart" property="element">
				<bean:define id="party" name="partyChart" property="element"/>
				<bean:write name="party" property="partyName"/>
				(<bean:write name="party" property="acronym"/>)
				<br/>
				<logic:iterate indexId="i" id="partyType" name="party" property="partyTypes">
					<logic:notEqual name="i" value="0">
						<br/>
					</logic:notEqual>
					<bean:write name="partyType" property="type"/>
					-
					<bean:write name="partyType" property="name.content"/>
				</logic:iterate>
			</logic:present>
		</td>
		<td align="center">
			<logic:present name="partyChart" property="element">
				<logic:present name="unitChart" property="element">
					<bean:define id="party" name="partyChart" property="element" type="module.organization.domain.Unit"/>
					<bean:define id="unit" name="unitChart" property="element" type="pt.ist.expenditureTrackingSystem.domain.organization.Unit"/>

					<% if (party.getExpenditureUnit() == null && unit.getUnit() == null) { %>
						<bean:define id="urlConnect">/connectUnits.do?method=connect&amp;partyId=<bean:write name="party" property="externalId"/>&amp;unitId=<bean:write name="unit" property="externalId"/></bean:define>
						<html:link action="<%= urlConnect %>">
							Ligar?
						</html:link>
					<% } else if (unit.getUnit() == party) { %>
						Connected
						<bean:define id="urlDisconnect">/connectUnits.do?method=disconnect&amp;partyId=<bean:write name="party" property="externalId"/>&amp;unitId=<bean:write name="unit" property="externalId"/></bean:define>
						<html:link action="<%= urlDisconnect %>">
							desligar
						</html:link>
					<% } else {%>
						<% if (party.getExpenditureUnit() != null) { %>
							--> <bean:write name="party" property="expenditureUnit.presentationName"/>
							<br/>
							<bean:define id="urlDisconnectParty">/connectUnits.do?method=disconnectParty&amp;partyId=<bean:write name="party" property="externalId"/>&amp;unitId=<bean:write name="unit" property="externalId"/></bean:define>
							<html:link action="<%= urlDisconnectParty %>">
								desligar
							</html:link>
							<br/>
						<% }%>
						<% if (unit.getUnit() != null) { %>
							<-- <bean:write name="unit" property="unit.presentationName"/>
							<br/>
							<bean:define id="urlDisconnectUnit">/connectUnits.do?method=disconnectUnit&amp;partyId=<bean:write name="party" property="externalId"/>&amp;unitId=<bean:write name="unit" property="externalId"/></bean:define>
							<html:link action="<%= urlDisconnectUnit %>">
								desligar
							</html:link>
							<br/>
						<% }%>
					<% }%>
				</logic:present>
			</logic:present>
		</td>
		<td align="center">
			<logic:present name="unitChart" property="element">
				<bean:define id="unit" name="unitChart" property="element"/>
				<bean:write name="unit" property="presentationName"/>
				<br/>
				<bean:write name="unit" property="class.name"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td width="47%" valign="top" align="center">
			<chart:orgChart id="party" name="partyChart" type="java.lang.Object">
				<div class="orgTBox orgTBoxLight">
					<bean:define id="urlSelectParty">/connectUnits.do?method=showUnits<logic:present name="unitChart" property="element">&amp;unitId=<bean:write name="unitChart" property="element.externalId"/></logic:present></bean:define>
					<html:link action="<%= urlSelectParty %>" paramId="partyId" paramName="party" paramProperty="externalId">
						<bean:write name="party" property="presentationName"/>
					</html:link>
				</div>
			</chart:orgChart>

			<logic:present name="possibleMatches">
				<br/>
				<logic:iterate id="possibleMatch" name="possibleMatches">
					<bean:define id="urlSelectParty">/connectUnits.do?method=showUnits<logic:present name="unitChart" property="element">&amp;unitId=<bean:write name="unitChart" property="element.externalId"/></logic:present></bean:define>
					<html:link action="<%= urlSelectParty %>" paramId="partyId" paramName="possibleMatch" paramProperty="externalId">
						<bean:write name="possibleMatch" property="presentationName"/>
					</html:link>
					<br/>
				</logic:iterate>
			</logic:present>
			<logic:present name="lessLikelyPossibleMatches">
				<br/>
				<logic:iterate id="possibleMatch" name="lessLikelyPossibleMatches">
					<bean:define id="urlSelectParty">/connectUnits.do?method=showUnits<logic:present name="unitChart" property="element">&amp;unitId=<bean:write name="unitChart" property="element.externalId"/></logic:present></bean:define>
					<html:link action="<%= urlSelectParty %>" paramId="partyId" paramName="possibleMatch" paramProperty="externalId">
						<bean:write name="possibleMatch" property="presentationName"/>
					</html:link>
					<br/>
				</logic:iterate>
			</logic:present>
		</td>
		<td width="6%">
		</td>
		<td width="47%"  valign="top" align="center">
			<chart:orgChart id="unit" name="unitChart" type="java.lang.Object">
				<div class="orgTBox orgTBoxLight">
					<bean:define id="urlSelectUnit">/connectUnits.do?method=showUnits<logic:present name="partyChart" property="element">&amp;partyId=<bean:write name="partyChart" property="element.externalId"/></logic:present></bean:define>
					<html:link action="<%= urlSelectUnit %>" paramId="unitId" paramName="unit" paramProperty="externalId">
						<bean:write name="unit" property="presentationName"/>
					</html:link>
				</div>
			</chart:orgChart> 
		</td>
	</tr>
</table>

<br/>
<bean:message key="label.message.acquisition.units.without.units" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
<br/>
<%
	int count = 0;
	int totalUnmatchedUnits = 0;
	int totalUnmatchedCostCenters = 0;
	int totalUnmatchedProjects = 0;
	int totalUnmatchedSubProjects = 0;
%>
<logic:iterate id="unit" name="acquisitionUnits">
	<logic:notPresent name="unit" property="unit">
		<logic:equal name="unit" property="class.name" value="pt.ist.expenditureTrackingSystem.domain.organization.Unit">
			<% totalUnmatchedUnits++; %>
		</logic:equal>
		<logic:equal name="unit" property="class.name" value="pt.ist.expenditureTrackingSystem.domain.organization.CostCenter">
			<% totalUnmatchedCostCenters++; %>
		</logic:equal>
		<logic:equal name="unit" property="class.name" value="pt.ist.expenditureTrackingSystem.domain.organization.Project">
			<% totalUnmatchedProjects++; %>
		</logic:equal>
		<logic:equal name="unit" property="class.name" value="pt.ist.expenditureTrackingSystem.domain.organization.SubProject">
			<% totalUnmatchedSubProjects++; %>
		</logic:equal>
	</logic:notPresent>
</logic:iterate>
<ul>
	<li>
		Unidades: <%= totalUnmatchedUnits %>
	</li>
	<li>
		Centros de Custo: <%= totalUnmatchedCostCenters %>
	</li>
	<li>
		Projectos: <%= totalUnmatchedProjects %>
	</li>
	<li>
		Linhas de Acção: <%= totalUnmatchedSubProjects %>
	</li>
</ul>
<logic:iterate id="unit" name="acquisitionUnits">
	<logic:notPresent name="unit" property="unit">
		<logic:equal name="unit" property="class.name" value="pt.ist.expenditureTrackingSystem.domain.organization.Unit">
			<% if (count++ < 100) { %>
				<bean:define id="urlSelectUnit">/connectUnits.do?method=showUnits<logic:present name="partyChart" property="element">&amp;partyId=<bean:write name="partyChart" property="element.externalId"/></logic:present></bean:define>
				<html:link action="<%= urlSelectUnit %>" paramId="unitId" paramName="unit" paramProperty="externalId">
					<bean:write name="unit" property="presentationName"/>
				</html:link>
				<br/>
			<% } %>
		</logic:equal>
	</logic:notPresent>
</logic:iterate>
<br/>
<logic:iterate id="unit" name="acquisitionUnits">
	<logic:notPresent name="unit" property="unit">
		<logic:equal name="unit" property="class.name" value="pt.ist.expenditureTrackingSystem.domain.organization.CostCenter">
			<% if (count++ < 100) { %>
				<bean:define id="urlSelectUnit">/connectUnits.do?method=showUnits<logic:present name="partyChart" property="element">&amp;partyId=<bean:write name="partyChart" property="element.externalId"/></logic:present></bean:define>
				<html:link action="<%= urlSelectUnit %>" paramId="unitId" paramName="unit" paramProperty="externalId">
					<bean:write name="unit" property="presentationName"/>
				</html:link>
				<br/>
			<% } %>
		</logic:equal>
	</logic:notPresent>
</logic:iterate>

