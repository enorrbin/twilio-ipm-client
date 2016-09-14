package org.emanuel.twilio.client;

import org.emanuel.twilio.client.api.TwilioKeyProvider;
import org.emanuel.twilio.client.impl.SimpleMessagingClient;
import org.emanuel.twilio.client.impl.UserPropertyFileTwilioKeyProvider;

/**
 * Twilio Client (cli)
 * 
 * @author emanuel
 */
public class Client {
	
	private static final TwilioKeyProvider keyProvider = new UserPropertyFileTwilioKeyProvider();

	public static void main( String[] args ) {
    
		SimpleMessagingClient client = new SimpleMessagingClient(keyProvider);
		try {
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
