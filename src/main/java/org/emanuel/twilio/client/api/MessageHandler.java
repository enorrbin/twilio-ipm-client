package org.emanuel.twilio.client.api;

@FunctionalInterface
public interface MessageHandler {

	void handleMessage(String msg);

}
