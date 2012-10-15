package simulator;

public class ActionPacket {

	private Action action;
	private Double argument;
	
	public ActionPacket(Action act, Double arg) {
		action = act;
		argument = arg;
	}

	public Action getAction(){
		return action;
	}
	
	public double getArgument(){
		return argument;
	}
}
