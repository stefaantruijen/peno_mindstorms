package bluebot.core;

import javax.swing.JOptionPane;

import bluebot.MotionListener;
import bluebot.sensors.SensorListener;

public class PCDriver implements SensorListener,MotionListener{
	
	private Controller controller;
	/*Sensor Values*/
	private int valueInfrared = -1;
	private int valueLight = -1;
	private int valueUltraSonic = -1;
	
	private float x = -1;
	private float y = -1;
	private float body = 0;
	private float head = 0;
	
	private int speed = -1;
	/*End sensor values*/
	
	private boolean startedReceiving = false;
	
	public PCDriver(Controller controller){
		this.controller = controller;
		this.controller.addListener((SensorListener) this);
		this.controller.addListener((MotionListener) this);
		
	
	}
	

	@Override
	public void onSensorValueInfrared(int value) {
		this.setValueInfrared(value);
	}

	@Override
	public void onSensorValueLight(int value) {
		this.setValueLight(value);
	}

	@Override
	public void onSensorValueUltraSonic(int value) {
		//System.out.println("PCDRIVER : Ultrasonic : "+value);
		this.setValueUltraSonic(value);
	}

	/**
	 * @return the valueInfrared
	 */
	public int getValueInfrared() {
		return valueInfrared;
	}

	/**
	 * @param valueInfrared the valueInfrared to set
	 */
	public void setValueInfrared(int valueInfrared) {
		this.valueInfrared = valueInfrared;
	}

	/**
	 * @return the valueLight
	 */
	public int getValueLight() {
		return valueLight;
	}

	/**
	 * @param valueLight the valueLight to set
	 */
	public void setValueLight(int valueLight) {
		this.valueLight = valueLight;
	}

	/**
	 * @return the valueUltraSonic
	 */
	public int readSensorUltraSonic() {
		//System.out.println("Ultrasonic : "+this.valueUltraSonic+ " "+this);
		return valueUltraSonic;
	}
	
	/**
	 * Check for infrared. 
	 */
	public boolean seeInfraRead(){
		return (this.getValueInfrared() > 2 || this.getValueInfrared() < 8 )? true : false;
	}

	/**
	 * @param valueUltraSonic the valueUltraSonic to set
	 */
	public void setValueUltraSonic(int valueUltraSonic) {
		this.valueUltraSonic = valueUltraSonic;
	}

	public Controller getController() {
		return controller;
	}

//	public void setController(Controller controller) {
//		this.controller = controller;
//	} -> Dit mag toch niet aangepast worden?

	public float getX() {
		return x;
	}


	public float getY() {
		return y;
	}

	public float getBody() {
		return body;
	}

	
	public float getHead() {
		return head;
	}
	
	
	public bluebot.graph.Orientation getOrientationBody(){
		return bluebot.graph.Orientation.forHeading(this.getBody());
	}
	
	public void setSpeed(int value){
		controller.setSpeed(value);
	}
	
	public void moveForward(float distance, boolean b){
		controller.moveForward(distance,b);
	}
	
	public void moveForward(){
		controller.moveForward();
	}
	
	public void turnRight(){
		controller.turnRight();
	}
	
	public void turnRight(float angle, boolean b){
		controller.turnRight(angle,b);
	}
	
	public void turnLeft(float angle, boolean b){
		controller.turnLeft(angle,b);
	}
	
	public void turnLeft(){
		controller.turnLeft();
	}
	
	public void resetOrientation(){
		controller.reset();
	}
	
	public void executeWhiteLine(){
		controller.doWhiteLineOrientation();
	}
	
	public void sendDebug(String string){
		System.out.println(string);
	}

	public void turnHeadCounterClockWise(int i) {
		controller.turnHeadCounterClockwise(i);
	}

	public void turnHeadClockWise(int i) {
		controller.turnHeadClockwise(i);
	}

	public void sendItemFound(int teamNumber) {
		//TODO: gui?
	}

	public void sendMessage(String string, String string2) {
		JOptionPane.showMessageDialog(null,
			    string2,
			    string,
			    JOptionPane.WARNING_MESSAGE);
	}
	
	public void modifyOrientation(){
		controller.modifyOrientation();
	}

	public int getSpeed() {
		return this.speed;
	}

	@Override
	public void onSensorSpeed(int value) {
		this.speed = value;
	}


	@Override
	public void onMotion(float x, float y, float body, float head) {
		System.out.println("PCDRIVER : x : "+x+" y :"+y+" body "+body+" head "+head);
		this.x = x;
		this.y = y;
		this.body = body;
		this.head = head;
		this.startedReceiving = true;
	}
	
	public synchronized boolean startedReceiving(){
		return this.startedReceiving;
	}
	
	public void setHead(float h){
		this.head = h;
	}
	
	public void setBody(float b){
		this.body = b;
	}
	
	public void doSeesaw(){
		controller.doSeesaw();
	}
	
	public void doPickUp(){
		controller.doPickUp();
	}
	
	public void doReadBarcode(){
		controller.doReadBarcode();
	}
	
	public int getReceivedBarcode(){
		return controller.getReceivedBarcode();
	}
	
	public void doWhiteLineOrientation(){
		controller.doWhiteLineOrientation();
	}
	
}
