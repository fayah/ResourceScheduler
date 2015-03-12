package resourcescheduler.impl;

import resourcescheduler.Gateway;
import resourcescheduler.Message;

public class GatewayImpl implements Gateway{

	@Override
	public void send(Message msg) {
		ResourceScheduler.getInstance().decrementAvailableResources();
	}

}
