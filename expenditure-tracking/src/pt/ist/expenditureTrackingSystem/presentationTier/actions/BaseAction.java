package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.fenix.tools.util.FileUtils;

public abstract class BaseAction extends DispatchAction {

    private static final Context CONTEXT = new Context(null);

    protected Context getContextModule() {
	return CONTEXT;
    }

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {
	final Context context = getContextModule();
	context.setAsActive();
	return super.execute(mapping, form, request, response);
    }

    public <T> T getAttribute(final HttpServletRequest request, final String attributeName) {
	final T t = (T) request.getAttribute(attributeName);
	return t == null ? (T) request.getParameter(attributeName) : t;
    }

    public <T extends DomainObject> T getDomainObject(final HttpServletRequest request, final String attributeName) {
	final String parameter = request.getParameter(attributeName);
	final Long oid = parameter != null ? Long.valueOf(parameter) : (Long) request.getAttribute(attributeName);
	return oid == null ? null : (T) Transaction.getObjectForOID(oid.longValue());
    }

    public <T extends Object> T getRenderedObject() {
	final IViewState viewState = RenderUtils.getViewState();
	return (T) getRenderedObject(viewState);
    }

    public <T extends Object> T getRenderedObject(final String id) {
	final IViewState viewState = RenderUtils.getViewState(id);
	return (T) getRenderedObject(viewState);
    }

    public <T extends Object> T getRenderedObject(final IViewState viewState) {
	if (viewState != null) {
	    MetaObject metaObject = viewState.getMetaObject();
	    if (metaObject != null) {
		return (T) metaObject.getObject();
	    }
	}
	return null;
    }

    public byte[] consumeInputStream(final InputStream inputStream) {
	byte[] result = null;
	if (inputStream != null) {
	    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    try {
		try {
		    FileUtils.copy(inputStream, byteArrayOutputStream);
		    byteArrayOutputStream.flush();
		    result = byteArrayOutputStream.toByteArray();
		    byteArrayOutputStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    } finally {
		try {
		    inputStream.close();
		    byteArrayOutputStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
	return result;
    }

}
