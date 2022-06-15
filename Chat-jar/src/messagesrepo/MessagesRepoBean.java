package messagesrepo;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;

import models.Message;

@Stateful
@LocalBean
public class MessagesRepoBean implements MessagesRepoRemote, MessagesRepoLocal{
	
	private List<Message> messages = new ArrayList<Message>();

	@Override
	public List<Message> getMessages() {
		return messages;
	}

	@Override
	public boolean addMessage(Message message) {
		messages.add(message);
		return true;
	}
}
