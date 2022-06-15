package chatmanager;

import java.util.List;

import javax.ejb.Remote;

import models.User;

@Remote
public interface ChatManagerRemote {

	public boolean login(User user);

	public User register(User user);

	public List<User> loggedInUsers();
	
	public List<User> registeredUsers();
	
	public boolean logout(String username);
	
	public void setLoggedIn(List<User> users);
	
	public void setRegistered(List<User> users);
	
	public User findByUsername(String username);
	
}
