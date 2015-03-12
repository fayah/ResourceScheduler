package resourcescheduler;


public interface Message {

	public void completed();
	
	public String getGroup();
}
