package chatmanager;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import connnectionmanager.ConnectionManager;
import models.Host;
import models.User;

// TODO Implement the rest of Client-Server functionalities 
/**
 * Session Bean implementation class ChatBean
 */
@Singleton
@LocalBean
public class ChatManagerBean implements ChatManagerRemote, ChatManagerLocal{

	private List<User> registered = new ArrayList<User>();
	private List<User> loggedIn = new ArrayList<User>();
	
	@EJB ConnectionManager connectionManager;
	
	public ChatManagerBean() {
	}

	@Override
	public User register(User user) {
		boolean exists = registered.stream().anyMatch(u->u.getUsername().equals(user.getUsername()));
		if(exists) 
			return null;
		
		registered.add(user); 
		connectionManager.registerNotifyNodes();
		return user;
	}

	@Override
	public boolean login(User user) {
		boolean exists = registered.stream().anyMatch(u->u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword()));
		if(!exists) {
			return false;
		}
		else {
			user.setHost(getUserHost());
			loggedIn.add(user);
			connectionManager.loginNotifyNodes();
			return true;
		}
	}

	@Override
	public List<User> loggedInUsers() {
		return loggedIn;
	}
	
	@Override
	public List<User> registeredUsers() {
		return registered;
	}

	@Override
	public boolean logout(String username) {
		for(User user : loggedIn) {
			if (user.getUsername().equals(username)) {
				loggedIn.remove(user);
				connectionManager.loginNotifyNodes();
				return true;
			}
		}
		return false;
	}
	
	private Host getUserHost() {
		String hostAlias = System.getProperty("jboss.node.name") + ":8080";
		String hostAddress = "";
		
		try {
			MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
			ObjectName http = new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http");
			hostAddress = (String) mBeanServer.getAttribute(http, "boundAddress");			
		} catch (MalformedObjectNameException | InstanceNotFoundException | AttributeNotFoundException | ReflectionException | MBeanException e) {
			e.printStackTrace();
		}	
		
		return new Host(hostAddress, hostAlias);
	}

	@Override
	public void setLoggedIn(List<User> users) {
		loggedIn = users;
	}

	@Override
	public void setRegistered(List<User> users) {
		registered = users;
	}

	@Override
	public User findByUsername(String username) {
		for(User user : loggedIn) {
			if(user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}
}
