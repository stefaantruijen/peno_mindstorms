package comms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;
import utils.Debug;

public class BTController extends Thread{
	
	private NXTConnector connection;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	public BTController(){
		connection = null;
	}
	@Override
	public void run(){
		
		
	}
	
	public boolean connect(){
		connection = new NXTConnector();
		Debug.print("Connecting to a bluetooth device.");
		
		boolean connected = connection.connectTo("btspp://");
		
		if (!connected) {
			Debug.print("Failed to connect to a bluetooth device.");
			return false;
		}else{
			dos = new DataOutputStream(connection.getOutputStream());
			dis = new DataInputStream(connection.getInputStream());
			return true;
		}
		
	}
	
	public boolean isConnected(){
		return (dos != null && dis != null && connection != null);
	}
	
	public void disConnect(){
		try{
			connection.close();
			connection = null;
			dis.close();
			dis = null;
			dos.close();
			dos = null;
		}catch(IOException e){
			Debug.print(e.getMessage());
		}
	}
	/**
	 * 
	 * @throws IOException
	 *
	public void sendCommand() throws IOException{
		if(!this.isConnected()){
			throw new IOException("Now bluetooth connection available.");
		}
		try{
			while(true){
			dos.writeChars(c.getVal());
			dos.flush();
		}catch(IOException e){
			Debug.print(e.getMessage());
		}
	}*/
	
	public NXTInfo[] getBricks(){
		NXTComm nxtComm;
		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			NXTInfo[] nxtInfo = nxtComm.search("NXT");
			return nxtInfo;
		} catch (NXTCommException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	


}
