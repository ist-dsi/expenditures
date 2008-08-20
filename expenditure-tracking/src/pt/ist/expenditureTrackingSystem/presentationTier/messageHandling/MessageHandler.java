package pt.ist.expenditureTrackingSystem.presentationTier.messageHandling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class MessageHandler implements Serializable {

    public static final String MESSAGE_HANDLER_NAME = "_MESSAGE_HANDLER_NAME_";

    private static final long serialVersionUID = 1L;

    private MessageMap messages;

    public MessageHandler() {
	messages = new MessageMap();
    }

    public void saveMessage(String bundle, MessageType type, String key) {
	messages.put(type, new Message(bundle, key, new String[] {}));
    }

    public void saveMessage(String bundle, String key, MessageType type, String... args) {
	messages.put(type, new Message(bundle, key, args));
    }

    public boolean hasMessages() {
	return !messages.isEmpty();
    }

    public boolean hasMessages(MessageType type) {
	return !messages.isEmpty(type);
    }

    public List<Message> getMessages() {
	return messages.getAllMessages();
    }

    public List<Message> getMessages(MessageType type) {
	return messages.getMessages(type);
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
	    return RenderUtils.getFormatedResourceString(bundle, key, (Object[]) args);
	}
    }

    public static enum MessageType {
	ERROR, WARN;
    }

    private static class MessageMap {

	private Map<MessageType, List<Message>> map;

	public MessageMap() {
	    map = new HashMap<MessageType, List<Message>>();
	}

	public void put(MessageType key, Message value) {
	    List<Message> list = map.get(key);
	    if (list == null) {
		list = new ArrayList<Message>();
		map.put(key, list);
	    }
	    list.add(value);
	}

	public List<Message> getMessages(MessageType type) {
	    if (type == null) {
		return getAllMessages();
	    }
	    return map.get(type);
	}

	public List<Message> getAllMessages() {
	    List<Message> result = new ArrayList<Message>();
	    for (List<Message> messages : map.values()) {
		result.addAll(messages);
	    }
	    return result;
	}

	public boolean isEmpty(MessageType type) {
	    if (type == null) {
		return isEmpty();
	    }
	    List<Message> list = map.get(type);
	    return list == null || list.isEmpty();
	}

	public boolean isEmpty() {
	    return map.isEmpty();
	}
    }
}
