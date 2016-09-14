package org.emanuel.twilio.client.impl;

import java.io.Console;

import org.emanuel.twilio.client.api.TwilioKeyProvider;

public class ConsoleMessaging {

	private final TwilioKeyProvider keyProvider = new UserPropertyFileTwilioKeyProvider();
	private final Console console;

	public ConsoleMessaging() {
		console = System.console();
	}
	
	public void start() throws Exception {
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
		}
		
		System.out.println("Stopping the client");
		client.stop();
	}
	

}