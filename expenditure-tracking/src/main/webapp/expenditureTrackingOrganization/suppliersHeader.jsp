<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL_MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.ACQUISITION_CENTRAL,pt.ist.expenditureTrackingSystem.domain.RoleType.SUPPLIER_MANAGER">
	<div class="infobox_dotted">
		<ul>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.SUPPLIER_MANAGER">
				<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER">
					<li>
						<html:link action="/expenditureTrackingOrganization.do?method=prepareCreateSupplier">
							<bean:message key="supplier.link.create" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
						</html:link>		
					</li>
				</logic:present>
			</logic:present>
			<li>
				<html:link action="/expenditureTrackingOrganization.do?method=listSuppliers">
					<bean:message key="supplier.link.list" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</html:link>
			</li>
			<logic:present role="pt.ist.expenditureTrackingSystem.domain.RoleType.MANAGER,pt.ist.expenditureTrackingSystem.domain.RoleType.SUPPLIER_MANAGER">
				<li>
					<html:link action="/expenditureTrackingOrganization.do?method=listGiafSuppliers">
						<bean:message key="supplier.link.list.giaf" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
					</html:link>
				</li>
			</logic:present>
			<li>
				<html:link action="/expenditureTrackingOrganization.do?method=listCPVReferences">
					<bean:message key="supplier.link.list.cpv" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>
				</html:link>
			</li>
		</ul>
	</div>
</logic:present>
