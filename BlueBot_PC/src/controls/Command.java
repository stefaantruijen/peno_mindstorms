package controls;
/**
 * Enumerations of commands that can be executed by the handler.
 * @author  Incalza Dario
 *
 */
public enum Command {
	
	MOVE_FORWARD("moveForward"),MOVE_STOP("moveStop"),TURN_LEFT("turnLeft"),TURN_RIGHT("turnRight");
	
	private String value;
	/**
	 * Initialize privately a command with a given value string. This given value string will be the value transmitted to the NXT Robot through BT.
	 * 
	 * @param v - A given value string
	 */
	Command(String v){
		this.value = v;
	}
	/**
	 * Get the value string respresenting this command.
	 * @return value : the value
	 */
	public String getVal(){
		return this.value;
	}
	/**
	 * Execute this command given a specific handler.
	 * No need to now if it is the simulator or the robot at this point.
	 * 
	 * @param handler : a given handler used to execute the command.
	 */
	public void execute(IHandler handler){
		switch(this){
			case MOVE_FORWARD:
				handler.move();
				return;
			case MOVE_STOP:
				handler.stop();
				return;
			case TURN_LEFT:
				handler.turnLeft();
				return;
			case TURN_RIGHT:
				handler.turnRight();
				return;
		}	
	}

}
