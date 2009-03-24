package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.File;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.MessageHandler;
import pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.MessageHandler.MessageType;
import pt.ist.expenditureTrackingSystem.presentationTier.util.FileUploadBean;

public abstract class BaseAction extends ContextBaseAction {

    private MessageHandler messageHandler = null;

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {
	messageHandler = new MessageHandler();
	request.setAttribute(MessageHandler.MESSAGE_HANDLER_NAME, messageHandler);
	return super.execute(mapping, form, request, response);

    }

    protected Person getLoggedPerson() {
	return Person.getLoggedPerson();
    }

   
    protected byte[] consumeInputStream(final FileUploadBean fileUploadBean) {
	final InputStream inputStream = fileUploadBean.getInputStream();
	return consumeInputStream(inputStream);
    }

    protected ActionForward download(final HttpServletResponse response, final String filename, final byte[] bytes,
	    final String contentType) throws IOException {
	final OutputStream outputStream = response.getOutputStream();
	response.setContentType(contentType);
	response.setHeader("Content-disposition", "attachment; filename=" + filename.replace(" ", "_"));
	response.setContentLength(bytes.length);
	if (bytes != null) {
	    outputStream.write(bytes);
	}
	outputStream.flush();
	outputStream.close();
	return null;
    }

    protected ActionForward download(final HttpServletResponse response, final File file) throws IOException {
	String filename = file.getFilename();
	if (filename == null) {
	    filename = file.getDisplayName();
	}
	return file != null && file.getContent() != null ? download(response, filename != null ? filename : "", file.getContent()
		.getBytes(), file.getContentType()) : null;
    }

    public void addMessage(String key, String bundle, String... args) {
	messageHandler.saveMessage(bundle, key, MessageType.WARN, args);
    }

    public void addErrorMessage(String key, String bundle, String... args) {
	messageHandler.saveMessage(bundle, key, MessageType.ERROR, args);
    }

}
