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
import com.twilio.sdk.resource.list.ipmessaging.MessageList;

public class SimpleMessagingClient implements MessagingClient {

	private final TwilioKeyProvider keyProvider;
	private final ScheduledExecutorService executor;
	private final Map<String, String> handledMessages = new HashMap<>();

	private Channel channel = null;
    private volatile MessageHandler messageHandler;

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
		channel.createMessage(params);
	}

	@Override
	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	@Override
	public void start() throws Exception {
		TwilioApiKeys keys;
		keys = keyProvider.getApiKeys();
		TwilioIPMessagingClient client = new TwilioIPMessagingClient(keys.getApiKey(), keys.getApiSecret());

		// Get the service for this client.
        Service service = client.getService(keys.getServiceSID());
        
        Map<String, String> params = new HashMap<>();
        params.put("FriendlyName", "general");
		channel = service.createChannel(params);
		
		Runnable command = () -> {
			MessageList messages = channel.getMessages();
			for( Message m : messages ) {
				String msgSid = m.getSid();
				if ( !handledMessages.containsKey(msgSid) ) {
					MessageHandler handler = messageHandler;
					if ( handler != null ) {
						handler.handleMessage(m.getBody());
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

}
