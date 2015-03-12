package resourcescheduler.impl;

import java.util.List;

import resourcescheduler.Message;
import resourcescheduler.AbstractPrioritizationStategy;

public class PrioritizationStrategyCustom extends AbstractPrioritizationStategy{

	@Override
	public Message chooseMessageToSend(List<Message> toSend) {
		
		if(toSend.size()!=0){
			String toSendGroup = null;
			int j=0;
			boolean groupFound =false;
			while(j < toSend.size()){
				toSendGroup = toSend.get(j).getGroup();
				int i=0;
				while((i<sent.size())&&(!groupFound)){
					if(sent.get(i).getGroup().equals(toSendGroup)){
						groupFound= true;
					}
					i++;
				}
				if(!groupFound){
					sent.add(toSend.get(j));
					return toSend.get(j);
				}
				j++;
				groupFound=false;
			}
			
			sent.add(toSend.get(0));
			return toSend.get(0);
			
		}
		return null;
	}
	

}
