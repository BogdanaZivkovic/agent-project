package messagemanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.inject.Default;

import agents.AID;

@Default
public class ACLMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	public Performative performative;
	public AID sender;
	public List<AID> receivers;
	public AID replyTo;
	public String content;
	public Object contentObj;
	public HashMap<String, Object> userArgs;
	public String language;
	public String encoding;
	public String ontology;
	public String protocol;
	public String conversationId;
	public String inReplyTo;
	public Long replyBy;
	
	public ACLMessage() {
		this.performative = Performative.NOT_UNDERSTOOD;
		receivers = new ArrayList<>();
		userArgs = new HashMap<>();
	}

	public ACLMessage(Performative performative) {
		this.performative = performative;
		receivers = new ArrayList<>();
		userArgs = new HashMap<>();
	}

	public ACLMessage(Performative performative, AID sender, List<AID> receivers, AID replyTo, String content,
			Object contentObj, HashMap<String, Object> userArgs, String language, String encoding, String ontology,
			String protocol, String conversationId, String inReplyTo, Long replyBy) {
		super();
		this.performative = performative;
		this.sender = sender;
		this.receivers = receivers;
		this.replyTo = replyTo;
		this.content = content;
		this.contentObj = contentObj;
		this.userArgs = userArgs;
		this.language = language;
		this.encoding = encoding;
		this.ontology = ontology;
		this.protocol = protocol;
		this.conversationId = conversationId;
		this.inReplyTo = inReplyTo;
		this.replyBy = replyBy;
	}
}
