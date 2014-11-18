package module.workingCapital.presentationTier.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.presentationTier.actions.ProcessManagement;
import module.workingCapital.domain.TransactionFile;
import module.workingCapital.domain.WorkingCapitalAcquisitionTransaction;
import module.workingCapital.domain.WorkingCapitalTransaction;
import module.workingCapital.domain.util.WorkingCapitalTransactionFileUploadBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.security.Authenticate;

import com.google.common.io.ByteStreams;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/workingCapitalTransaction")
public class TransactionManagement extends ProcessManagement {

    public static Map<Class<? extends WorkingCapitalTransaction>, ProcessRequestHandler<? extends WorkflowProcess>> handlers =
            new HashMap<Class<? extends WorkingCapitalTransaction>, ProcessRequestHandler<? extends WorkflowProcess>>();

    @Override
    protected User getLoggedPerson() {
        return Authenticate.getUser();
    }

    private ActionForward forwardToUpload(HttpServletRequest request, WorkingCapitalTransactionFileUploadBean bean) {

        if (!bean.isDefaultUploadInterfaceUsed()) {
            request.setAttribute("interface", "/" + bean.getSelectedInstance().getName().replace('.', '/') + "-upload.jsp");
        }
        return forward("/workingCapital/transactionFileUpload.jsp");
    }

    @Override
    public ActionForward fileUpload(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final WorkingCapitalAcquisitionTransaction transaction = getTransaction(request);
        final WorkflowProcess process = getProcess(request);
        Class<? extends TransactionFile> selectedInstance =
                (Class<? extends TransactionFile>) process.getUploadableFileTypes().get(0);
        WorkingCapitalTransactionFileUploadBean bean = new WorkingCapitalTransactionFileUploadBean(process, transaction);
        bean.setSelectedInstance(selectedInstance);

        request.setAttribute("transaction", transaction);
        request.setAttribute("bean", bean);
        request.setAttribute("process", process);

        return forwardToUpload(request, bean);
    }

    @Override
    public ActionForward upload(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        WorkingCapitalTransactionFileUploadBean bean = getRenderedObject("uploadFile");
        final WorkingCapitalAcquisitionTransaction transaction = getTransaction(request);
        final WorkflowProcess process = getProcess(request);

        try {
            transaction.addFile(bean.getDisplayName(), bean.getFilename(),
                    ByteStreams.toByteArray(bean.getInputStream()), bean);
        } catch (ProcessFileValidationException e) {
            request.setAttribute("bean", bean);
            request.setAttribute("process", process);
            request.setAttribute("transaction", transaction);
            addLocalizedMessage(request, e.getLocalizedMessage());
            return forwardToUpload(request, bean);
        } catch (DomainException e) {
            request.setAttribute("bean", bean);
            request.setAttribute("process", process);
            request.setAttribute("transaction", transaction);
            addLocalizedMessage(request, e.getLocalizedMessage());
            RenderUtils.invalidateViewState();
            return forwardToUpload(request, bean);
        }

        return viewProcess(process, request);
    }

    protected <T extends WorkingCapitalTransaction> T getTransaction(HttpServletRequest request) {
        return (T) getDomainObject(request, "transactionId");
    }

    @Override
    public ActionForward removeFile(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final TransactionFile file = getDomainObject(request, "fileId");
        final WorkflowProcess process = getProcess(request);
        final WorkingCapitalAcquisitionTransaction transaction = getTransaction(request);
        transaction.removeFiles(file);

        return viewProcess(process, request);

    }
}
