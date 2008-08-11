package pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.MessageHandler;

public class HasMessagesTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {
	MessageHandler handler = (MessageHandler) pageContext.getRequest().getAttribute((MessageHandler.MESSAGE_HANDLER_NAME));
	
	if ( handler == null || !handler.hasMessages()) {
	    return SKIP_BODY;
	}
	
	return EVAL_BODY_INCLUDE;
    }

    

}
