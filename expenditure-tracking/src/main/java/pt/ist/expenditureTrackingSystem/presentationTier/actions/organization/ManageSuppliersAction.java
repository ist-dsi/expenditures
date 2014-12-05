package pt.ist.expenditureTrackingSystem.presentationTier.actions.organization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.expenditureTrackingSystem.domain.dto.CreateSupplierBean;
import pt.ist.expenditureTrackingSystem.domain.dto.SupplierBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions.SearchPaymentProcessesAction;

@StrutsFunctionality(app = SearchPaymentProcessesAction.class, path = "expenditure-organization-supplier",
        titleKey = "supplier.link.manage", bundle = "ExpenditureOrganizationResources")
@Mapping(path = "/expenditureManageSuppliers")
public class ManageSuppliersAction extends BaseAction {

    @EntryPoint
    public final ActionForward manageSuppliers(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        SupplierBean supplierBean = getRenderedObject("supplierBean");
        if (supplierBean == null) {
            supplierBean = new SupplierBean();
            Supplier supplier = getDomainObject(request, "supplierOid");
            supplierBean.setSupplier(supplier);
        }

        request.setAttribute("supplierBean", supplierBean);
        return forward("/expenditureTrackingOrganization/manageSuppliers.jsp");
    }

    public final ActionForward manageSuppliers(final ActionMapping mapping, final HttpServletRequest request,
            final SupplierBean supplierBean) {
        request.setAttribute("supplierBean", supplierBean);
        return forward("/expenditureTrackingOrganization/manageSuppliers.jsp");
    }

    public final ActionForward viewSupplier(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Supplier supplier = getDomainObject(request, "supplierOid");

        return viewSupplier(mapping, form, request, response, supplier);
    }

    public final ActionForward viewSupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response, final Supplier supplier) {
        request.setAttribute("supplier", supplier);
        return forward("/expenditureTrackingOrganization/viewSupplier.jsp");
    }

    public final ActionForward prepareCreateSupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final CreateSupplierBean createSupplierBean = new CreateSupplierBean();

        request.setAttribute("bean", createSupplierBean);
        return forward("/expenditureTrackingOrganization/createSupplier.jsp");
    }

    public final ActionForward createSupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final CreateSupplierBean createSupplierBean = getRenderedObject();
        final Supplier supplier = Supplier.createNewSupplier(createSupplierBean);
        final SupplierBean supplierBean = new SupplierBean(supplier);
        return manageSuppliers(mapping, request, supplierBean);
    }

    public final ActionForward prepareEditSupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Supplier supplier = getDomainObject(request, "supplierOid");

        request.setAttribute("supplier", supplier);
        return forward("/expenditureTrackingOrganization/editSupplier.jsp");
    }

    public final ActionForward editSupplier(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Supplier supplier = getRenderedObject();
        final SupplierBean supplierBean = new SupplierBean(supplier);
        return manageSuppliers(mapping, request, supplierBean);
    }

    public final ActionForward deleteSupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Supplier supplier = getDomainObject(request, "supplierOid");
        supplier.delete();
        return manageSuppliers(mapping, form, request, response);
    }

    public final ActionForward mergeSupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Supplier supplierToTransfer = getDomainObject(request, "supplierToTransferOID");
        final Supplier supplierDestination = getDomainObject(request, "supplierDestinationOID");

        supplierDestination.merge(supplierToTransfer);

        final SupplierBean supplierBean = new SupplierBean(supplierDestination);

        return manageSuppliers(mapping, request, supplierBean);
    }

}
