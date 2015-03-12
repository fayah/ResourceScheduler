package resourcescheduler.impl;

import resourcescheduler.Message;

public class MessageImpl implements Message{
	
	private String group;

	public MessageImpl(String group){
		this.group = group;
	}
	
	@Override
	public void completed() {
		ResourceScheduler.getInstance().incrementAvailableResources();
		
	}

	@Override
	public String getGroup() {
		return group;
	}

}
