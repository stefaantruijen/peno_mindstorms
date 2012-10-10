    import java.io.*;


import PROTOCOL_API.OP_CODE;
import PROTOCOL_API.Packet;
import PROTOCOL_API.PacketHandler;
import PROTOCOL_API.PacketListener;
import lejos.nxt.*;
import lejos.nxt.comm.*;


    public class NXT implements PacketListener
    {
      private DataOutputStream dataOut;
      private DataInputStream dataIn;
      private BTConnection BTLink;
      private PacketHandler pckHandler;
      private final PilotController pc;
      
      public NXT(){
    	  this.pc = new PilotController();
      }
      
      public static void main(String [] args)
      {
     	 NXT nxt = new NXT();
     	 nxt.startListening();
     	 
 	     
      }
    
     public void startListening(){
    	 System.out.println("Waiting for connection...");
    	 this.connect();
     	 System.out.println("Connected");
     	 System.out.println("Start receiving...");
 	     pckHandler.startReceiving(this);
     }
    	 
    
     private void digestPacket(Packet p){
    	 Sound.beep();
    	 switch(p.getOpCode()){
    	 	case MOVE_FORWARD:
    	 		this.pc.moveForward(p.getPayLoad()[0]);
    	 		return;
    	 	case TURN_LEFT:
    	 		this.pc.turnLeft(p.getPayLoad()[0]);
    	 		return;
    	 	case TURN_RIGHT:
    	 		this.pc.turnRight(p.getPayLoad()[0]);
    	 		return;
    	 	case MOVE_STOP:
    	 		this.pc.stop();
    	 		return;
    	 		
    	 		
    	 }
    	 
     }
     
     public void connect()
     { 
        BTLink = Bluetooth.waitForConnection();   
        dataOut = BTLink.openDataOutputStream();
        dataIn = BTLink.openDataInputStream();
        this.pckHandler = new PacketHandler(dataIn,dataOut);
      

     }
     
     public DataInputStream getInput(){
    	 return this.dataIn;
     }

	@Override
	public void notify(Packet p) {
		Sound.beep();
   	 switch(p.getOpCode()){
   	 	case MOVE_FORWARD:
   	 		this.pc.moveForward(p.getPayLoad()[0]);
   	 		return;
   	 	case TURN_LEFT:
   	 		this.pc.turnLeft(p.getPayLoad()[0]);
   	 		return;
   	 	case TURN_RIGHT:
   	 		this.pc.turnRight(p.getPayLoad()[0]);
   	 		return;
   	 	case MOVE_STOP:
   	 		this.pc.stop();
   	 		return;

   	 }
		
	}
     
    }

