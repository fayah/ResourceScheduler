package resourcescheduler.impl;

import java.util.ArrayList;
import java.util.List;

import resourcescheduler.Message;
import resourcescheduler.AbstractPrioritizationStategy;

public class PrioritizationStrategyDefault extends AbstractPrioritizationStategy{
	
	public PrioritizationStrategyDefault(){
		sent = new ArrayList<Message>();
	}

	@Override
	public Message chooseMessageToSend(List<Message> toSend) {
		if(toSend.size()!=0){
			String sentGroup = null;
			int j=0;
			while(j < sent.size()){
				sentGroup = sent.get(j).getGroup();
				int i=0;
				while(i<toSend.size()){
					if(toSend.get(i).getGroup().equals(sentGroup)){
						sent.add(toSend.get(i));
						return toSend.get(i);
					}
					i++;
				}
				j++;
			}
			//new group, never sent before 
			sent.add(toSend.get(0));
			return toSend.get(0);
		}
		return null;
	}
	

}
