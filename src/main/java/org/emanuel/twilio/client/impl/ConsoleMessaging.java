package org.emanuel.twilio.client.impl;

import java.io.Console;

import org.emanuel.twilio.client.api.TwilioKeyProvider;

/**
 * Writes messages to the console and sends messages that are typed in to the console.
 * 
 * @author emanuel
 *
 */
public class ConsoleMessaging {

	private final TwilioKeyProvider keyProvider = new UserPropertyFileTwilioKeyProvider();
	private final Console console;

	public ConsoleMessaging() {
		console = System.console();
	}
	
	public void start() throws Exception {
		if ( console == null ) {
			throw new Exception("No console available.");
		}
		SimpleMessagingClient client = new SimpleMessagingClient(keyProvider);
		
		client.setMessageHandler((String msg) -> {
			System.out.println("Received message: " + msg);
		});
		
		client.start();
		
		// Start reading commands/messages from the console
		String cmd;
		while( ( cmd = console.readLine() ) != null ) {
			if ( cmd.startsWith("/q") ) {
				break;
			}
			else if ( !cmd.isEmpty() ) {
				try {
					client.sendMessage(cmd);
				}
				catch (Exception e) {
					System.err.println("Failed to send message: " + e.getMessage());
				}
			}
			// TODO: Could accept other commands such as
			// /channels - to list channels
			// /channel name - to switch to a channel
			// etc.
		}
		
		System.out.println("Stopping the client");
		client.stop();
	}
}