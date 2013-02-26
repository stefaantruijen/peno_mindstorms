package bluebot.ui.util;


import bluebot.io.Message;
import bluebot.io.Message.Type;
import bluebot.io.MessageListener;
import bluebot.io.RabbitMessage;



/**
 * 
 * @author Ruben Feyen
 */
public class RabbitListModel extends GenericListModel<RabbitMessage>
		implements MessageListener {
	private static final long serialVersionUID = 1L;
	
	
	
	private final void handleMessage(final Message msg) {
		if (msg.getType() == Type.RABBIT_MQ) {
			addElement((RabbitMessage)msg);
		}
	}
	
	public void onMessageIncoming(final Message msg) {
		handleMessage(msg);
	}
	
	public void onMessageOutgoing(final Message msg) {
		handleMessage(msg);
	}
	
}
