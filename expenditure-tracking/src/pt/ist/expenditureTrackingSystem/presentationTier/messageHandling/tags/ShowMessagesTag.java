package pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.MessageHandler;
import pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.MessageHandler.Message;

public class ShowMessagesTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {
	MessageHandler handler = (MessageHandler) pageContext.getRequest().getAttribute((MessageHandler.MESSAGE_HANDLER_NAME));
	JspWriter writer = pageContext.getOut();

	try {
	    for (Message message : handler.getMessages()) {
		writer.append(message.getMessage());
		writer.append("<br/>");
	    }
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
	return super.doStartTag();
    }
}
