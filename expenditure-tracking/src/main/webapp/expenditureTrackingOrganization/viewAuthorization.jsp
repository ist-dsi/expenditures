<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="authorizations.title.details" bundle="EXPENDITURE_RESOURCES"/></h2>

 <script src="<%= request.getContextPath() + "/javaScript/jquery.alerts.js"%>" type="text/javascript"></script> 
 <script src="<%= request.getContextPath() + "/javaScript/alertHandlers.js"%>" type="text/javascript"></script> 
 
<div class="infobox_dotted">
	<ul>
		<li>
			<html:link action="/expenditureTrackingOrganization.do?method=viewOrganization" paramId="unitOid" paramName="authorization" paramProperty="unit.externalId">
				<bean:message key="unit.label.view" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="/expenditureTrackingOrganization.do?method=viewPerson" paramId="personOid" paramName="authorization" paramProperty="person.externalId">
				<bean:message key="person.label.view" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
			</html:link>
		</li>
		<bean:define id="personFromAuthorizationOid" name="authorization" property="person.externalId" type="java.lang.String"/>
		<logic:equal name="USER_SESSION_ATTRIBUTE" property="user.expenditurePerson.externalId" value="<%= personFromAuthorizationOid %>">
			<logic:equal name="authorization" property="canDelegate" value="true">
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=chooseDelegationUnit" paramId="authorizationOid" paramName="authorization" paramProperty="externalId">
						<bean:message key="authorizations.link.delegate" bundle="EXPENDITURE_RESOURCES"/>
					</html:link>
				</li>
			</logic:equal>
		</logic:equal>
		<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.AQUISITIONS_UNIT_MANAGER">
			<li>
				<html:link action="/expenditureTrackingOrganization.do?method=editAuthorization" paramId="authorizationOid" paramName="authorization" paramProperty="externalId">
					<bean:message key="authorizations.link.edit" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
			</li>
		</logic:present>
		<logic:equal name="authorization" property="currentUserAbleToRevoke" value="true">
			<li>
				<html:link styleId="revokeLink" action="/expenditureTrackingOrganization.do?method=revokeAuthorization" paramId="authorizationOid" paramName="authorization" paramProperty="externalId">
					<bean:message key="authorizations.link.revoke" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>
				
				  <bean:define id="message">
	            	<bean:message key="label.revokeAuthorization" bundle="EXPENDITURE_RESOURCES"/>
	            </bean:define>
	           <bean:define id="title">
	            	<bean:message key="title.revokeAuthorization" bundle="EXPENDITURE_RESOURCES"/>
	            </bean:define>
	             <script type="text/javascript"> 
	   						linkConfirmationHook('revokeLink', '<%= message %>','<%= title %>'); 
	 			</script>  
			</li>
		</logic:equal>
		<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
			<li>
				<html:link styleId="removeLink" action="/expenditureTrackingOrganization.do?method=deleteAuthorization" paramId="authorizationOid" paramName="authorization" paramProperty="externalId">
					<bean:message key="authorizations.link.remove" bundle="EXPENDITURE_RESOURCES"/>
				</html:link>

	            <bean:define id="message">
	            	<bean:message key="label.removeAuthorization" bundle="EXPENDITURE_RESOURCES"/>
	            </bean:define>
	           <bean:define id="title">
	            	<bean:message key="title.removeAuthorization" bundle="EXPENDITURE_RESOURCES"/>
	            </bean:define>
	             <script type="text/javascript"> 
	   						linkConfirmationHook('removeLink', '<%= message %>','<%= title %>'); 
	 			</script> 
			</li>
		</logic:present>
	</ul>
</div>

<div class="infobox">
	<fr:view name="authorization" schema="viewAuthorization">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
	</fr:view>
</div>

<p class="mtop15 mbottom05"><strong><bean:message key="authorizations.label.delegationList" bundle="EXPENDITURE_RESOURCES"/></strong></p>
<logic:notEmpty name="authorization" property="delegatedAuthorizations">
	<fr:view name="authorization" property="delegatedAuthorizations" schema="viewDelegatedAuthorizations">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop05"/>
			<fr:property name="link(view)" value="/expenditureTrackingOrganization.do?method=viewAuthorization"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/authorizationOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="authorization" property="delegatedAuthorizations">
	<p class="mvert05"> 
		<em><bean:message key="authorizations.message.info.noDelegations" bundle="EXPENDITURE_RESOURCES"/>.</em>
	</p>
</logic:empty>
