package resourcescheduler.impl;

import java.util.List;

import resourcescheduler.Message;
import resourcescheduler.AbstractPrioritizationStategy;

public class PrioritizationContext {
	
	private AbstractPrioritizationStategy strategy;
	
	public PrioritizationContext(AbstractPrioritizationStategy strategy) {
		this.strategy = strategy;
	}
	
	public Message chooseMessageToSend(List<Message> toSend){
		return strategy.chooseMessageToSend(toSend);
	}
	
	public List<Message> getSentMessages(){
		return this.strategy.getSent();
	}

}
