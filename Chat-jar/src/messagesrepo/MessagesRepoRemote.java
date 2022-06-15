package messagesrepo;

import java.util.List;

import javax.ejb.Remote;

import models.Message;

@Remote
public interface MessagesRepoRemote {
	
	public List<Message> getMessages();
	
	public boolean addMessage(Message message);

}
