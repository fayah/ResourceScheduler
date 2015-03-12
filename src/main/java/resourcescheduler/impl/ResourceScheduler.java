package resourcescheduler.impl;

import java.util.ArrayList;
import java.util.List;

import resourcescheduler.Gateway;
import resourcescheduler.Message;

public class ResourceScheduler {
	
	private static ResourceScheduler resourceScheduler = null;
	private List <Message> messageQueue;
	private int availableResources = 0;
	private PrioritizationContext prioritizationContext;
	private Gateway gateway;
	
	private ResourceScheduler(){
		messageQueue = new ArrayList<Message>();
	}

	public static ResourceScheduler getInstance(){
		if(resourceScheduler==null)
			resourceScheduler = new ResourceScheduler();
		return resourceScheduler;
	}

	public synchronized void setAvailableResources(int availableResources) {
		if(availableResources>=0){
			this.availableResources = availableResources;
			if(availableResources!=0){
				Message messageToSend = prioritizationContext.chooseMessageToSend(messageQueue);
				sendMessage(messageToSend);
			}
		}
	}
	
	public void incrementAvailableResources(){
		setAvailableResources(getAvailableResources()+1);
	}
	public void decrementAvailableResources(){
		setAvailableResources(getAvailableResources()-1);
	}

	public int getAvailableResources() {
		return availableResources;
	}
	
	public void setPrioritizationContext(PrioritizationContext prioritizationContext) {
		this.prioritizationContext = prioritizationContext;
	}
	
	public void addMessageToQueue(Message message){
		messageQueue.add(message);
		if(availableResources!=0){
			Message messageToSend = prioritizationContext.chooseMessageToSend(messageQueue);
			sendMessage(messageToSend);
		}
	}
	
	public void sendMessage(Message message){
		if(message!=null){
			this.messageQueue.remove(message);
			gateway.send(message);
		}
	}
	
	public Gateway getGateway() {
		return gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

}
