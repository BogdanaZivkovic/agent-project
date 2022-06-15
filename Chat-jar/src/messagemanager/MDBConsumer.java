package messagemanager;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import agents.AID;
import agents.Agent;
import agents.CachedAgentsRemote;

/**
 * Message-Driven Bean implementation class for: MDBConsumer
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/topic/publicTopic") })
public class MDBConsumer implements MessageListener {


	@EJB
	private CachedAgentsRemote cachedAgents;
	/**
	 * Default constructor.
	 */
	public MDBConsumer() {

	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
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
			
			Agent agent = (Agent) cachedAgents.getByAID(receiverAID);
			
			if (agent != null) {
				agent.handleMessage(msg);
			} else {
				System.out.println("No such agent");
			}
		}
	}
}
