package pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.tags;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.MessageHandler;
import pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.MessageHandler.Message;
import pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.MessageHandler.MessageType;

public class ShowMessagesTag extends TagSupport {

    private String type = null;

    @Override
    public int doStartTag() throws JspException {
	MessageHandler handler = (MessageHandler) pageContext.getRequest().getAttribute((MessageHandler.MESSAGE_HANDLER_NAME));
	JspWriter writer = pageContext.getOut();

	try {
	    List<Message> messages = handler.getMessages(getMessageType());
	    if (messages != null) {
		for (Message message : messages) {
		    writer.append(message.getMessage());
		    writer.append("<br/>");
		}
	    }
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
	return super.doStartTag();
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getType() {
	return type;
    }

    private MessageType getMessageType() {
	return getType() != null ? MessageType.valueOf(getType()) : null;
    }
}
