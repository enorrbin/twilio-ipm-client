package org.emanuel.twilio.client;

import org.emanuel.twilio.client.impl.ConsoleMessaging;

/**
 * Twilio Client (cli)
 * 
 * @author emanuel
 */
public class Client {

	public static void main( String[] args ) {
		
		ConsoleMessaging console = new ConsoleMessaging();
		try {
			console.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
