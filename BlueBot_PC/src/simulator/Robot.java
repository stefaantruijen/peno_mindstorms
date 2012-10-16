package simulator;

import java.util.concurrent.LinkedBlockingQueue;

import bluebot.core.ControllerListener;
import bluebot.util.AbstractEventDispatcher;

import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveController;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.RotateMoveController;


/**
 * Represents the virtual robot.
 * Based on the DifferentialPilot API (e.g. implementing some of its interfaces we will be using)
 *  (see lejos.robotics.navigation.DifferentialPilot)
 * 
 * Goal is to make a seperate thread running the run() of this robot class (simulating the robot waiting for a command).
 * Other threads can then use the lejos API methods to steer the simulator. 
 * 
 * Action enum handles the actual thread sleeping, drawing, messaging, ect.
 * 
 *
 * @author Dieter
 *
 */
public class Robot extends AbstractEventDispatcher<ControllerListener>implements MoveController,RotateMoveController, Runnable{
	
	//TODO:Test how this works irl with the physical robot. Adapt the robot sim likewise.

	/*
	//TODO: find and replace 'system.out.println(' with 'fireMessage('
	public static void main(String[] args) {
		//TODO: extract to Junit test, only for quick and dirty testing here atm.
		final Robot LennySim = new Robot(30,30);
		final Thread simulatorThread = new Thread(LennySim);
		Runnable mainProg = new Runnable(){
			public void run(){
				int i;
				LennySim.fireMessage("startin lennysim 300 @" + System.currentTimeMillis());
				LennySim.travel(300);
				i=0;
				while(LennySim.isMoving()){
					i++;
				}
				LennySim.fireMessage("stopped Moving 300 @" + System.currentTimeMillis());

				
				LennySim.fireMessage("startin lennysim 600 @" + System.currentTimeMillis());
				LennySim.rotate(360);
				i=0;
				while(LennySim.isMoving()){
					i++;
				}
				LennySim.fireMessage("stopped Moving 600 @" + System.currentTimeMillis());
				LennySim.kill();
			}
		};
		Thread mainThread = new Thread(mainProg);
		LennySim.fireMessage("starting simulatorThread now");
		simulatorThread.start();
		LennySim.fireMessage("starting mainThread now");
		mainThread.start();
	}
	*/
	
	/**
	 * Static that holds the standard travel speed in mm/s. This is the speed we measured in the real NXT robot.
	 */
	public static double STANDARD_TRAVEL_SPEED = 30; //Probably get this value from other class.//TODO: see what this is irl
	/**
	 * Static that holds the standard rotate speed in degrees/s. This is the speed we measured in the real NXT robot.
	 */
	public static double STANDARD_ROTATE_SPEED = 30; //Probably get this value from other class.
	/**
	 * Variable representing the state of the robot
	 */
	private boolean isMoving = false;
	/**
	 * Variable holding the travel speed of the robot.
	 */
	private double travelSpeed;
	/**
	 * Variable hodling the rotate speed of the robot.
	 */
	private double rotateSpeed;
	/**
	 * Variable representing the orientation in degrees since the last move.
	 */
	private double currentOrientation;
	/**
	 *  Variable representing the distance traveled in *UNITS* since the last move.
	 */
	private double currentDistanceTraveled;
	
	/**
	 * Action(Packet) that will be executed next (now).
	 */
	private ActionPacket currentActionPacket;
	
	/**
	 * Flag for stopping the robot.
	 */
	private boolean stopFlag;
	/**
	 * Flag for killing the robot (and the thread it's running in).
	 */
	private boolean killFlag;
	
	/**
	 * Queue holding ActionPackets that will be executed in the given order bye the robot.
	 */
	LinkedBlockingQueue<ActionPacket> queue = new LinkedBlockingQueue<ActionPacket>();
	
	/**
	 * No-argument constructor. Makes a new robot object with the standard travel and rotate speed.
	 */
	public Robot(){
		this(STANDARD_TRAVEL_SPEED,STANDARD_ROTATE_SPEED);
	}
	
	/**
	 * Constructor that makes a robot with given travel and rotate speed.
	 * The robot is initially in a 'stop' state.
	 * 
	 * @param tSpeed 
	 * 	The travel speed in centimeters per second. TODO: check unit.
	 * @param rSpeed
	 * 	The travel speed in centimeters per second. TODO: check unit.
	 */
	public Robot(double tSpeed, double rSpeed){
		setTravelSpeed(tSpeed);
		setRotateSpeed(rSpeed);
		setStopFlag(false);
		setKillFlag(false);
		stop();
//		fireMessage("Simulator Robot initialized"); GUI not yet listening
	}
	
	/**
	 * Sets the stop flag on the given value.
	 * @param s
	 */
	private void setStopFlag(boolean s) {
		stopFlag = s;
	}

	/**
	 * Returns if the robot is currently performing an action (moving).
	 * 
	 * @return True if the is robot currently moving (an action has been started).
	 */
	public boolean isMoving(){
		return isMoving;
	}
	
	/**
	 * Sets the isMoving field to the given value.
	 * @param moving
	 */
	private void setIsMoving(boolean moving){
		isMoving = moving;
	}
	
	@Override
	public void rotate(double angle){
		enqueueAction(Action.ROTATE, angle);
		setIsMoving(true);
	}
	
	@Override
	public void stop(){
		setStopFlag(true);
	}
	
	/**
	 * Execution of the stop command associated with the stop flag.
	 * Clears the current action, clears the queue s
	 */
	public void doStop(){
		queue.clear();
		setIsMoving(false);
		setStopFlag(false);
		fireMessage("Simulator Robot stopped. Ready for new commands");
	}
	
	/**
	 * Get's calls at the end of an action.
	 */
	public void endMove(){
		setIsMoving(false);
		fireMessage("Simulator Robot stopped moving. Ready for new enqueued command.");
	}
	
	@Override
	public void travel(double distance){
		enqueueAction(Action.TRAVEL, distance);
		setIsMoving(true);
	}

	private void enqueueAction(Action action, double arg) {
		ActionPacket ap = new ActionPacket(action, arg);
		queue.offer(ap);
	}
	
	public Action getCurrentAction(){
		if(currentActionPacket != null){
			return this.currentActionPacket.getAction();
		}
		return null;
	}
	
	public double getCurrentArgument(){
		if(currentActionPacket != null){
			return this.currentActionPacket.getArgument();
		}
		return 0;	
	}

	@Override
	public Move getMovement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addMoveListener(MoveListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forward(){
		enqueueAction(Action.TRAVEL,Double.MAX_VALUE);
		setIsMoving(true);	
	}
	
	@Override
	public void backward() {
		enqueueAction(Action.TRAVEL,Double.MIN_VALUE);
		setIsMoving(true);		
	}

	@Override
	public void travel(double distance, boolean immediateReturn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTravelSpeed(double speed) {
		this.travelSpeed = speed;
	}

	@Override
	public double getTravelSpeed() {
		return this.travelSpeed;
	}

	@Override
	public double getMaxTravelSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void rotate(double angle, boolean immediateReturn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRotateSpeed(double speed) {
		this.rotateSpeed = speed;
	}

	@Override
	public double getRotateSpeed() {
		return this.rotateSpeed;
	}

	@Override
	public double getRotateMaxSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public boolean getStopFlag(){
		return stopFlag;
	}
	
	public boolean getKillFlag(){
		return killFlag;
	}

	private void setCurrentActionPacket(ActionPacket actionPacket) {
		this.currentActionPacket = actionPacket;
	}
	
	@Override
	public void run() {
		while (!getKillFlag()) {
			if (isMoving() && !getStopFlag()) {
				try { Thread.sleep(10); } catch (final InterruptedException e) {}
			} else if (getStopFlag()) {
				doStop();
			} else {
				ActionPacket nextActionPacket= queue.poll();
				if(nextActionPacket == null){
					// DO NOT fire a message here, it will make the GUI explode
//					fireMessage("nextActionPacket is Null, no action will be taken.");
				} else {
					setCurrentActionPacket(nextActionPacket);
					getCurrentAction().execute(this);
				}
			}
		}
		doKill();
		
		/*
//		System.out.println("RUN");
		while(!isMoving() && !getStopFlag() && !getKillFlag()){
//			fireMessage("Running in standby");
			try { Thread.sleep(10); } catch (final InterruptedException e) {}
		}
		if(getStopFlag()){
			//This can interrupt an action so it has to note progress.
			this.doStop();
		}else if(!getKillFlag()){
			run();
		} else {
			doKill();
		}
		ActionPacket nextActionPacket= queue.poll();
		if(nextActionPacket == null){
			fireMessage("nextActionPacket is Null, no action will be taken.");
		} else {
			this.setCurrentArgument(nextActionPacket.getArgument());
			nextActionPacket.getAction().execute(this);
		}
		*/
	}
	
	/**
	 * Kills the current robot object and the thread it's running in.
	 */
	public void kill(){
		setKillFlag(true);
	}
	
	private void doKill(){
		doStop();
		Thread.currentThread().interrupt();
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private void setKillFlag(boolean b) {
		killFlag = b;
	}

	
	public void fireMessage(final String msg){
		for (final ControllerListener listener : getListeners()) {
				listener.onMessageIncoming(msg);
		}
	}
	


}
