package pt.ist.expenditureTrackingSystem.presentationTier.messageHandling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class MessageHandler implements Serializable {

    public static final String MESSAGE_HANDLER_NAME = "_MESSAGE_HANDLER_NAME_";
    
    private static final long serialVersionUID = 1L;
    
    private List<Message> messages;

    public MessageHandler() {
	messages = new ArrayList<Message>();
    }

    public void saveMessage(String bundle, String key) {
	messages.add(new Message(bundle, key, new String[] {}));
    }

    public void saveMessage(String bundle, String key, String... args) {
	messages.add(new Message(bundle, key, args));
    }

    public boolean hasMessages() {
	return !messages.isEmpty();
    }

    public List<Message> getMessages() {
	return messages;
    }

    public static class Message implements Serializable {

	private String bundle;
	private String key;
	private String[] args;

	private Message(String bundle, String key, String... args) {
	    this.bundle = bundle;
	    this.key = key;
	    this.args = args;
	}

	public String getMessage() {
	    return RenderUtils.getFormatedResourceString(bundle, key, (Object[])args);
	}
    }
}
