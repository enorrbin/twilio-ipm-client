package org.emanuel.twilio.client.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.emanuel.twilio.client.api.MessageHandler;
import org.emanuel.twilio.client.api.MessagingClient;
import org.emanuel.twilio.client.api.TwilioApiKeys;
import org.emanuel.twilio.client.api.TwilioKeyProvider;

import com.twilio.sdk.TwilioIPMessagingClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.instance.ipmessaging.Channel;
import com.twilio.sdk.resource.instance.ipmessaging.Message;
import com.twilio.sdk.resource.instance.ipmessaging.Service;
import com.twilio.sdk.resource.list.ipmessaging.ChannelList;
import com.twilio.sdk.resource.list.ipmessaging.MessageList;

/**
 * Sends and receives simple string messages using the twilio IP Messaging API.
 * Uses the first available channel or creates a new one.
 * 
 * @author emanuel
 *
 */
public class SimpleMessagingClient implements MessagingClient {

	private final TwilioKeyProvider keyProvider;
	private final ScheduledExecutorService executor;
	private final Map<String, String> handledMessages = new HashMap<>();

	private volatile Channel channel = null;
    private volatile MessageHandler messageHandler = null;

    /**
     * Create a client using the provided key provider.
     * 
     * @param keyProvider
     */
    public SimpleMessagingClient(TwilioKeyProvider keyProvider) {
		this.keyProvider = keyProvider;
		this.executor = new ScheduledThreadPoolExecutor(1);
	}
	
	@Override
	public void sendMessage(String msg) throws TwilioRestException {
		if ( channel == null ) {
			return;
		}
		
		Map<String, String> params = new HashMap<>();
		params.put("Body", msg);
		Message createdMsg = channel.createMessage(params);
		synchronized (handledMessages) {
			handledMessages.put(createdMsg.getSid(), "");
		}
	}

	@Override
	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}
	
	private void sendToMessageHandler(String msg) {
		MessageHandler handler = messageHandler;
		if ( handler != null ) {
			handler.handleMessage(msg);
		}
	}
	
	/**
	 * Creates the channel by either getting the first channel or creating a new one.
	 * 
	 * @throws Exception
	 * @throws TwilioRestException
	 */
	private void createChannel() throws Exception, TwilioRestException {
		TwilioApiKeys keys;
		keys = keyProvider.getApiKeys();
		TwilioIPMessagingClient client = new TwilioIPMessagingClient(keys.getApiKey(), keys.getApiSecret());

        Service service = client.getService(keys.getServiceSID());
        
        // For this test, just use the first channel.
		ChannelList channels = service.getChannels();
		for( Channel c : channels ) {
			if ( channel == null ) {
				channel = c;
				break;
			}
		}

		// Or create a new one if there are no channels
		if ( channel == null ) {
	        Map<String, String> params = new HashMap<>();
	        params.put("FriendlyName", "general");
			channel = service.createChannel(params);			
		}

		// Tell what channel is used or fail.
		if ( channel != null ) {
			sendToMessageHandler("Joining channel: " + channel.getFriendlyName());
		}
		else {
			throw new IllegalStateException("Failed to create channel.");
		}
	}

	@Override
	public void start() throws Exception {
		createChannel();
		
		// There doesn't seem to be any push notification functionality for the Java SDK
		// so we will just poll for new messages every now and then.
		Runnable command = () -> {
			// Should filter on "new" messages.
			MessageList messages = channel.getMessages();
			for( Message m : messages ) {
				String msgSid = m.getSid();
				synchronized (handledMessages) {
					if ( !handledMessages.containsKey(msgSid) ) {
						sendToMessageHandler(m.getBody());
						handledMessages.put(msgSid, msgSid);
					}
				}
			}
		};

		// Start polling for messages
		executor.scheduleWithFixedDelay(command, 0, 10, TimeUnit.SECONDS);
	}

	@Override
	public void stop() {
		executor.shutdown();
	}
	
	/**
	 * Sanity test, start client, send and receive a message.
	 * 
	 * @param args
	 */
	public static void main( String[] args ) {
		TwilioKeyProvider keyProvider = new UserPropertyFileTwilioKeyProvider();

		// Simple test
		SimpleMessagingClient client = new SimpleMessagingClient(keyProvider);
		
		client.setMessageHandler((String msg) -> {
			System.out.println("Received message: " + msg);
		});
		
		try {
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			return;
		}
		
		try {
			client.sendMessage("Just a test message");
		} catch (TwilioRestException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			Thread.sleep(20,000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
	}

}
