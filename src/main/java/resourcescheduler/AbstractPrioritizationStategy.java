package resourcescheduler;

import java.util.List;

public abstract class AbstractPrioritizationStategy {

	protected static List <Message> sent;
	
	public abstract Message chooseMessageToSend(List<Message> toSend);
	
	public List<Message> getSent(){
		return sent;
	}
}
