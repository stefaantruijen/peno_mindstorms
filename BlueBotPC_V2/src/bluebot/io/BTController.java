package bluebot.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import bluebot.utils.Debug;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;


public class BTController extends Thread{
	
	private NXTComm connection;
	private DataOutputStream dos;
	private DataInputStream dis;

	public BTController(){
		connection = null;
	}
	@Override
	public void run(){
		
		
	}
	/**
	 * Connect to a device with a given device naam.
	 * 
	 * @param name
	 * @return TRUE if succesfully connected. FALSE otherwise.
	 */
	public boolean connect(String name){
		Debug.print("Connecting to a bluetooth device.");
			try {
				connection = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
				NXTInfo[] devices = connection.search(name);
				connection.open(devices[0]);
				dos = new DataOutputStream(connection.getOutputStream());
				dis = new DataInputStream(connection.getInputStream());
				System.out.print("success");
				return true;
			} catch (NXTCommException e) {
				e.printStackTrace();
				return false;
			}
	}
	/**
	 * Check if this Bluetooth controller is connected.
	 * 
	 * @return TRUE if connected, FALSE otherwise.
	 */
	public boolean isConnected(){
		return (dos != null && dis != null && connection != null);
	}
	/**
	 * Disconnect this Bluetooth controller.
	 */
	public void disConnect(){
		try{
			connection.close();
			connection = null;
			dis.close();
			dis = null;
			dos.close();
			dos = null;
		}catch(IOException e){
			bluebot.utils.Debug.print(e.getMessage());
		}
	}
	
	public void sentPacket(){
	
	}
	/**
	 * Just for testing purposes.
	 * @param args
	 */
	public static void main(final String[] args) {
		try {
			 BTController bt = new BTController();
			 bt.connect("Lenny");
			 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			 System.out.print("Execute ? Yes-No");
			 String str;
			 do {
			      str = br.readLine();
			      if(str.equals("MOVE")){
			    	  
			      }else if(str.equals("STOP")){
			    	  
			    	  
			      }else if(str.equals("LEFT")){
			    	 
			      }else if(str.equals("RIGHT")){
			    	  
			      }
			      
			    } while (!str.equals("stop"));
			 
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}

}
