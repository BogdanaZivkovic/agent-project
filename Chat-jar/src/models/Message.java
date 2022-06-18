package models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	private User receiver;
	private User sender;
	private LocalDateTime date;
	private String subject;
	private String content;
	
	public Message() { }

	public Message(User receiver, User sender, LocalDateTime date, String subject, String content) {
		super();
		this.receiver = receiver;
		this.sender = sender;
		this.date = date;
		this.subject = subject;
		this.content = content;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "" + sender.getUsername() + "," + date + "," + subject + "," + content;
	}
	
}
