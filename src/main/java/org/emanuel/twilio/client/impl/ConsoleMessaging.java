package org.emanuel.twilio.client.impl;

import java.io.Console;

import org.emanuel.twilio.client.api.TwilioKeyProvider;

public class ConsoleMessaging {

	private final TwilioKeyProvider keyProvider = new UserPropertyFileTwilioKeyProvider();
	private final Console console;
	ConsoleMessaging() {
		console = System.console();
	}
	
	void start() throws Exception {
		SimpleMessagingClient client = new SimpleMessagingClient(keyProvider);
		
		client.setMessageHandler((String msg) -> {
			// Just write the message to the console
			console.writer().println(msg);
		});
		
		client.start();
		
		// Start reading commands
		String msg;
		while( ( msg = console.readLine() ) != null ) {
			client.sendMessage(msg);
		}
	}
}
