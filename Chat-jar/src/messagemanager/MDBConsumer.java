package messagemanager;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import agents.AID;
import agents.Agent;
import agents.CachedAgentsRemote;
import rest.AgentRest;

/**
 * Message-Driven Bean implementation class for: MDBConsumer
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/topic/publicTopic") })
public class MDBConsumer implements MessageListener {


	@EJB
	private CachedAgentsRemote cachedAgents;

	public MDBConsumer() {}

	public void onMessage(Message message) {		
		try {
			processMessage(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	private void processMessage(Message message) throws JMSException {
		ACLMessage acl = (ACLMessage) ((ObjectMessage) message).getObject();
		deliverMessage(acl);
	}

	private void deliverMessage(ACLMessage msg) {
		
		for (AID receiverAID : msg.receivers) {
			
			if(!receiverAID.getHost().getAlias().equals(System.getProperty("jboss.node.name") + ":8080")) {
				
				forwardMessage(msg, receiverAID.getHost().getAlias());
			}
			
			else {
				Agent agent = (Agent) cachedAgents.getByAID(receiverAID);
				
				if (agent != null) {
					agent.handleMessage(msg);
				} else {
					System.out.println("No such agent");
				}
			}
		}
	}
	
	private void forwardMessage(ACLMessage message, String nodeAlias) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget rtarget = client.target("http://" + nodeAlias + "/Chat-war/api/agents");
		AgentRest rest = rtarget.proxy(AgentRest.class);
		rest.sendACLMessage(message);
		client.close();
	}
}
