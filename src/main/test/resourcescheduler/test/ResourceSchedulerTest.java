package resourcescheduler.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import resourcescheduler.Gateway;
import resourcescheduler.impl.GatewayImpl;
import resourcescheduler.impl.MessageImpl;
import resourcescheduler.impl.PrioritizationContext;
import resourcescheduler.impl.PrioritizationStrategyCustom;
import resourcescheduler.impl.PrioritizationStrategyDefault;
import resourcescheduler.impl.ResourceScheduler;

public class ResourceSchedulerTest {
	
	@Test
	public void oneResourceTest(){
		PrioritizationContext prioritization = new PrioritizationContext(new PrioritizationStrategyDefault());
		ResourceScheduler.getInstance().setPrioritizationContext(prioritization);
		Gateway gateway = new GatewayImpl();
		ResourceScheduler.getInstance().setGateway(gateway);
		ResourceScheduler.getInstance().setAvailableResources(1);
		
		MessageImpl message1 = new MessageImpl("group2");
		MessageImpl message2 = new MessageImpl("group1");
		MessageImpl message3 = new MessageImpl("group2");
		MessageImpl message4 = new MessageImpl("group3");
		
		ResourceScheduler.getInstance().addMessageToQueue(message1);
		ResourceScheduler.getInstance().addMessageToQueue(message2);
		ResourceScheduler.getInstance().addMessageToQueue(message3);
		ResourceScheduler.getInstance().addMessageToQueue(message4);
		
		//Here first and only message sent should be message1
		assertEquals(prioritization.getSentMessages().size(), 1);
		assertEquals(prioritization.getSentMessages().get(0), message1);
		
		message1.completed();
		
		//Here second message sent should be message3
		assertEquals(prioritization.getSentMessages().size(), 2);
		assertEquals(prioritization.getSentMessages().get(1), message3);
		
		message3.completed();
		
		//Here third message sent should be message2
		assertEquals(prioritization.getSentMessages().size(), 3);
		assertEquals(prioritization.getSentMessages().get(2), message2);
		
		message2.completed();
		
		//Here third message sent should be message2
		assertEquals(prioritization.getSentMessages().size(), 4);
		assertEquals(prioritization.getSentMessages().get(3), message4);
		
	}
	
	@Test
	public void twoResourceTest(){
		PrioritizationContext prioritization = new PrioritizationContext(new PrioritizationStrategyDefault());
		ResourceScheduler.getInstance().setPrioritizationContext(prioritization);
		Gateway gateway = new GatewayImpl();
		ResourceScheduler.getInstance().setGateway(gateway);
		ResourceScheduler.getInstance().setAvailableResources(2);
		
		MessageImpl message1 = new MessageImpl("group2");
		MessageImpl message2 = new MessageImpl("group1");
		MessageImpl message3 = new MessageImpl("group3");
		MessageImpl message4 = new MessageImpl("group3");
		MessageImpl message5 = new MessageImpl("group3");
		MessageImpl message6 = new MessageImpl("group3");
		MessageImpl message7 = new MessageImpl("group2");
		MessageImpl message8 = new MessageImpl("group1");
		
		ResourceScheduler.getInstance().addMessageToQueue(message1);
		ResourceScheduler.getInstance().addMessageToQueue(message2);
		ResourceScheduler.getInstance().addMessageToQueue(message3);
		ResourceScheduler.getInstance().addMessageToQueue(message4);
		ResourceScheduler.getInstance().addMessageToQueue(message5);
		ResourceScheduler.getInstance().addMessageToQueue(message6);
		ResourceScheduler.getInstance().addMessageToQueue(message7);
		ResourceScheduler.getInstance().addMessageToQueue(message8);
		
		//Here first two messages sent should be message1 and message2
		assertEquals(prioritization.getSentMessages().size(), 2);
		assertEquals(prioritization.getSentMessages().get(0), message1);
		assertEquals(prioritization.getSentMessages().get(1), message2);

		
		message1.completed();
		message2.completed();
		
		//Here next two messages sent should be message7 and message8
		assertEquals(prioritization.getSentMessages().size(), 4);
		assertEquals(prioritization.getSentMessages().get(2), message7);
		assertEquals(prioritization.getSentMessages().get(3), message8);

		message7.completed();
		message8.completed();

		//Here remaining messages are from group3 so it shoud be sent in the order they have been sent to the queue
		assertEquals(prioritization.getSentMessages().size(), 6);
		assertEquals(prioritization.getSentMessages().get(4), message3);
		assertEquals(prioritization.getSentMessages().get(5), message4);

		message3.completed();
		message4.completed();
		
		assertEquals(prioritization.getSentMessages().size(), 8);
		assertEquals(prioritization.getSentMessages().get(6), message5);
		assertEquals(prioritization.getSentMessages().get(7), message6);
		
	}
	
	@Test
	public void switchingPrioritizationStrategyTest(){
		PrioritizationContext prioritization = new PrioritizationContext(new PrioritizationStrategyDefault());
		ResourceScheduler.getInstance().setPrioritizationContext(prioritization);
		Gateway gateway = new GatewayImpl();
		ResourceScheduler.getInstance().setGateway(gateway);
		ResourceScheduler.getInstance().setAvailableResources(1);
		
		MessageImpl message1 = new MessageImpl("group2");
		MessageImpl message2 = new MessageImpl("group1");
		MessageImpl message3 = new MessageImpl("group3");
		MessageImpl message4 = new MessageImpl("group3");
		MessageImpl message5 = new MessageImpl("group3");
		MessageImpl message6 = new MessageImpl("group4");
		MessageImpl message7 = new MessageImpl("group2");
		MessageImpl message8 = new MessageImpl("group1");
		
		ResourceScheduler.getInstance().addMessageToQueue(message1);
		ResourceScheduler.getInstance().addMessageToQueue(message2);
		ResourceScheduler.getInstance().addMessageToQueue(message3);
		ResourceScheduler.getInstance().addMessageToQueue(message4);
		ResourceScheduler.getInstance().addMessageToQueue(message5);
		ResourceScheduler.getInstance().addMessageToQueue(message6);
		ResourceScheduler.getInstance().addMessageToQueue(message7);
		ResourceScheduler.getInstance().addMessageToQueue(message8);
		
		//Here we follow default strategy so first three messages to be message1, message7 and message2
		assertEquals(prioritization.getSentMessages().size(), 1);
		assertEquals(prioritization.getSentMessages().get(0), message1);

		message1.completed();
		
		assertEquals(prioritization.getSentMessages().size(), 2);
		assertEquals(prioritization.getSentMessages().get(1), message7);
		
		message7.completed();
		
		assertEquals(prioritization.getSentMessages().size(), 3);
		assertEquals(prioritization.getSentMessages().get(2), message2);
		
		//Then we switch to the custom strategy 
		//so next messages to be sent are message3, message6, message4, message5 and message8
		PrioritizationContext customPrioritization = new PrioritizationContext(new PrioritizationStrategyCustom());
		ResourceScheduler.getInstance().setPrioritizationContext(customPrioritization);
		
		message2.completed();
		
		assertEquals(prioritization.getSentMessages().size(), 4);
		assertEquals(prioritization.getSentMessages().get(3), message3);
		
		message3.completed();
		
		assertEquals(prioritization.getSentMessages().size(), 5);
		assertEquals(prioritization.getSentMessages().get(4), message6);
		
		message6.completed();
		
		assertEquals(prioritization.getSentMessages().size(), 6);
		assertEquals(prioritization.getSentMessages().get(5), message4);
		
	message4.completed();
		
		assertEquals(prioritization.getSentMessages().size(), 7);
		assertEquals(prioritization.getSentMessages().get(6), message5);
		
		message5.completed();
		
		assertEquals(prioritization.getSentMessages().size(), 8);
		assertEquals(prioritization.getSentMessages().get(7), message8);
		
	}

}
