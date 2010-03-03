package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForward;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.presentationTier.util.FileUploadBean;
import pt.ist.fenixframework.plugins.fileSupport.domain.GenericFile;

public abstract class BaseAction extends ContextBaseAction {

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

    protected ActionForward download(final HttpServletResponse response, final GenericFile file) throws IOException {
	String filename = file.getFilename();
	if (filename == null) {
	    filename = file.getDisplayName();
	}
	return file != null && file.getContent() != null ? download(response, filename != null ? filename : "",
		file.getContent(), file.getContentType()) : null;
    }

}
